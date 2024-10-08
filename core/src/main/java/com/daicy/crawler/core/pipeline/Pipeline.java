package com.daicy.crawler.core.pipeline;

import com.daicy.crawler.core.ResultItems;
import com.daicy.crawler.core.Task;

/**
 * Pipeline is the persistent and offline process part of crawler.<br>
 * The interface Pipeline can be implemented to customize ways of persistent.
 *
 * @author daichangya@163.com <br>
 * @since 0.1.0
 * @see ConsolePipeline
 * @see FilePipeline
 */
public interface Pipeline {

    /**
     * Process extracted results.
     *
     * @param resultItems resultItems
     * @param task task
     */
    public void process(ResultItems resultItems, Task task);
}
