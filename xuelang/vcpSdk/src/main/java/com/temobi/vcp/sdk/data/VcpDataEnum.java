package com.temobi.vcp.sdk.data;

/*
 * 各种 类型  枚举列表
 */
public class VcpDataEnum {

	// 录像位置
	public class RecordLocation {

		public static final int LOCATION_DEVICE = 1;// 设备
		public static final int LOCATION_CLOUD = 2;// 云/服务器
		public static final int LOCATION_ALL = 0;// 所有位置
	}

	/**
	 * 录像类型
	 * 
	 * @author Administrator
	 * 
	 */
	public class RecordType {

		public static final int RECORD_ALL = 0;// 所有
		public static final int RECORD_MANUAL = 1;// 手动
		public static final int RECORD_PLAN = 2;// 计划录像
		public static final int RECORD_MDETEION_ALARM = 4;// 移动侦测录像
		public static final int RECORD_SENCE_ALARM = 8;// 传感器告警录像/io告警
		public static final int RECORD_AUDIO_ALARM = 16;// 音频监测告警录像

	}

	/**
	 * 录像返回类型
	 */
	public class RecordReturnType {
		public static final int RETURN_TIME = 1;// 按时间片段返回
		public static final int RETURN_FILE = 2;// 按文件名返回
	}
	
	/**
	 * 开关类型
	 */
	public class SwitchType
	{
		public static final int SWITCH_ALARM = 1;// 告警开关
		public static final int SWITCH_CLOUDRECORD = 2;// 云录像开关
	}
	
	/**
	 * 告警文件类型
	 */
	public class AttachType
	{
		public static final int ALARM_PIC = 1;// 告警图片
		public static final int ALARM_VIDEO = 2;// 告警视频
		public static final int ALARM_ALL = 0;// 所有
	}

}
