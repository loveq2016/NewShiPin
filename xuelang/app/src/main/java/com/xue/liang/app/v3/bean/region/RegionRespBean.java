package com.xue.liang.app.v3.bean.region;

import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */

public class RegionRespBean {


    private int ret_code;


    private String ret_string;


    private List<RegionAreas> childList;

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


    public List<RegionAreas> getChildList() {
        return childList;
    }

    public void setChildList(List<RegionAreas> childList) {
        this.childList = childList;
    }

    public static class RegionAreas {
        private String region_id;
        private String region_name;

        private String group_id;


        private List<RegionAreas> childList;

        public String getRegion_id() {
            return region_id;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }

        public List<RegionAreas> getChildList() {
            return childList;
        }

        public void setChildList(List<RegionAreas> childList) {
            this.childList = childList;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }
    }
}
