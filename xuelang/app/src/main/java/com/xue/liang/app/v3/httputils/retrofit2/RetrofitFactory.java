package com.xue.liang.app.v3.httputils.retrofit2;

import com.xue.liang.app.v3.adapter.sadapter.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/10/26.
 */
public class RetrofitFactory {
    private static final long DEFAULT_TIMEOUT = 15_000L;

    public static Retrofit creatorGsonRetrofit(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url).client(genericClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }


    private static OkHttpClient genericClient() {
        try {
//            InputStream inputStream = AppManager.appContext().getAssets().open("server.bks");
//            InputStream clientIn = AppManager.appContext().getAssets().open("client1.bks");
//            HttpsManager.SSLParams ssl = HttpsManager.getSslSocketFactory(new InputStream[]{inputStream}, clientIn, Constant.PWD);

            return new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .addInterceptor(new LoggerInterceptor("xueliang", true))
//                .addInterceptor(new FakeInterceptor())
                    .build();
        } catch (Exception e) {
            return new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .addInterceptor(new LoggerInterceptor("xueliang", true))
//                .addInterceptor(new FakeInterceptor())
                    .build();
        }
    }
}
