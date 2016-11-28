package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.alarmhandle.HandlerRtspReqBean;
import com.xue.liang.app.v3.bean.alarmhandle.HandlerRtspRespBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jikun on 2016/11/28.
 */

public interface GetAlarmHandlerRtstpService {

    @POST("portal/interface/getIpcSrcUrl")
    Observable<HandlerRtspRespBean> getAlarmHandlerRtstpService(@Body HandlerRtspReqBean t);
}
