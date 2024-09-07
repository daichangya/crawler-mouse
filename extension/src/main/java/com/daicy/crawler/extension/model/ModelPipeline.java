package com.daicy.crawler.extension.model;

import com.daicy.crawler.core.ResultItems;
import com.daicy.crawler.core.Task;

import com.daicy.crawler.core.pipeline.Pipeline;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.pipeline.PageModelPipeline;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The extension to Pipeline for page model extractor.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
class ModelPipeline implements Pipeline {

    private Map<Class, PageModelPipeline> pageModelPipelines = new ConcurrentHashMap<Class, PageModelPipeline>();

    public ModelPipeline() {
    }

    public ModelPipeline put(Class clazz, PageModelPipeline pageModelPipeline) {
        pageModelPipelines.put(clazz, pageModelPipeline);
        return this;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<Class, PageModelPipeline> classPageModelPipelineEntry : pageModelPipelines.entrySet()) {
            Object o = resultItems.get(classPageModelPipelineEntry.getKey().getCanonicalName());
            if (o != null) {
                Annotation annotation = classPageModelPipelineEntry.getKey().getAnnotation(ExtractBy.class);
                if (annotation == null || !((ExtractBy) annotation).multi()) {
                    classPageModelPipelineEntry.getValue().process(o, task);
                } else {
                    List<Object> list = (List<Object>) o;
                    for (Object o1 : list) {
                        classPageModelPipelineEntry.getValue().process(o1, task);
                    }
                }
            }
        }
    }
}
