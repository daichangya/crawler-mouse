package com.daicy.crawler.extension.handler;

import com.daicy.crawler.core.Page;

/**
 * @author daichangya@163.com
 */
public interface SubPageProcessor extends RequestMatcher {

	/**
	 * process the page, extract urls to fetch, extract the data and store
	 *
	 * @param page page
	 *
	 * @return whether continue to match
	 */
	public MatchOther processPage(Page page);

}
