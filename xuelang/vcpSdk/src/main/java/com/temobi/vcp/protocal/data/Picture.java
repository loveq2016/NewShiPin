package com.temobi.vcp.protocal.data;

/**
 * 报警图片
 * @author Temobi
 *
 */
public class Picture {

	public int Type;//图片类型  0.所有抓图  4.移动侦测抓图  8.传感器抓图
	public String BeginTime;//抓图时间 ,HH:mm:s
	public String PictureURL;//录像ID
}
