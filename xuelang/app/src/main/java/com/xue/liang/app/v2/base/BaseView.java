package com.xue.liang.app.v2.base;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface BaseView<T> {


    void onSuccess(T t);

    void onFail(String info);
}
