package com.xue.liang.app.v2.data.request;

/**
 * Created by Administrator on 2016/7/31.
 */
public class RegisterReq extends  BaseRequest {

    public RegisterReq(String terminal_type, String reg_tel, String reg_id) {
        super(terminal_type, reg_tel, reg_id);
    }
}
