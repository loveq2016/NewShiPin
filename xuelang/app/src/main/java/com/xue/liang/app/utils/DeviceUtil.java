package com.xue.liang.app.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceUtil {
    /**
     * 判断设备是否是手机
     */
    public static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephony.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }
}
