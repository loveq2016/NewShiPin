package com.xue.liang.app.data.reponse;

import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class NoticeResp {

    /**
     * ret_code : 0
     * response : [{"guid":"18459b2e-2acd-4ecf-b70c-8483d25122c6","title":"通知","time":"2016-07-12"},{"guid":"9e3a7062-046b-4f13-bd50-8db13c66cf2a","title":"公告","time":"2016-07-13"},{"guid":"a99e90f3-0224-4cd2-8544-7d6b1999d60a","title":"公告","time":"2016-07-13"}]
     */

    private int ret_code;

    private String ret_string;
    /**
     * guid : 18459b2e-2acd-4ecf-b70c-8483d25122c6
     * title : 通知
     * time : 2016-07-12
     */

    private List<NoticeItem> response;

    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public List<NoticeItem> getResponse() {
        return response;
    }

    public void setResponse(List<NoticeItem> response) {
        this.response = response;
    }

    public static class NoticeItem {
        private String guid;
        private String title;
        private String time;

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "NoticeItem{" +
                    "guid='" + guid + '\'' +
                    ", title='" + title + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NoticeResp{" +
                "ret_code=" + ret_code +
                ", ret_string='" + ret_string + '\'' +
                ", response=" + response +
                '}';
    }
}
