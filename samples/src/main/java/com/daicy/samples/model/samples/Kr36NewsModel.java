package com.daicy.samples.model.samples;

import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.extension.model.OOSpider;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.model.annotation.ExtractByUrl;
import com.daicy.crawler.extension.model.annotation.HelpUrl;
import com.daicy.crawler.extension.model.annotation.TargetUrl;
import com.daicy.crawler.extension.pipeline.PageModelPipeline;

import javax.management.JMException;
import java.io.IOException;

/**
 * @author code4crafter@gmail.com <br>
 */
@TargetUrl("http://www.36kr.com/p/\\d+.html")
@HelpUrl("http://www.36kr.com/#/page/\\d+")
public class Kr36NewsModel {

    @ExtractBy("//h1[@class='entry-title sep10']")
    private String title;

    @ExtractBy("//div[@class='mainContent sep-10']/tidyText()")
    private String content;

    @ExtractByUrl
    private String url;

    public static void main(String[] args) throws IOException, JMException {
        //Just for benchmark
        Spider thread = OOSpider.create(Site.me().setSleepTime(0), new PageModelPipeline() {
            @Override
            public void process(Object o, Task task) {

            }
        }, Kr36NewsModel.class).thread(20).addUrl("http://www.36kr.com/");
        thread.start();
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
