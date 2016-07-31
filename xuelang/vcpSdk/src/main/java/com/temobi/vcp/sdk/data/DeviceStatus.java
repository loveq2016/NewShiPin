package com.temobi.vcp.sdk.data;

import java.io.Serializable;

/*
 * 单个设备状态
 */
public class DeviceStatus implements Serializable{

	public DeviceID sDeviceID;
	public int nStatus;////设备状态，0为离线，1为在线
}
