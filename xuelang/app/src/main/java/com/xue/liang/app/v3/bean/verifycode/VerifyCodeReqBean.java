package com.xue.liang.app.v3.bean.verifycode;

/**
 * Created by jikun on 17/4/25.
 */

public class VerifyCodeReqBean {
    private String verify_tel;
    private String verify_mac;

    public String getVerify_tel() {
        return verify_tel;
    }

    public void setVerify_tel(String verify_tel) {
        this.verify_tel = verify_tel;
    }

    public String getVerify_mac() {
        return verify_mac;
    }

    public void setVerify_mac(String verify_mac) {
        this.verify_mac = verify_mac;
    }
}
