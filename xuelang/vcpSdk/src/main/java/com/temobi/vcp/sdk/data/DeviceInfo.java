package com.temobi.vcp.sdk.data;

import java.io.Serializable;
import java.util.List;

/*
 * 设备信息
 */
public class DeviceInfo implements Serializable {
	public String sDeviceID; // 设备ID
	public String type;// 设备类型
	public int status;// 设备状态
	public String p2pID;// 设备P2Pid

	public List<CameraInfo> cameraInfoList;// 镜头

	@Override
	public String toString() {
		return "DeviceInfo [sDeviceID=" + sDeviceID + ", type=" + type
				+ ", status=" + status + ", p2pID=" + p2pID
				+ ", cameraInfoList=" + liststring() + "]";
	}

	private String liststring() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < cameraInfoList.size(); i++) {
			builder.append("------------" + i + "------------");
			builder.append(cameraInfoList.toString());
			builder.append("------------" + i + "------------");
		}
		return builder.toString();

	}

}
