package com.xue.liang.app.utils;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.DeviceListResp;
import com.xue.liang.app.data.reponse.NoticeDetailResp;
import com.xue.liang.app.data.reponse.NoticeResp;
import com.xue.liang.app.data.reponse.RegisterResp;
import com.xue.liang.app.data.reponse.SendAlarmResp;
import com.xue.liang.app.data.reponse.UpdateAlarmResp;
import com.xue.liang.app.data.request.DeviceListReq;
import com.xue.liang.app.data.request.NoticeDetailReq;
import com.xue.liang.app.data.request.NoticeReq;
import com.xue.liang.app.data.request.RegisterReq;
import com.xue.liang.app.data.request.SendAlarmReq;
import com.xue.liang.app.data.request.UpdateAlarmReq;
import com.xue.liang.app.http.manager.HttpManager;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.listenter.HttpListenter;
import com.xue.liang.app.http.manager.listenter.LoadingHttpListener;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Utils {
    public void getNoticeList(FragmentManager fragmentManager) {
        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<NoticeResp>() {
            @Override
            public void onFailed(String msg) {

            }

            @Override
            public void onSuccess(HttpReponse<NoticeResp> httpReponse) {


            }
        }, fragmentManager);

        String url = Config.getNoticeUrl();
        NoticeReq noticeReq = new NoticeReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC);
        HttpManager.HttpBuilder<NoticeReq, NoticeResp> httpBuilder = new HttpManager.HttpBuilder<>();
        httpBuilder.buildRequestValue(noticeReq)
                .buildResponseClass(NoticeResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("Notice");
    }


    public void getNoticeDetail(String id, FragmentManager fragmentManager) {


        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<NoticeDetailResp>() {
            @Override
            public void onFailed(String msg) {

            }

            @Override
            public void onSuccess(HttpReponse<NoticeDetailResp> httpReponse) {


            }
        }, fragmentManager);

        String url = Config.getNoticeDetailUrl();
        NoticeDetailReq noticeDetailReq = new NoticeDetailReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, id);
        HttpManager.HttpBuilder<NoticeDetailReq, NoticeDetailResp> httpBuilder = new HttpManager.HttpBuilder<>();
        httpBuilder.buildRequestValue(noticeDetailReq)
                .buildResponseClass(NoticeDetailResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("NoticeDetail");
    }


    public void getDeviceList(FragmentManager fragmentManager){
        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<DeviceListResp>() {
            @Override
            public void onFailed(String msg) {

            }

            @Override
            public void onSuccess(HttpReponse<DeviceListResp> httpReponse) {


            }
        }, fragmentManager);


        String url = Config.getDeviceListUrl();
        DeviceListReq deviceListReq=new DeviceListReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC,null,null);
        //NoticeDetailReq noticeDetailReq = new NoticeDetailReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, id);
        HttpManager.HttpBuilder<DeviceListReq, DeviceListResp> httpBuilder = new HttpManager.HttpBuilder<>();
        httpBuilder.buildRequestValue(deviceListReq)
                .buildResponseClass(DeviceListResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("DeviceList");
    }

    /**
     *
     * @param alermType 报警类型：101 医疗救助 102火灾 103盗抢 104 伤害
     * @param fragmentManager
     */
    public void sendAlarm(String alermType,FragmentManager fragmentManager){

        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<SendAlarmResp>() {
            @Override
            public void onFailed(String msg) {

            }

            @Override
            public void onSuccess(HttpReponse<SendAlarmResp> httpReponse) {


            }
        }, fragmentManager);

        String url = Config.getSendAlarmUrl();
        //DeviceListReq deviceListReq=new DeviceListReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC,null,null);
       SendAlarmReq sendAlarmReq=new SendAlarmReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC,alermType,null);
        //NoticeDetailReq noticeDetailReq = new NoticeDetailReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, id);
        HttpManager.HttpBuilder<SendAlarmReq, SendAlarmResp> httpBuilder = new HttpManager.HttpBuilder<>();
        httpBuilder.buildRequestValue(sendAlarmReq)
                .buildResponseClass(SendAlarmResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("SendAlarm");

    }

    public void updateAlermHelp(FragmentManager fragmentManager,String content,String fileid){



        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<UpdateAlarmReq>() {
            @Override
            public void onFailed(String msg) {

            }

            @Override
            public void onSuccess(HttpReponse<UpdateAlarmReq> httpReponse) {


            }
        }, fragmentManager);

        String url = Config.getUpdateAlarmUrl();

        UpdateAlarmReq updateAlarmReq=new UpdateAlarmReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC,content,fileid);
        //NoticeDetailReq noticeDetailReq = new NoticeDetailReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, id);
        HttpManager.HttpBuilder<UpdateAlarmReq, UpdateAlarmResp> httpBuilder = new HttpManager.HttpBuilder<>();
        httpBuilder.buildRequestValue(updateAlarmReq)
                .buildResponseClass(UpdateAlarmResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("UpdateAlarm");
    }
}
