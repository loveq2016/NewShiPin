package com.xue.liang.app.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.xue.liang.app.R;

import com.xue.liang.app.login.LoginActivity;
import com.xue.liang.app.main.MainActivity;
import com.xue.liang.app.utils.DeviceUtil;



import java.lang.ref.WeakReference;

import butterknife.ButterKnife;


public class SplashActivity extends FragmentActivity {

    private DelayHandler delayHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initView();
    }


    protected void initView() {
        DeviceUtil.initConfig(getApplicationContext());
        delayHandler = new DelayHandler(this);
        toDelayTimeActivity();

    }

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

    private static class DelayHandler extends Handler {
        private WeakReference<SplashActivity> reference;

        private DelayHandler(SplashActivity activity) {
            reference = new WeakReference<SplashActivity>(activity);
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

        if (DeviceUtil.isPhone(getApplicationContext())) {
            //如果是手机那么就跳转到登陆界面

            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
        } else {

            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        }

    }


}
