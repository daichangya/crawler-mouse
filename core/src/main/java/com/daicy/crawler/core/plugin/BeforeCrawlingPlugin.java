package com.daicy.crawler.core.plugin;

import com.daicy.crawler.core.Spider;

import java.net.URL;

/**
 * {@link Plugin} that is called before the crawling starts and before the initial URL has been
 * loaded. This kind of plugins can be used to do for example 'once in a crawl session' operations
 * like logging in a web application or reset the database to a 'clean' state.
 */
public interface BeforeCrawlingPlugin extends Plugin {

    /**
     * Method that is called before Crawljax loads the initial {@link URL} and before the core
     * starts crawling.
     *
     * @param spider The {@link Spider} for the coming crawl.
     */
    void handle(Spider spider);
}
