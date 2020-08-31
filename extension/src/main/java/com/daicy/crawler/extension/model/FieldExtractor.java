package com.daicy.crawler.extension.model;

import com.daicy.crawler.core.selector.Selector;
import com.daicy.crawler.extension.model.formatter.ObjectFormatter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Wrapper of field and extractor.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
class FieldExtractor extends Extractor {

    private final FieldModel fieldModel;

    private ObjectFormatter objectFormatter;

    public FieldExtractor(FieldModel fieldModel, Selector selector, Source source, boolean notNull, boolean multi) {
        super(selector, source, notNull, multi);
        this.fieldModel = fieldModel;
    }

    public FieldModel getFieldModel() {
        return fieldModel;
    }

    Selector getSelector() {
        return selector;
    }

    Source getSource() {
        return source;
    }


    boolean isNotNull() {
        return notNull;
    }

    ObjectFormatter getObjectFormatter() {
        return objectFormatter;
    }

    void setObjectFormatter(ObjectFormatter objectFormatter) {
        this.objectFormatter = objectFormatter;
    }
}
