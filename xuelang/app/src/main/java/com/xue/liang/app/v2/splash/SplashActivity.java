package com.xue.liang.app.v2.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.base.BaseActivity;
import com.xue.liang.app.v2.main.MainActivity;
import com.xue.liang.app.v2.utils.DeviceUtil;

import java.lang.ref.WeakReference;


public class SplashActivity extends BaseActivity {

    private DelayHandler delayHandler;


    private void toDelayTimeActivity() {
        delayHandler.sendEmptyMessageDelayed(0, 1);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        delayHandler.removeCallbacksAndMessages(null);
        delayHandler = null;
        super.onDestroy();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DeviceUtil.initConfig(getApplicationContext());
        delayHandler = new DelayHandler(this);
        toDelayTimeActivity();

    }

    private static class DelayHandler extends Handler {
        private WeakReference<SplashActivity> reference;

        private DelayHandler(SplashActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (reference.get() != null) {
                reference.get().toPlayerActivity();
                reference.get().finish();
            }
            super.handleMessage(msg);
        }
    }

    public void toPlayerActivity() {

        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);


    }


}
