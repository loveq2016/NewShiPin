package com.xue.liang.app.v2.presenter;

import com.xue.liang.app.v2.base.BaseView;
import com.xue.liang.app.v2.http.HttpResult;

/**
 * Created by jikun on 17/5/22.
 */

public interface IinfoDetail {

    interface IInfoDetailView extends BaseView {


        void getInfoDetailSuccess(HttpResult<String> result);


    }

    interface IInfoDetailPresenter {

        void getInfoDetail(String guid);

    }
}
