package com.daicy.crawler.downer;


import com.daicy.crawler.downer.WebDriverFactory;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.downer
 * @date:8/19/20
 */
public class DownerManagerTest {

    @Test
    public void buildDowner() {
//        String url = "https://club.autohome.com.cn/bbs/thread/446315ec5071d0e4/89288923-1.html#pvareaid=6830286";
//        DownerContext downerContext = new DownerContext(BrowserVersion.CHROME,false,false);
//        WebDriver downer = WebDriverFactory.getInstance().buildDowner(downerContext);
//        downer.get(url);
//        System.out.println(downer.getPageSource());
    }

    @Test
    public void homePage() throws Exception {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        final HtmlPage page = webClient.getPage("http://htmlunit.sourceforge.net");
        Assert.assertEquals("HtmlUnit - Welcome to HtmlUnit", page.getTitleText());

        final String pageAsXml = page.asXml();
        Assert.assertTrue(pageAsXml.contains("<body class=\"composite\">"));

        final String pageAsText = page.asText();
        Assert.assertTrue(pageAsText.contains("Support for the HTTP and HTTPS protocols"));

    }

    @Test
    public void cssSelector() throws Exception {
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("http://htmlunit.sourceforge.net");

            //get list of all divs
            final DomNodeList<DomNode> divs = page.querySelectorAll("div");
            for (DomNode div : divs) {
            }

            //get div which has the id 'breadcrumbs'
            final DomNode div = page.querySelector("div#breadcrumbs");
        }
    }

    @Test
    public void testHtmlUnitDriver() throws Exception {
        String url = "http://www.baidu.com";
        WebDriver webDriver = new HtmlUnitDriver(BrowserVersion.CHROME, true) {
            @Override
            protected WebClient modifyWebClient(WebClient client) {
                final WebClient webClient = super.modifyWebClient(client);
                // you might customize the client here
                webClient.getOptions().setCssEnabled(false);

                return webClient;
            }
        };
        webDriver.get(url);
        webDriver.getPageSource();
    }
}
