package com.daicy.crawler.extension.model;

import com.daicy.crawler.core.ResultItems;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.pipeline.Pipeline;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.pipeline.PageModelPipeline;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The extension to Pipeline for page model extractor.
 *
 * @author daichangya@163.com <br>
 * @since 0.2.0
 */
class ModelPipeline implements Pipeline {

    private Map<DynamicModel, PageModelPipeline> pageModelPipelines = new ConcurrentHashMap<DynamicModel, PageModelPipeline>();

    public ModelPipeline() {
    }

    public ModelPipeline put(DynamicModel dynamicModel, PageModelPipeline pageModelPipeline) {
        pageModelPipelines.put(dynamicModel, pageModelPipeline);
        return this;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        DynamicModel dynamicModel = resultItems.get(DynamicModel.KEY);
        PageModelPipeline pageModelPipeline = pageModelPipelines.get(dynamicModel);
        Object o = resultItems.get(dynamicModel.getModelName());
        if (o != null) {
            ExtractBy extractBy = dynamicModel.getExtractBy();
            if (extractBy == null || !extractBy.multi()) {
                pageModelPipeline.process(o, task);
            } else {
                List<Object> list = (List<Object>) o;
                for (Object o1 : list) {
                    pageModelPipeline.process(o1, task);
                }
            }
        }
    }
}
