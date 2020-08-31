package com.daicy.crawler.core.plugin;


import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Request;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.core.plugin
 * @date:8/25/20
 */
public interface AfterDownloadPlugin extends Plugin {

    void handle(Request request, Page page);

}
