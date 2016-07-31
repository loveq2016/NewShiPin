package com.temobi.vcp.sdk.data;

import java.io.Serializable;

//设置设备密码
public class CfgDevPwd implements Serializable{

	public String sDeviceID;//设备ID
	public int cameraId;//镜头ID
	public String sOldPasswd;//旧密码
	public String sNewPasswd;//新密码
	
}
