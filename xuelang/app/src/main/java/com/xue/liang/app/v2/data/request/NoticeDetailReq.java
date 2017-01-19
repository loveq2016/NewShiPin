package com.xue.liang.app.v2.data.request;

/**
 * Created by jk on 2016/8/2.
 */
public class NoticeDetailReq extends BaseRequest {
    private String guid;

    public NoticeDetailReq(String terminal_type, String reg_tel, String reg_id, String guid) {
        super(terminal_type, reg_tel, reg_id);
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
