package com.xue.liang.app.v2.data.request;

/**
 * Created by Administrator on 2016/8/1.
 */
public class BaseRequest {
    public String terminal_type;
    public String reg_tel;
    public String reg_id;

    public String getTerminal_type() {
        return terminal_type;
    }

    public void setTerminal_type(String terminal_type) {
        this.terminal_type = terminal_type;
    }

    public String getReg_tel() {
        return reg_tel;
    }

    public void setReg_tel(String reg_tel) {
        this.reg_tel = reg_tel;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    public BaseRequest(String terminal_type, String reg_tel, String reg_id) {
        this.terminal_type = terminal_type;
        this.reg_tel = reg_tel;
        this.reg_id = reg_id;
    }
}
