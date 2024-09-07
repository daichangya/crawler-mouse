package com.daicy.crawler.extension.model;



import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.daicy.crawler.core.selector.Selector;
import com.daicy.crawler.extension.model.formatter.ObjectFormatter;
import com.daicy.crawler.extension.model.sources.Source;
import lombok.Getter;
import lombok.Setter;

/**
 * Wrapper of field and extractor.
 * @author daichangya@163.com <br>
 * @since 0.2.0
 */
public class FieldExtractor extends Extractor {

    @Getter
    private final Field field;

    @Getter @Setter
    private Method setterMethod;

    @Getter @Setter
    private ObjectFormatter objectFormatter;

    public FieldExtractor(Field field, Selector selector, Source source, boolean notNull, boolean multi) {
        super(selector, source, notNull, multi);
        this.field = field;
    }
}
