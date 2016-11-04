package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.device.DeviceReqBean;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jikun on 2016/11/4.
 */

public interface GetDeviceListService {


    @POST("api/mobileservice/getDeviceList")
    Observable<DeviceRespBean> getDeviceListService(@Body DeviceReqBean t);
}
