package com.temobi.vcp.protocal.data;

/**
 * 报警附件查询请求
 * @author Temobi
 *
 */
public class AlarmAttachQuery {

	public String DevSN;//设备号
	public String CameraId;//镜头ID
	public String PageNo;//页码
	public int PageSize;//每页数量
	public String BeginTime;//开始时间
	public String EndTime;//结束时间
	public String AlarmID;//告警事件ID
	public int Type;//类型  0所有  1图片 2视频
}
