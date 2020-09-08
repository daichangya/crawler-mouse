package com.daicy.crawler.web.ttf;

import com.daicy.crawler.common.utils.UrlUtils;
import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.selector.RegexSelector;
import com.daicy.crawler.core.selector.XpathSelector;
import com.daicy.crawler.downer.WebDriverDownloader;
import com.daicy.crawler.extension.SimpleHttpClient;
import com.daicy.samples.samples.formatter.MarkDownFormatter;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.junit.Test;
import org.openqa.selenium.MutableCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.web.ttf
 * @date:9/4/20
 */
public class ConvertTtfTest {

    @Test
    public void getFontMap() throws IOException {
        String requestUrl = "https://club.autohome.com.cn/bbs/thread/6baeb60ef604ab86/89681155-1.html#pvareaid=6830286";
        Page page = new SimpleHttpClient().get(requestUrl);
        String content = page.getHtml().selectDocument(
                new XpathSelector("//*/div[@class='conttxt']"));
        String ttfUrl = page.getHtml().selectDocument(new RegexSelector("[A-Za-z0-9_/.]*..ttf"));
        URI url = UrlUtils.extractNewUrl(requestUrl, ttfUrl);
        TrueTypeFont currentFont = new TTFParser().parse(url.toURL().openStream());
        System.out.println(ConvertTtf.convertStr(currentFont,content));

        System.out.println(url);
        System.out.println(ttfUrl);
        content = new MarkDownFormatter().format(content);
        System.out.println(content);
//        ConvertTtf.getFontMap()
//        System.out.println(new MarkDownFormatter().format(content));
    }

    @Test
    public void testHtml() throws IOException {
        String requestUrl = "https://club.autohome.com.cn/bbs/forum-c-526-1.html";
        Page page = new SimpleHttpClient().get(requestUrl);
        String content = page.getHtml().selectDocument(
                new XpathSelector("//*/ul[@class='post-list']"));
        System.out.println(content);
//        ConvertTtf.getFontMap()
//        System.out.println(new MarkDownFormatter().format(content));
    }

    @Test
    public void download() {
        MutableCapabilities mutableCapabilities = new MutableCapabilities();
        mutableCapabilities.setCapability("browserName","htmlunit");
        String url = "https://club.autohome.com.cn/bbs/forum-c-526-1.html";
        Site site = Site.me();
        site.setDriverOptions(mutableCapabilities);
        WebDriverDownloader webDriverDownloader = new WebDriverDownloader();
        Page page = webDriverDownloader.download(new Request(url),site.toTask());
        String content = page.getHtml().selectDocument(
                new XpathSelector("//*/ul[@class='post-list']"));
        System.out.println(content);
    }
}