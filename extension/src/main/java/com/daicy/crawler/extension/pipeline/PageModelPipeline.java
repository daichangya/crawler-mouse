package com.daicy.crawler.extension.pipeline;

import com.daicy.crawler.core.Task;

/**
 * Implements PageModelPipeline to persistent your page model.
 *
 * @author daichangya@163.com <br>
 * @since 0.2.0
 */
public interface PageModelPipeline<T> {

    public void process(T t, Task task);

}
