package com.xue.liang.app.v3.bean.postalarm;

/**
 * Created by Administrator on 2016/11/6.
 */

public class PostAlermResp {

    private int ret_code;

    private String ret_string;

    private String alerm_id;


    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }

    public String getAlerm_id() {
        return alerm_id;
    }

    public void setAlerm_id(String alerm_id) {
        this.alerm_id = alerm_id;
    }
}
