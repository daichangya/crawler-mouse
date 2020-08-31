package com.daicy.crawler.extension.model.annotation.impl;

import com.daicy.crawler.extension.model.annotation.ComboExtract;
import com.daicy.crawler.extension.model.annotation.ExtractBy;

import java.lang.annotation.Annotation;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension.model.annotation.impl
 * @date:8/26/20
 */
public class ComboExtractImpl implements ComboExtract {
    private ExtractBy[] value;

    private Op op;

    private boolean notNull = false;

    private Source source;

    private boolean multi = false;

    public void setValue(ExtractBy[] value) {
        this.value = value;
    }

    public void setOp(Op op) {
        this.op = op;
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
    public ExtractBy[] value() {
        return value;
    }

    @Override
    public Op op() {
        return op;
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
