package com.temobi.vcp.sdk.data;

/**
 * 录像下载状态常量
 * @author Administrator
 *
 */
public class RecordDownLoadStatus {

	public static final int TM_RECORD_DOWNLOAD_CONNECT_ERROR=1060;//连接失败
	public static final int TM_RECORD_DOWNLOAD_CONNECT_OK=1061;//连接成功
	public static final int TM_RECORD_DOWNLOAD_BEGIN_ERROR=1062 ;//开始下载失败
	public static final int TM_RECORD_DOWNLOAD_RECONNECT=1066;//重连
	public static final int TM_RECORD_DOWNLOAD_TIMEOUT=1067; //下载超时
	public static final int TM_RECOR_DOWNLOAD_RESET=1068;	
	public static final int TM_RECORD_DOWNLOAD_OK=1069;//下载成功
	public static final int TM_RECORD_DOWNLOAD_RESERVED=1070;//预留状态
	public static final int TM_RECORD_DOWNLOAD_FAILED=1110;//录像下载失败 
	public static final int TM_RECORD_DOWNLOAD_GETTOTALLENGTH=TM_RECORD_DOWNLOAD_RESERVED+1;//下载进度
	
}
