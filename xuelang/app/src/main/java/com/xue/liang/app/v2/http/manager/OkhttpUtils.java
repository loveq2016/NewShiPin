package com.xue.liang.app.v2.http.manager;

import com.xue.liang.app.v2.http.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/8/19.
 */
public class OkhttpUtils {
    private static OkHttpClient okHttpClient = null;

    private OkhttpUtils() {

    }

    public static OkHttpClient getOkHttpClientInstance() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .addInterceptor(new LoggerInterceptor("xueliangv2", true))
                    .build();
        }

        return okHttpClient;
    }
}
