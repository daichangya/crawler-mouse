package com.daicy.crawler.extension.configurable;

import com.daicy.crawler.common.InterfaceAdapter;
import com.daicy.crawler.common.utils.JsonUtils;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.extension.model.DynamicModel;
import com.daicy.crawler.extension.model.FieldModel;
import com.daicy.crawler.extension.model.annotation.*;
import com.daicy.crawler.extension.model.annotation.impl.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.Map;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension.configurable
 * @date:8/26/20
 */
public class ConfigurableSpiderTest {

    @Test
    public void setPipeline2() {
        Map<DynamicModel, DynamicModel> dynamicModelMap = Maps.newHashMap();
        FieldModel fieldModel = new FieldModel("title");
        fieldModel.setExtractBy(new ExtractByImpl("#juejin > div.view-container > main > div > div.main-area.article-area.shadow > article > h1", ExtractBy.Type.Css));
        DynamicModel dynamicModel = new DynamicModel();
        dynamicModel.setFieldModels(Lists.newArrayList(fieldModel));
        dynamicModel.setTargetUrl(new TargetUrlImpl(new String[]{"http://juejin.im/post/\\w+"}));
        dynamicModel.setFieldModels(Lists.newArrayList(fieldModel));
        dynamicModelMap.put(dynamicModel, dynamicModel);
        System.out.println(JsonUtils.toJson(dynamicModelMap));
    }

    @Test
    public void setPipeline() {
        ConfigurableSpider configurableSpider = new ConfigurableSpider();
        FieldModel fieldModel = new FieldModel("title");
        fieldModel.setExtractBy(new ExtractByImpl("#juejin > div.view-container > main > div > div.main-area.article-area.shadow > article > h1", ExtractBy.Type.Css));
        DynamicModel dynamicModel = new DynamicModel();
        dynamicModel.setFieldModels(Lists.newArrayList(fieldModel));
        dynamicModel.setTargetUrl(new TargetUrlImpl(new String[]{"http://juejin.im/post/\\w+"}));
        dynamicModel.setFieldModels(Lists.newArrayList(fieldModel));
//        configurableSpider.setDynamicModel(dynamicModel);
//        configurableSpider.setPipeline("com.daicy.crawler.extension.model.ConsolePageModelPipeline");
        configurableSpider.setSite(Site.me().setSleepTime(100).setTimeOut(3000).setRetryTimes(2)
                .setUserAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)"));
        configurableSpider.setThreadNum(2);
        configurableSpider.setStartUrls(new String[]{"http://juejin.im/post/5e216eda6fb9a0300e1617eb"});
        System.out.println(JsonUtils.toJson(configurableSpider));
//        configurableSpider.build().run();
        String jsonStr = "{\n" +
                "    \"pipelineToModel\": {\n" +
                "        \"com.daicy.crawler.web.pipeline.ListDownPipeline\": [\n" +
                "            {\n" +
                "                \"targetUrl\": {\n" +
                "                    \"value\": [\n" +
                "                        \".*bbs.pcauto.com.cn/forum-.*html\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                \"helpUrl\": {\n" +
                "                    \"value\": [\n" +
                "                        \".*bbs.pcauto.com.cn/topic-.*html\"\n" +
                "                    ],\n" +
                "                    \"sourceRegion\": \"//*[@id='topic_list']/form/table\"\n" +
                "                },\n" +
                "                \"extractBy\": {\n" +
                "                    \"value\": \"#topic_list > form > table\",\n" +
                "                    \"type\": \"Css\"\n" +
                "                },\n" +
                "                \"fieldModels\": [\n" +
                "                    {\n" +
                "                        \"name\": \"title\",\n" +
                "                        \"extractBy\": {\n" +
                "                            \"value\": \"/html/head/title\",\n" +
                "                            \"source\": \"RawHtml\"\n" +
                "                        }\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"content\",\n" +
                "                        \"extractBy\": {\n" +
                "                            \"value\": \"/tbody\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ],\n" +
                "        \"com.daicy.crawler.web.pipeline.ImageDownPipeline\": [\n" +
                "            {\n" +
                "                \"targetUrl\": {\n" +
                "                    \"value\": [\n" +
                "                        \".*bbs.pcauto.com.cn/topic-.*html\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                \"extractBy\": {\n" +
                "                    \"value\": \"//*[@id='post_message_first']\"\n" +
                "                },\n" +
                "                \"fieldModels\": [\n" +
                "                    {\n" +
                "                        \"name\": \"title\",\n" +
                "                        \"extractBy\": {\n" +
                "                            \"value\": \"#subjectTitle\",\n" +
                "                            \"type\": \"Css\",\n" +
                "                            \"source\": \"RawHtml\"\n" +
                "                        }\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"content\",\n" +
                "                        \"extractBy\": {\n" +
                "                            \"value\": \"div.post_msg\",\n" +
                "                            \"type\": \"Css\"\n" +
                "                        }\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"imageList\",\n" +
                "                        \"extractBy\": {\n" +
                "                            \"value\": \"/img/@src\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"startUrls\": [\n" +
                "        \"https://bbs.pcauto.com.cn/forum-20308.html\"\n" +
                "    ],\n" +
                "    \"site\": {\n" +
                "        \"userAgent\": \"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)\",\n" +
                "        \"retryTimes\": 2\n" +
                "    },\n" +
                "    \"threadNum\": 5\n" +
                "}";
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ExtractBy.class, InterfaceAdapter.interfaceSerializer(ExtractByImpl.class));
        builder.registerTypeAdapter(TargetUrl.class, InterfaceAdapter.interfaceSerializer(TargetUrlImpl.class));
        builder.registerTypeAdapter(ComboExtract.class, InterfaceAdapter.interfaceSerializer(ComboExtractImpl.class));
        builder.registerTypeAdapter(ExtractByUrl.class, InterfaceAdapter.interfaceSerializer(ExtractByUrlImpl.class));
        builder.registerTypeAdapter(Formatter.class, InterfaceAdapter.interfaceSerializer(FormatterImpl.class));
        builder.registerTypeAdapter(HelpUrl.class, InterfaceAdapter.interfaceSerializer(HelpUrlImpl.class));
        builder.disableHtmlEscaping();
        Gson gson = builder.create();
        configurableSpider = gson.fromJson(jsonStr, ConfigurableSpider.class);
        configurableSpider.build().run();
    }
}
