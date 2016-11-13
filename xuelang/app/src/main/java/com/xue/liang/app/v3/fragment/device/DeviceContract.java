package com.xue.liang.app.v3.fragment.device;

import com.xue.liang.app.v3.base.BasePresenter;
import com.xue.liang.app.v3.base.BaseView;
import com.xue.liang.app.v3.bean.device.DeviceReqBean;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;
import com.xue.liang.app.v3.bean.postalarm.PostAlermReq;
import com.xue.liang.app.v3.bean.postalarm.PostAlermResp;

/**
 * Created by jikun on 2016/11/4.
 */

public class DeviceContract {

    interface View extends BaseView {

        void onSuccess(DeviceRespBean userInfo);

        void onFail(DeviceRespBean userInfo);

        void onPostAlermSuccess(PostAlermResp postAlermResp);

        void onPostAlermFail(String msg);

        void onPtzCmdSuccess(String msg);

        void onPtzCmdFail(String msg);


    }

    interface Presenter extends BasePresenter {
        void loadData(DeviceReqBean bean);

        void startPtzCmd(String sessionID, String cameraID, int cmdID, int param1, int param2);

        void postalarmType(PostAlermReq bean);
    }
}
