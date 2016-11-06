package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpReq;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpResp;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/6.
 */

public interface UpdateFileAlarmService {

    @POST("api/mobileservice/alarmForHelp")
    Observable<AlarmForHelpResp> getAlarmForHelpService(@Body AlarmForHelpReq t);
}
