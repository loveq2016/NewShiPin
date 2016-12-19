package com.xue.liang.app.v3.event;

import com.xue.liang.app.v3.bean.device.DeviceRespBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/18.
 */

public class RegionCamraEvent {
    private DeviceRespBean.ResponseBean responseBean;

    public RegionCamraEvent(DeviceRespBean.ResponseBean responseBean) {
        this.responseBean = responseBean;
    }

    public DeviceRespBean.ResponseBean getResponseBean() {
        return responseBean;
    }

    public void setResponseBean(DeviceRespBean.ResponseBean responseBean) {
        this.responseBean = responseBean;
    }
}
