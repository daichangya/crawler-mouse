//package com.daicy.crawler.web.controller;
//
//import com.daicy.crawler.core.Spider;
//import com.daicy.crawler.extension.configurable.ConfigurableSpider;
//import com.google.common.base.Splitter;
//import com.google.common.collect.ImmutableMap;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.util.ResourceUtils;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.File;
//import java.util.Map;
//
//@Controller
//public class BbsController {
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//
//    private Map<String, String> spiderTemplateMap = ImmutableMap.of("bbs.pcauto.com.cn", "pcautoBbs.json",
//            "club.autohome.com.cn/bbs", "autohomeBbs.json");
//
//    @RequestMapping(value = "/bbs", method = RequestMethod.GET)
//    public String bbs(HttpServletRequest httpServletRequest, ModelMap modelMap) {
//        modelMap.addAttribute("msg", "Hello dalaoyang , this is freemarker");
//        return "bbs/CrawlSpider";
//    }
//
//
//    @RequestMapping(value = "/bbs", method = RequestMethod.POST)
//    public String bbsPost(HttpServletRequest httpServletRequest, ModelMap modelMap) {
//        String ids = httpServletRequest.getParameter("ids");
//        if (!StringUtils.equals(ids, "wZPd1sBWWejJ$Kl0")) {
//            modelMap.addAttribute("message_ids", "认证码错误，请重新输入");
//            return "bbs/CrawlSpider";
//        }
//        String urls = httpServletRequest.getParameter("urls");
//        String email = httpServletRequest.getParameter("email");
//        if (StringUtils.isEmpty(urls)) {
//            modelMap.addAttribute("message_urls", "url输入有误，请重新输入");
//            return "bbs/CrawlSpider";
//        }
//        Iterable<String> urlList = Splitter.on("\r\n").split(urls);
//        for (String url : urlList) {
//            String templateName = getTemplate(url);
//            if (StringUtils.isEmpty(templateName)) {
//                modelMap.addAttribute("message_urls", "url输入有误，请重新输入" + url);
//                return "bbs/CrawlSpider";
//            }
//        }
//        for (String url : urlList) {
//            String templateName = getTemplate(url);
//            try {
//                Resource resource = new ClassPathResource(templateName);
//                String jsonStr = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
//                if (StringUtils.isNotBlank(email)) {
//                    jsonStr = jsonStr.replace("#email#", email);
//                }
//                String newJsonStr = jsonStr.replace("#startUrls#", url);
//                ConfigurableSpider configurableSpider = ConfigurableSpider.create(newJsonStr);
//                Spider spider = configurableSpider.build();
//                spider.run();
//                logger.info("Spider {} closed! {} pages downloaded. url:{}",
//                        spider.getUUID(), spider.getPageCount(), url);
//            } catch (Exception e) {
//                logger.error("爬虫抓取 url:{} 异常", url, e);
//            }
//        }
//        return "bbs/CrawlSpider";
//    }
//
//    private String getTemplate(String url) {
//        for (String key : spiderTemplateMap.keySet()) {
//            if (url.indexOf(key) >= 0) {
//                return spiderTemplateMap.get(key);
//            }
//        }
//        return null;
//    }
//
//}