package com.daicy.crawler.extension.model;

import com.daicy.crawler.core.selector.Selector;
import com.daicy.crawler.extension.model.sources.Source;
import lombok.Getter;
import lombok.Setter;

/**
 * The object contains 'ExtractBy' information.
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
public class Extractor {

    @Getter @Setter
    protected Selector selector;

    @Getter
    protected final Source source;

    protected final boolean notNull;

    protected final boolean multi;
  
    public Extractor(Selector selector, Source source, boolean notNull, boolean multi) {
        this.selector = selector;
        this.source = source;
        this.notNull = notNull;
        this.multi = multi;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public boolean isMulti() {
        return multi;
    }
}
