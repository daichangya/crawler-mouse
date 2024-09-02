package com.daicy.crawler.extension.model.formatter;

/**
 * @author daichangya@163.com
 */
public interface ObjectFormatter<T> {

    T format(String raw) throws Exception;

    Class<T> clazz();

    void initParam(String[] extra);

}
