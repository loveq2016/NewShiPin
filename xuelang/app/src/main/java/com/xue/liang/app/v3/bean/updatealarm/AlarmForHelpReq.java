package com.xue.liang.app.v3.bean.updatealarm;

import java.util.List;

/**
 * Created by Administrator on 2016/11/6.
 */

public class AlarmForHelpReq {

    private String termi_type;//是

    private String user_id;    //是

    private String termi_unique_code;//否//终端 mac 地址/卡号/唯一号

    private String alarm_text;//否   自定义报警内容，选填

    private List<String> filelist;//否 报警上传的 fileID 列表

    private double longitude; //否  精度

    private double latitude;//否  维度

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

    public String getAlarm_text() {
        return alarm_text;
    }

    public void setAlarm_text(String alarm_text) {
        this.alarm_text = alarm_text;
    }

    public List<String> getFilelist() {
        return filelist;
    }

    public void setFilelist(List<String> filelist) {
        this.filelist = filelist;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
