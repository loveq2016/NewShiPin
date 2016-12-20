package com.xue.liang.app.v3.fragment.device;

import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.xue.liang.app.v3.bean.device.DeviceReqBean;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;
import com.xue.liang.app.v3.bean.postalarm.PostAlermReq;
import com.xue.liang.app.v3.bean.postalarm.PostAlermResp;
import com.xue.liang.app.v3.bean.region.RegionReqBean;
import com.xue.liang.app.v3.bean.region.RegionRespBean;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.httputils.retrofit2.RetrofitFactory;
import com.xue.liang.app.v3.httputils.retrofit2.service.GetDeviceListService;
import com.xue.liang.app.v3.httputils.retrofit2.service.GetRegionListService;
import com.xue.liang.app.v3.httputils.retrofit2.service.PostAlermService;

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
//                        try {
//                            // Your onError handling code
//                            RetrofitError ex = (RetrofitError) e;
//                            Response res = ex.getResponse();
//
//                        } catch (Exception ex) {
//                            // Catch the culprit who's causing this whole problem
//                        }
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
//                        try {
//                            // Your onError handling code
//                            RetrofitError ex = (RetrofitError) e;
//                            Response res = ex.getResponse();
//
//                        } catch (Exception ex) {
//                            // Catch the culprit who's causing this whole problem
//                        }
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
     * 开始云台控制
     */
    public void startPtzCmd(String sessionID, String cameraID, int cmdID, int param1, int param2) {

        mView.showLoadingView("");

        Observable.just(startPtz(sessionID, cameraID, cmdID, param1, param2)).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoadingView();

            }

            @Override
            public void onNext(Boolean issuccess) {
                mView.hideLoadingView();
                if (issuccess) {
                    mView.onPtzCmdFail("");

                } else {
                    mView.onPtzCmdSuccess("");
                }


            }
        });


    }

    /**
     * @param sessionID 登录返回的sessionID
     * @param cameraID  监控点cameraid
     * @param cmdID     cmdID  云台控制命令操作码
     * @param param1    param1  云台控制速度（取值范围为1-10，1代表最慢，10代表最快）或 预置点操作时预置点编号
     * @param param2    param2  取值0
     * @return true表示发送成功，false表示发送失败
     */
    private boolean startPtz(String sessionID, String cameraID, int cmdID, int param1, int param2) {

        String servAddr = UriHelper.IP;  //camerainfo中的acsIP
        int port = UriHelper.PORT;       //camerainfo中的acsPort

        String testservAddr ="182.150.56.73";  //camerainfo中的acsIP
        int testport = 81;       //camerainfo中的acsPort
        String testsessionID="";
        String testcameraID="12732084001310000050";
        boolean issuccess = VMSNetSDK.getInstance().sendStartPTZCmd(testservAddr, testport, testsessionID, testcameraID, cmdID, param1, param2);
        return issuccess;
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
}
