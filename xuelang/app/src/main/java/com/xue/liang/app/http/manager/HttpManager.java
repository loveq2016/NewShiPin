package com.xue.liang.app.http.manager;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.xue.liang.app.data.request.RegisterReq;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.data.HttpRequest;
import com.xue.liang.app.http.manager.listenter.HttpListenter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/7/31.
 */
public class HttpManager<Q, R> {

    private Gson gson = new Gson();

    private Class<Q> mQclass;

    private Class<R> mRClass;

    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public String url;
    public HttpRequest<Q> httpRequest;
    public HttpReponse<R> httpReponse;
    public HttpListenter<R> httpListenter;

    private Handler uiHandler = new Handler(Looper.getMainLooper());
    ;


    public void ecUrl(String url) {
        this.url = url;
    }

    public void requestValue(Q q) {
        this.httpRequest = new HttpRequest<Q>();
        httpRequest.setData(q);

    }


//    public void requestClass(Class<Q> mTClass) {
//        this.mQclass = mTClass;
//
//    }


    public void responseClass(Class<R> mEClass) {
        this.mRClass = mEClass;

    }

    public void httpListenter(HttpListenter<R> httpListenter) {
        this.httpListenter = httpListenter;
    }

    public void dopost(String tag) {

        String json = gson.toJson(httpRequest.getData());
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request request = new Request.Builder().tag(tag).url(url).post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();
       // OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                final String msg = e.toString();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        httpListenter.onFailed(msg);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String json = response.body().toString();
                httpReponse = new HttpReponse();
                try{
                    R r = gson.fromJson(json, mRClass);
                    httpReponse.setData(r);
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpListenter.onSuccess(httpReponse);
                        }
                    });
                }catch (final Exception e){
                    final String msg=e.toString();
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpListenter.onFailed(msg);
                        }
                    });
                }



            }
        });


    }

    public void doGet() {

    }


    public static class HttpBuilder<Q, R> {

        private HttpManager httpManager;


        public HttpBuilder() {
            httpManager = new HttpManager();
        }


        public HttpBuilder<Q, R> buildUrl(String url) {

            httpManager.ecUrl(url);
            return this;

        }

        public HttpBuilder<Q, R> buildRequestValue(Q q) {

            httpManager.requestValue(q);
            return this;

        }


//        public HttpBuilder<Q, R> buildRequestClass(Class<Q> clazz) {
//
//            httpManager.requestClass(clazz);
//            return this;
//
//        }


        public HttpBuilder<Q, R> buildResponseClass(Class<R> clazz) {
            httpManager.responseClass(clazz);
            return this;
        }


        public HttpBuilder<Q, R> buildHttpListenter(HttpListenter<R> httpListenter) {
            httpManager.httpListenter(httpListenter);
            return this;
        }


        public HttpManager<Q, R> build() {
            return httpManager;
        }


    }


}
