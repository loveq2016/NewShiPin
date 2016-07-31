package com.temobi.vcp.protocal.data;

import java.util.List;

public class QueryRecord {
	// request info
	public String devId;
	public int BeginInex;
	public int RecordNum;
	public int pageSize;
	public String CAMERA_ID;
	public String BeginTime;
	public String EndTime;
	public String OrderBy;
	public List<String> EVENT_LIST;
	public int nRetriveType;//1:返回一个时间段的录像记录，2：按录像文件名返回
	public int Type;//查询录像方式1：同时查询云录像和设备录像     其他值：只查询云录像
	// response info
	public int location;//1 设备 2 云录像
	public List<RecordInfo> RecordInfos;
}
