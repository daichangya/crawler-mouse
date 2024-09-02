package com.daicy.samples.samples;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.processor.PageProcessor;

import java.util.List;

/**
 * @author daichangya@163.com <br>
 */
public class TianyaPageProcesser implements PageProcessor {

    @Override
    public void process(Page page) {
        List<String> strings = page.getHtml().regex("<a[^<>]*href=[\"']{1}(/post-free.*?\\.shtml)[\"']{1}").all();
        page.addTargetRequests(strings);
        page.putField("title", page.getHtml().xpath("//div[@id='post_head']//span[@class='s_title']//b"));
        page.putField("body",page.getHtml().smartContent());
    }

    @Override
    public Site getSite() {
        return Site.me().setDomain("http://bbs.tianya.cn/");  //To change body of implemented methods use File | Settings | File Templates.
    }
}
