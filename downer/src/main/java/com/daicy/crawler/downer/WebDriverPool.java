package com.daicy.crawler.downer;

import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Task;
import com.google.common.collect.Maps;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author daichangya@163.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午1:41 <br>
 */
class WebDriverPool {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final static int DEFAULT_CAPACITY = 5;

    private int capacity;

    private final static int STAT_RUNNING = 1;

    private final static int STAT_CLODED = 2;

    private AtomicInteger stat = new AtomicInteger(STAT_RUNNING);

    private Map<WebDriver, AtomicInteger> usedTime = Maps.newHashMap();

    private final int useTime;

    /**
     * store webDrivers created
     */
    private Map<Site, Queue> webDriverQueueMap = Maps.newHashMap();

    /**
     * store webDrivers created
     */
    private List<WebDriver> webDriverList = Collections
            .synchronizedList(new ArrayList<WebDriver>());


    public WebDriverPool(int capacity, int useTime) {
        this.capacity = capacity;
        this.useTime = useTime;
    }

    public WebDriverPool() {
        this(DEFAULT_CAPACITY, 1);
    }

    /**
     * @return
     * @throws InterruptedException
     */
    public synchronized WebDriver get(Task task) throws InterruptedException {
        checkRunning();
        Site site = task.getSite();
        BlockingDeque<WebDriver> innerQueue = new LinkedBlockingDeque<WebDriver>();
        WebDriver webDriver;
        BlockingDeque<WebDriver> oldQueue = (BlockingDeque<WebDriver>) webDriverQueueMap.putIfAbsent(site, innerQueue);
        if (null != oldQueue) {
            innerQueue = oldQueue;
        }
        webDriver = innerQueue.poll();
        if (webDriver != null) {
            return webDriver;
        }
        while (webDriverList.size() >= capacity) {
            logger.info("webdriver pool wait!!!!");
            wait(1000);
        }

        WebDriver mDriver = WebDriverFactory.getInstance().newWebDriver(task);
        WebDriver.Options manage = mDriver.manage();
        if (site.getCookies() != null) {
            for (Map.Entry<String, String> cookieEntry : site.getCookies()
                    .entrySet()) {
                Cookie cookie = new Cookie(cookieEntry.getKey(),
                        cookieEntry.getValue());
                manage.addCookie(cookie);
            }
        }
        innerQueue.add(mDriver);
        webDriverList.add(mDriver);
        return innerQueue.take();
    }

    public synchronized void returnToPool(Site site, WebDriver webDriver) {
        if (null == webDriver) {
            return;
        }
        checkRunning();
        if (useTime == 1) {
            webDriverList.remove(webDriver);
            webDriver.quit();
            return;
        }
        AtomicInteger oldTime = usedTime.putIfAbsent(webDriver, new AtomicInteger(1));
        if (null != oldTime) {
            if (oldTime.incrementAndGet() >= useTime) {
                webDriverList.remove(webDriver);
                webDriver.quit();
                return;
            }
        }
        BlockingDeque<WebDriver> innerQueue = new LinkedBlockingDeque<WebDriver>();
        BlockingDeque<WebDriver> oldQueue = (BlockingDeque<WebDriver>) webDriverQueueMap.putIfAbsent(site, innerQueue);
        if (null != oldQueue) {
            innerQueue = oldQueue;
        }
        innerQueue.add(webDriver);
    }

    protected void checkRunning() {
        if (!stat.compareAndSet(STAT_RUNNING, STAT_RUNNING)) {
            throw new IllegalStateException("Already closed!");
        }
    }

    public void closeAll() {
        boolean b = stat.compareAndSet(STAT_RUNNING, STAT_CLODED);
        if (!b) {
            throw new IllegalStateException("Already closed!");
        }
        for (WebDriver webDriver : webDriverList) {
            logger.info("Quit webDriver" + webDriver);
            webDriver.quit();
        }
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
