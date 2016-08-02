package com.xue.liang.app.data.reponse;

import java.util.List;

/**
 * Created by jk on 2016/8/2.
 */
public class DeviceListResp {

    /**
     * ret_code : 0
     * response : [{"dev_url":"rtsp://192.168.1.60:556/HongTranSvr?DevId=1071a8d2-44ec-438a-acc4-5ac6466dfa2d&Session=76553962637299","dev_name":"xx社区","dev_id":"1071a8d2-44ec-438a-acc4-5ac6466dfa2d"},{"dev_url":"rtsp://92.168.1.60:556/HongTranSvr?DevId=7c25c778-2891-4b5b-82a0-e80f6bd6f317&Session=76557655629900","dev_name":"xx路口","dev_id":"7c25c778-2891-4b5b-82a0-e80f6bd6f317"}]
     */

    private int ret_code;

    private String ret_string;

    /**
     * dev_url : rtsp://192.168.1.60:556/HongTranSvr?DevId=1071a8d2-44ec-438a-acc4-5ac6466dfa2d&Session=76553962637299
     * dev_name : xx社区
     * dev_id : 1071a8d2-44ec-438a-acc4-5ac6466dfa2d
     */

    private List<DeviceItem> response;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public List<DeviceItem> getResponse() {
        return response;
    }

    public void setResponse(List<DeviceItem> response) {
        this.response = response;
    }

    public static class DeviceItem {
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


    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }
}
