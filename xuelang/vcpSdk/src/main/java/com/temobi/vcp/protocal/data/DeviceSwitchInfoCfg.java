package com.temobi.vcp.protocal.data;

/**
 * 中心录像配置
 * @author Temobi 
 *
 */
public class DeviceSwitchInfoCfg {

	public String devID;
	public int cameraID;
	public int  nSwitchType;               //1是设备报警开关，2是中心录像开关
    public int  nSwitchFlag;                //开关，0关闭，1开启
}
