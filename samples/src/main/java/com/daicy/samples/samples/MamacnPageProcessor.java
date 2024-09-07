package com.daicy.samples.samples;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.processor.PageProcessor;
import com.daicy.crawler.core.selector.Selectable;
import com.daicy.crawler.extension.scheduler.FileCacheQueueScheduler;
import com.daicy.samples.samples.pipeline.OneFilePipeline;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author daichangya@163.com
 */
public class MamacnPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.mama.cn").setSleepTime(100);

    @Override
    public void process(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("//ul[@id=ma-thumb-list]/li").nodes();
        StringBuilder accum = new StringBuilder();
        for (Selectable node : nodes) {
            accum.append("img:").append(node.xpath("//a/@href").get()).append("\n");
            accum.append("title:").append(node.xpath("//img/@alt").get()).append("\n");
        }
        page.putField("",accum.toString());
        if (accum.length() == 0) {
            page.setSkip(true);
        }
        page.addTargetRequests(page.getHtml().links().regex("http://www\\.mama\\.cn/photo/.*\\.html").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        Spider.create(new MamacnPageProcessor())
                .setScheduler(new FileCacheQueueScheduler("/data/crawler/mamacn"))
                .addUrl("http://www.mama.cn/photo/t1-p1.html")
                .addPipeline(new OneFilePipeline("/data/crawler/mamacn/data"))
                .thread(5)
                .run();
    }
}
