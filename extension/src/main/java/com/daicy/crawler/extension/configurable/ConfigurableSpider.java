package com.daicy.crawler.extension.configurable;

import com.daicy.crawler.common.InterfaceAdapter;
import com.daicy.crawler.common.model.BrowserType;
import com.daicy.crawler.common.utils.UrlUtils;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.downloader.Downloader;
import com.daicy.crawler.core.plugin.Plugin;
import com.daicy.crawler.core.plugin.Plugins;
import com.daicy.crawler.downer.WebDriverDownloader;
import com.daicy.crawler.extension.RedisProxyProvider;
import com.daicy.crawler.extension.model.DynamicModel;
import com.daicy.crawler.extension.model.FieldModel;
import com.daicy.crawler.extension.model.OOSpider;
import com.daicy.crawler.extension.model.annotation.*;
import com.daicy.crawler.extension.model.annotation.impl.*;
import com.daicy.crawler.extension.pipeline.PageModelPipeline;
import com.daicy.crawler.extension.utils.ClassUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension.configurable
 * @date:8/26/20
 */
public class ConfigurableSpider {
    private Map<String, List<DynamicModel>> pipelineToModel = Maps.newHashMap();

    private Map<String, String> parameters = Maps.newHashMap();

    private String[] startUrls;

    private List<String> plugins = Lists.newArrayList();

    private String downloader;

    private Site site = Site.me();


    private int threadNum = 1;


    public String[] getStartUrls() {
        return startUrls;
    }

    public void setStartUrls(String[] startUrls) {
        this.startUrls = startUrls;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<String> plugins) {
        this.plugins = plugins;
    }

    public String getDownloader() {
        return downloader;
    }

    public void setDownloader(String downloader) {
        this.downloader = downloader;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public static ConfigurableSpider create(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ExtractBy.class, InterfaceAdapter.interfaceSerializer(ExtractByImpl.class));
        builder.registerTypeAdapter(TargetUrl.class, InterfaceAdapter.interfaceSerializer(TargetUrlImpl.class));
        builder.registerTypeAdapter(ComboExtract.class, InterfaceAdapter.interfaceSerializer(ComboExtractImpl.class));
        builder.registerTypeAdapter(ExtractByUrl.class, InterfaceAdapter.interfaceSerializer(ExtractByUrlImpl.class));
        builder.registerTypeAdapter(Formatter.class, InterfaceAdapter.interfaceSerializer(FormatterImpl.class));
        builder.registerTypeAdapter(HelpUrl.class, InterfaceAdapter.interfaceSerializer(HelpUrlImpl.class));
        builder.disableHtmlEscaping();
        Gson gson = builder.create();
        ConfigurableSpider configurableSpider = gson.fromJson(json, ConfigurableSpider.class);
        return configurableSpider;
    }

    public Spider build() {
        if (site.isUserProxyProvider()) {
            JedisPool jedisPool = new JedisPool(new GenericObjectPoolConfig(), "10.20.54.45", 6055, Protocol.DEFAULT_TIMEOUT, "redistest6055", 2);
            RedisProxyProvider redisProxyProvider = new RedisProxyProvider(jedisPool);
            site.setProxyProvider(redisProxyProvider);
        }
        if (site.isUseRealBrowser()) {
//            FirefoxOptions firefoxOptions = new FirefoxOptions();
//            FirefoxProfile profile = new FirefoxProfile();
////        profile.setPreference("permissions.default.image", 2);
//            profile.setPreference("browser.migration.version", 9001);
//            profile.setPreference("permissions.default.stylesheet", 2);
//            firefoxOptions.setProfile(profile);
//            firefoxOptions.setCapability("marionette", true);
//            firefoxOptions.setHeadless(true);
            DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
            desiredCapabilities.setCapability("remoteUrl", "http://standalone-firefox:4444/wd/hub");
            site.setDriverOptions(desiredCapabilities).setBrowserType(BrowserType.REMOTE).setUseRealBrowser(true);
            downloader = WebDriverDownloader.class.getName();
        } else if (StringUtils.equals(downloader, WebDriverDownloader.class.getName())) {
            MutableCapabilities mutableCapabilities = new MutableCapabilities();
            mutableCapabilities.setCapability("browserName", "htmlunit");
            site.setDriverOptions(mutableCapabilities);
        }

        List<? extends Plugin> pluginList = this.plugins.stream().map(pluginStr -> (Plugin) ClassUtils.newInstance(pluginStr)).collect(Collectors.toList());
        Plugins plugins = new Plugins();
        plugins.addPlugins(pluginList);
        Map<PageModelPipeline, List<DynamicModel>> pipelineToModel = Maps.newHashMap();
//        this.pipelineToModel.values().forEach(dynamicModels -> {
//            dynamicModels.forEach(dynamicModel -> {
//                List<String> fieldNames = dynamicModel.getFieldModels().stream().map(FieldModel::getName).collect(Collectors.toList());
//                String URL = "url";
//                if (!fieldNames.contains(URL)) {
//                    FieldModel fieldModel = new FieldModel();
//                    fieldModel.setName(URL);
//                    fieldModel.setExtractByUrl(new ExtractByUrlImpl());
//                    dynamicModel.getFieldModels().add(fieldModel);
//                }
//            });
//        });
        this.pipelineToModel.keySet().forEach(pipelineStr -> {
            pipelineToModel.put((PageModelPipeline) ClassUtils.newInstance(pipelineStr), this.pipelineToModel.get(pipelineStr));
        });
        Spider spider = new OOSpider(site, pipelineToModel).addUrl(startUrls).thread(threadNum);
        if (startUrls.length == 1) {
            spider.setUUID(UrlUtils.getFileNameByUrl(startUrls[0]));
        }
        spider.setPlugins(plugins);
        if (StringUtils.isNotEmpty(downloader)) {
            Downloader downloaderBean = (Downloader) ClassUtils.newInstance(downloader);
            downloaderBean.setThread(this.threadNum);
            spider.setDownloader(downloaderBean);
        }
        if (MapUtils.isNotEmpty(parameters)) {
            spider.setParameters(parameters);
        }
        return spider;
    }
}
