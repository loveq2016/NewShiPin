package com.xue.liang.app.v3.fragment.device;

import com.xue.liang.app.v3.base.BasePresenter;
import com.xue.liang.app.v3.base.BaseView;
import com.xue.liang.app.v3.bean.device.DeviceReqBean;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.bean.postalarm.PostAlermReq;
import com.xue.liang.app.v3.bean.postalarm.PostAlermResp;
import com.xue.liang.app.v3.bean.region.RegionRespBean;

/**
 * Created by jikun on 2016/11/4.
 */

public class DeviceContract {

    public interface View extends BaseView {

        void onSuccess(DeviceRespBean userInfo);

        void onFail(DeviceRespBean userInfo);

        void onPostAlermSuccess(PostAlermResp postAlermResp);

        void onPostAlermFail(String msg);

        void onPtzCmdSuccess(String msg);

        void onPtzCmdFail(String msg);

        void ongetRegionSuccess(RegionRespBean bean);

        void ongetRegionFail(RegionRespBean bean);


    }

    interface Presenter extends BasePresenter {
        void loadData(DeviceReqBean bean);

        void startPtzCmd(int cmdID,String cameraID,LoginRespBean loginRespBean);

        void postalarmType(PostAlermReq bean);
    }
}
