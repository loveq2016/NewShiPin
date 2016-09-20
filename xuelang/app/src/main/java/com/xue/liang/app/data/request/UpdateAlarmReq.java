package com.xue.liang.app.data.request;

import java.util.List;

/**
 * Created by Administrator on 2016/8/10.
 */
public class UpdateAlarmReq extends BaseRequest {

    private String alarm_text;

    private String dev_id;

    private List<String> filelist;


    public UpdateAlarmReq(String terminal_type, String reg_tel, String reg_id, String alarm_text, List<String> filelist, String dev_id) {
        super(terminal_type, reg_tel, reg_id);
        this.alarm_text = alarm_text;
        this.filelist = filelist;
        this.dev_id = dev_id;
    }

    public UpdateAlarmReq(String terminal_type, String reg_tel, String reg_id, String alarm_text, List<String> filelist) {
        super(terminal_type, reg_tel, reg_id);
        this.alarm_text = alarm_text;
        this.filelist = filelist;

    }

}
