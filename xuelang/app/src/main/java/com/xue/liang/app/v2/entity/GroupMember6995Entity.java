package com.xue.liang.app.v2.entity;


import java.util.List;

/**
 * Created by jikun on 17/5/20.
 */

public class GroupMember6995Entity {
    private SendCall6995Entity sendCall6995Entity;

    private List<MemberInfo> memberList;

    public SendCall6995Entity getSendCall6995Entity() {
        return sendCall6995Entity;
    }

    public void setSendCall6995Entity(SendCall6995Entity sendCall6995Entity) {
        this.sendCall6995Entity = sendCall6995Entity;
    }

    public List<MemberInfo> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MemberInfo> memberList) {
        this.memberList = memberList;
    }

    public static class MemberInfo {

        private String trueName;
        private String tel;
        private String groupName;

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

    }
}
