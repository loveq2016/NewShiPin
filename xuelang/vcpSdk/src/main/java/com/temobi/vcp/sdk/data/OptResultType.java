package com.temobi.vcp.sdk.data;

/*
 * 业务操作命令号
 */
public class OptResultType {

	public final static int Result_InitSDK = 1;//SDK初始化
	public  final static int Result_SetServerInfo = 2;//设置服务器信息
	public final static int Result_DestorySDK = 3;//释放SDK资源
	public final static int Result_LoginDevice = 4;//登陆设备
	public final static int Result_LogoutDevice = 5;//登出设备
	public final static int Result_StartPlay = 6;//开始播放实时视频
	public final static int Result_StopPlay = 7;//停止播放实时视频
	public final static int Result_PtzControl = 8;//云台控制
	public final static int Result_PresetControl = 9;//预置点设置
	public final static int Result_StartTalkVoice = 10;//开启语音对讲
	public final static int Result_StopTalkVoice = 11;//停止语音对讲
	public final static int Result_FindRecordInfo = 12;//录像查找
	public final static int Result_StartRecordFilePlay = 13;//按文件名播放录像
	public final static int Result_StartRecordTimePlay = 14;//按时间段播放录像
	public final static int Result_RecordPlayControl = 15;//录像控制
	public final static int Result_StopRecordPlay = 16;//录像停止播放
	public final static int Result_StartRecordFileDownload = 17;//按文件名开始下载录像
	public final static int Result_StartRecordTimeDownload = 18;//按时间段开始下载录像
	public final static int Result_StopRecordDownload = 19;//录像下载停止
	public final static int Result_GetDeviceInfo = 20;//获取设备信息
	public final static int Result_SetDeviceInfo = 21;//设置设备信息 参数
	public final static int Result_DelRecord = 22;//删除录像
	public final static int Result_DeviceAbilityQuery = 23;//设备能力查询
	public final static int Result_CloudRecordFlagSet = 24;//中心录像开关
	public final static int Result_FindAlarmFileInfo = 25;/*查询报警文件*/
	public final static int Result_StartAlarmFilePlay = 26;//播放告警录像
	public final static int Result_ReStartDevice = 27;//设备重启
	
//	public final static int Result_StartP2PUrl = 27;//获取设备信息

	//public final static int Result_FindGuardEventInfo, 

	
	
}
