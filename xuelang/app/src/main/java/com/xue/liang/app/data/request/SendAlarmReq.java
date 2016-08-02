package com.xue.liang.app.data.request;

/**
 * Created by jk on 2016/8/2.
 */
public class SendAlarmReq extends BaseRequest {
    private String alarm_type;

    private String dev_id;

    public SendAlarmReq(String terminal_type, String reg_tel, String reg_id, String alarm_type, String dev_id) {
        super(terminal_type, reg_tel, reg_id);
        this.alarm_type = alarm_type;
        this.dev_id = dev_id;
    }

    public String getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(String alarm_type) {
        this.alarm_type = alarm_type;
    }

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }
}
