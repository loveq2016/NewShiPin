package com.xue.liang.app.v3.fragment.alarmprocesse;

import com.xue.liang.app.v3.bean.alarm.AlarmReqBean;

import com.xue.liang.app.v3.bean.alarm.AlarmRespBean;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.httputils.retrofit2.RetrofitFactory;
import com.xue.liang.app.v3.httputils.retrofit2.service.GetAlarmListService;


import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/26.
 */
public class PendPresenter implements PendAlarmContract.Presenter {

    private PendAlarmContract.View mView;

    public Subscription subscrip;

    public PendPresenter(PendAlarmContract.View view) {
        mView = view;
    }


    @Override
    public void loadData(AlarmReqBean bean) {

        String GET_API_URL = UriHelper.getStartUrl();

        mView.showLoadingView("");
        Retrofit retrofit = RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        GetAlarmListService service = retrofit.create(GetAlarmListService.class);
        subscrip = service.getAlarmListService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AlarmRespBean>() {
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
                        mView.onFail();
                    }

                    @Override
                    public void onNext(AlarmRespBean bean) {
                        mView.hideLoadingView();
                        if (bean.getRet_code() == 0) {
                            mView.onSuccess(bean);
                        } else {
                            mView.onFail();
                        }
                    }
                });

    }

    @Override
    public void loadMoreData(AlarmReqBean bean) {

        String GET_API_URL = UriHelper.getStartUrl();

        mView.showLoadingView("");
        Retrofit retrofit = RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        GetAlarmListService service = retrofit.create(GetAlarmListService.class);
        subscrip = service.getAlarmListService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AlarmRespBean>() {
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
                        mView.onFailMore();
                    }

                    @Override
                    public void onNext(AlarmRespBean bean) {
                        mView.hideLoadingView();
                        if (bean.getRet_code() == 0) {
                            mView.onSuccessMore(bean);
                        } else {
                            mView.onFailMore();
                        }
                    }
                });

    }
}
