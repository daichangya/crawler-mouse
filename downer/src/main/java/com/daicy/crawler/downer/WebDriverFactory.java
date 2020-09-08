package com.daicy.crawler.downer;


import com.daicy.crawler.common.model.BrowserType;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.proxy.ProxyProvider;
import com.daicy.crawler.webdriver.BrowserProvider;
import com.daicy.crawler.webdriver.browser.EmbeddedBrowser;
import com.gargoylesoftware.htmlunit.WebClient;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author: create by daichangya
 * @site: zthinker.com
 * @date 创建时间：2016年10月14日 上午10:53:52
 */
public class WebDriverFactory {

    final static Logger log = LoggerFactory.getLogger(WebDriverFactory.class);

    /**
     * 获取单例
     *
     * @return
     */
    public static WebDriverFactory getInstance() {
        return DownerManagerUtils.webDriverFactory;
    }

    /**
     * 单例模式 实现赖加载
     *
     * @author: create by daichangya
     * @email 359852326@qq.com
     */
    private static class DownerManagerUtils {
        static WebDriverFactory webDriverFactory = new WebDriverFactory();
    }

    public WebDriver newWebDriver(Task task) {
        WebDriver webDriver;
        Site site = task.getSite();
        final BrowserType browserType = site.getBrowserType();
        final MutableCapabilities driverOptions = site.getDriverOptions();
        ProxyProvider proxyProvider = site.getProxyProvider();
        if (null != proxyProvider) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(proxyProvider.getProxy(task).getHostPort());
            proxy.setProxyType(Proxy.ProxyType.MANUAL);
            driverOptions.setCapability(CapabilityType.PROXY, proxy);
        }
        if (site.isUseRealBrowser()) {
            BrowserProvider provider = new BrowserProvider();
            EmbeddedBrowser embeddedBrowser = provider.newEmbeddedBrowser(browserType, driverOptions);
            webDriver = embeddedBrowser.getWebDriver();
        } else {
            webDriver = new HtmlUnitDriver(driverOptions) {
                @Override
                protected WebClient modifyWebClient(WebClient client) {
                    final WebClient webClient = super.modifyWebClient(client);
                    // you might customize the client here
                    webClient.getOptions().setCssEnabled(false);
                    return webClient;
                }
            };
        }
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS)
                .pageLoadTimeout(30, TimeUnit.SECONDS).setScriptTimeout(30, TimeUnit.SECONDS);
        return webDriver;
    }

}
