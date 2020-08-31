package com.daicy.crawler.extension.model;

import com.daicy.crawler.common.InterfaceAdapter;
import com.daicy.crawler.common.utils.JsonUtils;
import com.daicy.crawler.core.Request;
import com.daicy.crawler.extension.model.annotation.*;
import com.daicy.crawler.extension.model.annotation.impl.*;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension.model
 * @date:8/26/20
 */
public class DynamicModelTest {

    @Test
    public void setModelName() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ExtractBy.class, InterfaceAdapter.interfaceSerializer(ExtractByImpl.class));
        builder.registerTypeAdapter(TargetUrl.class, InterfaceAdapter.interfaceSerializer(TargetUrlImpl.class));
        builder.registerTypeAdapter(ComboExtract.class, InterfaceAdapter.interfaceSerializer(ComboExtractImpl.class));
        builder.registerTypeAdapter(ExtractByUrl.class, InterfaceAdapter.interfaceSerializer(ExtractByUrlImpl.class));
        builder.registerTypeAdapter(Formatter.class, InterfaceAdapter.interfaceSerializer(FormatterImpl.class));
        builder.registerTypeAdapter(HelpUrl.class, InterfaceAdapter.interfaceSerializer(HelpUrlImpl.class));
        builder.disableHtmlEscaping();
        Gson gson = builder.create();

        FieldModel fieldModel = new FieldModel("title");
        fieldModel.setExtractBy(new ExtractByImpl("'", ExtractBy.Type.Css));
        DynamicModel dynamicModel = new DynamicModel();
        dynamicModel.setFieldModels(Lists.newArrayList(fieldModel));
        dynamicModel.setTargetUrl(new TargetUrlImpl(new String[]{"http://juejin.im/post/\\w+"}));
        dynamicModel.setFieldModels(Lists.newArrayList(fieldModel));
        String jsonStr = gson.toJson(dynamicModel);
        System.out.println(jsonStr);
        System.out.println(gson.fromJson(jsonStr, DynamicModel.class));
    }

    @Test
    public void testExtractBy() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ExtractBy.class, InterfaceAdapter.interfaceSerializer(ExtractByImpl.class));
        builder.registerTypeAdapter(TargetUrl.class, InterfaceAdapter.interfaceSerializer(TargetUrlImpl.class));
        builder.registerTypeAdapter(ComboExtract.class, InterfaceAdapter.interfaceSerializer(ComboExtractImpl.class));
        builder.registerTypeAdapter(ExtractByUrl.class, InterfaceAdapter.interfaceSerializer(ExtractByUrlImpl.class));
        builder.registerTypeAdapter(Formatter.class, InterfaceAdapter.interfaceSerializer(FormatterImpl.class));
        builder.registerTypeAdapter(HelpUrl.class, InterfaceAdapter.interfaceSerializer(HelpUrlImpl.class));
        builder.disableHtmlEscaping();
        Gson gson = builder.create();

        TargetUrlImpl targetUrl = new TargetUrlImpl();
//        extractBy.setSource(ExtractBy.Source.RawHtml);
        targetUrl.setValue(new String[]{"//*[@id='post_message_first']"});
        String jsonStr = gson.toJson(targetUrl);
        System.out.println(jsonStr);
        targetUrl = gson.fromJson(jsonStr, TargetUrlImpl.class);
        System.out.println(targetUrl);
    }


    @Test
    public void testRegx() {
        String regx = ".*bbs.pcauto.com.cn/forum-.*html";
        String link = "https://bbs.pcauto.com.cn/forum-20308.html";
        Pattern targetUrlPattern = Pattern.compile(regx);
        Matcher matcher = targetUrlPattern.matcher(link);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
        }
    }
}
