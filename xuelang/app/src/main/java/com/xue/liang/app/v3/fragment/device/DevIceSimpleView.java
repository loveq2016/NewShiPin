package com.xue.liang.app.v3.fragment.device;

import com.xue.liang.app.v3.bean.postalarm.PostAlermResp;
import com.xue.liang.app.v3.bean.region.RegionRespBean;

/**
 * Created by jikun on 2016/12/19.
 */

public abstract class DeviceSimpleView implements DeviceContract.View {


    @Override
    public void onPostAlermSuccess(PostAlermResp postAlermResp) {

    }

    @Override
    public void onPostAlermFail(String msg) {

    }

    @Override
    public void onPtzCmdSuccess(String msg) {

    }

    @Override
    public void onPtzCmdFail(String msg) {

    }

    @Override
    public void ongetRegionSuccess(RegionRespBean bean) {

    }

    @Override
    public void ongetRegionFail(RegionRespBean bean) {

    }

    @Override
    public void showLoadingView(String msg) {

    }

    @Override
    public void hideLoadingView() {

    }

}
