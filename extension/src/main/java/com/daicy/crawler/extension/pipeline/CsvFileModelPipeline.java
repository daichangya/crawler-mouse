package com.daicy.crawler.extension.pipeline;

import com.daicy.crawler.common.utils.JsonUtils;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.utils.FilePersistentBase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Store results to files in JSON format.<br>
 *
 * @author daichangya@163.com <br>
 * @since 0.2.0
 */
public class CsvFileModelPipeline extends FilePersistentBase implements PageModelPipeline {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * new JsonFilePageModelPipeline with default path "/data/crawler/"
     */
    public CsvFileModelPipeline() {
        setPath("/data/crawler");
    }

    public CsvFileModelPipeline(String path) {
        setPath(path);
    }

    @Override
    public void process(Object o, Task task) {
        if (o instanceof Map) {
            Map<String, Object> map = (Map) o;
            String fileName = this.path + PATH_SEPERATOR + task.getUUID() + ".csv";
            try {
                PrintWriter printWriter = new PrintWriter(new FileWriter(getFile(fileName),true));
                printWriter.write(StringUtils.join(map.values(), "\t"));
                printWriter.write("\n");
                printWriter.close();
            } catch (IOException e) {
                logger.warn("write file error", e);
            }
        } else {
            System.out.println(JsonUtils.toJson(o));
        }

    }
}
