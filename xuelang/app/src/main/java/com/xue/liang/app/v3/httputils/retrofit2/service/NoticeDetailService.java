package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.noticedetail.NoticeDetailReqBean;
import com.xue.liang.app.v3.bean.noticedetail.NoticeDetailRespBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jikun on 2016/11/4.
 */

public interface NoticeDetailService {

    @POST("api/mobileservice/getNoticeDetail")
    Observable<NoticeDetailRespBean> getNoticeDetailService(@Body NoticeDetailReqBean t);
}
