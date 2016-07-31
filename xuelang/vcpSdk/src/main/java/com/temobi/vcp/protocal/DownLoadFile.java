package com.temobi.vcp.protocal;

import java.io.File;

import android.os.Handler;
import android.os.Message;

import com.temobi.vcp.http.download.HttpException;
import com.temobi.vcp.http.download.HttpHandler;
import com.temobi.vcp.http.download.HttpUtils;
import com.temobi.vcp.http.download.RequestCallBack;
import com.temobi.vcp.http.download.RequestParams;
import com.temobi.vcp.http.download.ResponseInfo;
import com.temobi.vcp.sdk.data.DownLoadStatus;

//需要注意的是：在JDK1.4以及之前的版本中，该方式仍然有问题。  这种方法叫做双重锁定(Double-Check Locking)的单例模式
public class DownLoadFile {
	private HttpUtils httpUtils;
	private HttpHandler<File> httpHandler;

	private Handler handler = null;

	private static volatile DownLoadFile instance = null;

	private DownLoadFile() {
		httpUtils = new HttpUtils();

	}

	public static DownLoadFile getInstance() {
		if (instance == null) {
			synchronized (DownLoadFile.class) {
				if (instance == null) {
					instance = new DownLoadFile();

				}
			}
		}
		return instance;
	}

	public void startdownload(String url, String target) {
		RequestParams requestParams = new RequestParams();
		requestParams.addHeader("Accept-Encoding", "identity");
		httpHandler = httpUtils.download(url, target, requestParams,
				new RequestCallBack<File>() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// TODO Auto-generated method stub

						int percent = (int) (current * 100 / total);
						onDownLoadResponse(
								DownLoadStatus.TM_RECORD_DOWNLOADING, percent);

						super.onLoading(total, current, isUploading);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

						onDownLoadResponse(
								DownLoadStatus.TM_RECORD_DOWNLOAD_FAILED, 0);

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						// TODO Auto-generated method stub
						onDownLoadResponse(
								DownLoadStatus.TM_RECORD_DOWNLOAD_OK, 0);
					}

				});

	}

	public void stopdownload() {
		if (null != httpHandler) {
			httpHandler.cancel();
		}

	}

	private void onDownLoadResponse(int responseCode, Object obj) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = responseCode;
		msg.obj = obj;
		handler.sendMessage(msg);

	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
