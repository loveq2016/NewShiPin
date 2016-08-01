package com.xue.liang.app.common;

/**
 * Created by Administrator on 2016/7/31.
 */
public class Config {

    public static final String HTPP = "http://";
    public static final String IP = "171.221.206.109";
    public static final String PORT = ":8013";

    private static String getStartUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HTPP);
        stringBuilder.append(IP);
        stringBuilder.append(PORT);
        return stringBuilder.toString();

    }

    /**
     *第一次登陆注册接口
     * @return 注册地址
     */
    public static String getRegisterUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStartUrl());
        stringBuilder.append("/mobileservice/register.ashx");
        return stringBuilder.toString();
    }

    /**
     * 获取公告列表接口
     * @return
     */
    public static String getNoticeUrl(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStartUrl());
        stringBuilder.append("/mobileservice/getnotice.ashx");
        return stringBuilder.toString();
    }
}
