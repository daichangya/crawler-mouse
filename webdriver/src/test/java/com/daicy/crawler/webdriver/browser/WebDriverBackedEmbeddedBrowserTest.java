package com.daicy.crawler.webdriver.browser;

import com.daicy.crawler.common.model.BrowserType;
import com.daicy.crawler.webdriver.BrowserProvider;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.webdriver.browser
 * @date:8/19/20
 */
public class WebDriverBackedEmbeddedBrowserTest {

    @Test
    public void withDriver() {
        String url = "https://club.autohome.com.cn/bbs/forum-c-2733-2.html";
        List<BrowserType> browserTypeList = ImmutableList.of(BrowserType.CHROME,BrowserType.FIREFOX);
        browserTypeList.forEach(browserType -> {
            if(BrowserType.CHROME.equals(browserType)){
//                ChromeOptions optionsChrome = new ChromeOptions();
//                optionsChrome.setHeadless(true);
//                testBrowser(url, browserType, optionsChrome);
//                testBrowser(url, browserType, null);
            }else {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(true);
                firefoxOptions.setCapability("marionette", true);
                testBrowser(url, browserType, firefoxOptions);
                testBrowser(url, browserType, new FirefoxOptions());
            }

        });

    }

    private void testBrowser(String url, BrowserType browserType, AbstractDriverOptions abstractDriverOptions) {
        BrowserProvider provider = new BrowserProvider();
        EmbeddedBrowser embeddedBrowser = provider.newEmbeddedBrowser(browserType,abstractDriverOptions);
        embeddedBrowser.goToUrl(URI.create(url));
//            String html = driver.getDom();
        List<String> titleList = embeddedBrowser.getWebDriver().findElements(By.cssSelector("p.post-title a")).stream().map(WebElement::getText)
                .collect(Collectors.toList());
        if (null != titleList && titleList.size() > 0) {
            titleList.forEach(System.out::println);
            System.out.println("title " + browserType);
        }else {
            System.out.println("kill "+browserType);
        }
        embeddedBrowser.getWebDriver().close();
    }

    @Test
    public void withDriver2() {
        String url = "https://club.autohome.com.cn/bbs/thread/05d763eb93d2d28a/89257756-1.html#pvareaid=6830286";
        BrowserProvider provider = new BrowserProvider();
        List<EmbeddedBrowser.BrowserType> browserTypeList = ImmutableList.of(
                EmbeddedBrowser.BrowserType.CHROME_HEADLESS,EmbeddedBrowser.BrowserType.CHROME,
                EmbeddedBrowser.BrowserType.FIREFOX_HEADLESS,EmbeddedBrowser.BrowserType.FIREFOX);
        browserTypeList.forEach(browserType -> {
            RemoteWebDriver driver = provider.newBrowser(browserType);
            driver.get(url);
//            String html = driver.getDom();
            List<String> titleList = driver.findElements(By.cssSelector("#F0 > div.conright.fr > div.rconten > div.conttxt > div"))
                    .stream().map(webElement -> {return webElement.getAttribute("innerHTML");})
                    .collect(Collectors.toList());
            if (null != titleList && titleList.size() > 0) {
                titleList.forEach(System.out::println);
                System.out.println("title " + browserType);
            }else {
                System.out.println("kill "+browserType);
            }
            driver.close();
        });

    }
}
