package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.verifycode.VerifyCodeReqBean;
import com.xue.liang.app.v3.bean.verifycode.VerifyCodeRespBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface VerifyCodeService {
    @POST("api/XnService/SendVerification")
    Observable<VerifyCodeRespBean> getVerifyCode(@Body VerifyCodeReqBean t);
}
