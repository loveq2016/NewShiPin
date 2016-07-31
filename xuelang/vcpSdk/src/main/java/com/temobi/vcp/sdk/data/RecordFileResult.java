package com.temobi.vcp.sdk.data;

import java.io.Serializable;

//录像记录查询
public class RecordFileResult implements Serializable {

	public int nRecordType;//记录类型
	public String sFileName;//文件名称
	public int nSize;//文件大小
	public String begTime;//开始时间
	public String endTime;//结束时间
	public String serverId;//录像服务器ID
	public String recordId;//记录ID
	
	
	public RecordFileResult(){
	}
	public RecordFileResult(int type,String fileName,int size){
		this.nRecordType = type;
		this.sFileName = fileName;
		this.nSize = size;
	}
}
