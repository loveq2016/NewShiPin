package com.temobi.vcp.sdk.data;

import java.io.Serializable;

//获取或设置设备参数信息
public class DeviceParam implements Serializable {

	public String sDeviceID;//
	public int mcamerid;
	public int nOSD;// //0为不启用，1-时间OSD，2-名字OSD，3-时间及名字OSD
	public String sDeviceName;//设备名字
}
