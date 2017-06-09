package com.xue.liang.app.v2.presenter.impl;

import com.xue.liang.app.v2.base.BasePresenter;
import com.xue.liang.app.v2.entity.InfoBean;
import com.xue.liang.app.v2.http.HttpResult;
import com.xue.liang.app.v2.http.RetrofitManager;
import com.xue.liang.app.v2.presenter.IinfoList;
import com.xue.liang.app.v2.rx.ApiSubscriber;
import com.xue.liang.app.v2.rx.RxUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;

/**
 * Created by jikun on 17/5/22.
 */

public class InfoListPresenter extends BasePresenter<IinfoList.IInfoListView> implements IinfoList.IInfoListPresenter {
    public InfoListPresenter(IinfoList.IInfoListView view) {
        super(view);
    }

    @Override
    public void getInfoList(String userid, String termi_unique_code) {

        Map<String, String> map = new HashMap<>();
        map.put("termi_type", "0");
        //map.put("user_id", userid);
        map.put("termi_unique_code", termi_unique_code);
        map.put("strat_index", "0");
        map.put("count", "50");
        Subscription subscribe = RetrofitManager.getInstance().getApiService()
                .getNoticeList(map)
                .compose(RxUtils.apiSameTransformer())
                .filter(o -> mView != null)
                .subscribe(new ApiSubscriber<HttpResult<List<InfoBean>>>(mView, true) {
                    @Override
                    public void onNext(HttpResult<List<InfoBean>> result) {
                        mView.getInfoDetailSuccess(result);

                    }
                });
        addSubscription(subscribe);

    }
}
