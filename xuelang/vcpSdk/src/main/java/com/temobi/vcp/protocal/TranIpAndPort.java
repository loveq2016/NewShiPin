package com.temobi.vcp.protocal;

import com.temobi.vcp.protocal.data.CloudRecordPlay;
import com.temobi.vcp.protocal.data.RecordDownLoad;
import com.temobi.vcp.protocal.data.SoundTalkServer;
import com.temobi.vcp.protocal.data.Stream;
import com.temobi.vcp.protocal.data.ThumbnailServer;

/**
 * 映射IP类
 * 
 * @author Administrator
 * 
 */
public class TranIpAndPort {
	public boolean isdebug = true;

	/** （流分发服务器 缩略图服务器 语音对讲 服务器） 共同本地IP */
	public final static String Stream_Local_Ip = "192.168.3.83";// 192.168.3.250
	/** （流分发服务器 缩略图服务器 语音对讲服务器）共同外网IP */
	public final static String Stream_Net_Ip = "218.17.55.76";

	/** 设备录像下载服务器本地IP */
	public final static String Device_Download_Local_Ip = "192.168.11.60";
	/** 设备录像下载服务器外网IP */
	public final static String Device_Download_Net_Ip = "120.197.98.38";

	/** 中心录像服务器本地IP */
	public final static String Center_Local_Ip = "192.168.9.65";
	/** 中心录像服务器外网IP */
	public final static String Center_Net_ip = "120.237.113.139";

	/**
	 * 测试所用 映射 IP地址
	 * 
	 * @param t
	 * @return
	 */
	public static <T> T testtranip(T t) {
		boolean islocal = false;
		if (MVPCommand.SERVER_IP.startsWith("192.168")) {
			islocal = true;
		} else if (MVPCommand.SERVER_IP.startsWith("218.17")) {
			islocal = false;
		} else {
			islocal = false;
		}

		if (t instanceof ThumbnailServer) {
			ThumbnailServer thumbnailServer = (ThumbnailServer) t;
			if (islocal) {
				thumbnailServer.host = TranIpAndPort.Stream_Local_Ip;
			} else {
				thumbnailServer.host = TranIpAndPort.Stream_Net_Ip;
			}
			return (T) thumbnailServer;
		} else if (t instanceof Stream) {

			Stream stream = (Stream) t;

			if (islocal) {
				stream.ip = TranIpAndPort.Stream_Local_Ip;
			} else {
				stream.ip = TranIpAndPort.Stream_Net_Ip;
			}
			return (T) stream;
		} else if (t instanceof RecordDownLoad) {
			RecordDownLoad recordDownLoad = (RecordDownLoad) t;

			if (islocal) {
				recordDownLoad.StreamIP = TranIpAndPort.Device_Download_Local_Ip;
			} else {
				recordDownLoad.StreamIP = TranIpAndPort.Device_Download_Net_Ip;
			}
			return (T) recordDownLoad;
		} else if (t instanceof SoundTalkServer) {
			SoundTalkServer soundTalkServer = (SoundTalkServer) t;

			if (islocal) {
				soundTalkServer.StreamIP = TranIpAndPort.Stream_Local_Ip;
			} else {
				soundTalkServer.StreamIP = TranIpAndPort.Stream_Net_Ip;
			}
			return (T) soundTalkServer;

		} else if (t instanceof CloudRecordPlay) {
			CloudRecordPlay cloudRecordPlay = (CloudRecordPlay) t;

			if (islocal) {
				cloudRecordPlay.ServerIP = TranIpAndPort.Center_Local_Ip;
			} else {
				cloudRecordPlay.ServerIP = TranIpAndPort.Center_Net_ip;
			}
			return (T) cloudRecordPlay;

		} else {
			return null;
		}

	}

}
