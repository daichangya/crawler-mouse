package com.daicy.crawler.extension.pipeline;

import com.daicy.crawler.core.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daichangya@163.com
 */
public class CollectorPageModelPipeline<T> implements PageModelPipeline<T> {

    private List<T> collected = new ArrayList<T>();

    @Override
    public synchronized void process(T t, Task task) {
        collected.add(t);
    }

    public List<T> getCollected() {
        return collected;
    }
}
