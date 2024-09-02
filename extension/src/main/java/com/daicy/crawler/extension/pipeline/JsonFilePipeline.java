package com.daicy.crawler.extension.pipeline;

import com.alibaba.fastjson.JSON;
import com.daicy.crawler.core.pipeline.Pipeline;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.daicy.crawler.core.ResultItems;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.utils.FilePersistentBase;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Store results to files in JSON format.<br>
 *
 * @author daichangya@163.com <br>
 * @since 0.2.0
 */
public class JsonFilePipeline extends FilePersistentBase implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * new JsonFilePageModelPipeline with default path "/data/crawler/"
     */
    public JsonFilePipeline() {
        setPath("/data/crawler");
    }

    public JsonFilePipeline(String path) {
        setPath(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".json")));
            printWriter.write(JSON.toJSONString(resultItems.getAll()));
            printWriter.close();
        } catch (IOException e) {
            logger.warn("write file error", e);
        }
    }
}
