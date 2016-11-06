package com.xue.liang.app.v3.bean.update;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class UpdateFileResp {


    private int ret_code;


    private String ret_string;


    private List<UpdateFile> response;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public List<UpdateFile> getResponse() {
        return response;
    }

    public void setResponse(List<UpdateFile> response) {
        this.response = response;
    }


    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }

    public static class UpdateFile {
        private int nState;
        private String file_name;
        private String file_id;

        public int getNState() {
            return nState;
        }

        public void setNState(int nState) {
            this.nState = nState;
        }

        public String getFile_id() {
            return file_id;
        }

        public void setFile_id(String file_id) {
            this.file_id = file_id;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public int getnState() {
            return nState;
        }

        public void setnState(int nState) {
            this.nState = nState;
        }
    }


}
