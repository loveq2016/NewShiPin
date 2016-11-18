package com.xue.liang.app.v3.config;

/**
 * Created by Administrator on 2016/10/26.
 */
public class UriHelper {


    private static final String HTPP = "http://";
    //121.41.32.246:9004     182.150.56.73:9003
    public final static String IP = "121.41.32.246";//182.150.56.75:9002    121.41.32.246:9004   182.150.56.73  182.150.56.75
    public final static int PORT = 9003;

    public final static  String EASY_PEOPLE_URL="http://www.uphsh.com/wap/38fceb89d44a4778bb2459556820c69a?userToken=";

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
    public static String getStayPendingAlarmUrl(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStartUrl());
        stringBuilder.append("/view/alarminfo.html?res_t=");
        return stringBuilder.toString();
    }
}
