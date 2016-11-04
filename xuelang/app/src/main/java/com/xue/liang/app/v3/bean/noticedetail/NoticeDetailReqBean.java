package com.xue.liang.app.v3.bean.noticedetail;

/**
 * Created by jikun on 2016/11/4.
 */

public class NoticeDetailReqBean {


    private String guid;

    private String user_id;//不是必须

    private String termi_unique_code;//不是必须


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

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
}
