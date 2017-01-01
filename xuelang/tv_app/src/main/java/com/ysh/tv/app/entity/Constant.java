package com.ysh.tv.app.entity;

import android.app.Activity;

/**
 * @author lr
 * @version 1.0
 * @2013-7-18
 * @des 公共参数
 */
public class Constant {

    public static Activity c;
    // 屏幕的宽
    public static Integer W;
    // 屏幕的高
    public static Integer ScreenH;
    // 手机屏幕除去状态栏后的高
    public static Integer H;
    //是否首次进入应用

    public static boolean isFirstIntoApp;
    //是否登录
    public static boolean isLogin;
    //token
    public static String mSecurity = "";
    //微信ID
    public static String WXPAY_ID = "";
    //微信支付是否成功
    public static boolean WXPAY_IS_SUCCESS = false;
    public static boolean DEBUG=true;
    public static String mDeviceToken="";
    public static String token="";



}
