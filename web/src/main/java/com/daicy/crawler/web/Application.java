package com.daicy.crawler.web;

import com.daicy.crawler.fileserver.FileServer;
import com.daicy.crawler.web.utils.SpringAppContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author daichangya
 */
@SpringBootApplication
public class Application {

	public static final int FILE_PORT = 9080;

	public static void main(String[] args) {
		System.setProperty("data.home",System.getProperty("user.home")+"/data");
		SpringApplication.run(Application.class, args);
		FileServer fileServer = new FileServer(FILE_PORT, null);
		fileServer.start();
	}

	@Bean
	@ConditionalOnMissingBean
	public SpringAppContextUtil springAppContextUtil() {
		return new SpringAppContextUtil();
	}

}
