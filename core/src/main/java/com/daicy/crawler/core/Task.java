package com.daicy.crawler.core;

import com.daicy.crawler.core.pipeline.Pipeline;

import java.util.Map;

/**
 * Interface for identifying different tasks.<br>
 *
 * @author daichangya@163.com <br>
 * @since 0.1.0
 * @see com.daicy.crawler.core.scheduler.Scheduler
 * @see Pipeline
 */
public interface Task {

    /**
     * unique id for a task.
     *
     * @return uuid
     */
    public String getUUID();

    /**
     * site of a task
     *
     * @return site
     */
    public Site getSite();


    public Map<String,String> getParameters();


}
