package com.xue.liang.app.v3.httputils.retrofit2;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface UserInfoService{
        @GET("user")
        Observable<String> getUser(@Query("access_token") String token);
    }