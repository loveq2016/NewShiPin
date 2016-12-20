package com.xue.liang.app.v3.bean.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.xue.liang.app.v3.bean.ServerInfoBean;

/**
 * Created by Administrator on 2016/10/26.
 */
public class LoginRespBean implements Parcelable {



    private int ret_code;//是	响应码:0注册成功 -1注册出错
    private String app_key;//是	推送appkey

    private String ret_string;//否	返回错误信息，只在注册出错是返回
    private String alias_id;//是	推送别名
    private String user_id;//user_id	String	是	用户id
    private int group_value;//group_value	int	是	用户分组值 0 村 1 乡/镇 2 区/县 3市 4省
    private int user_type;//user_type	int	是	用户类型  101普通用户   102 村镇管理员
    private String group_name;
    private ServerInfoBean server_info;


    public String getAlias_id() {
        return alias_id;
    }

    public void setAlias_id(String alias_id) {
        this.alias_id = alias_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getRet_string() {
        return ret_string;
    }

    public void setRet_string(String ret_string) {
        this.ret_string = ret_string;
    }


    public int getGroup_value() {
        return group_value;
    }

    public void setGroup_value(int group_value) {
        this.group_value = group_value;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public ServerInfoBean getServer_info() {
        return server_info;
    }



    public void setServer_info(ServerInfoBean server_info) {
        this.server_info = server_info;
    }


    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public LoginRespBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ret_code);
        dest.writeString(this.app_key);
        dest.writeString(this.ret_string);
        dest.writeString(this.alias_id);
        dest.writeString(this.user_id);
        dest.writeInt(this.group_value);
        dest.writeInt(this.user_type);
        dest.writeString(this.group_name);
        dest.writeParcelable(this.server_info, flags);
    }

    protected LoginRespBean(Parcel in) {
        this.ret_code = in.readInt();
        this.app_key = in.readString();
        this.ret_string = in.readString();
        this.alias_id = in.readString();
        this.user_id = in.readString();
        this.group_value = in.readInt();
        this.user_type = in.readInt();
        this.group_name = in.readString();
        this.server_info = in.readParcelable(ServerInfoBean.class.getClassLoader());
    }

    public static final Creator<LoginRespBean> CREATOR = new Creator<LoginRespBean>() {
        @Override
        public LoginRespBean createFromParcel(Parcel source) {
            return new LoginRespBean(source);
        }

        @Override
        public LoginRespBean[] newArray(int size) {
            return new LoginRespBean[size];
        }
    };
}
