package com.temobi.vcp.sdk.data;

/*
 * 获取设备信息命令
 */
public class DevCommand {
	public static final int GET_DEVICE_STATUS = 0x0000; // 获取设备状态
	public static final int GET_DEVICE_STATUS_LIST = 0x0001; // 获取设备列表状态
	public static final int GET_DEVICE_STREAM_IFNO = 0x0002; // 获取设备支持码流信息
	public static final int GET_DEVICE_VERSION_INFO = 0x0003; // 设备版本号获取
	public static final int SET_DEVICE_USER_PASSWD = 0x0004; // 用户密码设置
	public static final int GET_DEVICE_ALARM_PLAN = 0x0005; // 获取设备报警计划
	public static final int SET_DEVICE_ALARM_PLAN = 0x0006; // 设置设备报警计划
	public static final int GET_DEVICE_RECORD_PLAN = 0x0007; // 获取设备录像计划
	public static final int SET_DEVICE_RECORD_PLAN = 0x0008; // 设置设备录像计划
	public static final int GET_DEVICE_PARAM_IFNO = 0x0009; // 获取设备参数
	public static final int SET_DEVICE_PARAM_IFNO_PASSWORD = 0x000A; // 设置设备参数密码
	public static final int SET_DEVICE_PARAM_IFNO_OSD = 0x000B; // 设置设备参数OSD

	public static final int GET_DEVICE_ABILITY = 0x000C; // 获取设备能力信息
	public static final int SET_ALARM_SWITCH = 0x000D; // 设置报警开关(一键撤防布防)
	public static final int GET_ALARM_SWITCH = 0x000E; // 获取报警开关(一键撤防布防)
	public static final int SET_CLOUD_RECORD_SWITCH = 0x000F; // 设置云中心录像开关
	public static final int GET_CLOUD_RECORD_SWITCH = 0x0010; // 获取云中心录像开关
	
	

	public static final int GET_DEVICE_INFO = 0x000B; // 获取设备信息
}
