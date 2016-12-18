package com.xue.liang.app.v3.bean.region;

/**
 * Created by Administrator on 2016/12/17.
 */

public class RegionReqBean {
    private String user_id;//String	必须	用户id
    private String termi_unique_code;//string	否	终端mac地址/卡号/唯一号
    private String region_id;//String	否	村镇id

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTermi_unique_code() {
        return termi_unique_code;
    }

    public void setTermi_unique_code(String termi_unique_code) {
        this.termi_unique_code = termi_unique_code;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }
}
