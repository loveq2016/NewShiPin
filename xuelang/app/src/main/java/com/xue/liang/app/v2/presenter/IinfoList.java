package com.xue.liang.app.v2.presenter;

import com.xue.liang.app.v2.base.BaseView;
import com.xue.liang.app.v2.entity.InfoBean;
import com.xue.liang.app.v2.http.HttpResult;

import java.util.List;


/**
 * Created by jikun on 17/5/22.
 */

public interface IinfoList {
    interface IInfoListView extends BaseView {


        void getInfoDetailSuccess(HttpResult<List<InfoBean>> result);


    }

    interface IInfoListPresenter {

        void getInfoList(String userid, String termi_unique_code);

    }
}
