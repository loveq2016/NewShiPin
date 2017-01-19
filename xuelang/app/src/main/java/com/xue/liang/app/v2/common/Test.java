package com.xue.liang.app.v2.common;

import java.util.List;

/**
 * Created by Administrator on 2016/8/11.
 */
public class Test {


    /**
     * terminal_type : 2
     * reg_id : 123456789
     * reg_tel : 18000000000
     * alarm_text : 测试内容
     * filelist : ["5ebb50ae-56f4-4694-8832-4d3a29265eae","5ebb50ae-56f4-4694-8832-4d3a29265eae","5ebb50ae-56f4-4694-8832-4d3a29265eae"]
     */

    private String terminal_type;
    private String reg_id;
    private String reg_tel;
    private String alarm_text;
    private List<String> filelist;

    public String getTerminal_type() {
        return terminal_type;
    }

    public void setTerminal_type(String terminal_type) {
        this.terminal_type = terminal_type;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    public String getReg_tel() {
        return reg_tel;
    }

    public void setReg_tel(String reg_tel) {
        this.reg_tel = reg_tel;
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
}
