package com.daicy.samples.samples;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.processor.PageProcessor;
import com.daicy.crawler.core.selector.CssSelector;
import com.daicy.crawler.downer.WebDriverDownloader;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.core.samples
 * @date:8/20/20
 */


public class FirstDemo implements PageProcessor {
    private Site site = Site.me();

    @Override
    public void process(Page page) {
        page.putField("title", page.getHtml().select(new CssSelector("#juejin > div.view-container > main > div > div.main-area.article-area.shadow > article > h1")).toString());
    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) {

        Spider.create(new FirstDemo())
                .addUrl("http://juejin.im/post/5e216eda6fb9a0300e1617eb").run();

    }

}