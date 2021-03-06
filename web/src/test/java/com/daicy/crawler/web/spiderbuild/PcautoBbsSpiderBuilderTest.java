package com.daicy.crawler.web.spiderbuild;

import com.daicy.crawler.common.InterfaceAdapter;
import com.daicy.crawler.extension.configurable.ConfigurableSpider;
import com.daicy.crawler.extension.model.annotation.*;
import com.daicy.crawler.extension.model.annotation.impl.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.web.processor
 * @date:8/27/20
 */
public class PcautoBbsSpiderBuilderTest {

    @Test
    public void build() {
        System.setProperty("data.home",System.getProperty("user.home")+"/data");

//        Plugins plugins = new Plugins();
//        plugins.addPlugins(Lists.newArrayList(new AfterCrawlingZip()));
//
//        FirefoxOptions firefoxOptions = new FirefoxOptions();
//
////        FirefoxProfile profile = new FirefoxProfile();
//////        profile.setPreference("permissions.default.image", 2);
////        profile.setPreference("browser.migration.version", 9001);
////        profile.setPreference("permissions.default.stylesheet", 2);
////        firefoxOptions.setProfile(profile);
////        firefoxOptions.setCapability("marionette", true);
////        firefoxOptions.setHeadless(true);
//
//        Site site = Site.me().setCycleRetryTimes(1).setRetryTimes(3).setSleepTime(1000)
//                .setDriverOptions(firefoxOptions).setBrowserType(BrowserType.FIREFOX);
//
//        return Spider.create(new PcautoProcessor().setSite(site))
//                .addPipeline(new ImageDownPipeline())
//                .setDownloader(new HttpClientDownloader()).thread(2)
//                .addUrl("").setPlugins(plugins).setParameters(parameters);

        String jsonStr = "{\n" +
                "  \"pipelineToModel\": {\n" +
                "    \"com.daicy.crawler.web.pipeline.ListDownPipeline\": [\n" +
                "      {\n" +
                "        \"targetUrl\": {\n" +
                "          \"value\": [\n" +
                "            \".*club.autohome.com.cn/bbs/forum-.*html\"\n" +
                "          ]\n" +
                "        },\n" +
                "        \"fieldModels\": [\n" +
                "          {\n" +
                "            \"name\": \"title\",\n" +
                "            \"extractBy\": {\n" +
                "              \"value\": \"/html/head/title/text()\",\n" +
                "              \"source\": \"RawHtml\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"content\",\n" +
                "            \"extractBy\": {\n" +
                "              \"value\": \"//*/ul[@class='post-list']\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"links\",\n" +
                "            \"type\": \"java.util.List\",\n" +
                "            \"extractBy\": {\n" +
                "              \"value\": \"//*/p[@class='post-title']/a/@href\"\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ],\n" +
                "    \"com.daicy.crawler.web.pipeline.ImageDownPipeline\": [\n" +
                "      {\n" +
                "        \"targetUrl\": {\n" +
                "          \"value\": [\n" +
                "            \".*club.autohome.com.cn/bbs/thread/.*/.*\"\n" +
                "          ]\n" +
                "        },\n" +
                "        \"extractBy\": {\n" +
                "          \"value\": \"//*/div[@id='maxwrap-maintopic']//*/div[@class='rconten']\"\n" +
                "        },\n" +
                "        \"fieldModels\": [\n" +
                "          {\n" +
                "            \"name\": \"title\",\n" +
                "            \"extractBy\": {\n" +
                "              \"value\": \"//*/div[@class='maxtitle']/text()\",\n" +
                "              \"source\": \"RawHtml\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"content\",\n" +
                "            \"extractBy\": {\n" +
                "              \"value\": \"//*/div[@class='conttxt']\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"imageList\",\n" +
                "            \"type\": \"java.util.List\",\n" +
                "            \"extractBy\": {\n" +
                "              \"value\": \"//*/img/@src\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"ttfUrl\",\n" +
                "            \"extractBy\": {\n" +
                "              \"value\": \"[A-Za-z0-9-_/.]*..ttf\",\n" +
                "              \"type\": \"Regex\",\n" +
                "              \"source\": \"RawHtml\"\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"startUrls\": [\n" +
                "    \"https://club.autohome.com.cn/bbs/forum-c-526-1.html\"\n" +
                "  ],\n" +
                "  \"site\": {\n" +
                "    \"userAgent\": \"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)\",\n" +
                "    \"retryTimes\": 2,\n" +
                "    \"useRealBrowser\": true,\n" +
                "    \"userProxyProvider\": true\n" +
                "  },\n" +
                "  \"threadNum\": 3,\n" +
                "  \"plugins\": [\n" +
                "    \"com.daicy.crawler.web.plugin.AfterCrawlingZip\"\n" +
                "  ],\n" +
                "  \"parameters\":{\n" +
                "    \"email\":\"#email#\"\n" +
                "  }\n" +
                "}";
        ConfigurableSpider configurableSpider = ConfigurableSpider.create(jsonStr);
        configurableSpider.build().run();

    }
}
