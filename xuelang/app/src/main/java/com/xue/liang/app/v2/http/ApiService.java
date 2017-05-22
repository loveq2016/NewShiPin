package com.xue.liang.app.v2.http;


import com.xue.liang.app.v2.entity.DeviceEntity;
import com.xue.liang.app.v2.entity.InfoBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;


public interface ApiService {


    @POST("api/mobileservice/getDeviceList")
    Observable<HttpResult<List<DeviceEntity>>> getDeviceList(@Body Map<String, String> params);


    @POST("mss/interface/postalerm")
    Observable<HttpResult<String>> postalerm(@Body Map<String, String> params);


    @POST("api/mobileservice/getNotice")
    Observable<HttpResult<List<InfoBean>>> getNoticeList(@Body Map<String, String> params);

    @POST("api/mobileservice/getNoticeDetail")
    Observable<HttpResult<String>> getNoticeDetail(@Body Map<String, String> params);


}