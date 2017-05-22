package com.xue.liang.app.v2.http;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jikun on 17/5/19.
 */

public interface ApiService6995 {

    @POST("Xlgc/MobileService.aspx")
    Observable<String> get6995Url(@Body String params);


}
