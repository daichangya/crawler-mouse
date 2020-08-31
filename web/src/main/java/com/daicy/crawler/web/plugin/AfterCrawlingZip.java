package com.daicy.crawler.web.plugin;

import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.plugin.AfterCrawlingPlugin;
import com.daicy.crawler.web.utils.EmailUtils;
import com.daicy.samples.utils.ZipUtils;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.web.plugin
 * @date:8/27/20
 */
public class AfterCrawlingZip implements AfterCrawlingPlugin {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(Spider spider) {
        String home = System.getProperty("user.home");
        String filePath = home + "/" + spider.getUUID();
        FileOutputStream fos1 = null;
        try {
            File file = new File(filePath + ".zip");
            fos1 = new FileOutputStream(file);
            ZipUtils.toZip(filePath, fos1, true);
            String emailAdress = spider.getParameters().get("email");
            if (StringUtils.isNotBlank(emailAdress)) {
                String title = spider.getParameters().get("title");
                EmailUtils.sendEmail(emailAdress, title, "详情见附件", Lists.newArrayList(file));
            }
        } catch (FileNotFoundException e) {
            logger.error("zip file error ", spider.toString());
        } finally {
            IOUtils.closeQuietly(fos1);
        }
    }
}
