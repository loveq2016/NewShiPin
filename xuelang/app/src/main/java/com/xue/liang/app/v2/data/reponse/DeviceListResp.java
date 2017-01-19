package com.xue.liang.app.v2.data.reponse;

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


    private String user_tel;

    private String ret_string;


    private String groupname;

    private String groupid;

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    /**
     * dev_url : rtsp://192.168.1.60:556/HongTranSvr?DevId=1071a8d2-44ec-438a-acc4-5ac6466dfa2d&Session=76553962637299
     * dev_name : xx社区
     * dev_id : 1071a8d2-44ec-438a-acc4-5ac6466dfa2d
     */

    private DeviceListResponse response;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public DeviceListResponse getResponse() {
        return response;
    }

    public void setResponse(DeviceListResponse response) {
        this.response = response;
    }

    public static class DeviceListResponse {
        private String groupid;
        private String groupname;
        /**
         * dev_url : rtsp://60.255.1.179:558/HongTranSvr?DevId=f0fece8d-2d8e-4e6d-a888-229a033e3a54&Session=f0fece8d-2d8e-4e6d-a888-229a033e3a54
         * dev_name : 御景花园门口
         * dev_id : f0fece8d-2d8e-4e6d-a888-229a033e3a54
         */

        private List<DeviceItem> arList;

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public List<DeviceItem> getArList() {
            return arList;
        }

        public void setArList(List<DeviceItem> arList) {
            this.arList = arList;
        }
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


    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}
