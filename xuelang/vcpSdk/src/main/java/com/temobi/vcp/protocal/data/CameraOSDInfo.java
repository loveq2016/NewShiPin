package com.temobi.vcp.protocal.data;

/*
 * 摄像头OSD信息
 */
public class CameraOSDInfo {

	public int Flag;//0-没有OSD 1-时间OSD 2-名字OSD 3-时间及名字OSD
	public String Name;//OSD名字，如无另外设置，目前取值为摄像头名称
}
