package com.xue.liang.app.http.manager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/8/19.
 */
public class OkhttpUtils {
    private static OkHttpClient okHttpClient = null;

    private  OkhttpUtils(){

    }

    public static OkHttpClient getOkHttpClientInstance() {
        if(okHttpClient==null){
            okHttpClient=  new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .build();
        }

        return okHttpClient;
    }
}
