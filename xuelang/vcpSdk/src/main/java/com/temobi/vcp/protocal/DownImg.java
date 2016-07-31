package com.temobi.vcp.protocal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * 图片下载
 * 
 * @author Administrator
 * 
 */
public class DownImg {

	private static final String TAG = "DownImg";
	private static final int IO_BUFFER_SIZE = 2 * 1024;
	private OnDownLoadProcessListener onDownLoadListener = null;

	public DownImg(OnDownLoadProcessListener listener) {

		onDownLoadListener = listener;
	}

	/**
	 * 图片下载
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
			// Log.d(TAG, "DownLoadFile-->"+fileurl);
			if (fileUtils.IsFileExists(destPath)) {

				imgPath = destPath;
				onDownLoadListener.onDownLoadResponse(
						OnDownLoadProcessListener.DOWNLOAD_SCCUESSED, imgPath);
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
				onDownLoadListener.onDownLoadResponse(
						OnDownLoadProcessListener.DOWNLOAD_FAILED, "下载失败");

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
					onDownLoadListener.onDownLoadResponse(
							OnDownLoadProcessListener.DOWNLOADING,
							String.valueOf(percent));
				}
				imgPath = destPath;
				onDownLoadListener.onDownLoadResponse(
						OnDownLoadProcessListener.DOWNLOAD_SCCUESSED, imgPath);

			}
		} catch (Exception e) {
			Log.d(TAG, "downLoadFile exception=" + e.getMessage());
			onDownLoadListener.onDownLoadResponse(
					OnDownLoadProcessListener.DOWNLOAD_FAILED, "下载失败");
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

	/**
	 * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
	 * 
	 * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
	 * 
	 * B.本地路径:url="file://mnt/sdcard/photo/image.png";
	 * 
	 * C.支持的图片格式 ,png, jpg,bmp,gif等等
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap GetLocalOrNetBitmap(String url) {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;
		URLConnection conn = null;
		try {
			onDownLoadListener.onDownLoadResponse(
					OnDownLoadProcessListener.DOWNLOADING, "0");

			Log.d(TAG, "GetLocalOrNetBitmap()--url=" + url);
			int downloadSize = 0;
			URL Url = new URL(url);
			conn = Url.openConnection();
			conn.setRequestProperty("Accept-Encoding", "identity");
			conn.connect();

			in = conn.getInputStream();
			int fileSize = conn.getContentLength();
			Log.d(TAG, "GetLocalOrNetBitmap()--url connected...");
			if (fileSize <= 0) {
				return null;
			}
			Log.d(TAG, "GetLocalOrNetBitmap()--file size=" + fileSize);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);

			byte[] b = new byte[IO_BUFFER_SIZE];
			int read;
			while ((read = in.read(b)) != -1) {
				out.write(b, 0, read);
				downloadSize += read;
				int percent = (int) ((((double) downloadSize) / (double) fileSize) * 100);

				onDownLoadListener.onDownLoadResponse(
						OnDownLoadProcessListener.DOWNLOADING,
						String.valueOf(percent));

			}
			out.flush();
			byte[] data = dataStream.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			data = null;
			onDownLoadListener.onDownLoadResponse(
					OnDownLoadProcessListener.DOWNLOAD_SCCUESSED, bitmap);

			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			onDownLoadListener.onDownLoadResponse(
					OnDownLoadProcessListener.DOWNLOAD_FAILED, "下载失败");
			return null;
		} finally {
			try {
				if (conn != null) {
					conn = null;
				}
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();

				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
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

		/**
		 * 删除文件
		 */
		public boolean delFile(String fileName) {
			File file = new File(fileName);

			return file.delete();

		}

	}

}
