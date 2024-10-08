package com.daicy.crawler.core.pipeline;

import java.util.List;

/**
 * Pipeline that can collect and store results. <br>
 * Used for {@link com.daicy.crawler.core.Spider#getAll(java.util.Collection)}
 *
 * @author daichangya@163.com
 * @since 0.4.0
 */
public interface CollectorPipeline<T> extends Pipeline {

    /**
     * Get all results collected.
     *
     * @return collected results
     */
    public List<T> getCollected();
}
