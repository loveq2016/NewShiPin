package com.temobi.vcp.protocal;

import com.temobi.vcp.protocal.McpClientProtocalData.IPlayerStateCallback;

/**
 * 为了实时视频自动装换 写的 保存的请求信息类
 * 
 * @author Administrator
 * 
 */
public class PlaySaveAutoInfo {
	public String sSessionID;
	public String sOptID;
	public String sDevID;
	public int nCameraID;
	public String p2pID;
	public int nStreamType;

	public IPlayerStateCallback mplayerStateCallback;
}
