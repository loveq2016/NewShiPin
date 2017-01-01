package com.ysh.tv.app.entity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Administrator on 2016/7/12.
 */
public class SharedPreferencesUtil {

    // 定义一个存放参数的SharedPreferences
    public static SharedPreferences share;
    public static final String DATA_NAME = "dianshijiemu";

    /**
     * 往SharedPreferences中存放String类型的参数
     */
    public static void setParameter(String key, String value) {
        if (share == null) {
            share = Constant.c.getSharedPreferences(DATA_NAME,
                    Activity.MODE_PRIVATE);
        }
        Editor editor = share.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setParameter(String filename,String key, String value) {
        if (share == null) {
            share = Constant.c.getSharedPreferences(filename,
                    Activity.MODE_PRIVATE);
        }
        Editor editor = share.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 往SharedPreferences中存放long类型的参数
     */
    public static void setParameter(String key, long value) {
        if (share == null) {
            share = Constant.c.getSharedPreferences(DATA_NAME,
                    Activity.MODE_PRIVATE);
        }
        Editor editor = share.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 往SharedPreferences中存放布尔类型的参数
     */
    public static void setParameter(String key, Boolean value) {
        if (share == null) {
            share = Constant.c.getSharedPreferences(DATA_NAME,
                    Activity.MODE_PRIVATE);
        }
        Editor editor = share.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取数据类型为long的值
     */
    public static long getLongData(String key) {
        if (share == null) {
            share = Constant.c.getSharedPreferences(DATA_NAME,
                    Activity.MODE_PRIVATE);
        }
        long data = share.getLong(key, 0);
        return data;
    }


    public static String get(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    /**
     * 获取卡保存的数据
     */
    public static void getData() {
        if (share == null) {
            share = Constant.c.getSharedPreferences(DATA_NAME,
                    Activity.MODE_PRIVATE);
        }
        if (share != null) {
            Constant.token = share.getString("token","");
          //  Constant.exp = share.getLong("exp", 0);
            //Constant.userName = share.getString("userName", "");
            //Constant.pushSwitch = share.getBoolean("pushSwitch", true);
            //Constant.mCity = share.getString("mCity", "");
            Constant.mDeviceToken = share.getString("deviceToken","");
        }
    }

    /**
     *
     * @param context
     * @param fileName
     *            "userinfo":个人信息,"login":登录信息
     * @param key
     * @return true 删除成功，false 删除失败
     */
    public static boolean remove(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        if (preferences.contains(key)) {
            editor.remove(key);
        } else {
            return false;
        }
        boolean res = editor.commit();
        editor.apply();
        return res;

    }

    public static boolean removeAll(Context context, String fileName) {
        SharedPreferences preferences = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        return editor.commit();
    }

}
