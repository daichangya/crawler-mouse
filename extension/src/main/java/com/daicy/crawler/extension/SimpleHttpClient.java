package com.daicy.crawler.extension;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.downloader.HttpClientDownloader;
import com.daicy.crawler.core.proxy.ProxyProvider;
import com.daicy.crawler.extension.model.PageMapper;

/**
 * @author daichangya@163.com
 *         Date: 2017/5/27
 * @since 0.7.0
 */
public class SimpleHttpClient {

    private final HttpClientDownloader httpClientDownloader;

    private final Site site;

    public SimpleHttpClient() {
        this(Site.me());
    }

    public SimpleHttpClient(Site site) {
        this.site = site;
        this.httpClientDownloader = new HttpClientDownloader();
    }

    public void setProxyProvider(ProxyProvider proxyProvider){
        this.httpClientDownloader.setProxyProvider(proxyProvider);
    }

    public <T> T get(String url, Class<T> clazz) {
        return get(new Request(url), clazz);
    }

    public <T> T get(Request request, Class<T> clazz) {
        Page page = httpClientDownloader.download(request, site.toTask());
        if (!page.isDownloadSuccess()) {
            return null;
        }
        return new PageMapper<T>(clazz).get(page);
    }

    public Page get(String url) {
        return httpClientDownloader.download(new Request(url), site.toTask());
    }

    public Page get(Request request) {
        return httpClientDownloader.download(request, site.toTask());
    }

}
