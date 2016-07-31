package com.temobi.vcp.sdk.data;

import java.io.Serializable;

/**
 * 告警图片
 * @author Temobi
 *
 */
public class AlarmPicture implements Serializable{

	public int Type;//图片类型  0.所有抓图  4.移动侦测抓图  8.传感器抓图
	public String BeginTime;//抓图时间 ,HH:mm:s
	public String PictureURL;//录像ID
}
