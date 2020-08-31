package com.daicy.crawler.downer;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.downer
 * @date:8/25/20
 */
public class Test {
    private static Test ourInstance = new Test();

    public static Test getInstance() {
        return ourInstance;
    }

    private Test() {
    }
}
