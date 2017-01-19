package com.xue.liang.app.v2.http.manager.data;

/**
 * Created by Administrator on 2016/7/31.
 */
public class HttpRequest<T> {
    public String errorMsg;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
