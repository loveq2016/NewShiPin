package com.xue.liang.app.v3.bean.alarm;

import android.os.Parcel;
import android.os.Parcelable;

import com.temobi.cache.memory.MD5;
import com.xue.liang.app.v3.utils.MD5Util;

import java.util.List;

/**
 * Created by Administrator on 2016/11/20.
 */

public class AlarmRespBean implements Parcelable {

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

    public static class ResponseBean implements Parcelable {
        /**
         * camera_id :
         * dev_platform_type : 10
         */

        private CameraInfoBean camera_info;
        private String alarm_id;
        private int alarm_state;
        private String alarm_state_name;
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
        double user_longitude;
        double user_latitude;
        int    map_scale;
        private List<String> file_list;

        public String generateDistinguishId() {
            String md5 = MD5Util.string2MD5(toString());
            return md5;
        }

        @Override
        public String toString() {
            return "ResponseBean{" +
                    "alarm_id='" + alarm_id + '\'' +
                    ", alarm_type=" + alarm_type +
                    ", alarm_type_name='" + alarm_type_name + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", user_address='" + user_address + '\'' +
                    ", user_tel='" + user_tel + '\'' +
                    ", alarm_text='" + alarm_text + '\'' +
                    ", alarm_time='" + alarm_time + '\'' +
                    ", dev_name='" + dev_name + '\'' +
                    ", rtsp_id='" + rtsp_id + '\'' +
                    ", map_url='" + map_url + '\'' +
                    '}';
        }

        public double getUser_longitude() {
            return user_longitude;
        }

        public void setUser_longitude(double user_longitude) {
            this.user_longitude = user_longitude;
        }

        public double getUser_latitude() {
            return user_latitude;
        }

        public void setUser_latitude(double user_latitude) {
            this.user_latitude = user_latitude;
        }

        public int getMap_scale() {
            return map_scale;
        }

        public void setMap_scale(int map_scale) {
            this.map_scale = map_scale;
        }

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

        public int getAlarm_state() {
            return alarm_state;
        }

        public void setAlarm_state(int alarm_state) {
            this.alarm_state = alarm_state;
        }

        public String getAlarm_state_name() {
            return alarm_state_name;
        }

        public void setAlarm_state_name(String alarm_state_name) {
            this.alarm_state_name = alarm_state_name;
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

        public List<String> getFile_list() {
            return file_list;
        }

        public void setFile_list(List<String> file_list) {
            this.file_list = file_list;
        }

        public static class CameraInfoBean implements Parcelable {

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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.camera_id);
                dest.writeInt(this.dev_platform_type);
            }

            public CameraInfoBean() {
            }

            protected CameraInfoBean(Parcel in) {
                this.camera_id = in.readString();
                this.dev_platform_type = in.readInt();
            }

            public static final Creator<CameraInfoBean> CREATOR = new Creator<CameraInfoBean>() {
                @Override
                public CameraInfoBean createFromParcel(Parcel source) {
                    return new CameraInfoBean(source);
                }

                @Override
                public CameraInfoBean[] newArray(int size) {
                    return new CameraInfoBean[size];
                }
            };
        }

        public ResponseBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.camera_info, flags);
            dest.writeString(this.alarm_id);
            dest.writeInt(this.alarm_state);
            dest.writeString(this.alarm_state_name);
            dest.writeInt(this.alarm_type);
            dest.writeString(this.alarm_type_name);
            dest.writeString(this.user_name);
            dest.writeString(this.user_address);
            dest.writeString(this.user_tel);
            dest.writeString(this.alarm_text);
            dest.writeString(this.alarm_time);
            dest.writeString(this.dev_name);
            dest.writeString(this.rtsp_id);
            dest.writeString(this.map_url);
            dest.writeDouble(this.user_longitude);
            dest.writeDouble(this.user_latitude);
            dest.writeInt(this.map_scale);
            dest.writeStringList(this.file_list);
        }

        protected ResponseBean(Parcel in) {
            this.camera_info = in.readParcelable(CameraInfoBean.class.getClassLoader());
            this.alarm_id = in.readString();
            this.alarm_state = in.readInt();
            this.alarm_state_name = in.readString();
            this.alarm_type = in.readInt();
            this.alarm_type_name = in.readString();
            this.user_name = in.readString();
            this.user_address = in.readString();
            this.user_tel = in.readString();
            this.alarm_text = in.readString();
            this.alarm_time = in.readString();
            this.dev_name = in.readString();
            this.rtsp_id = in.readString();
            this.map_url = in.readString();
            this.user_longitude = in.readDouble();
            this.user_latitude = in.readDouble();
            this.map_scale = in.readInt();
            this.file_list = in.createStringArrayList();
        }

        public static final Creator<ResponseBean> CREATOR = new Creator<ResponseBean>() {
            @Override
            public ResponseBean createFromParcel(Parcel source) {
                return new ResponseBean(source);
            }

            @Override
            public ResponseBean[] newArray(int size) {
                return new ResponseBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ret_code);
        dest.writeString(this.ret_string);
        dest.writeInt(this.alarm_count);
        dest.writeTypedList(this.response);
    }

    public AlarmRespBean() {
    }

    protected AlarmRespBean(Parcel in) {
        this.ret_code = in.readInt();
        this.ret_string = in.readString();
        this.alarm_count = in.readInt();
        this.response = in.createTypedArrayList(ResponseBean.CREATOR);
    }

    public static final Parcelable.Creator<AlarmRespBean> CREATOR = new Parcelable.Creator<AlarmRespBean>() {
        @Override
        public AlarmRespBean createFromParcel(Parcel source) {
            return new AlarmRespBean(source);
        }

        @Override
        public AlarmRespBean[] newArray(int size) {
            return new AlarmRespBean[size];
        }
    };
}
