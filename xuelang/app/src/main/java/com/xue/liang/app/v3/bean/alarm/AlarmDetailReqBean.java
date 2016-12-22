package com.xue.liang.app.v3.bean.alarm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jikun on 2016/12/22.
 */

public class AlarmDetailReqBean implements Parcelable {
    private String termi_type;
    private String user_id;
    private String alarm_id;

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

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.termi_type);
        dest.writeString(this.user_id);
        dest.writeString(this.alarm_id);
    }

    public AlarmDetailReqBean() {
    }

    protected AlarmDetailReqBean(Parcel in) {
        this.termi_type = in.readString();
        this.user_id = in.readString();
        this.alarm_id = in.readString();
    }

    public static final Parcelable.Creator<AlarmDetailReqBean> CREATOR = new Parcelable.Creator<AlarmDetailReqBean>() {
        @Override
        public AlarmDetailReqBean createFromParcel(Parcel source) {
            return new AlarmDetailReqBean(source);
        }

        @Override
        public AlarmDetailReqBean[] newArray(int size) {
            return new AlarmDetailReqBean[size];
        }
    };
}
