package com.xue.liang.app.v3.activity.login;

import com.xue.liang.app.v3.bean.login.LoginReqBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.httputils.retrofit2.RetrofitFactory;
import com.xue.liang.app.v3.httputils.retrofit2.service.RegisterService;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/26.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;

    public Subscription subscrip;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
    }

    @Override
    public void loadData(LoginReqBean bean) {

        String GET_API_URL =  UriHelper.getStartUrl();
        mView.showLoadingView("");
        Retrofit retrofit= RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        RegisterService service=    retrofit.create(RegisterService.class);
        subscrip=   service.getRegisterService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginRespBean>() {
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
                        mView.onError(e.toString());
                    }

                    @Override
                    public void onNext(LoginRespBean bean) {
                        mView.hideLoadingView();
                        if(bean.getRet_code()==0){
                            mView.onSuccess(bean);
                        }else{
                            mView.onFail(bean);
                        }
                    }
                });


    }
}
