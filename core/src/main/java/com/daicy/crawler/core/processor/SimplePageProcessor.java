package com.daicy.crawler.core.processor;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Site;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * A simple PageProcessor.
 *
 * @author daichangya@163.com <br>
 * @since 0.1.0
 */
public class SimplePageProcessor implements PageProcessor {

    private String urlPattern;

    private Site site;

    public SimplePageProcessor() {
        this(null);
    }

    public SimplePageProcessor(String urlPattern) {
        this.site = Site.me();
        //compile "*" expression to regex
        if (StringUtils.isNotBlank(urlPattern)) {
            this.urlPattern = "(" + urlPattern.replace(".", "\\.").replace("*", "[^\"'#]*") + ")";
        }

    }

    @Override
    public void process(Page page) {
        if (StringUtils.isNotBlank(urlPattern)) {
            List<String> requests = page.getHtml().links().regex(urlPattern).all();
            //add urls to fetch
            page.addTargetRequests(requests);
        }
        //extract by XPath
        page.putField("title", page.getHtml().xpath("//title").toString());
        page.putField("html", page.getRawText());
        //extract by Readability
        page.putField("content", page.getHtml().smartContent());
    }

    @Override
    public Site getSite() {
        //settings
        return site;
    }
}
