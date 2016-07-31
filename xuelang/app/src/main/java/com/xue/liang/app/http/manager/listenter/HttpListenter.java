package com.xue.liang.app.http.manager.listenter;

import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.data.HttpRequest;

/**
 * Created by Administrator on 2016/7/31.
 */
public interface HttpListenter<T1, T2> {






    //void onLoading(int percent);

    void onFailed(HttpRequest<T1> httpRequest);

    void onSuccess(HttpReponse<T2> httpReponse);
}
