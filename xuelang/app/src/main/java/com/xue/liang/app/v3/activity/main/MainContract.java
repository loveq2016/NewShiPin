package com.xue.liang.app.v3.activity.main;

import com.xue.liang.app.v3.base.BasePresenter;
import com.xue.liang.app.v3.base.BaseView;

/**
 * Created by Administrator on 2016/11/1.
 */
public class MainContract {

    interface View extends BaseView {

        void onSuccess();

        void onFail( );


    }

    interface Presenter extends BasePresenter {
        void loadData( );
    }
}
