package com.xue.liang.app.v2.presenter.impl;

import com.xue.liang.app.v2.base.BasePresenter;
import com.xue.liang.app.v2.http.HttpResult;
import com.xue.liang.app.v2.http.RetrofitManager;
import com.xue.liang.app.v2.presenter.IinfoDetail;
import com.xue.liang.app.v2.rx.ApiSubscriber;
import com.xue.liang.app.v2.rx.RxUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;

/**
 * Created by jikun on 17/5/22.
 */

public class InfoDetailPresenter extends BasePresenter<IinfoDetail.IInfoDetailView> implements IinfoDetail.IInfoDetailPresenter {
    public InfoDetailPresenter(IinfoDetail.IInfoDetailView view) {
        super(view);
    }

    @Override
    public void getInfoDetail(String guid) {

        Map<String, String> map = new HashMap<>();
        map.put("guid",guid);
        Subscription subscribe = RetrofitManager.getInstance().getApiService()
                .getNoticeDetail(map)
                .compose(RxUtils.apiSameTransformer())
                .subscribe(new ApiSubscriber<HttpResult<String>>(mView, true) {
                    @Override
                    public void onNext(HttpResult<String> result) {
                        mView.getInfoDetailSuccess(result);

                    }
                });
        addSubscription(subscribe);

    }
}
