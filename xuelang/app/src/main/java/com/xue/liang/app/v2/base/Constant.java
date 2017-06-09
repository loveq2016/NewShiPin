package com.xue.liang.app.v2.base;

/**
 * Created by jikun on 17/5/19.
 */

public class Constant {

    public static final String DEFAULT_IP = "112.54.93.210";//117.173.19.185

    public static final String DEFAULT_PORT = "9003";

    public static final boolean Default_IS_6995 = false;

    public static final String HTPP = "http://";
    public static String IP = "";//182.150.56.75:9002    121.41.32.246:9004   182.150.56.73  182.150.56.75
    public static String PORT = "";

    public static final String IP_6995 = "218.200.206.182";
    public static final String PORT_6995 = "8005";


    public static final String KEY_6995 = "kingon!qaz@wsx#edc$rfv%^";//用来加密解密6695协议的KEY值，请不要修改


    public static final String Key_Termi_Unique_Code = "termi_unique_code";
    public static boolean is6995Open = false;


    public static String getBaseUrl() {

        return HTPP + IP + ":" + PORT + "/";

    }

    public static String get6995BaseUrl() {
        return HTPP + IP_6995 + ":" + PORT_6995 + "/";
    }
}
