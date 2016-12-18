package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.region.RegionReqBean;
import com.xue.liang.app.v3.bean.region.RegionRespBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/27.
 */

public interface GetRegionListService {

    @POST("api/mobileservice/getRegionList")
    Observable<RegionRespBean> getRegionListService(@Body RegionReqBean t);
}
