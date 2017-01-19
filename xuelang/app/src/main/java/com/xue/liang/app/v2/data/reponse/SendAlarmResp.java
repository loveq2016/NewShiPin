package com.xue.liang.app.v2.data.reponse;

/**
 * Created by jk on 2016/8/2.
 */
public class SendAlarmResp {
    private Integer ret_code;
    private String ret_string;
    private String alarm_id;

    public Integer getRet_code() {
        return ret_code;
    }

    public void setRet_code(Integer ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }
}
