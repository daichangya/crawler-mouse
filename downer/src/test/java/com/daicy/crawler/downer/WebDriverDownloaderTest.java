package com.daicy.crawler.downer;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.selector.XpathSelector;
import org.junit.Test;
import org.openqa.selenium.MutableCapabilities;

import static org.junit.Assert.*;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.downer
 * @date:9/4/20
 */
public class WebDriverDownloaderTest {

    @Test
    public void download() {
        System.out.println(WebDriverDownloader.class.getName());
    }
}