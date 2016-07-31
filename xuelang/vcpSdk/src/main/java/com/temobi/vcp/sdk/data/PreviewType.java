package com.temobi.vcp.sdk.data;

/**
 * 预览类型
 * 
 * @author Administrator
 * 
 */
public enum PreviewType {

	// 实时播放
	TmPreviewLive(0),
	// 设备录像预览
	TmPreviewDevRecord(1),
	// 云录像预览
	TmPreviewCloudRecord(2),
	//报警图片预览
	TmPreviewImageAlarm(3);

	private final int val;

	private PreviewType(int value) {
		val = value;
	}

	public int getValue() {
		return this.val;
	}
}
