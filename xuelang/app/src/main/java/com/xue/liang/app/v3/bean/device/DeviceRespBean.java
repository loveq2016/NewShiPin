package com.xue.liang.app.v3.bean.device;

import java.util.List;

/**
 * Created by jikun on 2016/11/4.
 */

public class DeviceRespBean {


    private int ret_code;
    private String user_tel;


    private List<ResponseBean> response;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public List<ResponseBean> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseBean> response) {
        this.response = response;
    }

    public static class ResponseBean {
        private String dev_url;
        private String dev_name;
        private String dev_id;

        public String getDev_url() {
            return dev_url;
        }

        public void setDev_url(String dev_url) {
            this.dev_url = dev_url;
        }

        public String getDev_name() {
            return dev_name;
        }

        public void setDev_name(String dev_name) {
            this.dev_name = dev_name;
        }

        public String getDev_id() {
            return dev_id;
        }

        public void setDev_id(String dev_id) {
            this.dev_id = dev_id;
        }
    }
}
