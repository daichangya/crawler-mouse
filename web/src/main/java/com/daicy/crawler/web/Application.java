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

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		FileServer fileServer = new FileServer(9080, null);
		fileServer.start();
	}

	@Bean
	@ConditionalOnMissingBean
	public SpringAppContextUtil springAppContextUtil() {
		return new SpringAppContextUtil();
	}

}
