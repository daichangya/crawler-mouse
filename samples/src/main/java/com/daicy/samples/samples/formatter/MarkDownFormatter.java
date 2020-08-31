package com.daicy.samples.samples.formatter;


import com.daicy.crawler.extension.model.formatter.ObjectFormatter;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author daichangya@163.com
 */
public class MarkDownFormatter implements ObjectFormatter<String> {

    private static FlexmarkHtmlConverter flexmarkHtmlConverter = FlexmarkHtmlConverter.builder().build();

    @Override
    public String format(String raw) {
        if(StringUtils.isEmpty(raw)){
            return raw;
        }
        return flexmarkHtmlConverter.convert(raw);
    }

    @Override
    public Class<String> clazz() {
        return String.class;
    }

    @Override
    public void initParam(String[] extra) {

    }
}
