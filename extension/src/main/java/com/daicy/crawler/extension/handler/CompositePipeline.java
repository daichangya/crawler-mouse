package com.daicy.crawler.extension.handler;

import com.daicy.crawler.core.ResultItems;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafer@gmail.com
 */
public class CompositePipeline implements Pipeline {

    private List<SubPipeline> subPipelines = new ArrayList<SubPipeline>();

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (SubPipeline subPipeline : subPipelines) {
            if (subPipeline.match(resultItems.getRequest())) {
                RequestMatcher.MatchOther matchOtherProcessorProcessor = subPipeline.processResult(resultItems, task);
                if (matchOtherProcessorProcessor == null || matchOtherProcessorProcessor != RequestMatcher.MatchOther.YES) {
                    return;
                }
            }
        }
    }

    public CompositePipeline addSubPipeline(SubPipeline subPipeline) {
        this.subPipelines.add(subPipeline);
        return this;
    }

    public CompositePipeline setSubPipeline(SubPipeline... subPipelines) {
        this.subPipelines = new ArrayList<SubPipeline>();
        for (SubPipeline subPipeline : subPipelines) {
            this.subPipelines.add(subPipeline);
        }
        return this;
    }

}
