package com.xue.liang.app.v3.activity.alarmprocess;

import com.xue.liang.app.v3.bean.alarmhandle.AlarmHandleReqBean;
import com.xue.liang.app.v3.bean.alarmhandle.AlarmHandleRespBean;
import com.xue.liang.app.v3.bean.alarmhandle.HandlerRtspReqBean;
import com.xue.liang.app.v3.bean.alarmhandle.HandlerRtspRespBean;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.httputils.retrofit2.RetrofitFactory;
import com.xue.liang.app.v3.httputils.retrofit2.service.AlarmHandleService;
import com.xue.liang.app.v3.httputils.retrofit2.service.GetAlarmHandlerRtstpService;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/27.
 */

public class AlarmProcessDeatailPresenter implements AlarmProcessDeatailContract.Presenter {


    private AlarmProcessDeatailContract.View mView;

    public Subscription subscrip;

    public AlarmProcessDeatailPresenter(AlarmProcessDeatailContract.View view) {
        mView = view;
    }

    @Override
    public void loadData(AlarmHandleReqBean bean) {

        String GET_API_URL = UriHelper.getStartUrl();
        mView.showLoadingView("");
        Retrofit retrofit = RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        AlarmHandleService service = retrofit.create(AlarmHandleService.class);
        subscrip = service.getAlarmHandleService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AlarmHandleRespBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoadingView();
                        mView.onError(e.toString());
                    }

                    @Override
                    public void onNext(AlarmHandleRespBean bean) {
                        mView.hideLoadingView();
                        if (bean.getRet_code() == 0) {
                            mView.onSuccess(bean);
                        } else {
                            mView.onFail(bean);
                        }
                    }
                });


    }

    @Override
    public void getRtspUrl(HandlerRtspReqBean bean) {


        String GET_API_URL = UriHelper.getStartUrl();
        mView.showLoadingView("");
        Retrofit retrofit = RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        GetAlarmHandlerRtstpService service = retrofit.create(GetAlarmHandlerRtstpService.class);
        subscrip = service.getAlarmHandlerRtstpService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HandlerRtspRespBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoadingView();
                        mView.getUrlError(e.toString());
                    }

                    @Override
                    public void onNext(HandlerRtspRespBean bean) {
                        mView.hideLoadingView();
                        if (bean.getStatus().equals("OK")) {
                            mView.getUrlSuccess(bean);
                        } else {
                            mView.getUrlFail(bean);
                        }
                    }
                });

    }
}
