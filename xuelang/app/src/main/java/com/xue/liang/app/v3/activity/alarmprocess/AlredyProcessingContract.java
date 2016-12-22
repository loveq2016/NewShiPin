package com.xue.liang.app.v3.activity.alarmprocess;

import com.xue.liang.app.v3.base.BasePresenter;
import com.xue.liang.app.v3.base.BaseView;
import com.xue.liang.app.v3.bean.alarm.AlarmDetailReqBean;
import com.xue.liang.app.v3.bean.alarm.AlarmDetailRespBean;

/**
 * Created by jikun on 2016/12/22.
 */

public class AlredyProcessingContract {

    interface View extends BaseView {
        void onSuccess(AlarmDetailRespBean bean);

        void onFail(AlarmDetailRespBean bean);

    }

    interface Presenter extends BasePresenter {

        void getAlreadyAlarmDetail(AlarmDetailReqBean bean);

    }
}
