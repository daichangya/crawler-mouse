package com.daicy.samples.model.samples;

import com.daicy.crawler.common.InterfaceAdapter;
import com.daicy.crawler.common.utils.JsonUtils;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Spider;
import com.daicy.crawler.extension.model.ConsolePageModelPipeline;
import com.daicy.crawler.extension.model.DynamicModel;
import com.daicy.crawler.extension.model.FieldModel;
import com.daicy.crawler.extension.model.OOSpider;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.model.annotation.TargetUrl;
import com.daicy.crawler.extension.model.annotation.impl.ExtractByImpl;
import com.daicy.crawler.extension.model.annotation.impl.TargetUrlImpl;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.samples.model.samples
 * @date:8/26/20
 */
@TargetUrl("http://juejin.im/post/\\w+")
public class JuejinModel {

    @ExtractBy(value = "#juejin > div.view-container > main > div > div.main-area.article-area.shadow > article > h1", type = ExtractBy.Type.Css)
    private String title;

    public static void main(String[] args) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ExtractBy.class, InterfaceAdapter.interfaceSerializer(ExtractByImpl.class));
        builder.registerTypeAdapter(TargetUrl.class, InterfaceAdapter.interfaceSerializer(TargetUrlImpl.class));
        Gson gson = builder.create();
        String jsonStr = "{\n" +
                "    \"targetUrl\": {\n" +
                "        \"value\": [\n" +
                "            \"http://juejin.im/post/\\\\w+\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"fieldModels\": [\n" +
                "        {\n" +
                "            \"name\": \"title\",\n" +
                "            \"extractBy\": {\n" +
                "                \"value\": \"#juejin > div.view-container > main > div > div.main-area.article-area.shadow > article > h1\",\n" +
                "                \"type\": \"Css\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        DynamicModel dynamicModel = gson.fromJson(jsonStr, DynamicModel.class);
//        FieldModel fieldModel = new FieldModel("title");
//        fieldModel.setExtractBy(new ExtractByImpl("#juejin > div.view-container > main > div > div.main-area.article-area.shadow > article > h1", ExtractBy.Type.Css));
//        DynamicModel dynamicModel = new DynamicModel();
//        dynamicModel.setFieldModels(Lists.newArrayList(fieldModel));
//        dynamicModel.setTargetUrl(new TargetUrlImpl(new String[]{"http://juejin.im/post/\\w+"}));
        Spider qqSpider = OOSpider.create(Site.me().setSleepTime(100).setTimeOut(3000).setRetryTimes(2)
                        .setUserAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)")
                , new ConsolePageModelPipeline(), dynamicModel).addUrl("http://juejin.im/post/5e216eda6fb9a0300e1617eb").thread(2);
        System.out.println(JsonUtils.toJson(qqSpider));
    }
}
