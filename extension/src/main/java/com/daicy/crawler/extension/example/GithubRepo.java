package com.daicy.crawler.extension.example;

import com.daicy.crawler.core.Site;
import com.daicy.crawler.extension.model.ConsolePageModelPipeline;
import com.daicy.crawler.extension.model.HasKey;
import com.daicy.crawler.extension.model.OOSpider;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.model.annotation.ExtractByUrl;
import com.daicy.crawler.extension.model.annotation.HelpUrl;
import com.daicy.crawler.extension.model.annotation.TargetUrl;

import java.util.List;

/**
 * @author daichangya@163.com <br>
 * @since 0.3.2
 */
@TargetUrl("https://github.com/\\w+/\\S+(?:-\\S+)*")
@HelpUrl({"https://github.com/\\w+\\?tab=repositories", "https://github.com/\\w+", "https://github.com/explore/*"})
public class GithubRepo implements HasKey {

    @ExtractBy(value = "//strong[@class='mr-2 flex-self-stretch']/a/text()", notNull = true)
    private String name;

    @ExtractByUrl("https://github\\.com/(\\w+)/.*")
    private String author;

    @ExtractBy("//article[@class='markdown-body entry-content container-lg']/tidyText()")
    private String readme;

    @ExtractBy(value = "//div[@class='repository-lang-stats']//li//span[@class='lang']/text()", multi = true)
    private List<String> language;

    @ExtractBy("//ul[@class='pagehead-actions']/li[1]//a[@class='social-count js-social-count']/text()")
    private int star;

    @ExtractBy("//ul[@class='pagehead-actions']/li[2]//a[@class='social-count']/text()")
    private int fork;

    @ExtractByUrl
    private String url;

    public static void main(String[] args) {
        OOSpider.create(Site.me().setSleepTime(100)
                , new ConsolePageModelPipeline(), GithubRepo.class)
                .addUrl("https://github.com/daichangya").thread(10).run();
    }

    @Override
    public String key() {
        return author + ":" + name;
    }

    public String getName() {
        return name;
    }

    public String getReadme() {
        return readme;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getLanguage() {
        return language;
    }

    public String getUrl() {
        return url;
    }

    public int getStar() {
        return star;
    }

    public int getFork() {
        return fork;
    }

    @Override
    public String toString() {
        return "GithubRepo{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", readme='" + readme + '\'' +
                ", language=" + language +
                ", star=" + star +
                ", fork=" + fork +
                ", url='" + url + '\'' +
                '}';
    }
}
