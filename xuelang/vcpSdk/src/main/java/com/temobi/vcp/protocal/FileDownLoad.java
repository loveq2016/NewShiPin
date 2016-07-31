package com.temobi.vcp.protocal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.temobi.vcp.protocal.DownImg.FileUtils;
import com.temobi.vcp.sdk.data.DownLoadStatus;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 文件下载
 * 
 * @author Administrator
 * 
 */
public class FileDownLoad extends AsyncTask<String, Void, String> implements
		OnDownLoadProcessListener {

	private static final String TAG = "FileDownLoad";

	private Handler handler = null;

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		//LoggerUtil.d("取消下载");
		super.onCancelled();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		String destPath = params[1];

		Log.d(TAG, "doInBackground-->url=" + url);
		Log.d(TAG, "doInBackground--destPath=" + destPath);

		
		if (isCancelled()) {
			return null;
			
		}else {
			String destFile = downLoadFile(url, destPath);

			return destFile;
		}
		
	}

	/**
	 * 文件下载
	 * 
	 * @param fileurl
	 *            图片路径
	 * @param path
	 *            存放路径
	 * @return
	 */
	public String downLoadFile(String fileurl, String destPath) {
		File file = null;
		String imgPath = "";
		InputStream is = null;
		FileOutputStream fos = null;
		try {

			FileUtils fileUtils = new FileUtils();
			Log.d(TAG, "DownLoadFile-->" + fileurl);
			if (fileUtils.IsFileExists(destPath)) {

				imgPath = destPath;
				onDownLoadResponse(DownLoadStatus.TM_RECORD_DOWNLOAD_OK,
						imgPath);
				return imgPath;
			}

			URL url = new URL(fileurl);// 获得或者确定下载的路径
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Accept-Encoding", "identity");
			conn.connect();
			is = conn.getInputStream();
			int fileSize = conn.getContentLength();
			Log.d(TAG, "downLoadFile-->fileSize=" + fileSize);
			if (fileSize < 1 || is == null) {
				onDownLoadResponse(DownLoadStatus.TM_RECORD_DOWNLOAD_FAILED,
						"下载失败");

			} else {

		
				file = new File(destPath);
				fos = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int len = -1;
				int downloadSize = 0;
				while ((len = is.read(bytes)) != -1) {
					fos.write(bytes, 0, len);
					// Log.d(TAG, "downLoadFile-->read size="+len);
					downloadSize += len;
					int percent = (int) ((((double) downloadSize) / (double) fileSize) * 100);
					onDownLoadResponse(DownLoadStatus.TM_RECORD_DOWNLOADING,
							percent);
				}
				imgPath = destPath;
				onDownLoadResponse(DownLoadStatus.TM_RECORD_DOWNLOAD_OK,
						imgPath);

			}
		} catch (Exception e) {
			Log.d(TAG, "downLoadFile exception=" + e.getMessage());
			onDownLoadResponse(DownLoadStatus.TM_RECORD_DOWNLOAD_FAILED, "下载失败");
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return imgPath;

	}

	@Override
	public void onDownLoadResponse(int responseCode, Object obj) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = responseCode;
		msg.obj = obj;
		handler.sendMessage(msg);

	}

	public class FileUtils {
		private String SDPATH;

		public FileUtils() {
			SDPATH = Environment.getExternalStorageDirectory() + "/";
			// "/mnt/sdcard/"
		}

		/**
		 * 获得当前系统的SDPATH路径字符
		 * */
		public String GetSDPATH() {
			return SDPATH;
		}

		/**
		 * 在SD卡上创建文件
		 * */
		public File CreateSDFile(String fileName) throws IOException {
			File file = new File(SDPATH + fileName);
			boolean isCreate = file.createNewFile();
			return file;
		}

		/**
		 * 在SD卡上创建文件夹
		 * */
		public File CreateSDDir(String dirName) {
			File file = new File(SDPATH + dirName);
			boolean isCreateDir = file.mkdir();
			return file;
		}

		/**
		 * 判断文件是否存在
		 * */
		public boolean IsFileExists(String fileName) {
			// "/mnt/sdcard/hhh/aaa.mp3"
			File file = new File(fileName);
			return file.exists();
		}

	}

}
