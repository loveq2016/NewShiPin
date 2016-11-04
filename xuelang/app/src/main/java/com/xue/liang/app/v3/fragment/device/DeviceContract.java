package com.xue.liang.app.v3.fragment.device;

import com.xue.liang.app.v3.base.BasePresenter;
import com.xue.liang.app.v3.base.BaseView;
import com.xue.liang.app.v3.bean.device.DeviceReqBean;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;

/**
 * Created by jikun on 2016/11/4.
 */

public class DeviceContract {

    interface View extends BaseView {

        void onSuccess(DeviceRespBean userInfo);

        void onFail(DeviceRespBean userInfo);


    }

    interface Presenter extends BasePresenter {
        void loadData(DeviceReqBean bean);
    }
}
