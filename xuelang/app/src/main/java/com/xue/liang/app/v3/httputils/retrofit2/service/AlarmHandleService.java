package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.alarmhandle.AlarmHandleReqBean;
import com.xue.liang.app.v3.bean.alarmhandle.AlarmHandleRespBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/27.
 */

public interface AlarmHandleService {

    @POST("api/mobileservice/alarmHandle")
    Observable<AlarmHandleRespBean> getAlarmHandleService(@Body AlarmHandleReqBean t);
}
