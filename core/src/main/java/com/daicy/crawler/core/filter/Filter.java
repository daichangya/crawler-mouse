package com.daicy.crawler.core.filter;

import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.SpiderContext;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.core
 * @date:9/7/20
 */
public interface Filter {
    boolean filterRequest(Request request, SpiderContext spiderContext);
}
