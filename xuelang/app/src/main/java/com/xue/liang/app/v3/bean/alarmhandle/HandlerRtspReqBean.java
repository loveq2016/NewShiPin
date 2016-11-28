package com.xue.liang.app.v3.bean.alarmhandle;

/**
 * Created by jikun on 2016/11/28.
 */

public class HandlerRtspReqBean {
    public String mod;//是	ipc
    public String guid;//是	摄像头列表获取的唯一标识GUID
    public String stb_id;//否	机顶盒序列号
    public String stb_car_id;//否	机顶盒智能卡号
    public Integer stb_type;//否	机顶盒分类标识，     0: 中间件机顶盒(默认) 1: 手机     2：OTT机顶盒


    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getStb_id() {
        return stb_id;
    }

    public void setStb_id(String stb_id) {
        this.stb_id = stb_id;
    }

    public String getStb_car_id() {
        return stb_car_id;
    }

    public void setStb_car_id(String stb_car_id) {
        this.stb_car_id = stb_car_id;
    }

    public Integer getStb_type() {
        return stb_type;
    }

    public void setStb_type(Integer stb_type) {
        this.stb_type = stb_type;
    }
}
