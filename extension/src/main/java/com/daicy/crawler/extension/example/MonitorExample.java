package com.daicy.crawler.extension.example;

import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.processor.example.GithubRepoPageProcessor;
import com.daicy.crawler.core.processor.example.ZhihuPageProcessor;

/**
 * @author daichangya@163.com
 * @since 0.5.0
 */
public class MonitorExample {

    public static void main(String[] args) throws Exception {

        Spider zhihuSpider = Spider.create(new ZhihuPageProcessor())
                .addUrl("http://my.oschina.net/flashsword/blog");
        Spider githubSpider = Spider.create(new GithubRepoPageProcessor())
                .addUrl("https://github.com/daichangya");

        zhihuSpider.start();
        githubSpider.start();
    }
}
