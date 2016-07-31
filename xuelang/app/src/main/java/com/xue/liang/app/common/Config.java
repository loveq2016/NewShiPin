package com.xue.liang.app.common;

/**
 * Created by Administrator on 2016/7/31.
 */
public class Config {

    public static final String HTPP = "http://";
    public static final String IP = "171.221.206.109";
    public static final String PORT = ":8013";

    private static String getStratUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HTPP);
        stringBuilder.append(IP);
        stringBuilder.append(PORT);
        return stringBuilder.toString();

    }

    public static String getRegisterUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStratUrl());
        stringBuilder.append("/mobileservice/register.ashx");
        return stringBuilder.toString();
    }
}
