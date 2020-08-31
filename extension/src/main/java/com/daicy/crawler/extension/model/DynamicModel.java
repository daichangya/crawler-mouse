package com.daicy.crawler.extension.model;

import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.model.annotation.HelpUrl;
import com.daicy.crawler.extension.model.annotation.TargetUrl;

import java.util.List;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension.model
 * @date:8/26/20
 */
public class DynamicModel {

    public static final String KEY = "DynamicModel";

    private TargetUrl targetUrl;

    private HelpUrl helpUrl;

    private ExtractBy extractBy;

    private List<FieldModel> fieldModels;

    private String modelName;

    private transient Class clazz;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
        this.modelName = clazz.getCanonicalName();
    }

    public TargetUrl getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(TargetUrl targetUrl) {
        this.targetUrl = targetUrl;
    }

    public HelpUrl getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(HelpUrl helpUrl) {
        this.helpUrl = helpUrl;
    }

    public ExtractBy getExtractBy() {
        return extractBy;
    }

    public void setExtractBy(ExtractBy extractBy) {
        this.extractBy = extractBy;
    }

    public List<FieldModel> getFieldModels() {
        return fieldModels;
    }

    public void setFieldModels(List<FieldModel> fieldModels) {
        this.fieldModels = fieldModels;
    }
}
