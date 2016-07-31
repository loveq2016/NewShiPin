package com.temobi.vcp.sdk.data;

import java.io.Serializable;

/*
 * 设备各种开关
 */
public class DeviceSwitchInfo implements Serializable{

	public String devID;
	public int cameraID;
	public int  nSwitchType;               //1是设备报警开关，2是中心录像开关
    public int  nSwitchFlag;                //开关，0关闭，1开启
}
