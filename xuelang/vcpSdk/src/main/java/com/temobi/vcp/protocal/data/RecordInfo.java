package com.temobi.vcp.protocal.data;

import java.io.Serializable;
import java.util.List;

import com.temobi.vcp.sdk.data.VcpDataEnum;




import android.os.Parcel;
import android.os.Parcelable;

public class RecordInfo implements Serializable,Parcelable {
	public String ContentId;
	public String Event;
	public String BeginTime;
	public String EndTime;
	public String ContentSize;
	
	public String duringTime;//本地录像的时长
	public String videoDate;//本地录像的发生的日期
	
	public int Location;//位置  1 ；设备  2 中心
	
	public String ServerId;//服务器ID
	
	public String recordType;//录像类型1.手动录像 2.排程录像 4.移动侦测录像 8.传感器报警录像 0所有录像 16,音频监测报警录像
	
	public String devId;//设备ID
	
	public String cameraId;//镜头ID
	
	public int returnType;//1按文件名返回，2按时间段返回  
	
	
	//云录像记录对应的告警文件集合
	public List<AlarmGuardEventInfo> alarmAttachList = null;
	
	
	
	
	
	
	public static final String EVENT_ALARM="ALARM";
	public static final String EVENT_TIMING="TIMING";
	public static final String EVENT_LOCAL="LOCAL";

	public RecordInfo()
	{
		
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ContentId=" + ContentId + ";Event=" + Event + ";BeginTime="
				+ BeginTime + ";EndTime=" + EndTime + ";ContenetSize="
				+ ContentSize+";duringTime="+duringTime+";videoDate="+videoDate;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public static final Parcelable.Creator<RecordInfo> CREATOR = new Parcelable.Creator<RecordInfo>() {
		public RecordInfo createFromParcel(Parcel in) {
			return new RecordInfo(in);
		}

		public RecordInfo[] newArray(int size) {
			return new RecordInfo[size];
		}
	};

	private RecordInfo(Parcel in) {
		ContentId = in.readString();
		
		Event = in.readString();
		BeginTime = in.readString();
		EndTime = in.readString();
		ContentSize = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(ContentId);
		dest.writeString(Event);
		dest.writeString(BeginTime);
		dest.writeString(EndTime);
		dest.writeString(ContentSize);
		
	}
	
	
}
