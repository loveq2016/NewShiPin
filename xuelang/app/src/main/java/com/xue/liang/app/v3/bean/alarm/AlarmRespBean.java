package com.xue.liang.app.v3.bean.alarm;

import java.util.List;

/**
 * Created by Administrator on 2016/11/20.
 */

public class AlarmRespBean {

    /**
     * response : [{"camera_info":{"camera_id":"","dev_platform_type":10},"file_list":[],"alarm_id":"0317031E-B114BAD-A547-45B95F475458","alarm_type":1,"alarm_type_name":"火灾","user_name":"xxxx","user_address":"xx村","user_tel":"13508112908","alarm_text":"","alarm_time":"2016/11/11 17:29:45","dev_name":"","rtsp_id":"","map_url":"/alarmpage.aspx?data=031114BAD-A547-45B95F475458&ip=192.168.1.11:9003&visit=0"},{"camera_info":{"camera_id":"","dev_platform_type":10},"file_list":[],"alarm_id":"06ACFBFA-329D-111195783","alarm_type":1,"alarm_type_name":"火灾","user_name":"张三","user_address":"x村五组","user_tel":"135135623383","alarm_text":"","alarm_time":"2016/3/18 13:36:58","dev_name":"","rtsp_id":"","map_url":"/alarmpage.aspx?data=06ACFBFA-311111154C79EA95783&ip=192.168.1.11:9003&visit=0"}]
     * ret_code : 0
     * ret_string :
     * alarm_count : 141
     */

    private int ret_code;
    private String ret_string;
    private int alarm_count;
    /**
     * camera_info : {"camera_id":"","dev_platform_type":10}
     * file_list : []
     * alarm_id : 0317031E-B114BAD-A547-45B95F475458
     * alarm_type : 1
     * alarm_type_name : 火灾
     * user_name : xxxx
     * user_address : xx村
     * user_tel : 13508112908
     * alarm_text :
     * alarm_time : 2016/11/11 17:29:45
     * dev_name :
     * rtsp_id :
     * map_url : /alarmpage.aspx?data=031114BAD-A547-45B95F475458&ip=192.168.1.11:9003&visit=0
     */

    private List<ResponseBean> response;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }

    public int getAlarm_count() {
        return alarm_count;
    }

    public void setAlarm_count(int alarm_count) {
        this.alarm_count = alarm_count;
    }

    public List<ResponseBean> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseBean> response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * camera_id :
         * dev_platform_type : 10
         */

        private CameraInfoBean camera_info;
        private String alarm_id;
        private int alarm_type;
        private String alarm_type_name;
        private String user_name;
        private String user_address;
        private String user_tel;
        private String alarm_text;
        private String alarm_time;
        private String dev_name;
        private String rtsp_id;
        private String map_url;
        private List<?> file_list;

        public CameraInfoBean getCamera_info() {
            return camera_info;
        }

        public void setCamera_info(CameraInfoBean camera_info) {
            this.camera_info = camera_info;
        }

        public String getAlarm_id() {
            return alarm_id;
        }

        public void setAlarm_id(String alarm_id) {
            this.alarm_id = alarm_id;
        }

        public int getAlarm_type() {
            return alarm_type;
        }

        public void setAlarm_type(int alarm_type) {
            this.alarm_type = alarm_type;
        }

        public String getAlarm_type_name() {
            return alarm_type_name;
        }

        public void setAlarm_type_name(String alarm_type_name) {
            this.alarm_type_name = alarm_type_name;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_address() {
            return user_address;
        }

        public void setUser_address(String user_address) {
            this.user_address = user_address;
        }

        public String getUser_tel() {
            return user_tel;
        }

        public void setUser_tel(String user_tel) {
            this.user_tel = user_tel;
        }

        public String getAlarm_text() {
            return alarm_text;
        }

        public void setAlarm_text(String alarm_text) {
            this.alarm_text = alarm_text;
        }

        public String getAlarm_time() {
            return alarm_time;
        }

        public void setAlarm_time(String alarm_time) {
            this.alarm_time = alarm_time;
        }

        public String getDev_name() {
            return dev_name;
        }

        public void setDev_name(String dev_name) {
            this.dev_name = dev_name;
        }

        public String getRtsp_id() {
            return rtsp_id;
        }

        public void setRtsp_id(String rtsp_id) {
            this.rtsp_id = rtsp_id;
        }

        public String getMap_url() {
            return map_url;
        }

        public void setMap_url(String map_url) {
            this.map_url = map_url;
        }

        public List<?> getFile_list() {
            return file_list;
        }

        public void setFile_list(List<?> file_list) {
            this.file_list = file_list;
        }

        public static class CameraInfoBean {
            private String camera_id;
            private int dev_platform_type;

            public String getCamera_id() {
                return camera_id;
            }

            public void setCamera_id(String camera_id) {
                this.camera_id = camera_id;
            }

            public int getDev_platform_type() {
                return dev_platform_type;
            }

            public void setDev_platform_type(int dev_platform_type) {
                this.dev_platform_type = dev_platform_type;
            }
        }
    }
}
