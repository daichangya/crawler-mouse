package com.daicy.crawler.web.baiduindex;

import com.daicy.crawler.common.utils.JsonUtils;
import com.daicy.crawler.common.utils.StringUtils;
import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.filter.FilterKeyWordRequest;
import com.daicy.crawler.core.filter.Filters;
import com.daicy.crawler.extension.model.OOSpider;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import com.daicy.crawler.extension.model.annotation.ExtractByUrl;
import com.daicy.crawler.extension.model.annotation.TargetUrl;
import com.daicy.crawler.extension.pipeline.CsvFileModelPipeline;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author daichangya@163.com
 */
@TargetUrl("http://index.baidu.com/api/SearchApi/index.*")
public class BaiduIndexModel {

    private static Logger logger = LoggerFactory.getLogger(BaiduIndexModel.class);

    private static Site site = Site.me().setDomain("index.baidu.com").setSleepTime(1000)
            .addCookie("BDUSS",
                    "kJ4SEYtT05PM2U4NlVPQWY2dWNZRjNmMng1ZkI5TkVSclRWd3g5VmZaeWdlbjVmSVFBQUFBJCQAAAAAAAAAAAEAAAAQTRyIwfXP~rz8cQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKDtVl-g7VZfZ").
                    setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");


    @ExtractBy(value = "status", type = ExtractBy.Type.JsonPath)
    private String status;

    @ExtractBy(value = "data.userIndexes[0].word[0].name", type = ExtractBy.Type.JsonPath)
    private String keyWord;

    @ExtractBy(value = "data.generalRatio[0].all.avg", type = ExtractBy.Type.JsonPath)
    private String all;

    @ExtractBy(value = "data.generalRatio[0].wise.avg", type = ExtractBy.Type.JsonPath)
    private String wise;

    @ExtractBy(value = "data.generalRatio[0].pc.avg", type = ExtractBy.Type.JsonPath)
    private String pc;

    @ExtractByUrl("areaName=(.*)")
    private String areaName;

    public static void main(String[] args) {
        Spider spider = baiduIndexBuild(null);
        spider.run();
    }

    public static Spider baiduIndexBuild(String cookie) {
        List<Request> urlList = Lists.newArrayList();
        String urlTemp = "http://index.baidu.com/api/SearchApi/index?area={}&word=[[%7b%22name%22:%22{}%22,%22wordType%22:1%7d]]&days=7&areaName={}";
        Resource provinceRes = new ClassPathResource("baiduindex/province.json");
        String jsonStr = null;
        try {
            jsonStr = new String(FileCopyUtils.copyToByteArray(provinceRes.getInputStream()));
            Map<String, String> provinceMap = JsonUtils.toObject(jsonStr, Map.class);

            Resource cityRes = new ClassPathResource("baiduindex/city.json");
            String cityStr = new String(FileCopyUtils.copyToByteArray(cityRes.getInputStream()));
            Map<String, String> cityMap = JsonUtils.toObject(cityStr, Map.class);
            provinceMap.putAll(cityMap);

            Resource keyWordRes = new ClassPathResource("baiduindex/keywords.txt");
            String[] keyWords = new String(FileCopyUtils.copyToByteArray(keyWordRes.getInputStream())).split("\n");
            for (int i = 0; i < keyWords.length; i++) {
                for (String key : provinceMap.keySet()) {
                    String url = StringUtils.stringFormat(urlTemp, provinceMap.get(key), keyWords[i], key);
                    Request request = new Request(url);
                    request.putExtra(Request.DEL_KEY_WORD, keyWords[i]);
                    urlList.add(request);
                }
            }
        } catch (IOException e) {
            logger.error("baiduIndexBuild error", e);
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(cookie)){
            site.addCookie("BDUSS",cookie);
        }
        Filters filters = new Filters();
        filters.addFilters(Lists.newArrayList(new FilterKeyWordRequest()));
        Spider spider = OOSpider.create(site
                , new CsvFileModelPipeline(), BaiduIndexModel.class).addRequest(urlList.toArray(new Request[0])).thread(1)
                .setFilters(filters);
        return spider;
    }

}
