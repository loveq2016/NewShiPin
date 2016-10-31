package com.xue.liang.app.v3.bean.login;

/**
 * Created by Administrator on 2016/10/26.
 */
public class LoginReqBean {
    private String termi_type;

    private String reg_tel;

    private String termi_unique_code;


    public String getTermi_type() {
        return termi_type;
    }

    public void setTermi_type(String termi_type) {
        this.termi_type = termi_type;
    }

    public String getReg_tel() {
        return reg_tel;
    }

    public void setReg_tel(String reg_tel) {
        this.reg_tel = reg_tel;
    }

    public String getTermi_unique_code() {
        return termi_unique_code;
    }

    public void setTermi_unique_code(String termi_unique_code) {
        this.termi_unique_code = termi_unique_code;
    }
}
