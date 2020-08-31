package com.daicy.crawler.core;

import com.daicy.crawler.common.model.BrowserType;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.downer
 * @date:8/19/20
 */
public class SpiderContext {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private BrowserType browserType;

    private Capabilities driverOptions;

    private boolean useRealBrowser;

    public SpiderContext(BrowserType browserType, boolean useRealBrowser) {
        this(browserType, null, useRealBrowser);
    }

    public SpiderContext(BrowserType browserType, AbstractDriverOptions driverOptions, boolean useRealBrowser) {
        this.browserType = browserType;
        this.driverOptions = driverOptions;
        this.useRealBrowser = useRealBrowser;
    }

    public Capabilities getDriverOptions() {
        return driverOptions;
    }

    public void setDriverOptions(Capabilities driverOptions) {
        this.driverOptions = driverOptions;
    }

    public BrowserType getBrowserType() {
        return browserType;
    }

    public void setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }


    public boolean isUseRealBrowser() {
        return useRealBrowser;
    }

    public void setUseRealBrowser(boolean useRealBrowser) {
        this.useRealBrowser = useRealBrowser;
    }
}
