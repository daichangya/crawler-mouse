package com.daicy.crawler.web.controller;

import com.daicy.crawler.core.Spider;
import com.daicy.crawler.web.utils.EmailUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Map;

@RestController
public class IndexController {

    @RequestMapping("/")
    String index() {
        return "xxl job executor running.";
    }

//    @RequestMapping("/crawlerBbbs")
//    public String crawlerBbbs() throws Exception {
//        String mainUrl = "https://club.autohome.com.cn/bbs/forum-c-2733-2.html";
//        Map<String,String> parameters = Maps.newHashMap();
//        parameters.put(Spider.MAINURL,mainUrl);
//        parameters.put("email","dai.changya@xcar.com.cn");
//        new BbsSpiderBuilder().build(parameters).run();
//        return "OK";
//    }

    @RequestMapping("/testEmail")
    public String testEmail() throws Exception {
        String emailAdress = "dai.changya@xcar.com.cn";
        String title = "nihao!";
        File file = new File("/home/daichangya/club.autohome.com.cn.zip");
        EmailUtils.sendEmail(emailAdress, title, "详情见附件", Lists.newArrayList(file));
        return "OK";
    }



}