package com.daicy.crawler.core.utils;

import org.junit.Test;

import java.net.URI;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.core.utils
 * @date:8/21/20
 */
public class UrlUtilsTest {

    @Test
    public void canonicalizeUrl() {
        String currentUrl = "https://club.autohome.com.cn/bbs/thread/58898500202bb30a/89079986-1.html#pvareaid=6830286";
        String href = "//club2.autoimg.cn/album/g1/M09/64/69/userphotos/2020/05/26/17/500_ChsEj17M3lWAQtqLAAU_Vd4QD1U509.jpg";
        String uri = UrlUtils.canonicalizeUrl(currentUrl,href);
        System.out.println(uri);
    }
}
