package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.alarm.AlarmReqBean;
import com.xue.liang.app.v3.bean.alarm.AlarmRespBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jikun on 2016/11/4.
 */

public interface GetAlarmListService {


    @POST("api/mobileservice/getAlarmList")
    Observable<AlarmRespBean> getAlarmListService(@Body AlarmReqBean t);
}
