package com.xue.liang.app.v3.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jikun on 2016/11/18.
 */

public class ServerInfoBean implements Parcelable {


    /**
     * server_type : 10
     * server_ip :
     * server_port : 0
     * login_name :
     * login_pass :
     */

    private int server_type;//Int	是	服务器类型10海康 11大华 12 宇视 13华为
    private String server_ip;//Strig	是	服务器IP
    private int server_port;//Int	是	服务器port
    private String login_name;//是	服务器登录用户名
    private String login_pass;//String	是	服务器登录密码

    public int getServer_type() {
        return server_type;
    }

    public void setServer_type(int server_type) {
        this.server_type = server_type;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public int getServer_port() {
        return server_port;
    }

    public void setServer_port(int server_port) {
        this.server_port = server_port;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getLogin_pass() {
        return login_pass;
    }

    public void setLogin_pass(String login_pass) {
        this.login_pass = login_pass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.server_type);
        dest.writeString(this.server_ip);
        dest.writeInt(this.server_port);
        dest.writeString(this.login_name);
        dest.writeString(this.login_pass);
    }

    public ServerInfoBean() {
    }

    protected ServerInfoBean(Parcel in) {
        this.server_type = in.readInt();
        this.server_ip = in.readString();
        this.server_port = in.readInt();
        this.login_name = in.readString();
        this.login_pass = in.readString();
    }

    public static final Parcelable.Creator<ServerInfoBean> CREATOR = new Parcelable.Creator<ServerInfoBean>() {
        @Override
        public ServerInfoBean createFromParcel(Parcel source) {
            return new ServerInfoBean(source);
        }

        @Override
        public ServerInfoBean[] newArray(int size) {
            return new ServerInfoBean[size];
        }
    };
}
