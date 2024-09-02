package com.daicy.samples.samples.pipeline;

import com.daicy.crawler.core.ResultItems;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.pipeline.Pipeline;
import com.daicy.samples.samples.formatter.MarkDownFormatter;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

/**
 * Write results in console.<br>
 * Usually used in test.
 *
 * @author daichangya@163.com <br>
 * @since 0.1.0
 */
public class MarkDownPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println((String) resultItems.get("title"));
        String html = resultItems.get("html");
        String markDown = new MarkDownFormatter().format(html);
        System.out.println(markDown);
    }
}
