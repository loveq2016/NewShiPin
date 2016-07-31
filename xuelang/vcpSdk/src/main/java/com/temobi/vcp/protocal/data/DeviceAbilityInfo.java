package com.temobi.vcp.protocal.data;

/*
 * 设备能力结构体
 */
public class DeviceAbilityInfo {

	public String  sDeviceID;   //设备ID
    public int nRealPlay;        //实时视频  / /1:支持，0：不支持
    public int nAudioTalk;       //语音对讲  / /1:支持，0：不支持
    public int nAlarmReport;     //报警上报  / /1:支持，0：不支持
    public int nAlarmRecord;     //报警录像  / /1:支持，0：不支持
    public int nAlarmPicture;     //报警抓图  / /1:支持，0：不支持
    public int nParamConfig;     //设备参数配置  / /1:支持，0：不支持
    public int nFileQuery;       //文件检索       / /1:支持，0：不支持
    public int nFilePlay;         //录像文件播放  / /1:支持，0：不支持
    public int nFileDownload;    //文件下载      / /1:支持，0：不支持
    public int nPtz;//云台控制  1 支持  0 不支持
}
