package com.temobi.vcp.sdk.data;

import java.io.Serializable;

/**
 * 录像记录
 * 
 * @author Administrator
 * 
 */
public class RecordResult implements Serializable {
	public int nRecordFrom; // LOCATION_DEVICE , 或 LOCATION_CLOUD

	public int nRecordType; // 录像类型  
	public String sBeginTime; // 录像开始时间
	public String sEndTime; // 录像结束时间

	// 仅当nRecordFrom=LOCATION_DEVICE,查询设备录像时有
	public String sFileName; // 文件名称
	public String nSize; // 文件大小

	// 仅当nRecordFrom=LOCATION_CLOUD,云录像时有
	public String sServerID; // 服务器ID
	/**
	 * 当为设备录像时，为设备告警的ContentId 
	 * 当为云录像时，如果为手动和排程录像，为云录像ID
	 * 当为云录像时，如果为移动侦测录像或者传感器报警录像时，为告警ID
	 */
	public String sRecordID;

}
