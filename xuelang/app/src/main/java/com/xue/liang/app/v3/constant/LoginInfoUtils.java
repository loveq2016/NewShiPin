package com.xue.liang.app.v3.constant;

import com.xue.liang.app.v3.bean.login.LoginRespBean;

/**
 * Created by Administrator on 2016/11/27.
 */

public class LoginInfoUtils {

    private LoginRespBean loginRespBean;

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
}
