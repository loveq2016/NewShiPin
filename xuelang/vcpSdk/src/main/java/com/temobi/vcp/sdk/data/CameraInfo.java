package com.temobi.vcp.sdk.data;

import java.io.Serializable;

/*
 * 镜头信息
 */
public class CameraInfo implements Serializable {

	public int cameraId; // 镜头ID
	public int PTZType;// 1:支持云台控制，0：不支持云台控制
	public DevicStreamInfo devicStreamInfo;// 码流信息

	@Override
	public String toString() {
		return "CameraInfo [cameraId=" + cameraId + ", PTZType=" + PTZType
				+ ", devicStreamInfo=" + devicStreamInfo.toString() + "]";
	}

}
