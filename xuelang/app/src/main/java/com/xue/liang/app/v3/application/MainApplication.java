package com.xue.liang.app.v3.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/8/11.
 */
public class MainApplication extends MultiDexApplication {


    private static MainApplication instance;

    public Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;
        initOkUtils();
        Bugly.init(getApplicationContext(), "e1b4f29d14", false);
        // CrashReport.initCrashReport(getApplicationContext(), "e1b4f29d14", false);
        initJpushSdk();
        initHaiKangYunYaiSdk();

    }

    private void initOkUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }


    private void initJpushSdk() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }


    public static MainApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return getInstance().getApplicationContext();
    }

    //初始化海康云台控制SDK
    private void initHaiKangYunYaiSdk() {
        System.loadLibrary("gnustl_shared");
        MCRSDK.init();
        RtspClient.initLib();
        MCRSDK.setPrint(1, null);
        VMSNetSDK.getInstance().openLog(true);
    }
}
