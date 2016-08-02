package com.xue.liang.app.splash;

import java.lang.ref.WeakReference;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.xue.liang.app.R;
import com.xue.liang.app.login.LoginActivity_;
import com.xue.liang.app.utils.DeviceUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends FragmentActivity {

	private DelayHandler delayHandler;


	@AfterViews
	protected  void initView(){
		delayHandler = new DelayHandler(this);
		toDelayTimeActivity();
	}

	private void toDelayTimeActivity() {
		delayHandler.sendEmptyMessageDelayed(0, 2000);

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
		if(DeviceUtil.isPhone(getApplicationContext())){
			//如果是手机那么就跳转到登陆界面

			Intent intent = new Intent();
			intent.setClass(this, LoginActivity_.class);
			startActivity(intent);
		}else{

			Intent intent = new Intent();
			intent.setClass(this, LoginActivity_.class);
			startActivity(intent);
		}

	}

}
