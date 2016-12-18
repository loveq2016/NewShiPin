package com.xue.liang.app.v3.event;

import com.xue.liang.app.v3.bean.device.DeviceRespBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/18.
 */

public class RegionCamraEvent {

    List<DeviceRespBean.ResponseBean> responseBeanList;

    public List<DeviceRespBean.ResponseBean> getResponseBeanList() {
        return responseBeanList;
    }

    public void setResponseBeanList(List<DeviceRespBean.ResponseBean> responseBeanList) {
        this.responseBeanList = responseBeanList;
    }
}
