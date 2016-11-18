package com.xue.liang.app.v3.bean;

/**
 * Created by jikun on 2016/11/18.
 */

public class StayPendBean {
    private String termi_type;//是	终端类型， 0: 中间件机顶盒(默认)     1: OTT机顶盒 2：手机


    private String user_id;//是	用户id


    private String termi_unique_code; //string	否	终端mac地址/卡号/唯一号

    private String ip;//否	客户端请求服务器ip

    private String  port;//否	客户端请求服务器port

    public String getTermi_type() {
        return termi_type;
    }

    public void setTermi_type(String termi_type) {
        this.termi_type = termi_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTermi_unique_code() {
        return termi_unique_code;
    }

    public void setTermi_unique_code(String termi_unique_code) {
        this.termi_unique_code = termi_unique_code;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
