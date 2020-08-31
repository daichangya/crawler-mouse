package com.daicy.crawler.core;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.core
 * @date:8/21/20
 */
public class HttpProxy {

    private String host;// 代理主机ip
    private int port;// 代理主机端口

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }
}
