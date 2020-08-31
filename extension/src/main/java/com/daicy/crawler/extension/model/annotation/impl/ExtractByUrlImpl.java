package com.daicy.crawler.extension.model.annotation.impl;

import com.daicy.crawler.extension.model.annotation.ExtractByUrl;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension.model.annotation.impl
 * @date:8/26/20
 */
public class ExtractByUrlImpl implements ExtractByUrl {
    private String value = StringUtils.EMPTY;

    private boolean notNull = false;

    private boolean multi = false;

    public void setValue(String value) {
        this.value = value;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public boolean notNull() {
        return notNull;
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
