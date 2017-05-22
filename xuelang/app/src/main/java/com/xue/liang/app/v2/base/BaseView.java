package com.xue.liang.app.v2.base;

public interface BaseView {
    void onError();

    void toast(String msg);

    void showLoading();

    void dismissLoading();

}