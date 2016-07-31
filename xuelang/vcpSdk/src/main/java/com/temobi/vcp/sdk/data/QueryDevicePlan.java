package com.temobi.vcp.sdk.data;

import java.io.Serializable;

//获取设备的录像计划或报警计划
public class QueryDevicePlan implements Serializable{

	public String sDeviceID;
	public int cameraID;
	public int  nPlanType;//1为录像计划，2为报警计划
}
