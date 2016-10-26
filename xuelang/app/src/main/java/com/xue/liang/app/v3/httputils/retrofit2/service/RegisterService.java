package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.login.LoginReqBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface RegisterService {
    @POST("api/mobileservice/register")
    Observable<LoginRespBean> getRegisterService(@Body LoginReqBean t);
}
