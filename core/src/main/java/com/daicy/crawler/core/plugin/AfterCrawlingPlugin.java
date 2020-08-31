package com.daicy.crawler.core.plugin;

import com.daicy.crawler.core.Spider;

/**
 * Plugin type that is called after the crawling phase is finished. Examples: report generation,
 * test generation
 */
public interface AfterCrawlingPlugin extends Plugin {

	/**
	 * Method that is called after the crawling is finished. Warning: changing the session can
	 * change the behavior of other post crawl plugins. It is not a copy!
	 *
	 * @param spider The {@link Spider} for the coming crawl.
	 */
	void handle(Spider spider);

}
