package com.daicy.crawler.web.pipeline;

import com.daicy.crawler.common.utils.UrlUtils;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.utils.FilePersistentBase;
import com.daicy.crawler.extension.model.DynamicModel;
import com.daicy.crawler.extension.pipeline.PageModelPipeline;
import com.daicy.crawler.web.ttf.ConvertTtf;
import com.daicy.samples.samples.formatter.MarkDownFormatter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Usually used in test.
 *
 * @author daichangya@163.com
 * @since 0.1.0
 */
public class ImageDownPipeline extends FilePersistentBase implements PageModelPipeline {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void process(Object o, Task task) {
        logger.debug("start process uuid : {}", task.getUUID());
        String mainPath = task.getParameters().get("mainPath");
        if (StringUtils.isEmpty(mainPath)) {
            mainPath = System.getProperty("data.home") + "/" + task.getUUID() + "/";
        }
        if (o instanceof Map) {
            Map<String, Object> map = (Map) o;
            String requestUrl = (String) map.get(DynamicModel.REQUESTURL);
            if (null == map.get("title")) {
                logger.info("title is null requestUrl:{}", requestUrl);
                return;
            }
            String pathUrl = mainPath + "/" + StringUtils.trim(UrlUtils.getFileNameByUrl((String) map.get("title")));
            createFile(pathUrl, requestUrl, map);
            List<String> imageList = (List<String>) map.get("imageList");
            List<String> imageList2 = (List<String>) map.get("imageList2");
            imageList = CollectionUtils.isEmpty(imageList) ? Lists.newArrayList() : imageList;
            imageList2 = CollectionUtils.isEmpty(imageList2) ? Lists.newArrayList() : imageList2;
            Set<String> imageSet = Sets.union(Sets.newHashSet(imageList), Sets.newHashSet(imageList2)).stream()
                    .filter(imageUrl -> StringUtils.isNotBlank(imageUrl)).collect(Collectors.toSet());
            requestUrl = requestUrl.replace("https", "http");
            if (CollectionUtils.isNotEmpty(imageSet)) {
                for (String imageUrl : imageSet) {
                    try {
                        URL url = UrlUtils.extractNewUrl(requestUrl, imageUrl).toURL();
                        String fileName = pathUrl + "/" + url.getFile().substring(url.getFile().lastIndexOf("/"));
                        File file = getFile(fileName);
                        FileUtils.copyURLToFile(url, file);
                    } catch (IOException e) {
                        logger.error("imageDown write imageUrl:{} ", imageUrl);
                    }
                }
            }
        }
        logger.debug("finish process uuid : {}", task.getUUID());
    }

    private void createFile(String pathUrl, String requestUrl, Map<String, Object> map) {
        String fileName = pathUrl + "/" + StringUtils.trim(UrlUtils.getFileNameByUrl((String) map.get("title"))) + ".md";
        String content = (String) map.get("content");
        try {
            if (requestUrl.indexOf("club.autohome.com.cn/bbs/thread") > 0) {
                String ttfUrl = (String) map.get("ttfUrl");
                URI url = UrlUtils.extractNewUrl(requestUrl, ttfUrl);
                TrueTypeFont currentFont = new TTFParser().parse(url.toURL().openStream());
                content = ConvertTtf.convertStr(currentFont, content);
            }
            File file = getFile(fileName);
            FileUtils.write(file, new MarkDownFormatter().format(content));
        } catch (IOException e) {
            logger.error("file write fileName:{} content:{}", fileName, content);
        }
    }
}
