package com.xue.liang.app.v3.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class DeviceUtil {
    /**
     * 判断设备是否是手机
     */
    private static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephony.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }


    /**
     * 获取当前设备是属于手机还是机顶盒
     * @param context
     * @return
     */
    public static String getWhickPhoneType(Context context) {
        String type = "";
        if (DeviceUtil.isPhone(context)) {
            type = Constant.PHONE;//2为手机
        } else {
            type = Constant.TV;//1为机顶盒
        }
        return type;
    }

    private static String getLocalMacAddress() {

        Enumeration<NetworkInterface> interfaces = null;
        String mac = "";
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();


            while (interfaces.hasMoreElements()) {
                NetworkInterface iF = interfaces.nextElement();
                byte[] addr = iF.getHardwareAddress();
                if (addr == null || addr.length == 0) {
                    continue;
                }
                StringBuilder buf = new StringBuilder();
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                mac = buf.toString();
                Log.d("mac", "interfaceName=" + iF.getName() + ", mac=" + mac);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return mac;
    }


    public static String getMac() {
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            String line;
            while ((line = input.readLine()) != null) {
                macSerial += line.trim();
            }

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return macSerial;
    }


}
