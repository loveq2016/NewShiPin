package com.xue.liang.app.data.reponse;

/**
 * Created by Administrator on 2016/8/10.
 */
public class UpdateAlarmResp {


    private int ret_code;
    private String alarm_id;
    private String ret_string;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }
}
