package com.daicy.crawler.extension.handler;

import com.daicy.crawler.core.ResultItems;
import com.daicy.crawler.core.Task;

/**
 * @author daichangya@163.com
 * @since 0.5.0
 */
public interface SubPipeline extends RequestMatcher {

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param resultItems resultItems
     * @param task task
     * @return whether continue to match
     */
    public MatchOther processResult(ResultItems resultItems, Task task);

}
