package com.xue.liang.app.v3.bean.device;

/**
 * Created by jikun on 17/5/5.
 */

public class GirdInfoBean {

    private String title;
    private String iv_header_url;
    private String user_name;
    private String user_phonenum;

    public GirdInfoBean(String title,String iv_header_url, String user_name, String user_phonenum) {
        this.title = title;
        this.iv_header_url = iv_header_url;
        this.user_name = user_name;
        this.user_phonenum = user_phonenum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIv_header_url() {
        return iv_header_url;
    }

    public void setIv_header_url(String iv_header_url) {
        this.iv_header_url = iv_header_url;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phonenum() {
        return user_phonenum;
    }

    public void setUser_phonenum(String user_phonenum) {
        this.user_phonenum = user_phonenum;
    }
}
