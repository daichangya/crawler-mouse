package com.daicy.samples.samples.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.daicy.crawler.core.ResultItems;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.pipeline.Pipeline;
import com.daicy.crawler.core.utils.FilePersistentBase;

import java.io.*;
import java.util.Map;

/**
 * @author code4crafer@gmail.com
 */
public class OneFilePipeline extends FilePersistentBase implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private PrintWriter printWriter;

    public OneFilePipeline() throws FileNotFoundException, UnsupportedEncodingException {
        this("/data/crawler/");
    }

    public OneFilePipeline(String path) throws FileNotFoundException, UnsupportedEncodingException {
        setPath(path);
        printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(path)), "UTF-8"));
    }

    @Override
    public synchronized void process(ResultItems resultItems, Task task) {
        printWriter.println("url:\t" + resultItems.getRequest().getUrl());
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getValue() instanceof Iterable) {
                Iterable value = (Iterable) entry.getValue();
                printWriter.println(entry.getKey() + ":");
                for (Object o : value) {
                    printWriter.println(o);
                }
            } else {
                printWriter.println(entry.getKey() + ":\t" + entry.getValue());
            }
        }
        printWriter.flush();
    }
}
