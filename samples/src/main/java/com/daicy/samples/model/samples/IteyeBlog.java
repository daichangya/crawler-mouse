package com.daicy.samples.model.samples;

import com.daicy.crawler.core.Site;
import com.daicy.crawler.extension.model.OOSpider;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.model.annotation.TargetUrl;

/**
 * @author daichangya@163.com <br>
 * Date: 13-8-2 <br>
 * Time: 上午7:52 <br>
 */
@TargetUrl("http://*.iteye.com/blog/*")
public class IteyeBlog implements Blog{

    @ExtractBy("//title")
    private String title;

    @ExtractBy(value = "div#blog_content",type = ExtractBy.Type.Css)
    private String content;

    @Override
    public String toString() {
        return "IteyeBlog{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public static void main(String[] args) {
        OOSpider.create(Site.me(), IteyeBlog.class).addUrl("http://flashsword20.iteye.com/blog").run();
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
