package com.xue.liang.app.v3.bean.verifycode;

/**
 * Created by jikun on 17/4/25.
 */

public class VerifyCodeRespBean {
    private Integer ret_code;

    private String ret_string;

    private int valid_time;

    private String valid_guid;

    public Integer getRet_code() {
        return ret_code;
    }

    public void setRet_code(Integer ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }

    public int getValid_time() {
        return valid_time;
    }

    public void setValid_time(int valid_time) {
        this.valid_time = valid_time;
    }

    public String getValid_guid() {
        return valid_guid;
    }

    public void setValid_guid(String valid_guid) {
        this.valid_guid = valid_guid;
    }
}
