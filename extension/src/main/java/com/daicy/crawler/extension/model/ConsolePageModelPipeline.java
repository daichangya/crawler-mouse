package com.daicy.crawler.extension.model;

import com.daicy.crawler.common.utils.JsonUtils;
import com.daicy.crawler.extension.pipeline.PageModelPipeline;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.daicy.crawler.core.Task;

import java.util.Map;

/**
 * Print page model in console.<br>
 * Usually used in test.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
public class ConsolePageModelPipeline implements PageModelPipeline {
    @Override
    public void process(Object o, Task task) {
        if (o instanceof Map) {
//            Map map = (Map) o;
//            if(map.get("status").equals("0")){
//                System.out.println(JsonUtils.toJson(o));
//            }
            System.out.println(JsonUtils.toJson(o));
        } else {
            System.out.println(ToStringBuilder.reflectionToString(o));
        }
    }
}
