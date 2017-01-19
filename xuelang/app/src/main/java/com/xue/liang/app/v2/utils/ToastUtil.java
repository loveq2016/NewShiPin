package com.xue.liang.app.v2.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	private static Toast mToast = null;

	public static void showToast(Context context, String text, int duration) {

		if (context == null) {
			return;
		}
		if (mToast == null) {
			mToast = Toast.makeText(context, text, duration);
		} else {
			mToast.setText(text);
			mToast.setDuration(duration);
		}

		mToast.show();
	}
}
