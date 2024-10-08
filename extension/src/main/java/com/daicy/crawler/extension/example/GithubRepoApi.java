package com.daicy.crawler.extension.example;

import com.daicy.crawler.core.Site;
import com.daicy.crawler.extension.model.ConsolePageModelPipeline;
import com.daicy.crawler.extension.model.HasKey;
import com.daicy.crawler.extension.model.OOSpider;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.model.annotation.ExtractByUrl;

import java.util.List;

/**
 * @author daichangya@163.com <br>
 * @since 0.4.1
 */
public class GithubRepoApi implements HasKey {

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.name", source = ExtractBy.Source.RawText)
    private String name;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$..owner.login", source = ExtractBy.Source.RawText)
    private String author;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.language",multi = true, source = ExtractBy.Source.RawText)
    private List<String> language;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.stargazers_count", source = ExtractBy.Source.RawText)
    private int star;

    @ExtractBy(type = ExtractBy.Type.JsonPath, value = "$.forks_count", source = ExtractBy.Source.RawText)
    private int fork;

    @ExtractByUrl
    private String url;

    public static void main(String[] args) {
        OOSpider.create(Site.me().setSleepTime(100)
                , new ConsolePageModelPipeline(), GithubRepoApi.class)
                .addUrl("https://api.github.com/repos/daichangya/crawler-mouse").run();
    }

    @Override
    public String key() {
        return author + ":" + name;
    }

    public String getName() {
        return name;
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
}
