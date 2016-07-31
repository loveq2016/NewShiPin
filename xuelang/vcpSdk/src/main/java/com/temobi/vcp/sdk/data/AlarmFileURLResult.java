package com.temobi.vcp.sdk.data;

import java.io.Serializable;

/**
 * 告警视频，图片
 * 
 * @author Administrator
 * 
 */
public class AlarmFileURLResult implements Serializable {

	public int nFileType; // 文件类型
	public String sPictureURL; // 图片的URL
	public String sRecordURL; // 录像文件的URL
	public int nAlarmType; // 告警类型
	public String sBeginTime; // 文件开始时间
	public int nTimeSize; // 播放时长
	public int nLocation; // 存储位置 1 设备 2 中心
}

/**
 * 说明：
 * 
 * 1. nFileType,文件类型,参考 VcpDataEnum.AlarmType
 * 2. sPictureURL[256]; //图片的URL
 * 
 * 当告警来自中心时:(nLocation)
 * 
 * 如果nFileType 是 视频，该字段未定义 如果nFileType 是
 * 图片，为告警图片，完整的图片文件url地址，［该地址来自中心录像记录下的附件信息］
 * 
 * 当告警来自设备时:
 * 如果nFileType 是 视频，为缩略图，完整的图片文件url地址，［该地址来自组合的帧数据图片］
 * 如果nFileType 是图片，无［设备告警，不会产生告警图片］
 * 
 * 
 * 
 * 3. sRecordURL[256]; //录像文件的URL //设备告警录像URL为 RTSP串不带IP与端口的内容
 * 
 * 当告警来自中心时， 如果nFileType 是视频，完整录像url，http方式播放 如果nFileType 是图片， 该字段未定义 当告警来自设备时，
 * 如果nFileType 是视频，rtsp相对url，rtsp方式播放 如果nFileType 是图片， 无［设备告警，不会产生告警图片］
 * 
 * 
 * 4. nAlarmType; //告警类型,参考 VcpDataEnum.RecordType
 * 5. sBeginTime[33]; //文件开始时间 6.
 * nTimeSize; //播放时长,该录像当时长(秒)
 * 
 * 7. nLocation; //存储位置,参考 VcpEnums.RecordLocations
 */
