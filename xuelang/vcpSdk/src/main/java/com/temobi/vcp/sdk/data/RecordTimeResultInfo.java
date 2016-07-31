package com.temobi.vcp.sdk.data;

import java.io.Serializable;
import java.util.List;


/*
 * 录像列表(录像片段)
 */
public class RecordTimeResultInfo implements Serializable{

	public  int nRecordCount;//录像条数
	public List<RecordTimeResult> arryRedInfo;//录像片段集合 
	
}
