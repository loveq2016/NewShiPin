package com.temobi.vcp.protocal.data;

/*
 * 摄像头网络信息
 */
public class CameraNetInfo {

	public String NetType;//网络类型：2G、3G、WIFI、LAN
	
	public int SignalStrength;//信号强度 信号强度，取值0:弱,1:中,2:强 如果是WIFI和LAN填值为2
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "NetType="+NetType+";SignalStrength="+SignalStrength;
	}
}
