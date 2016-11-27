package com.xue.liang.app.v3.bean.alarmhandle;

/**
 * Created by Administrator on 2016/11/27.
 */

public class AlarmHandleReqBean {

    public String termi_type;  //   是  终端类型，  0: 中间件机顶盒(默认)   1: OTT机顶盒    2：手机


    public String user_id;  //是  用户id

    public String termi_unique_code; //否	终端mac地址/卡号/唯一号s

    public Integer alarm_state; //是	0 完成  1 上报

    public String alarm_id; //是	报警ID

    public String alarm_type;// 是	上报报警类型

    public String alarm_text;// 否	报警处理描述

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

    public Integer getAlarm_state() {
        return alarm_state;
    }

    public void setAlarm_state(Integer alarm_state) {
        this.alarm_state = alarm_state;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(String alarm_type) {
        this.alarm_type = alarm_type;
    }

    public String getAlarm_text() {
        return alarm_text;
    }

    public void setAlarm_text(String alarm_text) {
        this.alarm_text = alarm_text;
    }
}
