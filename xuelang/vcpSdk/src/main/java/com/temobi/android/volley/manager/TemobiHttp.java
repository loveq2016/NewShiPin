package com.temobi.android.volley.manager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.temobi.android.volley.AuthFailureError;
import com.temobi.android.volley.DefaultRetryPolicy;
import com.temobi.android.volley.Response;
import com.temobi.android.volley.RetryPolicy;
import com.temobi.android.volley.VolleyError;
import com.temobi.android.volley.Request.Method;
import com.temobi.android.volley.toolbox.StringRequest;
import com.temobi.vcp.protocal.LoggerUtil;
import com.temobi.vcp.protocal.MVPCommand;

public class TemobiHttp {

	private String mpath;
	private String mpostData;
	private String msessionId;
	private String mx_ipcsn;
	private String mcameraId;
	private String moperId;

	private String Tag = "Temobi";

	private static volatile TemobiHttp instance = null;

	private TemobiHttp() {
	}

	public static TemobiHttp getInstance() {
		if (instance == null) {
			synchronized (TemobiHttp.class) {
				if (instance == null) {
					instance = new TemobiHttp();
				}
			}
		}
		return instance;
	}

	public void doPost(String path, String postData, String sessionId,
			String x_ipcsn, String cameraId, String operId,
			Response.Listener<String> responseListener,
			Response.ErrorListener errorListener) {
		mpath = path;
		mpostData = postData;
		msessionId = sessionId;
		mx_ipcsn = x_ipcsn;
		mcameraId = cameraId;
		moperId = operId;

		StringRequest request = new StringRequest(Method.POST, mpath,
				responseListener, errorListener) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				// TODO Auto-generated method stub
				HashMap hashmap = new HashMap();
				hashmap.put(MVPCommand.SESSIONID_FIELD, msessionId);
				hashmap.put(MVPCommand.IPCSN_FIELD, mx_ipcsn);
				hashmap.put(MVPCommand.CAMERA_ID, mcameraId);
				hashmap.put(MVPCommand.OPERID, moperId);

				return hashmap;
			}

			@Override
			public byte[] getBody() throws AuthFailureError {
				// TODO Auto-generated method stub
				byte[] bytePostData = null;
				try {
					bytePostData = mpostData.getBytes("UTF-8");

					// LoggerUtil.d("Temobihttp请求" + mpostData);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return bytePostData;
			}

		};

		RetryPolicy retryPolicy = new DefaultRetryPolicy(20 * 1000, 1, 1.0f);

		request.setRetryPolicy(retryPolicy);
		RequestManager.addRequest(request, Tag);

		// RequestManager.addRequest(new StringRequest(Method.POST, mpath,
		// responseListener, errorListener) {
		//
		// @Override
		// public Map<String, String> getHeaders() throws AuthFailureError {
		// // TODO Auto-generated method stub
		// HashMap hashmap = new HashMap();
		// hashmap.put(MVPCommand.SESSIONID_FIELD, msessionId);
		// hashmap.put(MVPCommand.IPCSN_FIELD, mx_ipcsn);
		// hashmap.put(MVPCommand.CAMERA_ID, mcameraId);
		// hashmap.put(MVPCommand.OPERID, moperId);
		//
		// return hashmap;
		// }
		//
		// @Override
		// public byte[] getBody() throws AuthFailureError {
		// // TODO Auto-generated method stub
		// byte[] bytePostData = null;
		// try {
		// bytePostData = mpostData.getBytes("UTF-8");
		//
		// // LoggerUtil.d("Temobihttp请求" + mpostData);
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// return bytePostData;
		// }
		//
		// @Override
		// public RetryPolicy getRetryPolicy() {
		// // TODO Auto-generated method stub
		//
		// RetryPolicy retryPolicy = new DefaultRetryPolicy(
		// DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
		// DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
		// DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		// return retryPolicy;
		//
		// }
		//
		// }, Tag);

	}

	public void cancelAll() {
		// LoggerUtil.d("取消请求");
		RequestManager.cancelAll(Tag);

	}

}
