package com.xue.liang.app.v2.http.manager.data;

/**
 * Created by Administrator on 2016/7/31.
 */
public class HttpReponse<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
