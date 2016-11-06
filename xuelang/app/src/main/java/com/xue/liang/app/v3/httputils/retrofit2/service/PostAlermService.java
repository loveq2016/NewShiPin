package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.postalarm.PostAlermReq;
import com.xue.liang.app.v3.bean.postalarm.PostAlermResp;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/6.
 */

public interface PostAlermService {

    @POST("mss/interface/postalerm")
    Observable<PostAlermResp> getPostAlermService(@Body PostAlermReq t);
}
