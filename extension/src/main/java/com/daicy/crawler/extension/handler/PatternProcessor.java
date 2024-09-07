package com.daicy.crawler.extension.handler;

/**
 * @author daichangya@163.com
 */
public abstract class PatternProcessor extends PatternRequestMatcher implements SubPipeline, SubPageProcessor {
    /**
     * @param pattern url pattern to handle
     */
    public PatternProcessor(String pattern) {
        super(pattern);
    }
}
