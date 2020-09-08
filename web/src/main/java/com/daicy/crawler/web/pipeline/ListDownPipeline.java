package com.daicy.crawler.web.pipeline;

import com.daicy.crawler.common.utils.UrlUtils;
import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.utils.FilePersistentBase;
import com.daicy.crawler.extension.model.DynamicModel;
import com.daicy.crawler.extension.pipeline.PageModelPipeline;
import com.daicy.samples.samples.formatter.MarkDownFormatter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Usually used in test.
 *
 * @author daichangya@163.com
 * @since 0.1.0
 */
public class ListDownPipeline extends FilePersistentBase implements PageModelPipeline {

    private Logger logger = LoggerFactory.getLogger(getClass());


    private String mainPath;

    @Override
    public void process(Object o, Task task) {
        logger.debug("start process uuid : {}", task.getUUID());
        if (o instanceof Map) {
            Map<String, Object> map = (Map) o;
            mainPath = System.getProperty("data.home") + "/" + task.getUUID() + "/";
            logger.info("mainPath:{}",mainPath);
            task.getParameters().put("mainPath", mainPath);
            task.getParameters().put("title", (String) map.get("title"));
            this.setPath(mainPath);
            createFile(mainPath, map);
            List<String> links = (List<String>) map.get("links");
            if (CollectionUtils.isNotEmpty(links)) {
                Spider spider = (Spider) task;
                links.stream().map(link -> UrlUtils.extractNewUrl((String) map.get(DynamicModel.REQUESTURL), link)).collect(Collectors.toList())
                        .forEach(link -> {
                            spider.addUrl(link.toString());
                        });
            }
        }
        logger.debug("finish process uuid: {}", task.getUUID());
    }

    private void createFile(String pathUrl, Map<String, Object> map) {
        String fileName = pathUrl + "/" + StringUtils.trim(UrlUtils.getFileNameByUrl((String) map.get("title"))) + ".md";
        File file = getFile(fileName);
        String content = (String) map.get("content");
        try {
            FileUtils.write(file, new MarkDownFormatter().format(content));
        } catch (IOException e) {
            logger.error("file write fileName:{} content:{}", fileName, content);
        }
    }
}
