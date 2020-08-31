package com.daicy.samples.samples.pipeline;

import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.processor.SimplePageProcessor;
import org.junit.Test;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.samples.samples.pipeline
 * @date:8/21/20
 */
public class MarkDownPipelineTest {

    @Test
    public void process() {
        Spider.create(new SimplePageProcessor()).addPipeline(new MarkDownPipeline())
                .addUrl("http://www.baidu.com/").run();
    }
}
