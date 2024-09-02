package com.daicy.crawler.extension.example;

import com.daicy.crawler.core.Site;
import com.daicy.crawler.extension.model.OOSpider;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.model.annotation.TargetUrl;
import com.daicy.crawler.extension.pipeline.JsonFilePageModelPipeline;

import java.util.Date;
import java.util.List;

/**
 * @author daichangya@163.com <br>
 * @since 0.3.2
 */
@TargetUrl("http://my.oschina.net/flashsword/blog/\\d+")
public class OschinaBlog {

    @ExtractBy("//title/text()")
    private String title;

    @ExtractBy(value = "div.BlogContent", type = ExtractBy.Type.Css)
    private String content;

    @ExtractBy(value = "//div[@class='BlogTags']/a/text()", multi = true)
    private List<String> tags;

    @ExtractBy("//div[@class='BlogStat']/regex('\\d+-\\d+-\\d+\\s+\\d+:\\d+')")
    private Date date;

    public static void main(String[] args) {
        //results will be saved to "/data/crawler/" in json format
        OOSpider.create(Site.me(), new JsonFilePageModelPipeline("/data/crawler/"), OschinaBlog.class)
                .addUrl("http://my.oschina.net/flashsword/blog").run();
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

    public Date getDate() {
        return date;
    }

}
