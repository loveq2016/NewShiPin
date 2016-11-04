package com.xue.liang.app.v3.bean.notice;

/**
 * Created by jikun on 2016/11/4.
 */

public class NoticeReqBean {
    private String termi_type;

    private String user_id;

    private String termi_unique_code;

    private Integer strat_index;

    private Integer count;


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

    public Integer getStrat_index() {
        return strat_index;
    }

    public void setStrat_index(Integer strat_index) {
        this.strat_index = strat_index;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
