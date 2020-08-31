package com.daicy.samples.model.samples;

import com.daicy.crawler.core.Site;
import com.daicy.crawler.extension.model.ConsolePageModelPipeline;
import com.daicy.crawler.extension.model.OOSpider;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.model.annotation.TargetUrl;

/**
 * @author code4crafter@gmail.com
 */
@TargetUrl("http://meishi.qq.com/beijing/c/all[\\-p2]*")
@ExtractBy(value = "//ul[@id=\"promos_list2\"]/li",multi = true)
public class QQMeishi {

    @ExtractBy("//div[@class=info]/a[@class=title]/h4/text()")
    private String shopName;

    @ExtractBy("//div[@class=info]/a[@class=title]/text()")
    private String promo;

    public static void main(String[] args) {
        OOSpider.create(Site.me(), new ConsolePageModelPipeline(), QQMeishi.class).addUrl("http://meishi.qq.com/beijing/c/all").thread(4).run();
    }

}
