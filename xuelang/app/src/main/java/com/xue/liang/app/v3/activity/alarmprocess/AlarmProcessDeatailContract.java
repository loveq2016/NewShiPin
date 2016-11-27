package com.xue.liang.app.v3.activity.alarmprocess;

import com.xue.liang.app.v3.base.BasePresenter;
import com.xue.liang.app.v3.base.BaseView;
import com.xue.liang.app.v3.bean.alarmhandle.AlarmHandleReqBean;
import com.xue.liang.app.v3.bean.alarmhandle.AlarmHandleRespBean;

/**
 * Created by Administrator on 2016/11/27.
 */

public class AlarmProcessDeatailContract {


    interface View extends BaseView {

        void onSuccess(AlarmHandleRespBean alarmHandleRespBean);

        void onFail(AlarmHandleRespBean alarmHandleRespBean);


    }

    interface Presenter extends BasePresenter {
        void loadData(AlarmHandleReqBean bean);
    }
}
