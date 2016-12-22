package com.xue.liang.app.v3.activity.alarmprocess;

import com.xue.liang.app.v3.bean.alarm.AlarmDetailReqBean;
import com.xue.liang.app.v3.bean.alarm.AlarmDetailRespBean;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.httputils.retrofit2.RetrofitFactory;
import com.xue.liang.app.v3.httputils.retrofit2.service.AlarmDetailService;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jikun on 2016/12/22.
 */

public class AlredyProcessingPresenter implements AlredyProcessingContract.Presenter {


    private AlredyProcessingContract.View mView;

    public Subscription subscrip;

    public AlredyProcessingPresenter(AlredyProcessingContract.View view) {
        mView = view;
    }

    @Override
    public void getAlreadyAlarmDetail(AlarmDetailReqBean bean) {


        String GET_API_URL = UriHelper.getStartUrl();
        mView.showLoadingView("");
        Retrofit retrofit = RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        AlarmDetailService service = retrofit.create(AlarmDetailService.class);
        subscrip = service.getAlarmDetailService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AlarmDetailRespBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoadingView();
                        mView.onError(e.toString());
                    }

                    @Override
                    public void onNext(AlarmDetailRespBean bean) {
                        mView.hideLoadingView();
                        if (bean.getRet_code() == 0) {
                            mView.onSuccess(bean);
                        } else {
                            mView.onFail(bean);
                        }
                    }
                });
    }
}
