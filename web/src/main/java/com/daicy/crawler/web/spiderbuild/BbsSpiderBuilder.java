//package com.daicy.crawler.web.spiderbuild;
//
//import com.daicy.crawler.common.model.BrowserType;
//import com.daicy.crawler.core.Site;
//import com.daicy.crawler.core.Spider;
//import com.daicy.crawler.core.plugin.Plugins;
//import com.daicy.crawler.downer.WebDriverDownloader;
//import com.daicy.crawler.extension.RedisProxyProvider;
//import com.daicy.crawler.web.plugin.AfterCrawlingZip;
//import com.daicy.crawler.web.pipeline.ImageDownPipeline;
//import com.google.common.collect.Lists;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.firefox.FirefoxProfile;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.Protocol;
//
//import java.util.Map;
//
///**
// * @author: create by daichangya
// * @version: v1.0
// * @description: com.daicy.crawler.web.processor
// * @date:8/25/20
// */
//public class BbsSpiderBuilder {
//    public Spider build(Map<String, String> parameters) {
//        JedisPool jedisPool = new JedisPool(new GenericObjectPoolConfig(), "10.20.54.45", 6055, Protocol.DEFAULT_TIMEOUT, "redistest6055", 2);
//        RedisProxyProvider redisProxyProvider = new RedisProxyProvider(jedisPool);
//        Plugins plugins = new Plugins();
//        plugins.addPlugins(Lists.newArrayList(new AfterCrawlingZip()));
//        FirefoxOptions firefoxOptions = new FirefoxOptions();
//        FirefoxProfile profile = new FirefoxProfile();
////        profile.setPreference("permissions.default.image", 2);
//        profile.setPreference("browser.migration.version", 9001);
//        profile.setPreference("permissions.default.stylesheet", 2);
//        firefoxOptions.setProfile(profile);
//        firefoxOptions.setCapability("marionette", true);
//        firefoxOptions.setHeadless(true);
//
//        Site site = Site.me().setCycleRetryTimes(1).setRetryTimes(3).setSleepTime(1000)
//                .setDriverOptions(firefoxOptions)
//                .setProxyProvider(redisProxyProvider).setBrowserType(BrowserType.FIREFOX).setUseRealBrowser(true);
//
//        return Spider.create(new AutohomeProcessor().setSite(site))
//                .addPipeline(new ImageDownPipeline())
//                .setDownloader(new WebDriverDownloader(4)).thread(2)
//                .addUrl(parameters.get("MAINURL")).setPlugins(plugins).setParameters(parameters);
//    }
//
//
//}
