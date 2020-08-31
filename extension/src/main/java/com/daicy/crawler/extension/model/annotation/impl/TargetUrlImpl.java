package com.daicy.crawler.extension.model.annotation.impl;

import com.daicy.crawler.extension.model.annotation.TargetUrl;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension.model.annotation.impl
 * @date:8/26/20
 */
public class TargetUrlImpl implements TargetUrl {

    private String[] value;

    private String sourceRegion = StringUtils.EMPTY;

    public TargetUrlImpl() {
    }

    public TargetUrlImpl(String[] value) {
        this.value = value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public void setSourceRegion(String sourceRegion) {
        this.sourceRegion = sourceRegion;
    }

    @Override
    public String[] value() {
        return value;
    }

    @Override
    public String sourceRegion() {
        return sourceRegion;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
