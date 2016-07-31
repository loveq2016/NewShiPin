package com.temobi.vcp.protocal.data;

/*
 * 录像下载
 */
public class RecordDownLoad {

	public int Range;//录像开始时间，单位为毫秒，该值应小于等于录像文件时长。该字段只要用于录像播放回到上一次播放功能。如果不需要跳过的传0即可
	public String RecordId;//录像内容ID
	public String DevSN;//设备编号
	public String CameraId;//镜头ID
	public int Type;//类型，1为按文件名，2为时间段
	public int Location;//位置：1是设备，2是中心
	
	
	
	public String StreamIP;//流服务器向播放器提供服务的外网IP地址
	public int StreamPort;//流服务器向播放器提供服务的播放端口
	public String DevSessionId;//视频分发服务器与设备本次会话标识，由流分发服务器生成，拿到后客户端可以使用此值对当前录像进行控制，如暂停,拖动
	public String Transport;//视频分发服务器告诉设备推流的网络信息。RTP/AVP/UDT;unicast;interleaved=0-1;destination=172.18.68.61;client_port=8352

	public String sDestFileName;//文件存放路径
}
