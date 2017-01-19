package com.xue.liang.app.v2.data.reponse;

import com.xue.liang.app.v2.data.bean.MemberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public class YidongGroupMemberResp {

    private YiDongAlarmResp yiDongAlarmResp;

    private List<MemberInfo> memberList;



    public YidongGroupMemberResp(){
        yiDongAlarmResp=new YiDongAlarmResp();
        memberList=new ArrayList<>();
    }

    public YiDongAlarmResp getYiDongAlarmResp() {
        return yiDongAlarmResp;
    }

    public void setYiDongAlarmResp(YiDongAlarmResp yiDongAlarmResp) {
        this.yiDongAlarmResp = yiDongAlarmResp;
    }

    public List<MemberInfo> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MemberInfo> memberList) {
        this.memberList = memberList;
    }


}
