package com.xue.liang.app.utils;

import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.NoticeResp;
import com.xue.liang.app.data.reponse.RegisterResp;
import com.xue.liang.app.data.request.NoticeReq;
import com.xue.liang.app.data.request.RegisterReq;
import com.xue.liang.app.http.manager.HttpManager;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.listenter.HttpListenter;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Utils {
    public void getNoticeList() {
        HttpListenter<NoticeResp> httpListenter = new HttpListenter<NoticeResp>() {
            @Override
            public void onFailed(String msg) {

            }

            @Override
            public void onSuccess(HttpReponse<NoticeResp> httpReponse) {

            }
        };

        String url = Config.getNoticeUrl();
        NoticeReq noticeReq = new NoticeReq("1", "13808102118", "38:BC:1A:C5:DA:4F");
        HttpManager.HttpBuilder<NoticeReq, NoticeResp> httpBuilder = new HttpManager.HttpBuilder<>();
        httpBuilder.buildRequestValue(noticeReq)
                .buildResponseClass(NoticeResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("Notice");
    }
}
