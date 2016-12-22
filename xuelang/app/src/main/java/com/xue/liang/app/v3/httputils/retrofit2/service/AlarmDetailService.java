package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.alarm.AlarmDetailReqBean;
import com.xue.liang.app.v3.bean.alarm.AlarmDetailRespBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/27.
 */

public interface AlarmDetailService {

    @POST("api/mobileservice/getAlarmDetail")
    Observable<AlarmDetailRespBean> getAlarmDetailService(@Body AlarmDetailReqBean t);
}
