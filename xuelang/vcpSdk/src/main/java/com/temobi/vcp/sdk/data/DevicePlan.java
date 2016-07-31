package com.temobi.vcp.sdk.data;

/**
 * //设置一个设备的录像计划或报警计划
 * @author Administrator
 *
 */
public class DevicePlan {

	public String sDeviceID;
	public int cameraID;
//	/*** 1为录像计划，2为报警计划 */
//	public int  nPlanType;      //1为录像计划，2为报警计划
	public WeekInfo weekPlan;////一周计划
}
