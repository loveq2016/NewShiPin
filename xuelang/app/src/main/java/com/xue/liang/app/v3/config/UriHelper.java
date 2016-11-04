package com.xue.liang.app.v3.config;

/**
 * Created by Administrator on 2016/10/26.
 */
public class UriHelper {

    private static final String HTPP = "http://";
    //121.41.32.246:9004
    private static String IP = "182.150.56.73";//182.150.56.75:9002    121.41.32.246:9004   182.150.56.73  182.150.56.75
    private static String PORT = "9003";

    public static String getStartUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HTPP);
        stringBuilder.append(IP);
        stringBuilder.append(":" + PORT);
        stringBuilder.append("/");
        return stringBuilder.toString();
    }
}
