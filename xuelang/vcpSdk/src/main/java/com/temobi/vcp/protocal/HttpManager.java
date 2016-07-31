package com.temobi.vcp.protocal;



import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpManager {
	private static String TAG = "HttpManager";
	private HttpURLConnection httpconn = null;
	private OutputStream os = null;
	private InputStream is = null;
	private ByteArrayOutputStream bos = null;
	
	public byte[] doGet(String path){
		byte[] results = null;
		// do http get
		return results;
	}
	
	public byte[] doPost(String path, byte[] postData, String sessionId, String x_ipcsn,String cameraId,String operId){
		Log.i(TAG, "doPost path:"+path);
		byte[] postResult = null;
		try{
			URL url = new URL(path);
			httpconn = (HttpURLConnection) url.openConnection();
			
			
			
			
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);
			httpconn.setConnectTimeout(10000);
			httpconn.setRequestMethod("POST");
			httpconn.setReadTimeout(5000);
			
			

			httpconn.addRequestProperty(MVPCommand.SESSIONID_FIELD, sessionId);
			httpconn.addRequestProperty(MVPCommand.IPCSN_FIELD, x_ipcsn);
			httpconn.addRequestProperty(MVPCommand.CAMERA_ID, cameraId);
			httpconn.addRequestProperty(MVPCommand.OPERID, operId);
			
			os = httpconn.getOutputStream();
			if (os != null) {
				Log.i(TAG, "output");
				os.write(postData);
				os.flush();
			}
			int code = httpconn.getResponseCode();
			is = httpconn.getInputStream();
			bos = new ByteArrayOutputStream();
			byte[] bytes = new byte[64];
			while (true) {

				int tmp = is.read(bytes);
				if (tmp == -1) {
					break;
				} else {
					bos.write(bytes, 0, tmp);
				}
			}
			postResult = bos.toByteArray();
		
			if (code == 200) {
				Log.i(TAG, "-------------------> success! ");
			} else {
				Log.e(TAG, "-------------------> response code = " + code);
				Log.e(TAG, "-------------------> doPost fail! So try again~~~ ");
			}
			httpconn.disconnect();
		}catch(Exception e){
			
		LoggerUtil.d("显示异常"+e.toString());
			
		}finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
				if (httpconn != null) {
					httpconn.disconnect();
				}
				bos = null;
				os = null;
				is = null;
				httpconn = null;
			} catch (Exception e) {
			}
		}		
		return postResult;
	}
	
	
	
}
