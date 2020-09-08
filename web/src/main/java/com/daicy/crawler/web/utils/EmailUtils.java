package com.daicy.crawler.web.utils;

import com.daicy.crawler.extension.utils.IPUtils;
import com.daicy.crawler.web.Application;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.web.utils
 * @date:8/25/20
 */
public class EmailUtils {

    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;

    private static final String URL_TEMPLETE = "文件下载链接: http://%s/%s\n";

    private static Logger logger = LoggerFactory.getLogger(EmailUtils.class);

    public static void sendEmail(String emailAdress, String title, String content, List<File> fileList) {
        JavaMailSender javaMailSender = SpringAppContextUtil.AppContext.getBean(JavaMailSender.class);
        MailProperties mailProperties = SpringAppContextUtil.AppContext.getBean(MailProperties.class);
        if (StringUtils.isNotBlank(emailAdress) && null != javaMailSender) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            try {
                final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(mailProperties.getUsername(), "分布式任务调度平台XXL-JOB");
                helper.setTo(emailAdress);
                helper.setSubject(title);
                if (CollectionUtils.isNotEmpty(fileList)) {
                    for (File file : fileList) {
                        if (file.length() > MAX_FILE_SIZE) {
                            content = content + String.format(URL_TEMPLETE, "crawler-file.xxxxx.com.cn", file.getName());
                            continue;
                        }
                        helper.addAttachment(file.getName(), file);
                    }
                }
                helper.setText(content, true);
                javaMailSender.send(mimeMessage);
            } catch (Exception e) {
                logger.error("sendEmail error emailAdress:{} title:{}", emailAdress, title, e);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(String.format(URL_TEMPLETE, "2", "3"));
    }
}
