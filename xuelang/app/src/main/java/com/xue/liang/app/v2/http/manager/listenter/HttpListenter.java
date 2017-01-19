package com.xue.liang.app.v2.http.manager.listenter;

import com.xue.liang.app.v2.http.manager.data.HttpReponse;

/**
 * Created by Administrator on 2016/7/31.
 */
public interface HttpListenter<T> {






    //void onLoading(int percent);

    void onFailed(String msg);

    void onSuccess(HttpReponse<T> httpReponse);
}
