package com.xue.liang.app.v3.bean.alarm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/20.
 */

public class AlarmReqBean implements Parcelable {


    public static final int TYPE_COMPLETED = 0; // 0已完成

    public static final int TYPE_TO_BE_PROCESSED = 1;//1 待处理

    public static final int TYPE_HAS_BEEN_REPORTED = 2;//2 已上报

    public static final int TYPE_HAS_BEEN_AUTOMATICALLY_REPORTED = 3;//3 已自动上报


    private String termi_type;//是	终端类型，0: 中间件机顶盒(默认)  1: OTT机顶盒   2：手机

    private String user_id;//是	用户id
    private int group_value;//是	注册返回group_value值

    private int[] type; //  否    请求类型数组：0已完成 1 待处理 2 已上报3 已自动上报

    private int start_index; //是	请求开始位置

    private int count;  //是	请求开始位置

    private String ip;//否	客户端请求服务器ip

    private String port;//否	客户端请求服务器port

    public AlarmReqBean(String termi_type, String user_id, int group_value, int[] type, int start_index, int count) {
        this.termi_type = termi_type;
        this.user_id = user_id;
        this.group_value = group_value;
        this.type = type;
        this.start_index = start_index;
        this.count = count;
        this.ip = ip;
        this.port = port;
    }

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

    public int getGroup_value() {
        return group_value;
    }

    public void setGroup_value(int group_value) {
        this.group_value = group_value;
    }

    public int[] getType() {
        return type;
    }

    public void setType(int[] type) {
        this.type = type;
    }

    public int getStart_index() {
        return start_index;
    }

    public void setStart_index(int start_index) {
        this.start_index = start_index;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.termi_type);
        dest.writeString(this.user_id);
        dest.writeInt(this.group_value);
        dest.writeIntArray(this.type);
        dest.writeInt(this.start_index);
        dest.writeInt(this.count);
        dest.writeString(this.ip);
        dest.writeString(this.port);
    }

    public AlarmReqBean() {
    }

    protected AlarmReqBean(Parcel in) {
        this.termi_type = in.readString();
        this.user_id = in.readString();
        this.group_value = in.readInt();
        this.type = in.createIntArray();
        this.start_index = in.readInt();
        this.count = in.readInt();
        this.ip = in.readString();
        this.port = in.readString();
    }

    public static final Parcelable.Creator<AlarmReqBean> CREATOR = new Parcelable.Creator<AlarmReqBean>() {
        @Override
        public AlarmReqBean createFromParcel(Parcel source) {
            return new AlarmReqBean(source);
        }

        @Override
        public AlarmReqBean[] newArray(int size) {
            return new AlarmReqBean[size];
        }
    };
}
