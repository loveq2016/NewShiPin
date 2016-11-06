package com.xue.liang.app.v3.activity.newinfo;

import com.xue.liang.app.v3.activity.login.LoginContract;
import com.xue.liang.app.v3.bean.login.LoginReqBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.bean.notice.NoticeRespBean;
import com.xue.liang.app.v3.bean.noticedetail.NoticeDetailReqBean;
import com.xue.liang.app.v3.bean.noticedetail.NoticeDetailRespBean;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.httputils.retrofit2.RetrofitFactory;
import com.xue.liang.app.v3.httputils.retrofit2.service.NoticeDetailService;
import com.xue.liang.app.v3.httputils.retrofit2.service.NoticeService;
import com.xue.liang.app.v3.httputils.retrofit2.service.RegisterService;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/26.
 */
public class NewInfoDetailPresenter implements NewInfoDetailContract.Presenter {

    private NewInfoDetailContract.View mView;

    public Subscription subscrip;

    public NewInfoDetailPresenter(NewInfoDetailContract.View view) {
        mView = view;
    }

    @Override
    public void loadData(NoticeDetailReqBean bean) {


        String GET_API_URL = UriHelper.getStartUrl();
        mView.showLoadingView("");
        Retrofit retrofit = RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        NoticeDetailService service = retrofit.create(NoticeDetailService.class);
        subscrip = service.getNoticeDetailService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoticeDetailRespBean>() {
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
                    public void onNext(NoticeDetailRespBean bean) {
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
