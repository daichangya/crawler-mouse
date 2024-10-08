package com.daicy.samples.samples;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.processor.PageProcessor;

import java.util.List;

/**
 * @author daichangya@163.com <br>
 */
public class HuxiuProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        List<String> requests = page.getHtml().links().regex(".*article.*").all();
        page.addTargetRequests(requests);
        page.putField("title",page.getHtml().xpath("//div[@class='clearfix neirong']//h1/text()"));
        page.putField("content",page.getHtml().xpath("//div[@id='neirong_box']/tidyText()"));
    }

    @Override
    public Site getSite() {
        return Site.me().setDomain("www.huxiu.com");
    }

    public static void main(String[] args) {
        Spider.create(new HuxiuProcessor()).addUrl("http://www.huxiu.com/").run();
    }

}
