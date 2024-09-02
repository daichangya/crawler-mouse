package com.daicy.crawler.core.downloader;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.selector.Html;

/**
 * Base class of downloader with some common methods.
 *
 * @author daichangya@163.com
 * @since 0.5.0
 */
public abstract class AbstractDownloader implements Downloader {

    /**
     * A simple method to download a url.
     *
     * @param url url
     * @return html
     */
    public Html download(String url) {
        return download(url, null);
    }

    /**
     * A simple method to download a url.
     *
     * @param url url
     * @param charset charset
     * @return html
     */
    public Html download(String url, String charset) {
        Page page = download(new Request(url), Site.me().setCharset(charset).toTask());
        return (Html) page.getHtml();
    }

    protected void onSuccess(Request request) {
    }

    protected void onError(Request request) {
    }

}
