package com.xue.liang.app.v2.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xue.liang.app.v2.common.Config;

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
    public static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephony.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    //    public static String getLocalMacAddress(Context context) {
//        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = wifi.getConnectionInfo();
//        return info.getMacAddress();
//    }
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

    public static void initConfig(Context context) {
        Config.IP = SharedDB.getStringValue(context,
                ShareKey.IP_KEY, DefaultData.Default_IP);
        Config.PORT = SharedDB.getStringValue(context,
                ShareKey.PORT_KEY, DefaultData.Default_Port);

        String mac = DeviceUtil.getLocalMacAddress();
        Config.TEST_MAC = mac;
        if (DeviceUtil.isPhone(context)) {
            Config.TEST_TYPE = Constant.PHONE;//2为手机
        } else {
            Config.TEST_TYPE = Constant.TV;//1为机顶盒
        }

    }
}
