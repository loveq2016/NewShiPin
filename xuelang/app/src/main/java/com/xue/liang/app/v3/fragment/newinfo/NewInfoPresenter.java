package com.xue.liang.app.v3.fragment.newinfo;

import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.bean.notice.NoticeReqBean;
import com.xue.liang.app.v3.bean.notice.NoticeRespBean;
import com.xue.liang.app.v3.httputils.retrofit2.RetrofitFactory;
import com.xue.liang.app.v3.httputils.retrofit2.service.NoticeService;
import com.xue.liang.app.v3.httputils.retrofit2.service.RegisterService;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jikun on 2016/11/5.
 */

public class NewInfoPresenter implements NewInfoContract.Presenter {

    private NewInfoContract.View mView;

    public Subscription subscrip;

    public NewInfoPresenter(NewInfoContract.View view) {
        mView = view;
    }

    @Override
    public void loadData(NoticeReqBean bean) {


        String GET_API_URL = "http://182.150.56.73:9003/";
        mView.showLoadingView("");
        Retrofit retrofit = RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        NoticeService service = retrofit.create(NoticeService.class);
        subscrip = service.getNoticeService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoticeRespBean>() {
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
                    public void onNext(NoticeRespBean bean) {
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
