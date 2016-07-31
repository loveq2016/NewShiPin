package com.temobi.vcp.sdk.data;

/*
 * 云台相关命令
 */
public class PTZCommand {

	public static final int CAMERA_PAN_UP = 1;// 云台向上
	public static final int CAMERA_PAN_DOWN = 2;// 云台向下
	public static final int CAMERA_PAN_LEFT = 3; // 云台向左
	public static final int CAMERA_PAN_RIGHT = 4; // 云台向右
	public static final int CAMERA_FOCUS_IN = 5;// 焦点前调
	public static final int CAMERA_FOCUS_OUT = 6;// 焦点后调
	public static final int CAMERA_IRIS_IN = 7;// 光圈调大
	public static final int CAMERA_IRIS_OUT = 8;// 光圈调小
	public static final int CAMERA_ZOOM_IN = 9;// 倍率调大
	public static final int CAMERA_ZOOM_OUT = 10; // 倍率调小
	public static final int CAMERA_LIGHT_CTRL = 11;// 灯控制
	public static final int CAMERA_BRUSH_CTRL = 12;// 雨刷控制
	public static final int CAMERA_HEATER_CTRL = 13; // 加热器控制
	public static final int CAMERA_AUX_CTRL = 14;// 辅助设备控制
	public static final int CAMERA_AUTO_TURN = 15;// 云台自动旋转
	public static final int PTZ_PRESET_SET = 16; // 设置预置点
	public static final int PTZ_PRESET_DELETE = 17;// 删除预置点
	public static final int PTZ_PRESET_GOTO = 18; // 执行预置点
	public static final int CAMERA_PTZ_FLIP = 19; // 云台翻转
	public static final int CAMERA_PTZ_MIRROR = 20; // 云台镜像
	public static final int CAMERA_PTZ_STOP = 21; // 停止云台控制
}
