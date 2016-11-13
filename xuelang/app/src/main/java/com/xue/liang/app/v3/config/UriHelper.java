package com.xue.liang.app.v3.config;

/**
 * Created by Administrator on 2016/10/26.
 */
public class UriHelper {

    private static final String HTPP = "http://";
    //121.41.32.246:9004
    public final static String IP = "182.150.56.73";//182.150.56.75:9002    121.41.32.246:9004   182.150.56.73  182.150.56.75
    public final static int PORT = 9003;

    public static String getStartUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HTPP);
        stringBuilder.append(IP);
        stringBuilder.append(":" + PORT);
        stringBuilder.append("/");
        return stringBuilder.toString();
    }

    /**
     * 上传文件接口
     *
     * @return
     */
    public static String getUpdateFile() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStartUrl());
        stringBuilder.append("/mobileservice/uploadfiles.ashx");
        return stringBuilder.toString();
    }
}
