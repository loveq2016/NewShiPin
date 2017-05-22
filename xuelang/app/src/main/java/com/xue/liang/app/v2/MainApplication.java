package com.xue.liang.app.v2;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.tencent.bugly.Bugly;
import com.xue.liang.app.v2.base.AppManager;
import com.xue.liang.app.v2.base.Constant;

/**
 * Created by Administrator on 2016/8/11.
 */
public class MainApplication extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppManager.getInstance().init(this);

        Bugly.init(getApplicationContext(), "e88fe7825f", false);


    }


}
