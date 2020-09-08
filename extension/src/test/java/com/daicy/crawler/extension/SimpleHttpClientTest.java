package com.daicy.crawler.extension;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.selector.CssSelector;
import com.daicy.crawler.core.selector.XpathSelector;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension
 * @date:9/4/20
 */
public class SimpleHttpClientTest {

    @Test
    public void get() {
        Page page = new SimpleHttpClient().get("https://club.autohome.com.cn/bbs/forum-c-526-1.html");
//        String content = page.getHtml().selectDocument(
//                new XpathSelector("//*/div[@class='conttxt']"));
        System.out.println(page.getRawText());
    }
}