//package com.daicy.crawler.web.jobhandler;
//
//import com.daicy.crawler.common.utils.JsonUtils;
//import com.daicy.crawler.core.Spider;
//import com.daicy.crawler.extension.configurable.ConfigurableSpider;
//import com.google.common.base.Splitter;
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.annotation.XxlJob;
//import com.xxl.job.core.log.XxlJobLogger;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ResourceUtils;
//
//import java.io.File;
//import java.util.Map;
//
///**
// * XxlJob开发示例（Bean模式）
// * <p>
// * 开发步骤：
// * 1、在Spring Bean实例中，开发Job方法，方式格式要求为 "public ReturnT<String> execute(String param)"
// * 2、为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。
// * 3、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
// *
// * @author xuxueli 2019-12-11 21:52:51
// */
//@Component
//public class CrawlerXxlJob {
//    private static Logger logger = LoggerFactory.getLogger(CrawlerXxlJob.class);
//
//
//    /**
//     * 1、简单任务示例（Bean模式）
//     */
//    @XxlJob("autohomeBBs")
//    public ReturnT<String> autohomeBBs(String param) throws Exception {
//        XxlJobLogger.log("XXL-JOB, autohomeBBs.");
//        Map<String, String> parameters = JsonUtils.toObject(param, Map.class);
//        String startUrls = parameters.get("startUrls");
//        if (StringUtils.isEmpty(startUrls)) {
//            XxlJobLogger.log("XXL-JOB, startUrls is null.");
//            return ReturnT.FAIL;
//        }
//        File jsonFile = ResourceUtils.getFile("classpath:" + parameters.get("templateName"));
//        String jsonStr = FileUtils.readFileToString(jsonFile);
//        Iterable<String> urlList = Splitter.on(",").split(startUrls);
//        for (String url : urlList) {
//            String newJsonStr = jsonStr.replace("#startUrls#", url);
//            ConfigurableSpider configurableSpider = ConfigurableSpider.create(newJsonStr);
//            Spider spider = configurableSpider.build();
//            spider.run();
//            XxlJobLogger.log("Spider {} closed! {} pages downloaded.",
//                    spider.getUUID(), spider.getPageCount());
//
//        }
//        return ReturnT.SUCCESS;
//    }
//
////    @XxlJob("pcautoBBs")
////    public ReturnT<String> pcautoBBs(String param) throws Exception {
////        XxlJobLogger.log("XXL-JOB, pcautoBBs.");
////        Map<String, String> parameters = JsonUtils.toObject(param, Map.class);
////        String startUrls = parameters.get("startUrls");
////        if (StringUtils.isEmpty(startUrls)) {
////            XxlJobLogger.log("XXL-JOB, startUrls is null.");
////            return ReturnT.FAIL;
////        }
////        File jsonFile = ResourceUtils.getFile("classpath:" + parameters.get("templateName"));
////        String jsonStr = FileUtils.readFileToString(jsonFile);
////        Iterable<String> urlList = Splitter.on(",").split(startUrls);
////        for (String url : urlList) {
////            jsonStr = jsonStr.replace("#startUrls#", url);
////            ConfigurableSpider configurableSpider = ConfigurableSpider.create(jsonStr);
////            configurableSpider.build().run();
////        }
////        return ReturnT.SUCCESS;
////    }
//
//    public void init() {
//        logger.info("init");
//    }
//
//    public void destroy() {
//        logger.info("destory");
//    }
//
//
//}
