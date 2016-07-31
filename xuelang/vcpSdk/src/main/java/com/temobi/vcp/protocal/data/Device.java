package com.temobi.vcp.protocal.data;

import java.util.List;

//设备
public class Device {

	public String DeviceId;
	public String Type;
	public String RunStatus;
	
	public String P2PId;
	
	
	public List<MyCamera> cameraList ;
	
	public ThumbnailServer thumnailServer;//缩略图服务器信息
}
