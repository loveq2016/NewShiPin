package com.xue.liang.app.v2.presenter.impl;

import com.xue.liang.app.v2.base.BasePresenter;
import com.xue.liang.app.v2.entity.DeviceEntity;
import com.xue.liang.app.v2.entity.GroupMember6995Entity;
import com.xue.liang.app.v2.entity.SendCall6995Entity;
import com.xue.liang.app.v2.http.HttpResult;
import com.xue.liang.app.v2.http.RetrofitManager;
import com.xue.liang.app.v2.presenter.IMain;
import com.xue.liang.app.v2.rx.ApiFun1Builder;
import com.xue.liang.app.v2.rx.ApiSubscriber;
import com.xue.liang.app.v2.rx.RxUtils;
import com.xue.liang.app.v2.utils.DateUtil;
import com.xue.liang.app.v2.utils.HttpInfo6995Builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscription;

/**
 * Created by jikun on 17/5/19.
 */

public class MainPresenterImpl extends BasePresenter<IMain.MianView> implements IMain.MainPresenter {


    public MainPresenterImpl(IMain.MianView view) {
        super(view);
    }

    private Map<String, String> getDeviceCameraRequestInfo(String termi_unique_code) {
        Map<String, String> map = new HashMap<>();
        map.put("termi_type", "0");//  string      0: 中间件机顶盒(默认) 1: O 机顶盒 //2:手机  // 终端类型
        map.put("termi_unique_code", termi_unique_code);//string 终端 mac 地址/卡号/唯一号
        map.put("stream_type", "0");   //  int  否 返回摄像头码流类型:0 主码流(默认)1 子码流
        map.put("bchild", "0");//Int  否    是否返回自分组下所有摄像头(0 返回 1 不返回) 默认为 0
        return map;
    }


    private Map<String, String> getAlarmRequestInfo(String mac, String type, String userId, DeviceEntity entity) {
        Map<String, String> map = new HashMap<>();
        map.put("termi_type", "0");
        map.put("stb_id", mac);
        map.put("stb_type", "0");
        map.put("alerm_level", "0");
        map.put("alerm_type", "0");
        map.put("cam_dev_uid", entity.getDev_id());
        map.put("cam_dev_name", entity.getDev_name());
        map.put("cam_url", entity.getDev_url());
        map.put("user_id", userId);

        map.put("update_time", DateUtil.getCurrentTime());

        return map;

    }

    @Override
    public void getDeviceCameraList(String businessID, String lineMac, String wifiMac) {


//        Subscription subscribe = RetrofitManager.getInstance().getApiService()

        Subscription subscribe = Observable.just(new HttpResult<List<DeviceEntity>>())
                .flatMap(new ApiFun1Builder().getDeviceListFuc1(getDeviceCameraRequestInfo(businessID)))
                .flatMap(new ApiFun1Builder().getDeviceListFuc1(getDeviceCameraRequestInfo(lineMac)))
                .flatMap(new ApiFun1Builder().getDeviceListFuc1(getDeviceCameraRequestInfo(wifiMac)))
                .compose(RxUtils.apiSameTransformer())
                .filter(o -> mView != null)
                .subscribe(new ApiSubscriber<HttpResult<List<DeviceEntity>>>(mView, true) {
                    @Override
                    public void onNext(HttpResult<List<DeviceEntity>> httpResult) {
                        if (mView != null)
                            mView.getDeviceListSuccess(httpResult);
                    }
                });
        addSubscription(subscribe);
    }

    @Override
    public void getPostalerm(String mac, String type, String userId, DeviceEntity entity) {
        Subscription subscribe = RetrofitManager.getInstance().getApiService()
                .postalerm(getAlarmRequestInfo(mac, type, userId, entity))
                .compose(RxUtils.apiChildTransformer())
                .filter(o -> mView != null)
                .subscribe(new ApiSubscriber<String>(mView, true) {
                    @Override
                    public void onNext(String info) {
                        if (mView != null)
                            mView.postAlermSuccess();
                    }
                });
        addSubscription(subscribe);

    }

    @Override
    public void get6995sendCall(String phonenum, int type) {

        String request = HttpInfo6995Builder.buildSendCall(phonenum, type);
        RetrofitManager.getInstance().get6995ApiService()
                .get6995Url(request)
                .compose(RxUtils.ioToMain())
                .filter(o -> mView != null)
                .subscribe(new ApiSubscriber<String>(mView, false, false) {
                    @Override
                    public void onNext(String s) {

                        SendCall6995Entity sendCall6995Entity = HttpInfo6995Builder.XmlAnalysisUtils.analysisSendCall6995EntityXML(s);

                        mView.post6995AlarmSuccess(sendCall6995Entity);
                    }
                });

    }

    @Override
    public void get6995GroupMember(String phonenum) {

        String request = HttpInfo6995Builder.buildGetGroupMember(phonenum);
        RetrofitManager.getInstance().get6995ApiService()
                .get6995Url(request)
                .compose(RxUtils.ioToMain())
                .filter(o -> mView != null)
                .subscribe(new ApiSubscriber<String>(mView, true) {
                    @Override
                    public void onNext(String s) {


                        GroupMember6995Entity groupMember6995Entity = HttpInfo6995Builder.XmlAnalysisUtils.analysisGroupMember6995EntityXML(s);

                        mView.get6995GroupMemberSuccess(groupMember6995Entity);
                    }
                });

    }

    @Override
    public void get6995AddMember(String phonenum, String addPhonenum, String addName) {

        String request = HttpInfo6995Builder.buildAddGroupMemberInfo(phonenum, addPhonenum, addName);
        RetrofitManager.getInstance().get6995ApiService()
                .get6995Url(request)
                .compose(RxUtils.ioToMain())
                .filter(o -> mView != null)
                .subscribe(new ApiSubscriber<String>(mView, true) {
                    @Override
                    public void onNext(String s) {
                        SendCall6995Entity sendCall6995Entity = HttpInfo6995Builder.XmlAnalysisUtils.analysisSendCall6995EntityXML(s);
                        mView.add6995GroupMemberSuccess(sendCall6995Entity);
                    }
                });

    }


}
