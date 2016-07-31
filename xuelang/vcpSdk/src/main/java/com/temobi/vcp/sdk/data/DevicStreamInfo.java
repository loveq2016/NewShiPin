package com.temobi.vcp.sdk.data;

import java.io.Serializable;

//设备支持码流
public class DevicStreamInfo implements Serializable {

	public String sDeviceID;
	public int nStreamMain; // 主码流： 0表示不支持，1表示支持
	public int nSubStream1; // 子码流1： 0表示不支持，1表示支持
	public int nSubStream2; // 子码流2： 0表示不支持，1表示支持
	public int nSubStream3; // 子码流3： 0表示不支持，1表示支持

	@Override
	public String toString() {
		return "DevicStreamInfo [sDeviceID=" + sDeviceID + ", nStreamMain="
				+ nStreamMain + ", nSubStream1=" + nSubStream1
				+ ", nSubStream2=" + nSubStream2 + ", nSubStream3="
				+ nSubStream3 + "]";
	}

}
