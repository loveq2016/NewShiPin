package com.xue.liang.app.v3.bean.login;

/**
 * Created by Administrator on 2016/10/26.
 */
public class LoginRespBean {

    private int ret_code;
    private String app_key;

    private String ret_string;
    private String alias_id;
    private String user_id;


    public String getAlias_id() {
        return alias_id;
    }

    public void setAlias_id(String alias_id) {
        this.alias_id = alias_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }
}
