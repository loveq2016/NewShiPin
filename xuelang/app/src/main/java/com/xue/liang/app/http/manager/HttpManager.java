package com.xue.liang.app.http.manager;

import com.google.gson.Gson;
import com.xue.liang.app.data.request.RegisterRq;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.data.HttpRequest;
import com.xue.liang.app.http.manager.listenter.HttpListenter;

import java.io.IOException;
import java.sql.Time;

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
public class HttpManager<T, T2> {

    private Gson gson = new Gson();

    private Class<T> mTClass;

    private Class<T2> mEClass;

    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public String url;
    public HttpRequest<T> httpRequest;
    public HttpReponse<T2> httpReponse;
    public HttpListenter httpListenter;


    public void ecUrl(String url) {
        this.url = url;
    }

    public void ecRequest(Class<T> mTClass) {
        this.mTClass = mTClass;

    }


    public void ecResponse(Class<T2> mEClass) {
        this.mEClass = mEClass;

    }

    public void echttpListenter(HttpListenter httpListenter) {
        this.httpListenter = httpListenter;
    }

    public void dopost(T t) {

        httpRequest = new HttpRequest();
        httpRequest.setData(t);

        String json = gson.toJson(httpRequest.getData());
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpRequest.setErrorMsg(e.toString());
                httpListenter.onFailed(httpRequest);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String json = response.body().toString();
                httpReponse = new HttpReponse();
                T2 e = gson.fromJson(json, mEClass);
                httpReponse.setData(e);
                httpListenter.onSuccess(httpReponse);

            }
        });


    }

    public void doGet() {

    }

    public interface Builder {

        void buildUrl(String url);


        void buildHttpListenter(HttpListenter httpListenter);

    }

    public static class HttpBuilder<T, T2> {

        private HttpManager httpManager;


        public HttpBuilder() {
            httpManager = new HttpManager();
        }


        public HttpBuilder buildUrl(String url) {

            httpManager.ecUrl(url);
            return this;

        }


        public HttpBuilder buildRequestClazz(Class<T> mTClass) {

            httpManager.ecRequest(mTClass);
            return this;

        }


        public HttpBuilder buildResponseClazz(Class<T2> mEClass) {
            httpManager.ecResponse(mEClass);
            return this;
        }


        public HttpBuilder buildHttpListenter(HttpListenter<T,T2> httpListenter) {
            httpManager.echttpListenter(httpListenter);
            return this;
        }


        public HttpManager build() {
            return httpManager;
        }


    }

}
