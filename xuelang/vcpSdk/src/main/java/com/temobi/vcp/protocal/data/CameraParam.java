package com.temobi.vcp.protocal.data;

/*
 * 摄像头配置参数信息
 */
public class CameraParam {

	public String devId;
	
	public int cameraId;
	//摄像头基本信息
	public MyCamera cameraBaseInfo;
	
	//摄像头网络信息
	public CameraNetInfo cameraNetInfo;
	
	//摄像头各类开关信息
	public CameraSwitchInfo cameraSwitchInfo;
	
	//摄像头存储卡信息
	public CameraDiskInfo cameraDiskInfo;
	
	//摄像头OSD信息
	public CameraOSDInfo cameraOsdInfo;
	
	//安全信息
	public SecurityInfo securityInfo;
	
	
}
