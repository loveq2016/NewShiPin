package com.temobi.vcp.protocal.data;

import java.util.List;

public class MyCamera {
	public String CameraId;//摄像头Id
	public String CameraName = "";//摄像头别名
	public int PTZType = 1;//云台类型：1:支持云台控制，0：不支持云台控制
	public String Type;//摄像头类型:客户端需要根据这个信息做一些操作控制，如TD摄像头不允许码流选择切换	TD：TD摄像头，WIFI：家庭终端的WIFI摄像头 
	public int Runstatus = 2;//1在线,2离线
	public int UpStreamNum;//设备当前正在向平台上行的流数量，直播+录像流
	public String Msisdn;//摄像头绑定的手机号
	public String Ver;//摄像头软件版本号
	public String BuildTime;//软件包发行时间,yyyyMMdd HH:mm
	public long RunDuration;//摄像头运行时长,单位：秒
	public String Company;//摄像头制造商简写
	public List<Stream> Stream_List;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "cameraId=" + CameraId + ";cameraName=" + CameraName
				+ ";PTZType=" + PTZType + ";Type=" + Type;
	}
}




