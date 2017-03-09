package com.xue.liang.app.v3.fragment.device;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.xue.liang.app.v3.application.MainApplication;
import com.xue.liang.app.v3.bean.ServerInfoBean;
import com.xue.liang.app.v3.bean.device.DeviceReqBean;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.bean.postalarm.PostAlermReq;
import com.xue.liang.app.v3.bean.postalarm.PostAlermResp;
import com.xue.liang.app.v3.bean.region.RegionReqBean;
import com.xue.liang.app.v3.bean.region.RegionRespBean;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.httputils.retrofit2.RetrofitFactory;
import com.xue.liang.app.v3.httputils.retrofit2.service.GetDeviceListService;
import com.xue.liang.app.v3.httputils.retrofit2.service.GetRegionListService;
import com.xue.liang.app.v3.httputils.retrofit2.service.PostAlermService;
import com.xue.liang.app.v3.utils.DeviceUtil;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/26.
 */
public class DevicePresenter implements DeviceContract.Presenter {

    private DeviceContract.View mView;

    public Subscription subscrip;

    private  ServInfo mServInfo;

    public DevicePresenter(DeviceContract.View view) {
        mView = view;
    }

    @Override
    public void loadData(DeviceReqBean bean) {

        String GET_API_URL = UriHelper.getStartUrl();

        mView.showLoadingView("");
        Retrofit retrofit = RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        GetDeviceListService service = retrofit.create(GetDeviceListService.class);
        subscrip = service.getDeviceListService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeviceRespBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoadingView();
                        String errorinfo = "";
                        if (e != null && e.toString() != null) {
                            errorinfo = e.toString();
                        }
                        mView.onError(errorinfo);
                    }

                    @Override
                    public void onNext(DeviceRespBean bean) {
                        mView.hideLoadingView();
                        if (bean.getRet_code() == 0) {
                            mView.onSuccess(bean);
                        } else {
                            mView.onFail(bean);
                        }
                    }
                });


    }


    public void postalarmType(PostAlermReq bean) {


        String GET_API_URL = UriHelper.getStartUrl();

        mView.showLoadingView("");
        Retrofit retrofit = RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        PostAlermService service = retrofit.create(PostAlermService.class);
        subscrip = service.getPostAlermService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostAlermResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.hideLoadingView();
                        String errorinfo = "";
                        if (e != null && e.toString() != null) {
                            errorinfo = e.toString();
                        }
                        mView.onPostAlermFail(errorinfo);
                    }

                    @Override
                    public void onNext(PostAlermResp resp) {
                        mView.hideLoadingView();
                        if (resp.getRet_code() == 1) {
                            mView.onPostAlermSuccess(resp);
                        } else {
                            String errorinfo = "";
                            if (resp != null && resp.getRet_string() != null) {
                                errorinfo = resp.getRet_string();
                            }
                            mView.onPostAlermFail(errorinfo);
                        }
                    }
                });

    }


    /**
     * @param userId 登陆的userId
     */
    public void getRegion(String userId) {
        RegionReqBean bean = new RegionReqBean();
        bean.setUser_id(userId);

        String GET_API_URL = UriHelper.getStartUrl();

        mView.showLoadingView("");
        Retrofit retrofit = RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        GetRegionListService service = retrofit.create(GetRegionListService.class);
        subscrip = service.getRegionListService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegionRespBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.hideLoadingView();
                        String errorinfo = "";
                        if (e != null && e.toString() != null) {
                            errorinfo = e.toString();
                        }
                        mView.onError(errorinfo);
                    }

                    @Override
                    public void onNext(RegionRespBean bean) {
                        mView.hideLoadingView();
                        if (bean.getRet_code() == 0) {
                            mView.ongetRegionSuccess(bean);
                        } else {
                            mView.ongetRegionFail(bean);
                        }
                    }
                });
    }



    /**
     * 开始云台控制
     */
    public void startPtzCmd(final int cmdID, final String cameraID, final LoginRespBean mloginRespBean) {

        mView.showLoadingView("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean issuccess=startPtz(cmdID,cameraID,mloginRespBean);
                if(issuccess){
                    sendMsgToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onPtzCmdSuccess("");
                        }
                    });

                }else {
                    sendMsgToMainThread(new Runnable() {
                        @Override
                        public void run() {

                            mView.onPtzCmdFail("");
                        }
                    });

                }
                sendMsgToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideLoadingView();
                    }
                });

            }
        }).start();


    }






    private void sendMsgToMainThread(Runnable r){
        Handler handler=new Handler(Looper.getMainLooper());

     handler.post(r);

    }


    /**
     * @param cameraID  监控点cameraid

     * @return true表示发送成功，false表示发送失败
     */


    private boolean startPtz(int cmdID,String cameraID,LoginRespBean loginRespBean) {


        ServerInfoBean serverInfoBean=loginRespBean.getServer_info();
        String loginIP = loginRespBean.getServer_info().getServer_ip(); //云台控制的服务器IP
        int loginPort = loginRespBean.getServer_info().getServer_port(); //云台控制的服务器port
        String userName=serverInfoBean.getLogin_name();//云台控制的用户名
        String password=serverInfoBean.getLogin_pass();//云台控制的密码

        mServInfo = new ServInfo();
        String  macAddress = DeviceUtil.getMacAddress(MainApplication.getInstance().getApplicationContext());
        boolean issuccess= VMSNetSDK.getInstance().login( "https://"+loginIP, userName, password, macAddress, mServInfo);

        int errorCode = VMSNetSDK.getInstance().getLastErrorCode();
        String errorDesc = VMSNetSDK.getInstance().getLastErrorDesc();




        Log.e("登陆","云台："+"登陆:"+issuccess+",errorCode:" + errorCode + ",errorDesc:" + errorDesc);

        String sessionId= mServInfo.getSessionID();

        if(issuccess){
            issuccess=VMSNetSDK.getInstance().sendStartPTZCmd(loginIP, loginPort,sessionId,cameraID, cmdID, 3,600,0+"");
        }



        Log.e("云台控制","云台："+"云台控制:"+issuccess+",errorCode:" + errorCode + ",errorDesc:" + errorDesc);


//        boolean isstopsuccess =
//                VMSNetSDK.getInstance().sendStopPTZCmd(null,
//                        0,
//                        sessionId,
//                        cameraID,"0");
//
//
//        int stoperrorCode = VMSNetSDK.getInstance().getLastErrorCode();
//        String stoperrorDesc= VMSNetSDK.getInstance().getLastErrorDesc();
//        Log.e("停止","云台："+"停止云台控制:"+isstopsuccess+",errorCode:" + stoperrorCode + ",errorDesc:" + stoperrorDesc);



        return issuccess;
    }

    public void stopPtzCmd(final String cameraID, final LoginRespBean loginRespBean){
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {

                String loginIP = loginRespBean.getServer_info().getServer_ip(); //云台控制的服务器IP
                int loginPort = loginRespBean.getServer_info().getServer_port(); //云台控制的服务器port
                String sessionId = "";
                if(mServInfo!=null){
                    sessionId = mServInfo.getSessionID();
                }

                boolean isstopsuccess =
                        VMSNetSDK.getInstance().sendStopPTZCmd(null,
                                0,
                                sessionId,
                                cameraID,"0");


                int stoperrorCode = VMSNetSDK.getInstance().getLastErrorCode();
                String stoperrorDesc= VMSNetSDK.getInstance().getLastErrorDesc();
                Log.e("停止","云台："+"停止云台控制:"+isstopsuccess+",errorCode:" + stoperrorCode + ",errorDesc:" + stoperrorDesc);



            }
        }).start();

    }
}
