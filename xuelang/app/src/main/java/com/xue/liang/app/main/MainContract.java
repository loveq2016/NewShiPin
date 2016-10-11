package com.xue.liang.app.main;

import com.xue.liang.app.base.BasePresenter;
import com.xue.liang.app.base.BaseView;

/**
 * Created by Administrator on 2016/10/11.
 */
public class MainContract {

    interface View<T> extends BaseView<T> {
        void onFail(String info);

    }

    interface Presenter extends BasePresenter {

        void sendCall(String phonenum);

    }
}
