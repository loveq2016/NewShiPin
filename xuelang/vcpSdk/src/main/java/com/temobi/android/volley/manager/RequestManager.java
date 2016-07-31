/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.temobi.android.volley.manager;

import com.temobi.android.volley.Request;
import com.temobi.android.volley.RequestQueue;

import com.temobi.android.volley.toolbox.Volley;

import android.content.Context;

public class RequestManager {
	private static RequestQueue mRequestQueue;

	private RequestManager() {
		// no instances
	}

	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}

	public static void addRequest(Request<?> request, Object tag) {
		if (null != mRequestQueue) {

			if (null != tag) {
				if (request!=null) {
					request.setTag(tag);
				}
				
			}
			if (null != request) {
				mRequestQueue.add(request);
			}
		}

	}

	public static void cancelAll(Object tag) {

		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}

	}

}
