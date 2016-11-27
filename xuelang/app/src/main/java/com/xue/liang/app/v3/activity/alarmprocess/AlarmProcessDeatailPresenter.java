package com.xue.liang.app.v3.activity.alarmprocess;

import com.xue.liang.app.v3.activity.login.LoginContract;
import com.xue.liang.app.v3.bean.alarmhandle.AlarmHandleReqBean;
import com.xue.liang.app.v3.bean.alarmhandle.AlarmHandleRespBean;
import com.xue.liang.app.v3.bean.login.LoginReqBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.httputils.retrofit2.RetrofitFactory;
import com.xue.liang.app.v3.httputils.retrofit2.service.AlarmHandleService;
import com.xue.liang.app.v3.httputils.retrofit2.service.RegisterService;

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
}
