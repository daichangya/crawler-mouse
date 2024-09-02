package com.daicy.crawler.downer;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.downloader.Downloader;
import com.daicy.crawler.core.selector.Html;
import com.daicy.crawler.core.selector.PlainText;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.print.Pageable;
import java.io.Closeable;
import java.io.IOException;

/**
 * 使用Selenium调用浏览器进行渲染。目前仅支持chrome。<br>
 * 需要下载Selenium driver支持。<br>
 *
 * @author daichangya@163.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午1:37 <br>
 */
public class WebDriverDownloader implements Downloader, Closeable {

    private volatile WebDriverPool webDriverPool;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private int sleepTime = 0;

    private static int poolSize = 2;

    public WebDriverDownloader(int poolSize, int useTime) {
        webDriverPool = new WebDriverPool(poolSize, useTime);
    }


    public WebDriverDownloader(int useTime) {
        this(poolSize, useTime);
    }

    /**
     * Constructor without any filed. Construct PhantomJS browser
     *
     * @author bob.li.0718@gmail.com
     */
    public WebDriverDownloader() {
        this(1);
    }

    /**
     * set sleep time to wait until load success
     *
     * @param sleepTime sleepTime
     * @return this
     */
    public WebDriverDownloader setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    @Override
    public Page download(Request request, Task task) {
        checkInit();
        WebDriver webDriver = null;
        try {
            logger.info("download start url:{}", request.getUrl());
            webDriver = webDriverPool.get(task);
        } catch (Exception e) {
            logger.warn("download error", e);
            return Page.fail();
        }
        Page page = new Page();
        try {
            logger.info("downloading page " + request.getUrl());
            String content = getPage(task, request.getUrl(), webDriver);
            if (StringUtils.equals("用户访问安全认证V3", webDriver.getTitle())) {
                logger.warn("用户访问安全认证V3 url:{}", request.getUrl());
                return Page.fail();
            }
            if (StringUtils.equals("ERROR: El URL solicitado no se ha podido conseguir", webDriver.getTitle())) {
                logger.warn("ERROR: El URL solicitado no se ha podido conseguir url:{}", request.getUrl());
                return Page.fail();
            }
            page.setRawText(content);
            page.setHtml(new Html(content, request.getUrl()));
            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);
        } catch (TimeoutException e) {
            logger.warn("TimeoutException request:{}", request);
            return Page.fail();
        } catch (Exception e) {
            logger.error("download Exception request:{}", request, e);
            return Page.fail();
        } finally {
            logger.debug("down finish url:{}", request.getUrl());
            webDriverPool.returnToPool(task.getSite(), webDriver);
        }
        return page;
    }

    private String getPage(Task task, String url, WebDriver webDriver) {
        synchronized (webDriver) {
            for (int execCount = 1; ; execCount++) {
                WebElement webElement = null;
                try {
                    webDriver.get(url);
                    logger.debug("downloader url:{} title:{}", url, webDriver.getTitle());
                    return getContent(webDriver);
                } catch (TimeoutException e) {
                    logger.warn("TimeoutException url:{}", url);
                    if (execCount > task.getSite().getRetryTimes()) {
                        throw e;
                    }
                    continue;
                } catch (Exception e) {
                    logger.error("Exception url:{} webElement:{}", url, webElement, e);
                    if (execCount > task.getSite().getRetryTimes()) {
                        throw e;
                    }
                    continue;
                }
            }
        }
    }

    private String getContent(WebDriver webDriver) {
        int time = 1;
        while (true) {
            try {
                Thread.sleep(1000);
                WebElement webElement = webDriver.findElement(By.xpath("/html"));
                String content = webElement.getAttribute("outerHTML");
                return content;
            } catch (StaleElementReferenceException e) {
                if (time >= 20) {
                    logger.debug("getAttribute is borked, throwing up...");
                    throw e;
                } else {
                    logger.debug("getAttribute failed again but we're still trying after " + time);
                }
                time++;
            } catch (InterruptedException e) {
                logger.error("getAttribute", e);
            }
        }
    }

    private void checkInit() {
        if (webDriverPool == null) {
            synchronized (this) {
                webDriverPool = new WebDriverPool(poolSize, 1);
            }
        }
    }

    @Override
    public void setThread(int thread) {
        this.poolSize = thread;
        webDriverPool.setCapacity(thread);
    }

    @Override
    public void close() throws IOException {
        webDriverPool.closeAll();
    }
}
