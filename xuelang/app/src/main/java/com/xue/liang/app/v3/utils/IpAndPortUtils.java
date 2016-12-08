package com.xue.liang.app.v3.utils;

import android.content.Context;

import com.xue.liang.app.v3.config.UriHelper;

/**
 * Created by jikun on 2016/12/8.
 */

public class IpAndPortUtils {

    public static void setUpIpAndPort(Context context) {
        UriHelper.IP = SharedDB.getStringValue(context, UriHelper.KEY_IP, UriHelper.DEFAUT_IP);

        UriHelper.PORT = SharedDB.getIntValue(context, UriHelper.KEY_PORT, UriHelper.DEFAUT_PORT);
    }


    public static void saveIpAndPort(Context context, String ip, int port) {
        SharedDB.putStringValue(context, UriHelper.KEY_IP, ip);
        SharedDB.putIntValue(context, UriHelper.KEY_PORT, port);
        UriHelper.IP = ip;
        UriHelper.PORT = port;
    }

    public static void recoveryIpAndPort(Context context) {
        SharedDB.putStringValue(context, UriHelper.KEY_IP, UriHelper.DEFAUT_IP);
        SharedDB.putIntValue(context, UriHelper.KEY_PORT, UriHelper.DEFAUT_PORT);
        UriHelper.IP = UriHelper.DEFAUT_IP;
        UriHelper.PORT = UriHelper.DEFAUT_PORT;
    }

    public static String getIp() {
        return UriHelper.IP;
    }

    public static int getPort() {
        return UriHelper.PORT;
    }
}
