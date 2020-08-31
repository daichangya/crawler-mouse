package com.daicy.crawler.extension.model;

import com.daicy.crawler.extension.model.annotation.ComboExtract;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.model.annotation.ExtractByUrl;
import com.daicy.crawler.extension.model.annotation.Formatter;
import com.google.gson.annotations.Expose;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension.model
 * @date:8/26/20
 */
public class FieldModel {
    private String name;

    private String type = "java.lang.String";

    private ExtractBy extractBy;

    private ComboExtract comboExtract;

    private ExtractByUrl extractByUrl;

    private Formatter formatter;

    public FieldModel() {
    }

    public FieldModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public Class getClassType() {
        return ClassToModel.toClass(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public ExtractBy getExtractBy() {
        return extractBy;
    }

    public void setExtractBy(ExtractBy extractBy) {
        this.extractBy = extractBy;
    }

    public ComboExtract getComboExtract() {
        return comboExtract;
    }

    public void setComboExtract(ComboExtract comboExtract) {
        this.comboExtract = comboExtract;
    }

    public ExtractByUrl getExtractByUrl() {
        return extractByUrl;
    }

    public void setExtractByUrl(ExtractByUrl extractByUrl) {
        this.extractByUrl = extractByUrl;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }
}
