package com.xue.liang.app.v2.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jikun on 17/5/19.
 */

public class DeviceEntity implements Parcelable {


    /**
     * camera_info : {"camera_id":"11","dev_platform_type":1}
     * dev_name : 观大道路口
     * dev_id : 71156b50-178a95f1-a94c8676b81d
     * dev_url :  rtsp:///xxxxxxxx/2b-4c1b-9665-2b743b2b66d2
     */

    private CameraInfo camera_info;
    private String dev_name;
    private String dev_id;
    private String dev_url;
    private boolean ischoosed;

    public boolean ischoosed() {
        return ischoosed;
    }

    public void setIschoosed(boolean ischoosed) {
        this.ischoosed = ischoosed;
    }

    public CameraInfo getCamera_info() {
        return camera_info;
    }

    public void setCamera_info(CameraInfo camera_info) {
        this.camera_info = camera_info;
    }

    public String getDev_name() {
        return dev_name;
    }

    public void setDev_name(String dev_name) {
        this.dev_name = dev_name;
    }

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }

    public String getDev_url() {
        return dev_url;
    }

    public void setDev_url(String dev_url) {
        this.dev_url = dev_url;
    }

    public static class CameraInfo implements Parcelable {
        /**
         * camera_id : 11
         * dev_platform_type : 1
         */

        private String camera_id;
        private int dev_platform_type;

        public String getCamera_id() {
            return camera_id;
        }

        public void setCamera_id(String camera_id) {
            this.camera_id = camera_id;
        }

        public int getDev_platform_type() {
            return dev_platform_type;
        }

        public void setDev_platform_type(int dev_platform_type) {
            this.dev_platform_type = dev_platform_type;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.camera_id);
            dest.writeInt(this.dev_platform_type);
        }

        public CameraInfo() {
        }

        protected CameraInfo(Parcel in) {
            this.camera_id = in.readString();
            this.dev_platform_type = in.readInt();
        }

        public static final Parcelable.Creator<CameraInfo> CREATOR = new Parcelable.Creator<CameraInfo>() {
            @Override
            public CameraInfo createFromParcel(Parcel source) {
                return new CameraInfo(source);
            }

            @Override
            public CameraInfo[] newArray(int size) {
                return new CameraInfo[size];
            }
        };
    }

    public DeviceEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.camera_info, flags);
        dest.writeString(this.dev_name);
        dest.writeString(this.dev_id);
        dest.writeString(this.dev_url);
        dest.writeByte(this.ischoosed ? (byte) 1 : (byte) 0);
    }

    protected DeviceEntity(Parcel in) {
        this.camera_info = in.readParcelable(CameraInfo.class.getClassLoader());
        this.dev_name = in.readString();
        this.dev_id = in.readString();
        this.dev_url = in.readString();
        this.ischoosed = in.readByte() != 0;
    }

    public static final Creator<DeviceEntity> CREATOR = new Creator<DeviceEntity>() {
        @Override
        public DeviceEntity createFromParcel(Parcel source) {
            return new DeviceEntity(source);
        }

        @Override
        public DeviceEntity[] newArray(int size) {
            return new DeviceEntity[size];
        }
    };
}
