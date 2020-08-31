package com.daicy.crawler.extension.model;

import com.daicy.crawler.core.ResultItems;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.pipeline.CollectorPipeline;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.pipeline.CollectorPageModelPipeline;

import java.util.List;

/**
 * @author code4crafter@gmail.com
 * @since 0.4.0
 */
class PageModelCollectorPipeline<T> implements CollectorPipeline<T> {

    private final CollectorPageModelPipeline<T> classPipeline = new CollectorPageModelPipeline<T>();

    private final DynamicModel dynamicModel;

    PageModelCollectorPipeline(DynamicModel dynamicModel) {
        this.dynamicModel = dynamicModel;
    }

    @Override
    public List<T> getCollected() {
        return classPipeline.getCollected();
    }

    @Override
    public synchronized void process(ResultItems resultItems, Task task) {
        Object o = resultItems.get(dynamicModel.getModelName());
        if (o != null) {
            ExtractBy extractBy = dynamicModel.getExtractBy();
            if (extractBy == null || !extractBy.multi()) {
                classPipeline.process((T) o, task);
            } else {
                List<Object> list = (List<Object>) o;
                for (Object o1 : list) {
                    classPipeline.process((T) o1, task);
                }
            }
        }
    }
}
