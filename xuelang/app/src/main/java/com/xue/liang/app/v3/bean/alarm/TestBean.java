package com.xue.liang.app.v3.bean.alarm;

import java.util.List;

/**
 * Created by jikun on 2016/12/22.
 */

public class TestBean {


    /**
     * call_info : {"call_user_name":"测试村长","call_user_tel":"13000000002","call_user_detail":"测试","call_alarm_type":"危险伤害","call_alarm_time":"2016/12/22 10:07:59","call_cam_name":"郭家村村委会","call_group_name":"测试村","call_alarm_state":"完成"}
     * receiv_list : [{"receiv_name":"测试","receiv_tel":"13000000001","receiv_detail":"测试","receiv_group_name":"测试村","receiv_time":"2016/12/22 10:07:59"}]
     * alarm_text :
     * ret_code : 0
     * ret_string :
     */

    private CallInfoBean call_info;
    private String alarm_text;// string 是 备注
    private int ret_code;//int 是 响应码:0 请求成功 -1 请求失败
    private String ret_string;//string 是 错误返回信息，只在出错时返回
    private List<ReceivBean> receiv_list;

    public CallInfoBean getCall_info() {
        return call_info;
    }

    public void setCall_info(CallInfoBean call_info) {
        this.call_info = call_info;
    }

    public String getAlarm_text() {
        return alarm_text;
    }

    public void setAlarm_text(String alarm_text) {
        this.alarm_text = alarm_text;
    }

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

    public List<ReceivBean> getReceiv_list() {
        return receiv_list;
    }

    public void setReceiv_list(List<ReceivBean> receiv_list) {
        this.receiv_list = receiv_list;
    }

    public static class CallInfoBean {
        /**
         * call_user_name : 测试村长
         * call_user_tel : 13000000002
         * call_user_detail : 测试
         * call_alarm_type : 危险伤害
         * call_alarm_time : 2016/12/22 10:07:59
         * call_cam_name : 郭家村村委会
         * call_group_name : 测试村
         * call_alarm_state : 完成
         */

        private String call_user_name;//string 是 报警人姓名
        private String call_user_tel;//string 是 报警人电话
        private String call_user_detail;// string 是 报警人详细地址
        private String call_alarm_type;//string 是 报警类型
        private String call_alarm_time;// string 是 报警时间;
        private String call_cam_name;//string 是 报警摄像头名称
        private String call_group_name;//String 是 报警人所属名称
        private String call_alarm_state;//string 是 报警状态

        public String getCall_user_name() {
            return call_user_name;
        }

        public void setCall_user_name(String call_user_name) {
            this.call_user_name = call_user_name;
        }

        public String getCall_user_tel() {
            return call_user_tel;
        }

        public void setCall_user_tel(String call_user_tel) {
            this.call_user_tel = call_user_tel;
        }

        public String getCall_user_detail() {
            return call_user_detail;
        }

        public void setCall_user_detail(String call_user_detail) {
            this.call_user_detail = call_user_detail;
        }

        public String getCall_alarm_type() {
            return call_alarm_type;
        }

        public void setCall_alarm_type(String call_alarm_type) {
            this.call_alarm_type = call_alarm_type;
        }

        public String getCall_alarm_time() {
            return call_alarm_time;
        }

        public void setCall_alarm_time(String call_alarm_time) {
            this.call_alarm_time = call_alarm_time;
        }

        public String getCall_cam_name() {
            return call_cam_name;
        }

        public void setCall_cam_name(String call_cam_name) {
            this.call_cam_name = call_cam_name;
        }

        public String getCall_group_name() {
            return call_group_name;
        }

        public void setCall_group_name(String call_group_name) {
            this.call_group_name = call_group_name;
        }

        public String getCall_alarm_state() {
            return call_alarm_state;
        }

        public void setCall_alarm_state(String call_alarm_state) {
            this.call_alarm_state = call_alarm_state;
        }
    }

    public static class ReceivBean {
        /**
         * receiv_name : 测试
         * receiv_tel : 13000000001
         * receiv_detail : 测试
         * receiv_group_name : 测试村
         * receiv_time : 2016/12/22 10:07:59
         */

        private String receiv_name;// String 是 接警人姓名
        private String receiv_tel;// string 是 接警人电话
        private String receiv_detail;// string 是 接警人地址
        private String receiv_group_name;// string 是 接警人所属名称
        private String receiv_time;// string 是 接警人处理时间

        public String getReceiv_name() {
            return receiv_name;
        }

        public void setReceiv_name(String receiv_name) {
            this.receiv_name = receiv_name;
        }

        public String getReceiv_tel() {
            return receiv_tel;
        }

        public void setReceiv_tel(String receiv_tel) {
            this.receiv_tel = receiv_tel;
        }

        public String getReceiv_detail() {
            return receiv_detail;
        }

        public void setReceiv_detail(String receiv_detail) {
            this.receiv_detail = receiv_detail;
        }

        public String getReceiv_group_name() {
            return receiv_group_name;
        }

        public void setReceiv_group_name(String receiv_group_name) {
            this.receiv_group_name = receiv_group_name;
        }

        public String getReceiv_time() {
            return receiv_time;
        }

        public void setReceiv_time(String receiv_time) {
            this.receiv_time = receiv_time;
        }
    }
}
