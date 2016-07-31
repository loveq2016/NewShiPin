package com.temobi.vcp.sdk.data;

import java.io.Serializable;
import java.util.List;

/*
 * 批量设备状态
 */
public class DeviceStatusList implements Serializable{

	public int nCount;// //设备状态个数
	public List<DeviceStatus> arryDevStList;//设备状态数组
}
