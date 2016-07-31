package com.temobi.vcp.protocal.data;

import java.util.List;

import com.temobi.vcp.sdk.data.RecordFileResult;

public class ProtocalData {
	static public LoginInfo mLoginInfo; // 请求登陆和服务器返回的消息
	static public SendSmsRequest mSendSmsRequest;// 请求动态密码
	static public ModifyPwd mModifyPwd; // 修改密码消息内容
	static public RtspUrlRequest mRtspUrlRequest;// 请求摄像头url消息内容
	static public List<MyCamera> mMyCameraList; // 摄像头列表
	static public MediaAuth mMediaAuth; // 摄像头使用人数和权限
	static public CameraCtrl mCameraCtrl; // 控制摄像头的请求参数
	static public QueryRecord mQueryRecord; // 录像查询
	static public SetAlarm mSetAlarm; // 安防设置
	static public TerminalDevice mTerminalDevice;// 终端设备信息
	static public AlarmRecordRequest mAlarmRecordRequest;// 查询告警记录
	static public List<AlarmRecord> mAlarmRecordList; // 告警记录列表
	static public Stream mCurStream; // 请求当前url消息内容
	static public MyCamera mCurrentCamera;// 当前操作的摄像头
	static public MyCamera mRenameCamera; // 修改摄像头名单
	static public String mCurrentCameraId; // 当前请求的摄像头ID
	static public long mTimeOffset = 0; // 服务器时间跟客户端时间差值,单位秒
	static public String mCurrentDevNumber; // 当前请求的设备编号
	static public String mCurrentStreamId; // 当前请求的流ID
	static public boolean Haspivilege; // 操作云台的权限的
	static public int mPlayType; // 当前请求播放类型
	static public Device mDevice;// 当前设备
	static public List<Device> mDeviceList;// 当前设备列表
	static public String mCameraParamCategoryInfo;// 需要查询的摄像头参数配置分类ALL:所有分类
													// ,BasicInfo:摄像头基本信息，NetInfo:网络信息,SwitchInfo:各类开关量
													// DiskInfo:存储卡信息OSDInfo：OSD信息
	static public String mDevPlanCategoryInfo;// 设备计划查询/设置
												// 报警计划="AlarmPlan",录像计划="RecordPlan"
	static public CameraParam mCameraParam = null;
	static public String operId;// 当前操作编号
	static public String currentSessionId;// 当前会话编号
	static public RecordInfo currentRecordFile;// 当前操作的录像记录
	static public String mDevSessionId;// 视频分发服务器与设备本次会话标识，由流分发服务器生成，拿到后客户端可以使用此值对当前录像进行控制，如暂停,拖动等
	static public int mPlayRecordType;// 1按文件名播放录像，2按时间范围播放
	static public SecurityInfo mDeviceSecurityInfo;// 当前设备的安全配置信息
	static public PlanList mDevPlanList;// 当前设备的录像/告警计划
	static public SoundTalkServer mSoundTalkServer = null;// 语音对讲服务器信息
	static public RecordDownLoad mRecordDownLoad = null;// 录像下载需要的相关数据
	static public CloudRecordPlay mCloudRecordPlay = null;// 云录像播放需要的相关数据
	static public List<RecordInfo> mRecordList = null;// 当前录像集合
	static public DeviceAbilityInfo mDeviceAbility = null;// 保存当前设备的设备能力信息
	static public DeviceSwitchInfoCfg mDeviceSwitchCfg = null;// 设备各种开关信息
	static public AlarmAttachQuery mAlarmAttachReq = null;// 报警附件查询请求信息
	static public List<Picture> mAlarmPictureList = null;// 报警图片集合
	static public List<AlarmGuardEventInfo> mAlarmAttachList = null;// 报警附件集合
	static public ThumbnailServer mThumbnailServer = null;// 获取缩略图需要的服务器信息

	static public Osddata osddata = null;// 获取缩略图需要的服务器信息

	static public ServerInfo serverInfo = null;// 获取服务器信息（P2P）

	static public String client_id;
	
	static public String mCurrentP2pID;

}
