package com.xue.liang.app.v2.data.reponse;

/**
 * Created by jk on 2016/8/2.
 */
public class NoticeDetailResp {

    /**
     * ret_code : 0
     * content :
     */
    private String ret_string;
    private int ret_code;
    private String content;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }
}
