package com.temobi.vcp.sdk.data;

import java.io.Serializable;

/*
 * 一个时间段范围 录像记录 
 */
public class RecordTimeResult implements Serializable {
	public int nRecordType;//记录类型
	public String sBeginTime;//文件开始时间
	public String sEndTime;//文件结束时间
	public String serverId;//录像服务器ID
	public String recordId;//记录ID
	
}
