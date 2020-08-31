package com.daicy.crawler.fileserver;

public class Config {

    public static String DIR = System.getProperty("user.home");

    public static String getDIR() {
        return DIR;
    }

    public static void setDIR(String DIR) {
        Config.DIR = DIR;
    }
}
