package com.daicy.crawler.common.utils;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.common.utils
 * @date:9/7/20
 */
public class StringUtils {

    /**
     * 字符串格式化(占位符填充, 占位符: {}, 占位符可以自定义)
     *
     * @param origin
     * @param args
     * @return
     */
    public static String stringFormat(String origin, String... args) {
        StringBuffer result = new StringBuffer(origin);
        String rep = "{}";
        for (String arg : args) {
            int start = result.indexOf(rep);
            int end = start + rep.length();
            result.replace(start, end, arg);
        }
        return result.toString();
    }

}
