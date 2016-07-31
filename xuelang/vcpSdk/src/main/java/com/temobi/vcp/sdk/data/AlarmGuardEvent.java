package com.temobi.vcp.sdk.data;

import java.io.Serializable;

/**
 * 报警附件信息
 * 
 * @author Administrator
 * 
 */
public class AlarmGuardEvent implements Serializable {
	public int Type; // 附件类型 1：图片 2：视频
	public String BeginTime; // 抓图时间,HH:mm:ss
	public String AttchURL;
}
