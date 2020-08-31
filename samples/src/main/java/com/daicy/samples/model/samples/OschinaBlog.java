package com.daicy.samples.model.samples;

import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.extension.model.OOSpider;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.model.annotation.TargetUrl;
import com.daicy.crawler.extension.pipeline.PageModelPipeline;

import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 */
@TargetUrl("http://my.oschina.net/flashsword/blog/\\d+")
public class OschinaBlog{

    @ExtractBy("//title")
    private String title;

    @ExtractBy(value = "div.BlogContent",type = ExtractBy.Type.Css)
    private String content;

    @ExtractBy(value = "//div[@class='BlogTags']/a/text()", multi = true)
    private List<String> tags;

    public static void main(String[] args) {
        OOSpider.create(Site.me()
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36")
                .setSleepTime(0)
                .setRetryTimes(3)
                ,new PageModelPipeline() {
            @Override
            public void process(Object o, Task task) {

            }
        }, OschinaBlog.class).thread(10).addUrl("http://my.oschina.net/flashsword/blog").run();
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<String> getTags() {
        return tags;
    }
}
