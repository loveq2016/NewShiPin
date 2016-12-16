package com.xue.liang.app.v3.constant;

import com.xue.liang.app.v3.bean.login.LoginRespBean;

/**
 * Created by Administrator on 2016/11/27.
 */

public class LoginInfoUtils {

    private LoginRespBean loginRespBean;


    private String phoneNum;

    private String macAdrress;

    public static LoginInfoUtils instance;

    private LoginInfoUtils() {

    }

    public static LoginInfoUtils getInstance() {
        if (instance == null) {
            instance = new LoginInfoUtils();
        }
        return instance;

    }

    public LoginRespBean getLoginRespBean() {
        return loginRespBean;
    }

    public void setLoginRespBean(LoginRespBean loginRespBean) {
        this.loginRespBean = loginRespBean;
    }

    public String getMacAdrress() {
        return macAdrress;
    }

    public void setMacAdrress(String macAdrress) {
        this.macAdrress = macAdrress;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
