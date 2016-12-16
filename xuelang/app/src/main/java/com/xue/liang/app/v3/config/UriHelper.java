package com.xue.liang.app.v3.config;

/**
 * Created by Administrator on 2016/10/26.
 *
 *
 *     public static String phoneNum = "13000000002";
 public static String termi_unique_code = "e1:5a:86:be:a6:aa";
 */
//
public class UriHelper {


    public static final String KEY_IP = "V3_IP";

    public static final String KEY_PORT = "V3_PORT";

    public final static String DEFAUT_IP="121.41.32.246";

    public final static int DEFAUT_PORT=9003;


    private static final String HTPP = "http://";
    //121.41.32.246:9004     182.150.56.73:9003
    public static String IP = DEFAUT_IP;//182.150.56.75:9002    121.41.32.246:9004   182.150.56.73  182.150.56.75
    public static int PORT = DEFAUT_PORT;

    public final static String EASY_PEOPLE_URL = "http://www.uphsh.com/wap/38fceb89d44a4778bb2459556820c69a?userToken=";

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
        stringBuilder.append("api/mobileservice/uploadfiles");
        return stringBuilder.toString();
    }

    public static String getStayPendingAlarmUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getStartUrl());
        stringBuilder.append("/view/alarminfo.html?res_t=");
        return stringBuilder.toString();
    }
}
