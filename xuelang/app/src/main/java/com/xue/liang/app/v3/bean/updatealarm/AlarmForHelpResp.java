package com.xue.liang.app.v3.bean.updatealarm;

/**
 * Created by Administrator on 2016/11/6.
 */

public class AlarmForHelpResp {

    private int ret_code;
    private String ret_string;
    private String alarm_id;


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

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }
}
