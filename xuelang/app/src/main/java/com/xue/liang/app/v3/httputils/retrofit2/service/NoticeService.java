package com.xue.liang.app.v3.httputils.retrofit2.service;

import com.xue.liang.app.v3.bean.notice.NoticeReqBean;
import com.xue.liang.app.v3.bean.notice.NoticeRespBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jikun on 2016/11/4.
 */

public interface NoticeService {

    @POST("api/mobileservice/getNotice")
    Observable<NoticeRespBean> getNoticeService(@Body NoticeReqBean t);
}
