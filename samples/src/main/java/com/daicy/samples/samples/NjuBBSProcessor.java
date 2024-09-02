package com.daicy.samples.samples;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.processor.PageProcessor;

import java.util.List;

/**
 * @author daichangya@163.com <br>
 * Date: 13-4-21
 * Time: 下午8:08
 */
public class NjuBBSProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        List<String> requests = page.getHtml().regex("<a[^<>]*href=(bbstcon\\?board=Pictures&file=[^>]*)").all();
        page.addTargetRequests(requests);
        page.putField("title",page.getHtml().xpath("//div[@id='content']//h2/a"));
        page.putField("content",page.getHtml().smartContent());
    }

    @Override
    public Site getSite() {
        return Site.me().setDomain("bbs.nju.edu.cn");
    }

    public static void main(String[] args) {
        Spider.create(new NjuBBSProcessor()).addUrl("http://bbs.nju.edu.cn/board?board=Pictures").run();
    }
}
