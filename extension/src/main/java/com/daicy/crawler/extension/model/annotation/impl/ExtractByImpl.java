package com.daicy.crawler.extension.model.annotation.impl;

import com.daicy.crawler.extension.model.annotation.ExtractBy;

import java.lang.annotation.Annotation;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension.model.annotation.impl
 * @date:8/26/20
 */
public class ExtractByImpl implements ExtractBy {

    private String value;

    private ValueType valueType = ValueType.Text;

    private boolean isDownLoad = false;

    private ExtractBy.Type type = ExtractBy.Type.XPath;

    private boolean notNull = false;

    private ExtractBy.Source source = ExtractBy.Source.SelectedHtml;

    private boolean multi = false;

    public ExtractByImpl() {
    }

    public ExtractByImpl(String value) {
        this.value = value;
    }

    public ExtractByImpl(String value, Type type) {
        this.value = value;
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public void setDownLoad(boolean downLoad) {
        isDownLoad = downLoad;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public ValueType valueType() {
        return valueType;
    }

    @Override
    public boolean isDownLoad() {
        return isDownLoad;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public boolean notNull() {
        return notNull;
    }

    @Override
    public Source source() {
        return source;
    }

    @Override
    public boolean multi() {
        return multi;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
