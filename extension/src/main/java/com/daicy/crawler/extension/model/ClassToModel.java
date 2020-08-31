package com.daicy.crawler.extension.model;

import com.daicy.crawler.common.InterfaceAdapter;
import com.daicy.crawler.extension.model.annotation.*;
import com.daicy.crawler.extension.model.annotation.impl.*;
import com.daicy.crawler.extension.utils.ClassUtils;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension.model
 * @date:8/26/20
 */
public class ClassToModel {

    private static Logger logger = LoggerFactory.getLogger(ClassToModel.class);

    private static Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ExtractBy.class, InterfaceAdapter.interfaceSerializer(ExtractByImpl.class));
        builder.registerTypeAdapter(TargetUrl.class, InterfaceAdapter.interfaceSerializer(TargetUrlImpl.class));
        builder.registerTypeAdapter(ComboExtract.class, InterfaceAdapter.interfaceSerializer(ComboExtractImpl.class));
        builder.registerTypeAdapter(ExtractByUrl.class, InterfaceAdapter.interfaceSerializer(ExtractByUrlImpl.class));
        builder.registerTypeAdapter(Formatter.class, InterfaceAdapter.interfaceSerializer(FormatterImpl.class));
        builder.registerTypeAdapter(HelpUrl.class, InterfaceAdapter.interfaceSerializer(HelpUrlImpl.class));
        gson = builder.create();
    }


    public static String toJson(Object ob) {
        if (null == ob) {
            throw new RuntimeException("this ob must not null");
        }
        return gson.toJson(ob);
    }

    public static <T> T toObject(String json, Class<T> clz) {
        if (StringUtils.isBlank(json)) {
            throw new RuntimeException("this json must not be blank");
        }
        // 替换掉json里带有 单个反斜杠 避免转换异常
        json = StringUtils.replace(json, "\\", "\\\\");
        TypeToken<T> typeToken = TypeToken.of(clz);
        return gson.fromJson(json.toString(), typeToken.getType());
    }

    public static Class toClass(String className) {
        try {
            return org.apache.commons.lang3.ClassUtils.getClass(className);
        } catch (Exception e) {
            logger.error("toClass error className:{}", className, e);
            return String.class;
        }
    }

    public static DynamicModel[] toModel(Class... pageModels) {
        DynamicModel[] dynamicModels = new DynamicModel[pageModels.length];
        for (int i = 0; i < pageModels.length; i++) {
            dynamicModels[i] = toModel(pageModels[i]);
        }
        return dynamicModels;
    }

    public static DynamicModel toModel(Class clazz) {
        DynamicModel dynamicModel = new DynamicModel();
        dynamicModel.setClazz(clazz);
        Annotation annotation = clazz.getAnnotation(TargetUrl.class);
        if (annotation != null) {
            dynamicModel.setTargetUrl((TargetUrl) annotation);
        }
        annotation = clazz.getAnnotation(HelpUrl.class);
        if (annotation != null) {
            dynamicModel.setHelpUrl((HelpUrl) annotation);
        }
        annotation = clazz.getAnnotation(ExtractBy.class);
        if (annotation != null) {
            dynamicModel.setExtractBy((ExtractBy) annotation);
        }

        List<FieldModel> fieldModels = Lists.newArrayList();
        for (Field field : ClassUtils.getFieldsIncludeSuperClass(clazz)) {
            FieldModel fieldModel = new FieldModel(field.getName());
            fieldModel.setType(field.getType().getCanonicalName());
            fieldModel.setExtractBy(field.getAnnotation(ExtractBy.class));
            fieldModel.setComboExtract(field.getAnnotation(ComboExtract.class));
            fieldModel.setExtractByUrl(field.getAnnotation(ExtractByUrl.class));
            fieldModel.setFormatter(field.getAnnotation(Formatter.class));
            fieldModels.add(fieldModel);
        }
        dynamicModel.setFieldModels(fieldModels);
        return dynamicModel;
    }
}
