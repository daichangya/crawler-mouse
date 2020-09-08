package com.daicy.crawler.core;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.downer
 * @date:8/19/20
 */
public class SpiderContext {

    private List<String> delRequestKeyWords = Lists.newArrayList();

    public List<String> getDelRequestKeyWords() {
        return delRequestKeyWords;
    }

    public void addDelKeyWord(String delKeyWord) {
        delRequestKeyWords.add(delKeyWord);
    }

    public void addDelKeyWords(List<String> delKeyWords) {
        delRequestKeyWords.addAll(delKeyWords);
    }
}
