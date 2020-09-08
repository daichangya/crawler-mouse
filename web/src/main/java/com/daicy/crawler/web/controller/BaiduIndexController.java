package com.daicy.crawler.web.controller;

import com.daicy.crawler.core.Spider;
import com.daicy.crawler.web.baiduindex.BaiduIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class BaiduIndexController {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping("/baiduindex")
    public String baiduindex(String cookie) {
        logger.info("cookie :{}", cookie);
        Spider spider = BaiduIndexModel.baiduIndexBuild(cookie);
        spider.run();
        return spider.getUUID() + " run!";
    }
}