package com.daicy.crawler.extension.model.annotation.impl;

import com.daicy.crawler.extension.model.annotation.Formatter;
import com.daicy.crawler.extension.model.formatter.ObjectFormatter;

import java.lang.annotation.Annotation;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension.model.annotation.impl
 * @date:8/26/20
 */
public class FormatterImpl implements Formatter {
    private String[] value;

    private Class subClazz;

    private Class<? extends ObjectFormatter> formatter = Formatter.DEFAULT_FORMATTER;


    public void setValue(String[] value) {
        this.value = value;
    }

    public void setSubClazz(Class subClazz) {
        this.subClazz = subClazz;
    }

    public void setFormatter(Class<? extends ObjectFormatter> formatter) {
        this.formatter = formatter;
    }

    @Override
    public String[] value() {
        return value;
    }

    @Override
    public Class subClazz() {
        return subClazz;
    }

    @Override
    public Class<? extends ObjectFormatter> formatter() {
        return formatter;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
