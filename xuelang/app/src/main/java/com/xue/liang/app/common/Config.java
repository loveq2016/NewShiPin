package com.xue.liang.app.common;

/**
 * Created by Administrator on 2016/7/31.
 */
public class Config {

    public static  String TEST_PHONE_NUMBER="18000000000";//18000000000
    public static  String TEST_MAC="38:BC:1A:C5:DA:4F";//38:BC:1A:C5:DA:4F
    public static  String TEST_TYPE="2";//


    public static final String HTPP = "http://";
    //121.41.32.246:9004
    public static  String IP = "182.150.56.75";//182.150.56.75:9002    121.41.32.246:9004
    public static  String PORT = "9002";

    private static String getStartUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HTPP);
        stringBuilder.append(IP);
        stringBuilder.append(":"+PORT);
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

    /**
     * 获取公告列表详情接口
     * @return
     */
    public static String getNoticeDetailUrl(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStartUrl());
        stringBuilder.append("/mobileservice/getnoticedetail.ashx");
        return stringBuilder.toString();
    }

    /**
     * 获取设备列表接口
     * @return
     */
    public static String getDeviceListUrl(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStartUrl());
        stringBuilder.append("/mobileservice/getdevicelist.ashx");
        return stringBuilder.toString();
    }

    /**
     * 获取火警地址
     * @return
     */
    public static String getSendAlarmUrl(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStartUrl());
        stringBuilder.append("/mobileservice/sendalarm.ashx");
        return stringBuilder.toString();
    }


    public static String getUpdateFile(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStartUrl());
        stringBuilder.append("/mobileservice/uploadfiles.ashx");
        return stringBuilder.toString();
    }

    public static String getUpdateAlarmUrl(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStartUrl());
        stringBuilder.append("/mobileservice/alarmforhelp.ashx");
        return stringBuilder.toString();
    }
}
