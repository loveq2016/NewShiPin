package com.xue.liang.app.v3.fragment.alarmprocesse;

import com.xue.liang.app.v3.base.BasePresenter;
import com.xue.liang.app.v3.base.BaseView;
import com.xue.liang.app.v3.bean.alarm.AlarmReqBean;
import com.xue.liang.app.v3.bean.alarm.AlarmRespBean;


/**
 * Created by jikun on 2016/11/4.
 */

public class PendAlarmContract {

    interface View extends BaseView {

        void onSuccess(AlarmRespBean userInfo);
        void onFail();

        void onSuccessMore(AlarmRespBean userInfo);

        void onFailMore();




    }

    interface Presenter extends BasePresenter {
        void loadData(AlarmReqBean bean);

        void loadMoreData(AlarmReqBean bean);

    }
}
