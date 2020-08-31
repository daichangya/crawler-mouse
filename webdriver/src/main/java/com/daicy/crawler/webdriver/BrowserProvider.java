package com.daicy.crawler.webdriver;

import com.daicy.crawler.common.model.BrowserType;
import com.daicy.crawler.webdriver.browser.EmbeddedBrowser;
import com.daicy.crawler.webdriver.browser.WebDriverBackedEmbeddedBrowser;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSortedSet;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BrowserProvider implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(BrowserProvider.class);
    private List<WebDriver> usedBrowsers = new LinkedList<>();

    public static EmbeddedBrowser.BrowserType getBrowserType() {
        // typically read from the profile in pom.xml
        String browser = System.getProperty("test.browser");
        if (!Strings.isNullOrEmpty(browser)) {
            return EmbeddedBrowser.BrowserType.valueOf(browser);
        } else {
            return EmbeddedBrowser.BrowserType.CHROME_HEADLESS;
        }
    }


    public EmbeddedBrowser newEmbeddedBrowser() {
        return WebDriverBackedEmbeddedBrowser.withDriver(newBrowser(getBrowserType()));
    }

    /**
     * Return the browser.
     */
    public RemoteWebDriver newBrowser(EmbeddedBrowser.BrowserType browserType) {
        RemoteWebDriver driver;
        switch (browserType) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case CHROME_HEADLESS:
                WebDriverManager.chromedriver().setup();
                ChromeOptions optionsChrome = new ChromeOptions();
                optionsChrome.addArguments("--headless");
                driver = new ChromeDriver(optionsChrome);
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case FIREFOX_HEADLESS:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability("marionette", true);
                firefoxOptions.setHeadless(true);
                driver = new FirefoxDriver(firefoxOptions);
                break;
            default:
                throw new IllegalStateException("Unsupported browser type " + browserType);
        }

        /* Store the browser as a used browser so we can clean it up later. */
        usedBrowsers.add(driver);

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS)
                .pageLoadTimeout(30, TimeUnit.SECONDS).setScriptTimeout(30, TimeUnit.SECONDS);

        driver.manage().deleteAllCookies();

        return driver;
    }

    /**
     * Return the browser.
     */
    public EmbeddedBrowser newEmbeddedBrowser(BrowserType browserType, Capabilities driverOptions) {
        EmbeddedBrowser browser = null;
        long crawlWaitReload = 10 * 1000;
        long crawlWaitEvent = 0;
        ImmutableSortedSet<String> filterAttributes = ImmutableSortedSet.<String>of();
        switch (browserType) {
            case CHROME:
                browser = newChromeBrowser((ChromeOptions) driverOptions,filterAttributes, crawlWaitReload, crawlWaitEvent);
                break;
            case FIREFOX:
                browser = newFirefoxBrowser((FirefoxOptions) driverOptions,filterAttributes, crawlWaitReload, crawlWaitEvent);
                break;
            default:
                throw new IllegalStateException("Unsupported browser type " + browserType);
        }

        /* Store the browser as a used browser so we can clean it up later. */
        usedBrowsers.add(browser.getWebDriver());

        return browser;
    }

    private EmbeddedBrowser newFirefoxBrowser(FirefoxOptions driverOptions,ImmutableSortedSet<String> filterAttributes,
                                              long crawlWaitReload, long crawlWaitEvent) {

        WebDriverManager.firefoxdriver().setup();
        return WebDriverBackedEmbeddedBrowser.withDriver(new FirefoxDriver(driverOptions),
                filterAttributes, crawlWaitReload, crawlWaitEvent);
    }

    private EmbeddedBrowser newChromeBrowser(ChromeOptions driverOptions,ImmutableSortedSet<String> filterAttributes,
                                             long crawlWaitReload, long crawlWaitEvent) {

        WebDriverManager.chromedriver().setup();

        ChromeDriver driverChrome = new ChromeDriver(driverOptions);
        return WebDriverBackedEmbeddedBrowser.withDriver(driverChrome, filterAttributes,
                crawlWaitEvent, crawlWaitReload);
    }

    @Override
    public void close() throws Exception {
        for (WebDriver browser : usedBrowsers) {
            try {

                /* Make sure we clean up properly. */
                if (!browser.toString().contains("(null)")) {
                    browser.close();
                }

            } catch (RuntimeException e) {
                LOG.warn("Could not close the browser: {}", e.getMessage());
            }
        }
    }
}
