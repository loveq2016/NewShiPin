package com.temobi.vcp.protocal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.Enumeration;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.widget.RelativeLayout;

import com.temobi.android.player.TMPCPlayer;
import com.temobi.android.player.TMPCPlayer.OnBufferingUpdateListener;
import com.temobi.android.player.TMPCPlayer.OnCompletionListener;
import com.temobi.android.player.TMPCPlayer.OnErrorListener;
import com.temobi.android.player.TMPCPlayer.OnPreparedListener;
import com.temobi.android.player.TMPCPlayer.OnRecodeListener;
import com.temobi.android.player.TMPCPlayer.OnSeekCompleteListener;
import com.temobi.android.player.TMPCPlayer.OnVideoSizeChangedListener;
import com.temobi.android.player.TMPCPlayer.OperationException;

import com.temobi.android.volley.manager.RequestManager;

import com.temobi.transfer.NativeRecord;
import com.temobi.ttalk.CustomRecord;
import com.temobi.ttalk.NativeSupport;
import com.temobi.vcp.protocal.data.CameraCtrl;
import com.temobi.vcp.protocal.data.CameraParam;
import com.temobi.vcp.protocal.data.AlarmAttachQuery;
import com.temobi.vcp.protocal.data.AlarmGuardEventInfo;
import com.temobi.vcp.protocal.data.CameraOSDInfo;
import com.temobi.vcp.protocal.data.CameraSwitchInfo;
import com.temobi.vcp.protocal.data.CloudRecordPlay;
import com.temobi.vcp.protocal.data.DevPlan;
import com.temobi.vcp.protocal.data.Device;
import com.temobi.vcp.protocal.data.DeviceAbilityInfo;
import com.temobi.vcp.protocal.data.DeviceSwitchInfoCfg;

import com.temobi.vcp.protocal.data.LoginInfo;
import com.temobi.vcp.protocal.data.MediaAuth;
import com.temobi.vcp.protocal.data.MyCamera;
import com.temobi.vcp.protocal.data.Osddata;

import com.temobi.vcp.protocal.data.PlanList;
import com.temobi.vcp.protocal.data.QueryRecord;
import com.temobi.vcp.protocal.data.RecordDownLoad;
import com.temobi.vcp.protocal.data.SecurityInfo;
import com.temobi.vcp.protocal.data.ServerInfo;
import com.temobi.vcp.protocal.data.SoundTalkServer;
import com.temobi.vcp.protocal.data.Stream;
import com.temobi.vcp.protocal.data.ProtocalData;
import com.temobi.vcp.protocal.data.RecordInfo;
import com.temobi.vcp.protocal.data.ThumbnailServer;
import com.temobi.vcp.sdk.data.ActionMode;
import com.temobi.vcp.sdk.data.AlarmFileURLResult;
import com.temobi.vcp.sdk.data.AlarmGuardEvent;
import com.temobi.vcp.sdk.data.AlarmImagePreview;

import com.temobi.vcp.sdk.data.CameraInfo;
import com.temobi.vcp.sdk.data.CfgDevPwd;
import com.temobi.vcp.sdk.data.ClientServerInfo;
import com.temobi.vcp.sdk.data.CloudRecordPreview;
import com.temobi.vcp.sdk.data.CommHeader;
import com.temobi.vcp.sdk.data.CommInfo;
import com.temobi.vcp.sdk.data.DevCommand;
import com.temobi.vcp.sdk.data.DevRecordPreview;
import com.temobi.vcp.sdk.data.DevicStreamInfo;
import com.temobi.vcp.sdk.data.DeviceAbility;

import com.temobi.vcp.sdk.data.DeviceInfo;
import com.temobi.vcp.sdk.data.DeviceParam;
import com.temobi.vcp.sdk.data.DevicePlan;
import com.temobi.vcp.sdk.data.DeviceSwitchInfo;
import com.temobi.vcp.sdk.data.DeviceVersion;
import com.temobi.vcp.sdk.data.LivePreview;

import com.temobi.vcp.sdk.data.OptResultType;
import com.temobi.vcp.sdk.data.Osdinfo;

import com.temobi.vcp.sdk.data.PTZCommand;
import com.temobi.vcp.sdk.data.PlayCommand;
import com.temobi.vcp.sdk.data.PreviewBase;
import com.temobi.vcp.sdk.data.PreviewType;
import com.temobi.vcp.sdk.data.QueryDevicePlan;
import com.temobi.vcp.sdk.data.DownLoadStatus;

import com.temobi.vcp.sdk.data.RecordResult;
import com.temobi.vcp.sdk.data.RecordTimeResult;
import com.temobi.vcp.sdk.data.RecordTimeResultInfo;
import com.temobi.vcp.sdk.data.TalkVoiceStatus;
import com.temobi.vcp.sdk.data.VcpDataEnum;
import com.temobi.vcp.sdk.data.WeekInfo;

/*
 * 客户端  sdk 函数封装
 */
public class McpClientProtocalData implements OnBufferingUpdateListener,
		OnErrorListener, OnRecodeListener, OnCompletionListener {

	public static final String TAG = "McpClientProtocalData";
	public static TMPCPlayer player = null;

	// private static ProtocalEngine mProtocalEngine;

	private static McpClientProtocalData mcpClientProtocalInstance = null;

	private static IResultCallback resultCallback = null;// 业务回调接口
	private IPlayerStateCallback playerStateCallback = null;// 播放器回调接口

	private int playType = 1;
	public static final int PLAY_TYPE_MONITOR = 1;// 播放实时视频
	public static final int PLAY_TYPE_RECORDFILE = 2;// 按文件播放录像
	public static final int PLAY_TYPE_RECORDTIME = 3;// 按时间段播放录像

	private int recordCurrentPlayDuration = 0;// 录像当前播放进度

	private int recordMediaType = 5;// 中心录像OR设备录像
	public static final int PLAY_CLOUD = 6;// 中心录像
	public static final int PLAY_DEVICE = 7;// 设备录像

	/*
	 * 录像拖动时使用
	 */
	private String mRecordId;// 录像id
	private String mServerId;// 中心录像服务器ID

	private SurfaceHolder mSurfaceHolder = null;

	private NativeSupport mNativeSupport;// 语音对讲JNI操作类
	private CustomRecord customRecord = null;// 语音对讲使用的线程类
	private NativeRecord mNativeRecord;// 录像下载JNI操作类
	private IRecordingStateCallback recordCallback;// 录像开始/结束回调接口
	private boolean mIsDisplayOutside = false;// false 内显 true 外显
	public boolean isTempUrl = false;
	private boolean isMute = false;// 是否静音，true静音 false非静音
	private ProtocalDataCallback mProtocalDataCallback = new ProtocalDataCallback();
	private ThumbnailServer thumbnailServer = null;// 视频预览缩略图依赖的服务器信息
	private int mAttachType = VcpDataEnum.AttachType.ALARM_ALL;// 告警文件类型 1视频 2
																// 视频
	private Handler mHandler = null;
	private FileDownLoad fileDownlad = null;

	private DownLoadFile downLoadFile = DownLoadFile.getInstance();

	/*** 是否为调试模式 */
	public boolean isdebug = false;

	/** * P2P视频是否为直播或者点播 true为直播 false为点播(但是现在没有点播 只有直播) */
	private boolean isp2plive = true;

	/**
	 * 直播缓冲2000秒
	 */
	private int livebufferTime = 1000;
	/**
	 * 点播缓冲3000秒
	 */
	private int nolivebufferTime = 2000;

	private Context mcContext;

	public static McpClientProtocalData getInstance() {
		if (mcpClientProtocalInstance == null) {
			mcpClientProtocalInstance = new McpClientProtocalData();
			// resultCallbackMap = new HashMap<Integer, IResultCallback>();

		}

		return mcpClientProtocalInstance;
	}

	/*
	 * 初始化SDK信息
	 */
	public int initSDK(Context mcontext) {
		mcContext = mcontext;
		int retCode = 1;
		RequestManager.init(mcContext);

		// LoggerUtil.d("手机的IP地址" + getLocalIpAddress());
		gethashcode();
		return retCode;
	}

	/**
	 * 生成客户端id
	 */
	private void gethashcode() {
		String randomString = RandomUtil.generateString(16);

		int APHash = randomString.hashCode();

		if (APHash < 0) {
			APHash = 0 - APHash;
		}

		ProtocalData.client_id = String.valueOf(APHash);
		LoggerUtil.d("显示client_id" + APHash);

	}

	private int retCode = -1;

	public int destorySDK() {
		int retCode = -1;

		return retCode;
	}

	/**
	 * 2.3设置客户端服务器信息(异步函数)
	 * 
	 * @param sServerIP
	 *            客户端服务器ip地址或者为域名
	 * @param nPort
	 *            客户端服务器端口
	 * @param resultCback
	 *            业务操作结果回调接口 IResultCallback类
	 * @return 此函数为异步函数，返回1表示在回调函数处理结果 -1则没有成功
	 * @throws Exception
	 */
	public int setServerInfo(String sServerIP, int nPort,
			IResultCallback resultCback) throws Exception {
		resultCallback = resultCback;
		int retCode = -1;
		if (sServerIP != null && nPort > 0) {
			MVPCommand.SERVER_IP = sServerIP;
			MVPCommand.SERVER_PORT = ":" + String.valueOf(nPort);
		}

		if (resultCallback != null) {

			resultCallback.ResultCallbackFun(
					OptResultType.Result_SetServerInfo, 1, null, 0);
		}
		// resultCallbackMap.put(key, value)
		return retCode;
	}

	/**
	 * 获取客户端设置的服务器信息
	 */

	public ClientServerInfo getServerInfo() {

		ClientServerInfo serverInfo = new ClientServerInfo();
		serverInfo.ip = MVPCommand.SERVER_IP;
		serverInfo.port = Integer.parseInt(MVPCommand.SERVER_PORT.replace(":",
				""));
		return serverInfo;
	}

	/**
	 * 登陆设备
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param sDevID
	 * @param sUserName
	 * @param sPasswd
	 * @return 设备标示
	 */
	public int loginDevice(String sSessionID, String sOptID, String sDevID,
			String sUserName, String sPasswd, IResultCallback resultCback) {
		resultCallback = resultCback;
		int deviceMark = 1;
		// 登录操作
		// 登录成功，执行客户端回调
		if (resultCback != null) {

			CommHeader header = new CommHeader(OptResultType.Result_LoginDevice);
			CommInfo commInfo = new CommInfo(header, sDevID);
			resultCback.ResultCallbackFun(OptResultType.Result_LoginDevice, 1,
					commInfo, 0);
		}
		return deviceMark;
	}

	/**
	 * 3.2登出设备
	 * 
	 * @param String
	 *            devId 设备号
	 * @return
	 * @throws Exception
	 */
	public int logoutDeviceID(String devId, IResultCallback resultCback) {
		int retCode = -1;
		// 登出操作
		// 登出成功，执行客户端回调
		if (resultCback != null) {
			CommHeader header = new CommHeader(
					OptResultType.Result_LogoutDevice);
			CommInfo commInfo = new CommInfo(header, devId);
			resultCback.ResultCallbackFun(OptResultType.Result_LogoutDevice, 1,
					commInfo, 0);
		}
		return retCode;
	}

	/**
	 * 初始化播放器
	 * 
	 * @param context
	 *            上下文对象
	 * @param holder
	 *            画布控制器
	 * @return 1 成功 -1失败
	 * @throws Exception
	 */
	public int initPlayer(Context context, SurfaceHolder holder)
			throws Exception {
		int retCode = 1;

		player = TMPCPlayer.createPlayer(context.getPackageName(), null,
				TMPCPlayer.APN_CMNET, "", holder);
		if (player == null) {
			Log.d(TAG, "initPlayer---->player is null");
		}

		Log.d(TAG, "initPlayer---->tempBuff-----" + livebufferTime);
		mIsDisplayOutside = true;// 0 内显 1 外显
		player.SetDisplayOutside(mIsDisplayOutside);// false 内显 true 外显

		player.set_BufferTime(livebufferTime);

		player.setBitmapCacheOn(context);
		player.set_moniter(0);
		player.set_LinkType(1);// LINK_TYPE
		player.set_BufferMode(0);
		// player.setDataSource(mediaUrl);
		player.setDisplay(holder);
		player.disableHardwareCodec(1);
		player.EnableAccelerateVideoRender(0);

		player.SetShouldBufferTime(livebufferTime);

		// player.SetShouldBufferTime(livebufferTime);

		player.setOnBufferingUpdateListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
		player.setOnRecodeListener(this);
		mSurfaceHolder = holder;

		return retCode;
	}

	/**
	 * 视频是否处于播放状态 true 是 false 否
	 */
	public boolean isPlaying() {
		if (player == null) {
			return false;
		} else {
			return player.isPlaying();
		}

	}

	/*** 连接方式（0自动，1为P2P，2为中转, ) */
	private int Sdk_nConnType = -100;
	/*** P2P播放模式= 1 */
	public static final int PLAY_MODEL_P2P = 1;
	/*** 中转播放模式= 2 */
	public static final int PLAY_MODEL_RTSP_TRANSIT = 2;
	/*** 自动播放模式=0 */
	public static final int PLAY_MODEL_AUTO_CHOOSE = 0;

	/**
	 * 保存实时视频的请求信息类
	 */
	private PlaySaveAutoInfo mplaySaveAutoInfo = new PlaySaveAutoInfo();

	public int startPlay(String sSessionID, String sOptID, String sDevID,
			int nCameraID, String sp2pId, int nStreamType, int nConnType,
			IPlayerStateCallback mplayerStateCallback) throws Exception {

		if (player != null) {
			player.set_BufferTime(livebufferTime);
			player.SetShouldBufferTime(livebufferTime);
		}

		int retcode = -1;

		Sdk_nConnType = nConnType;
		if (Sdk_nConnType == PLAY_MODEL_RTSP_TRANSIT) {
			retcode = startRtsLivePlay(sSessionID, sOptID, sDevID, nCameraID,
					nStreamType, mplayerStateCallback);
		} else if (Sdk_nConnType == PLAY_MODEL_P2P) {
			retcode = startP2PLivePlay(sSessionID, sOptID, sDevID, nCameraID,
					sp2pId, nStreamType, mplayerStateCallback);
		} else if (Sdk_nConnType == PLAY_MODEL_AUTO_CHOOSE) {
			// 自动模式下先调P2P请求

			mplaySaveAutoInfo.sSessionID = sSessionID;
			mplaySaveAutoInfo.sOptID = sOptID;
			mplaySaveAutoInfo.sDevID = sDevID;
			mplaySaveAutoInfo.p2pID = sp2pId;
			mplaySaveAutoInfo.nCameraID = nCameraID;
			mplaySaveAutoInfo.nStreamType = nStreamType;
			mplaySaveAutoInfo.mplayerStateCallback = mplayerStateCallback;

			retcode = startP2PLivePlay(sSessionID, sOptID, sDevID, nCameraID,
					sp2pId, nStreamType, mplayerStateCallback);
		} else {
			mplayerStateCallback.onPlayerStateCallback(
					TmPlayerStatus.TmPlayerError,
					TMPCPlayer.TMPC_NO_HTTP_URL_FAILED, null);
			throw new Exception("nConnType=" + nConnType + ",is not 0,1,2");

		}

		return retcode;

	}

	/**
	 * 开始播放视频
	 * 
	 * @param sSessionID
	 *            用户登陆合法ID
	 * @param sOptID
	 *            用户操作合法ID
	 * @param sDevID
	 *            设备ID编号
	 * @param nChannelNum
	 *            镜头ID
	 * @param nConnType
	 *            连接方式（0自动，1为P2P，2为中转, )
	 * @param nStreamType
	 *            流类型
	 * @param resultCback
	 *            操作后的结果回调接口
	 * @return 1 成功开始 -1失败
	 * @throws Exception
	 */
	private int startPlay2(String sSessionID, String sOptID, String sDevID,
			int nCameraID, int nConnType, int nStreamType,
			IPlayerStateCallback mplayerStateCallback) throws Exception {

		int retCode = -1;
		this.playerStateCallback = mplayerStateCallback;

		playType = PLAY_TYPE_RECORDFILE;
		ProtocalData.currentSessionId = sSessionID;
		ProtocalData.operId = sOptID;
		ProtocalData.mCurrentDevNumber = sDevID;
		ProtocalData.mCurrentCameraId = String.valueOf(nCameraID);
		ProtocalData.mCurrentStreamId = String.valueOf(nStreamType);

		ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		protocalEngine.addObserver(mProtocalDataCallback);
		try {
			protocalEngine.sendHttpRequest(MVPCommand.VCP_MEDIA_PLAY);
			retCode = 1;
		} catch (Exception ex) {
			retCode = -1;
			Log.d(TAG, "startPlay--exception=" + ex.getMessage());
			playerStateCallback.onPlayerStateCallback(
					TmPlayerStatus.TmPlayerError,
					TMPCPlayer.TMPC_NO_HTTP_URL_FAILED, null);

		}

		ProtocalData.mCurrentDevNumber = sDevID;
		ProtocalData.mCurrentCameraId = String.valueOf(nCameraID);
		return retCode;
	}

	/**
	 * 开始播放服务器器中转直播视频
	 * 
	 * @param sSessionID
	 *            用户登陆合法ID
	 * @param sOptID
	 *            用户操作合法ID
	 * @param sDevID
	 *            设备ID编号
	 * 
	 * @param nStreamType
	 *            流类型
	 * @param mplayerStateCallback
	 *            操作后的结果回调接口
	 * @return 1 成功开始 -1失败
	 * @throws Exception
	 */
	private int startRtsLivePlay(String sSessionID, String sOptID,
			String sDevID, int nCameraID, int nStreamType,
			IPlayerStateCallback mplayerStateCallback) {

		int retCode = -1;
		this.playerStateCallback = mplayerStateCallback;

		playType = PLAY_TYPE_RECORDFILE;
		ProtocalData.currentSessionId = sSessionID;
		ProtocalData.operId = sOptID;
		ProtocalData.mCurrentDevNumber = sDevID;
		ProtocalData.mCurrentCameraId = String.valueOf(nCameraID);
		ProtocalData.mCurrentStreamId = String.valueOf(nStreamType);

		ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		protocalEngine.addObserver(mProtocalDataCallback);
		try {
			protocalEngine.sendHttpRequest(MVPCommand.VCP_MEDIA_PLAY);
			retCode = 1;
		} catch (Exception ex) {
			retCode = -1;
			Log.d(TAG, "startPlay--exception=" + ex.getMessage());
			playerStateCallback.onPlayerStateCallback(
					TmPlayerStatus.TmPlayerError,
					TMPCPlayer.TMPC_NO_HTTP_URL_FAILED, null);

		}

		ProtocalData.mCurrentDevNumber = sDevID;
		ProtocalData.mCurrentCameraId = String.valueOf(nCameraID);
		return retCode;
	}

	/**
	 * 
	 * @param sSessionID
	 *            用户登陆合法ID
	 * @param sOptID
	 *            用户操作合法ID
	 * @param devsn
	 *            设备ID编号
	 * @param nCameraID
	 *            镜头ID
	 * @param p2pid
	 *            设备P2PID号
	 * @param nStreamType
	 *            流类型
	 * @param mplayerStateCallback
	 *            操作后的结果回调接口
	 * 
	 * @category 服务器类型 1：业务管理系统 2：接入服务器； 3：录像服务器； 4：流媒体服务器； 5：客户端服务器 6：业务管理服务器
	 *           7：P2P服务器 现在默认写成7
	 * @return 1为成功发送 -1为失败未能成功发送
	 */
	private int startP2PLivePlay(String sSessionID, String sOptID,
			String devsn, int nCameraID, String p2pid, int nStreamType,
			IPlayerStateCallback mplayerStateCallback) {
		if (player != null) {
			player.set_BufferTime(livebufferTime);
			player.SetShouldBufferTime(livebufferTime);
		}

		int retCode = -1;
		isp2plive = true;// 现在写死写成true;只有直播Live

		ProtocalData.currentSessionId = sSessionID;
		ProtocalData.operId = sOptID;
		ProtocalData.mCurrentDevNumber = devsn;
		ProtocalData.mCurrentCameraId = String.valueOf(nCameraID);
		ProtocalData.mCurrentP2pID = p2pid;
		ProtocalData.mCurrentStreamId = String.valueOf(nStreamType);

		ProtocalData.serverInfo = new ServerInfo();
		ProtocalData.serverInfo.ServerType = "7";
		ProtocalData.serverInfo.DevSN = devsn;

		this.playerStateCallback = mplayerStateCallback;

		ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		protocalEngine.addObserver(mProtocalDataCallback);

		try {
			protocalEngine.sendHttpRequest(MVPCommand.VCP_GET_SERVER_INFO);
			retCode = 1;
		} catch (Exception ex) {
			retCode = -1;

		}

		return retCode;
	}

	/**
	 * 
	 * @param devsn
	 *            设备id
	 * @param misp2plive
	 *            boolean 是否为 直播流 点播流 true为直播 false为点播
	 * 
	 * @return
	 */

	private String getp2pPlayurl(boolean misp2plive) {
		String p2pplayurl = "";

		String ip = "";
		String port = "";

		ip = ProtocalData.serverInfo.PublicIp;
		port = ProtocalData.serverInfo.PublicPort;

		if (misp2plive) {

			// "tp2p://p2p_server_ip:port/dev_id/live/stream_name?selfid=client_id"

			p2pplayurl = "tp2p:" + "//" + ip + ":" + port + "/"
					+ ProtocalData.serverInfo.ID + "/live/" + "cam_"
					+ ProtocalData.mCurrentCameraId + "_"
					+ ProtocalData.mCurrentStreamId + "?selfid="
					+ ProtocalData.client_id;

			p2pplayurl = "tp2p:" + "//" + ip + ":" + port + "/"
					+ ProtocalData.mCurrentP2pID + "/live/" + "cam_"
					+ ProtocalData.mCurrentCameraId + "_"
					+ ProtocalData.mCurrentStreamId + "?selfid="
					+ ProtocalData.client_id;

		} else {

			// tp2p://tp2p_server_ip:port/dev_id/file/filepath?selfid=client_id

			// ProtocalData.mCurrentStreamId 为文件地址
			p2pplayurl = "tp2p:" + "//" + ip + ":" + port + "/"
					+ ProtocalData.serverInfo.ID + "/file/" + "cam_"
					+ ProtocalData.mCurrentCameraId + "_"
					+ ProtocalData.mCurrentStreamId + "?selfid="
					+ ProtocalData.client_id;

		}
		// LoggerUtil.d("显示播放P2P的地址" + p2pplayurl);
		return p2pplayurl;
	}

	/**
	 * 停止播放
	 * 
	 * @return 1 成功 -1失败
	 * @throws Exception
	 */
	public int stopPlay() {
		int retCode = -1;
		try {
			if (player != null) {
				player.stop();
				// 发送退出视频播放请求
				ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
				protocalEngine.sendHttpRequest(MVPCommand.VCP_MEDIA_QUIT);
				retCode = 1;
			}
		} catch (OperationException e) {
			// TODO Auto-generated catch block
			retCode = -1;
			e.printStackTrace();
		}

		return retCode;
	}

	/*
	 * 释放播放器
	 */
	public int releasePlayer() {
		int retCode = -1;
		if (player != null) {
			player.release();
			player = null;

			retCode = 1;
		}
		return retCode;
	}

	/**
	 * 获取播放总时间
	 */
	public int getDuration() {
		int duration = 60000;
		if (player != null) {

			duration = player.getDuration();
		}
		return duration;
	}

	/**
	 * 获取当前播放进度
	 */
	public int getCurrentPosition() {
		int currentDuration = 30000;
		if (player != null) {

			currentDuration = player.getCurrentPosition();
		}
		return currentDuration;

	}

	/**
	 * 云台控制
	 * 
	 * @param sessionId
	 *            用户会话ID
	 * @param operId
	 *            用户操作合法ID
	 * @param player
	 *            播放句柄
	 * @param nCtrlCmd
	 *            云台控制指令
	 * @param speed
	 *            云台操作速度 速度(1-10) 在云台上下左右移动时有效，镜像和翻转时无效
	 * @param nStart
	 *            启动标记，1是启动，0是停止；在 翻转和镜像时候 开启填写1 关闭填0
	 * @param resultCback回调函数
	 * @return retCode -1：无权限 0 操作失败 1 操作成功
	 */
	public int ptzControl(String sSessionID, String sOptID, final int nCtrlCmd,
			final int nSpeed, final int nStart, IResultCallback resultCback) {

		this.resultCallback = resultCback;

		// 云台控制命令
		CameraCtrl ptzCtrl = new CameraCtrl();
		ptzCtrl.CameraId = String.valueOf(ProtocalData.mCurrentCameraId);
		ptzCtrl.devId = ProtocalData.mCurrentDevNumber;
		ptzCtrl.nStart = nStart;
		if (nStart == 0) {
			// 停止转动
			ptzCtrl.OpCode = "PTZ_STOP";
		}

		ptzCtrl.Param1 = "1";// 因为一般监控 只有连续模式 所以填1
		switch (nCtrlCmd) {
		case PTZCommand.CAMERA_PAN_UP:
			ptzCtrl.OpCode = "PTZ_UP";
			ptzCtrl.Param2 = String.valueOf(nSpeed);
			// 上移动
			break;
		case PTZCommand.CAMERA_PAN_LEFT:
			ptzCtrl.OpCode = "PTZ_LEFT";
			ptzCtrl.Param2 = String.valueOf(nSpeed);
			// 左移动
			break;
		case PTZCommand.CAMERA_PAN_RIGHT:
			ptzCtrl.OpCode = "PTZ_RIGHT";
			ptzCtrl.Param2 = String.valueOf(nSpeed);
			// 右移动
			break;
		case PTZCommand.CAMERA_PAN_DOWN:
			ptzCtrl.OpCode = "PTZ_DOWN";
			ptzCtrl.Param2 = String.valueOf(nSpeed);
			// 下移动
			break;
		case PTZCommand.CAMERA_FOCUS_OUT:
			ptzCtrl.OpCode = "PTZ_LENS_FOCAL_FAR";
			ptzCtrl.Param2 = String.valueOf(nSpeed);
			break;
		case PTZCommand.CAMERA_FOCUS_IN:
			ptzCtrl.OpCode = "PTZ_LENS_FOCAL_NEAT";
			ptzCtrl.Param2 = String.valueOf(nSpeed);
			break;
		case PTZCommand.CAMERA_IRIS_OUT:
			ptzCtrl.OpCode = "PTZ_LENS_APERTURE_CLOSE";
			ptzCtrl.Param2 = String.valueOf(nSpeed);
			break;
		case PTZCommand.CAMERA_IRIS_IN:
			ptzCtrl.OpCode = "PTZ_LENS_APERTURE_OPEN";
			ptzCtrl.Param2 = String.valueOf(nSpeed);
			break;
		case PTZCommand.CAMERA_ZOOM_IN:
			ptzCtrl.OpCode = "PTZ_LENS_ZOOM_IN";
			ptzCtrl.Param2 = String.valueOf(nSpeed);
			break;
		case PTZCommand.CAMERA_ZOOM_OUT:
			ptzCtrl.OpCode = "PTZ_LENS_ZOOM_OUT";
			ptzCtrl.Param2 = String.valueOf(nSpeed);
			break;
		case PTZCommand.CAMERA_LIGHT_CTRL:
			ptzCtrl.OpCode = "PTZ_AUX_OPEN";
			break;
		case PTZCommand.CAMERA_BRUSH_CTRL:
			ptzCtrl.OpCode = "PTZ_AUX_OPEN";
			break;
		case PTZCommand.CAMERA_HEATER_CTRL:
			ptzCtrl.OpCode = "PTZ_AUX_OPEN";
			break;
		case PTZCommand.CAMERA_AUX_CTRL:
			ptzCtrl.OpCode = "PTZ_AUX_OPEN";

			break;
		case PTZCommand.CAMERA_AUTO_TURN:
			ptzCtrl.OpCode = "PTZ_AUTO";
			ptzCtrl.Param2 = String.valueOf(nSpeed);
			break;
		case PTZCommand.CAMERA_PTZ_FLIP:
			ptzCtrl.OpCode = "PTZ_FLIP";
			ptzCtrl.Param1 = String.valueOf(nStart);
			ptzCtrl.Param2 = "5";
			break;
		case PTZCommand.CAMERA_PTZ_MIRROR:
			ptzCtrl.OpCode = "PTZ_MIRROR";
			ptzCtrl.Param1 = String.valueOf(nStart);
			ptzCtrl.Param2 = "5";
			break;

		case PTZCommand.CAMERA_PTZ_STOP:
			ptzCtrl.OpCode = "PTZ_STOP";
			ptzCtrl.Param2 = "5";
			break;

		case PTZCommand.PTZ_PRESET_SET:
			// 暂不做
			break;
		case PTZCommand.PTZ_PRESET_DELETE:
			// 暂不做
			break;
		case PTZCommand.PTZ_PRESET_GOTO:
			// 暂不做
			break;

		default:
			break;
		}

		ProtocalData.mCameraCtrl = ptzCtrl;

		// 云台鉴权
		MediaAuth mediaAuth = new MediaAuth();
		mediaAuth.CameraId = String.valueOf(ProtocalData.mCurrentCameraId);
		mediaAuth.devId = ProtocalData.mCurrentDevNumber;
		ProtocalData.mMediaAuth = mediaAuth;
		ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		protocalEngine.addObserver(mProtocalDataCallback);
		protocalEngine.sendHttpRequest(MVPCommand.VCP_MEDIA_AUTH);

		return retCode;
	}

	/**
	 * * @param sSessionID 用户登陆合法ID
	 * 
	 * @param sOptID
	 *            用户操作合法ID
	 * @param devsn
	 *            设备ID编号
	 * @param nCameraID
	 *            镜头ID 重启设备
	 * @param resultCback
	 *            业务操作结果回调接口 IResultCallback类
	 */

	public int restartDevice(String sSessionID, String sOptID, String sDevID,
			int nCameraID, IResultCallback resultCback) {
		ProtocalData.mCurrentDevNumber = sDevID;
		ProtocalData.mCurrentCameraId = String.valueOf(nCameraID);
		this.resultCallback = resultCback;

		int retcode = -1;
		try {
			ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_RESTART_DEVICE);
			retcode = 1;
		} catch (Exception e) {
			// TODO: handle exception
			retcode = -1;
		}
		return retcode;

	}

	/**
	 * 设置预设点 nPTZIndex:0-128
	 */
	public int presetControl(String sSessionID, String sOptID, int nCtrlCmd,
			int nPTZIndex, IResultCallback resultCback) throws Exception {

		int retCode = -1;
		this.resultCallback = resultCback;
		// 预设位设置 请求参数赋值 暂无
		// mProtocalEngine.addObserver(mProtocalDataCallback);
		// mProtocalEngine.sendHttpRequest(MVPCommand.VCP_PRE_SET);

		CommHeader cmdHeader = new CommHeader(nCtrlCmd);
		CommInfo cmdInfo = new CommInfo(cmdHeader, null);
		if (resultCallback != null) {
			resultCallback.ResultCallbackFun(
					OptResultType.Result_PresetControl, 1, cmdInfo, 0);
		}

		return retCode;
	}

	/**
	 * 本地抓图 player:播放器引用 picName:图片SD卡下完整路径及名称 图片保存的文件名，路径自行定义 返回1表示成功
	 */
	public int capturePicture(String sPictureName) throws Exception {
		boolean isCreatedScuess = false;
		int retCode = -1;
		int ret = -1;

		if (player != null && player.isPlaying()) {
			int buffer[];
			buffer = new int[player.getVideoHeight() * player.getVideoWidth()];
			Log.d(TAG,
					"capturePicture-->player.getVideoHeight()="
							+ player.getVideoHeight()
							+ ".player.getVideoHeight()="
							+ player.getVideoHeight());

			int isSuccess = -1;
			if (mIsDisplayOutside) {
				// 外显截图
				buffer = player.getRawPicture999();
				if (buffer != null && buffer.length > 0) {
					isSuccess = 1;
				}
			} else {
				// 内显截图
				boolean isOk = false;
				isOk = player.getRawPicture888(buffer);

				int k = 0;
				// 重复截图，直到成�?
				while (!isOk && k < 5) {
					isOk = player.getRawPicture888(buffer);
					k++;
				}
				if (isOk) {
					isSuccess = 1;
				}
			}

			if (isSuccess == 1) {
				Log.i(TAG,
						"---buffer" + buffer.length + " ;buffer[0]="
								+ buffer[0] + "" + "buffer[1]=" + buffer[1]
								+ " " + player.getVideoHeight() + ","
								+ player.getVideoWidth());

				Bitmap b = Bitmap.createBitmap(buffer, player.getVideoWidth(),
						player.getVideoHeight(), Bitmap.Config.RGB_565);

				Log.d(TAG, "capturePicture--->shot bitmap info:" + b.toString());

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				int options = 100;// 从80开始,
				b.compress(Bitmap.CompressFormat.JPEG, options, baos);
				while (baos.toByteArray().length / 1024 > 100) {
					// 大于100K 则压缩

					baos.reset();

					b.compress(Bitmap.CompressFormat.JPEG, options, baos);

					options -= 10;
				}

				// 创建目录
				String dirPath = null;
				File file = null;

				try {
					dirPath = sPictureName.substring(0,
							sPictureName.lastIndexOf("/"));
					Log.d(TAG, "capturePicture-->dirPath=" + dirPath);
					File dir = new File(dirPath);
					dir.mkdirs();
					// 创建文件
					String fName = sPictureName.substring(
							sPictureName.lastIndexOf("/") + 1,
							sPictureName.length());
					Log.d(TAG, "capturePicture-->fileName=" + fName);
					file = new File(dirPath + File.separator + fName);

					try {
						file.createNewFile();
						isCreatedScuess = true;
					} catch (Exception ex) {
						retCode = -1;
						Log.d(TAG, "capturePicture() create file exception:"
								+ ex.getMessage());
					}
					// 图片压缩

				} catch (Exception ex) {
					retCode = -1;
					Log.d(TAG,
							"capturePicture() create dir exception:"
									+ ex.getMessage());
				}

				// && b.compress(Bitmap.CompressFormat.PNG, 100,outputStream)
				if (isCreatedScuess) {
					FileOutputStream outputStream = null;
					try {
						outputStream = new FileOutputStream(file);
						if (b != null && outputStream != null) {
							outputStream.write(baos.toByteArray());
							outputStream.flush();
							retCode = 1;
						} else {
							Log.d(TAG,
									"----------capturePicture.compress is false");
						}
					} catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
						ret = -1;
					} finally {
						if (outputStream != null) {
							try {
								outputStream.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} else {
					retCode = -1;
				}
			}
		} else {
			retCode = -1;
		}

		return retCode;
	}

	/**
	 * 本地录像
	 * 
	 * @param player
	 *            播放器引用
	 * @param fileName
	 *            录像文件名
	 * @param val
	 *            录像时间(毫秒单位) 录像的时间,单位毫秒 val值必须大于等于0 val为0时 为一直录像，请手动停止录像
	 * @return 1 已成功开始录像 -1开始录像失败 -2未播放状态
	 */
	public int localRecordFile(String sFileName, int val,
			IRecordingStateCallback rCallback) {
		Log.d(TAG, "localRecordFile-->sFileName=" + sFileName + ";val=" + val);
		int retCode = 1;
		if (player != null && player.isPlaying()) {
			try {
				this.recordCallback = rCallback;
				if (val == 0) {
					player.StartRecord(sFileName, 2147483647);
				} else {
					player.StartRecord(sFileName, val);
				}

				retCode = 1;
			} catch (Exception ex) {

				retCode = -1;
			}

		} else {
			retCode = -2;
		}
		return retCode;
	}

	/**
	 * 停止本地录像
	 * 
	 * @return 1 已成功停止 -1 停止录像失败
	 */
	public int stopLocalRecordFile() {
		int retCode = -1;
		try {
			player.StopRecord();
			retCode = 1;
		} catch (Exception ex) {

			retCode = -1;
		}

		return retCode;
	}

	/**
	 * 音频开关
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param bOpenSound
	 *            语音开关标示 1开，0是关闭
	 * @return
	 */
	public int controlSound(String sSessionID, String sOptID, int bOpenSound) {

		int retCode = -1;
		try {
			if (player != null) {

				switch (bOpenSound) {
				case 1:
					// 开启音频
					isMute = false;
					player.nativeSetAudioMute(0);
					break;
				case 0:
					// 静音
					isMute = true;
					player.nativeSetAudioMute(1);
					break;
				}
				retCode = 1;

			}
		} catch (Exception ex) {
			retCode = -1;
			Log.d(TAG, "controlSound excetpion=" + ex.getMessage());
		}

		return retCode;
	}

	/**
	 * 获取静音状态 true 静音 false 未静音
	 * 
	 * @return
	 */
	public boolean getMuteState() {
		return isMute;
	}

	public int loginDevice(String sSessionID, String sOptID, String sDevID,
			String sUserName, String sPasswd) throws Exception {
		int retCode = -1;
		return retCode;
	}

	/**
	 * 8.2对讲关闭
	 */
	public void stopTalkVoice() {
		if (customRecord != null) {
			customRecord.setCallback(null);
			try {
				customRecord.pause1();
			} catch (Exception e) {
				// TODO: handle exception
			}

			customRecord = null;
		}
		if (mNativeSupport != null) {
			mNativeSupport.native_destroy();
			mNativeSupport = null;
		}
	}

	/**
	 * 8.1对讲开启
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param sDevID
	 *            设备ID
	 * @param nVoiceChannelNum
	 *            音频通道号, 默认为1
	 * 
	 * @return 成功1 失败 -1
	 */
	public int startTalkVoice(String sSessionID, String sOptID, String sDevID,
			int cameraId, IResultCallback retCallback) {

		// int retCode = 1;
		// this.resultCallback = retCallback;
		//
		// mNativeSupport = new NativeSupport();
		// mHandler = new MainHandler();
		// mNativeSupport.setHandler(mHandler);
		// ProtocalData.mCurrentDevNumber = sDevID;
		// ProtocalData.mCurrentCameraId = String.valueOf(cameraId);
		// ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		// protocalEngine.addObserver(mProtocalDataCallback);
		// protocalEngine.sendHttpRequest(MVPCommand.VCP_GET_SOUNDTALK_SERVER);
		//
		// return retCode;

		// ///////////
		int retCode = 1;
		this.resultCallback = retCallback;
		if (player != null
				&& player.getTPlayerState() >= TMPCPlayer.PLAYER_BUFFERING) {
			mNativeSupport = new NativeSupport();
			mHandler = new MainHandler();
			mNativeSupport.setHandler(mHandler);
			ProtocalData.mCurrentDevNumber = sDevID;
			ProtocalData.mCurrentCameraId = String.valueOf(cameraId);
			ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_GET_SOUNDTALK_SERVER);
		} else {
			resultCallback.ResultCallbackFun(
					OptResultType.Result_StartTalkVoice,
					TalkVoiceStatus.TM_AUDIOTALK_TRANSFER_CONNECT_ERROR, null,
					0);
		}

		return retCode;
	}

	/**
	 * 9.1.1计划录像检索
	 * 
	 * @param sessionId
	 *            用户会话ID
	 * @param operId
	 *            操作ID
	 * @param devId
	 *            设备ID
	 * @param nCameraID
	 *            镜头ID
	 * @param begTime
	 *            搜索开始时间 日期格式：2014-06-12 12:00:00
	 * @param endTime
	 *            搜索结束时间 日期格式：2014-06-12 12:00:00
	 * @param location
	 *            存储位置 1是设备，2是中心 3 所有位置
	 * 
	 * @return 是否成功开始检索 1 成功 0 失败
	 */
	public int FindRecordInfo(String sSessionID, String sOptID, String sDevID,
			int nCameraID, String sStartTime, String sEndTime, int nLocation,
			IResultCallback retCallback) {
		Log.d(TAG, "FindRecordInfo-->sSessionID=" + sSessionID + ";sOptID="
				+ sOptID + ";sOptID=" + sOptID + ";nCameraID=" + nCameraID);
		int retCode = -1;
		this.resultCallback = retCallback;
		try {
			QueryRecord queryRecord = new QueryRecord();
			queryRecord.BeginInex = 1;
			queryRecord.RecordNum = 1000;
			queryRecord.BeginTime = sStartTime;
			queryRecord.EndTime = sEndTime;
			queryRecord.OrderBy = "DESC";
			queryRecord.location = nLocation;
			queryRecord.devId = sDevID;
			List<String> eventList = new ArrayList<String>();
			eventList.add(String.valueOf(VcpDataEnum.RecordType.RECORD_PLAN));// 2计划录像

			queryRecord.EVENT_LIST = eventList;// 录像类型
			queryRecord.CAMERA_ID = String.valueOf(nCameraID);
			// queryRecord.nRetriveType = nRetriveType;

			if (nLocation == VcpDataEnum.RecordLocation.LOCATION_ALL) {
				// type=1 表示请求服务器同时查询设备录像和云录像 查询录像方式1：同时查询云录像和设备录像 其他值：只查询云录像
				queryRecord.Type = 1;
			}
			ProtocalData.mQueryRecord = queryRecord;

			ProtocalData.currentSessionId = sSessionID;
			ProtocalData.operId = sOptID;
			ProtocalData.mCurrentDevNumber = sDevID;
			ProtocalData.mCurrentCameraId = String.valueOf(nCameraID);
			ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);

			if (nLocation == VcpDataEnum.RecordLocation.LOCATION_DEVICE) {
				// 设备录像
				protocalEngine.sendHttpRequest(MVPCommand.VCP_QUERY_RECORD);

			} else if (nLocation == VcpDataEnum.RecordLocation.LOCATION_CLOUD) {
				// 云录像
				protocalEngine.sendHttpRequest(MVPCommand.VCP_GET_CLOUDRECORD);
			} else if (nLocation == VcpDataEnum.RecordLocation.LOCATION_ALL) {
				// 所有录像
				protocalEngine.sendHttpRequest(MVPCommand.VCP_GET_CLOUDRECORD);
			}
			retCode = 1;
		} catch (Exception ex) {
			retCode = -1;
			Log.d(TAG, "FindRecordInfo exception=" + ex.getMessage());
			resultCallback.ResultCallbackFun(
					OptResultType.Result_FindRecordInfo, -1, null, 0);
		}

		return retCode;
	}

	/**
	 * 中心录像按时间段删除录像
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param DevID
	 * @param nCameraID
	 * @param sStartTime
	 *            开始时间 多个时间以逗号隔开
	 * @param sEndTime
	 *            //结束时间 多个时间以逗号隔开
	 * 
	 * @param ListrecordIds
	 *            录像ID列表，删除录像时使用
	 * @param callback
	 * @return
	 */
	public int CloudRecordTimeDelete(String sSessionID, String sOptID,
			String DevID, int nCameraID, ArrayList<String> ListrecordIds,
			IResultCallback callback) {
		int retCode = -1;
		this.resultCallback = callback;
		ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		protocalEngine.addObserver(mProtocalDataCallback);

		// 删除中心录像
		try {
			List<RecordInfo> recordList = new ArrayList<RecordInfo>();

			for (int i = 0; i < ListrecordIds.size(); i++) {

				RecordInfo recordInfo = new RecordInfo();

				recordInfo.ContentId = ListrecordIds.get(i);
				recordInfo.devId = DevID;
				recordInfo.cameraId = String.valueOf(nCameraID);
				recordInfo.returnType = 2;// 2为按时间段返回
				recordInfo.Location = VcpDataEnum.RecordLocation.LOCATION_CLOUD;
				recordList.add(recordInfo);
			}
			ProtocalData.mRecordList = recordList;
			Log.d(TAG, "recordList size=" + recordList.size());
			protocalEngine.sendHttpRequest(MVPCommand.VCP_CLOUD_RECORD_DEL);
			retCode = 1;

		} catch (Exception ex) {
			retCode = -1;
			Log.d(TAG, "RecordTimeDelete cloud record ex=" + ex.getMessage());

		}

		return retCode;

	}

	/**
	 * 按文件名删除录像(暂无使用)
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param sDevID
	 * @param nCameraID
	 * @param sFileName
	 *            多个文件名以逗号隔开
	 * @param nLocation
	 * @param callback
	 * @return
	 */
	private int RecordFileDelete(String sSessionID, String sOptID,
			String sDevID, int nCameraID, String sFileName, int nLocation,
			String recordIds, IResultCallback callback) {
		int retCode = -1;
		this.resultCallback = callback;
		ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		protocalEngine.addObserver(mProtocalDataCallback);
		if (nLocation == 1) {
			// 设备录像删除
			try {

				List<RecordInfo> recordList = new ArrayList<RecordInfo>();
				if (sFileName != null && !sFileName.equals("")) {

					String[] fileNameArray = sFileName.split(",");

					for (int i = 0; i < fileNameArray.length; i++) {

						RecordInfo recordInfo = new RecordInfo();
						recordInfo.ContentId = fileNameArray[i];
						recordInfo.devId = sDevID;
						recordInfo.cameraId = String.valueOf(nCameraID);
						recordInfo.returnType = 1;// 2为按文件名返回
						recordInfo.Location = nLocation;
						recordList.add(recordInfo);
					}
					Log.d(TAG, "recordList size=" + recordList.size());
					ProtocalData.mRecordList = recordList;

					protocalEngine.sendHttpRequest(MVPCommand.VCP_RECORD_DEL);
					retCode = 1;

				}

			} catch (Exception ex) {
				retCode = -1;
				Log.d(TAG, "RecordFileDelete ex=" + ex.getMessage());

			}
		} else if (nLocation == 2) {
			// 中心录像删除
			try {
				List<RecordInfo> recordList = new ArrayList<RecordInfo>();
				if (recordIds != null && recordIds.length() > 0) {
					String[] recordIdsArray = recordIds.split(",");
					for (int i = 0; i < recordIdsArray.length; i++) {

						RecordInfo recordInfo = new RecordInfo();

						recordInfo.ContentId = recordIdsArray[i];
						recordInfo.devId = sDevID;
						recordInfo.cameraId = String.valueOf(nCameraID);
						recordInfo.returnType = 1;// 1为文件返回
						recordInfo.Location = nLocation;
						recordList.add(recordInfo);
					}
					ProtocalData.mRecordList = recordList;
					Log.d(TAG, "recordList size=" + recordList.size());
					protocalEngine
							.sendHttpRequest(MVPCommand.VCP_CLOUD_RECORD_DEL);
					retCode = 1;

				}

			} catch (Exception ex) {
				retCode = -1;
				Log.d(TAG,
						"RecordTimeDelete cloud record ex=" + ex.getMessage());

			}
		}

		return retCode;
	}

	/**
	 * 中心录像删除
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param sDevID
	 * @param nCameraID
	 * @param recordId
	 *            录像ID列表
	 * @param callback
	 * @return
	 */
	private int CloudRecordDelete(String sSessionID, String sOptID,
			String sDevID, int nCameraID, ArrayList<String> ListrecordIds,
			IResultCallback callback) {
		int retCode = -1;
		ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		protocalEngine.addObserver(mProtocalDataCallback);
		try {
			List<RecordInfo> recordList = new ArrayList<RecordInfo>();

			for (int i = 0; i < ListrecordIds.size(); i++) {

				RecordInfo recordInfo = new RecordInfo();
				recordInfo.ContentId = ListrecordIds.get(i);
				recordInfo.devId = sDevID;
				recordInfo.cameraId = String.valueOf(nCameraID);
				recordInfo.returnType = 2;// 2为按时间段返回
				recordInfo.Location = VcpDataEnum.RecordLocation.LOCATION_CLOUD;// 2是中心录像
				recordList.add(recordInfo);
			}
			ProtocalData.mRecordList = recordList;
			Log.d(TAG, "recordList size=" + recordList.size());
			protocalEngine.sendHttpRequest(MVPCommand.VCP_CLOUD_RECORD_DEL);

			retCode = 1;

		} catch (Exception ex) {
			retCode = -1;
			Log.d(TAG, "CloudRecordDelete cloud record ex=" + ex.getMessage());

		}
		return retCode;
	}

	/**
	 * 9.2.1按文件名播放录像 如果是本地录像 int nCameraID, int nLocation都传0
	 * 
	 * @param sessionId
	 * @param operId
	 * @param devId
	 * @param channelNum
	 * @param fileName
	 * @param location
	 * @return
	 * @throws OperationException
	 */
	private int startRecordFilePlay(String sSessionID, String sOptID,
			String sDevID, int nCameraID, String sFileName, int nLocation,
			int currentDuraion, IPlayerStateCallback playerStateCallback) {
		int retCode = -1;

		this.playerStateCallback = playerStateCallback;
		playType = PLAY_TYPE_RECORDFILE;
		if (nCameraID == 0) {

			// 本地录像
			String url = sFileName;
			try {
				Log.d(TAG, "startRecordFilePlay-->local file url=" + url);
				player.setDataSource(url);
				player.start(currentDuraion);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				playerStateCallback.onPlayerStateCallback(
						TmPlayerStatus.TmPlayerError,
						TMPCPlayer.TMPC_MEDIA_SPEC_ERROR, null);
				e.printStackTrace();
			}

		} else {

			this.recordCurrentPlayDuration = currentDuraion;
			if (nLocation == VcpDataEnum.RecordLocation.LOCATION_DEVICE) {
				recordMediaType = PLAY_DEVICE;
				// 设备录像预览
				ProtocalData.currentSessionId = sSessionID;
				ProtocalData.operId = sOptID;
				ProtocalData.mCurrentDevNumber = sDevID;
				ProtocalData.mCurrentCameraId = String.valueOf(nCameraID);
				ProtocalData.mPlayRecordType = 1;
				RecordInfo recordInfo = new RecordInfo();
				recordInfo.ContentId = sFileName;
				recordInfo.Location = nLocation;
				recordInfo.recordType = String
						.valueOf(VcpDataEnum.RecordType.RECORD_PLAN);
				ProtocalData.currentRecordFile = recordInfo;
				ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
				protocalEngine.addObserver(mProtocalDataCallback);
				try {
					protocalEngine
							.sendHttpRequest(MVPCommand.VCP_MEDIA_PLAY_RECORD);
					retCode = 1;

				} catch (Exception ex) {
					Log.d(TAG,
							"startRecordFilePlay--exception=" + ex.getMessage());
					playerStateCallback.onPlayerStateCallback(
							TmPlayerStatus.TmPlayerError,
							TMPCPlayer.TMPC_MEDIA_SPEC_ERROR, null);
				}
			} else if (nLocation == VcpDataEnum.RecordLocation.LOCATION_CLOUD) {
				recordMediaType = PLAY_CLOUD;
				// 云录像预览
				CloudRecordPlay cloudRcordPlay = new CloudRecordPlay();
				cloudRcordPlay.DevSN = sDevID;
				cloudRcordPlay.CameraId = String.valueOf(nCameraID);
				cloudRcordPlay.RecordId = sFileName;
				cloudRcordPlay.ServerId = "";
				ProtocalData.mCloudRecordPlay = cloudRcordPlay;

				ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
				protocalEngine.addObserver(mProtocalDataCallback);
				try {
					protocalEngine
							.sendHttpRequest(MVPCommand.VCP_MEDIA_PLAY_CLOUD_RECORD);
					retCode = 1;
				} catch (Exception ex) {
					Log.d(TAG,
							"startRecordFilePlay--exception=" + ex.getMessage());
					playerStateCallback.onPlayerStateCallback(
							TmPlayerStatus.TmPlayerError,
							TMPCPlayer.TMPC_MEDIA_SPEC_ERROR, null);

				}

			}

		}
		return retCode;
	}

	/**
	 * 9.2.2按时间段播放录像
	 * 
	 * @param sessionId
	 * @param operId
	 * @param devId
	 * @param channelNum
	 * @param startTime
	 * @param endTime
	 * @param location
	 * @param currentDuration
	 *            当前已经播放进度 单位毫秒
	 * @param int serverId 录像服务器ID 播放云录像时传值，其他传空字符
	 * @return
	 */
	public int startRecordTimePlay(String sSessionID, String sOptID,
			String sDevID, int nCameraID, String sStartTime, String sEndTime,
			int nLocation, int currentDuration, String serverId,
			IPlayerStateCallback playerStateCallback) {

		if (player != null) {
			player.set_BufferTime(nolivebufferTime);
			player.SetShouldBufferTime(nolivebufferTime);
		}
		int retCode = -1;
		this.playerStateCallback = playerStateCallback;
		playType = PLAY_TYPE_RECORDTIME;

		this.recordCurrentPlayDuration = currentDuration;
		if (nLocation == VcpDataEnum.RecordLocation.LOCATION_DEVICE) {

			recordMediaType = PLAY_DEVICE;
			this.mRecordId = sStartTime.replace(" ", "_") + "_"
					+ sEndTime.replace(" ", "_");

			ProtocalData.currentSessionId = sSessionID;
			ProtocalData.operId = sOptID;
			ProtocalData.mCurrentDevNumber = sDevID;
			ProtocalData.mCurrentCameraId = String.valueOf(nCameraID);
			ProtocalData.mPlayRecordType = 2;
			RecordInfo recordInfo = new RecordInfo();
			recordInfo.ContentId = sStartTime.replace(" ", "_") + "_"
					+ sEndTime.replace(" ", "_");
			recordInfo.BeginTime = sStartTime;
			recordInfo.EndTime = sEndTime;
			recordInfo.Location = nLocation;

			recordInfo.recordType = String
					.valueOf(VcpDataEnum.RecordType.RECORD_PLAN);
			ProtocalData.currentRecordFile = recordInfo;
			ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			try {
				protocalEngine
						.sendHttpRequest(MVPCommand.VCP_MEDIA_PLAY_RECORD);
				retCode = 1;
			} catch (Exception ex) {
				Log.d(TAG, "startRecordTimePlay--exception=" + ex.getMessage());
				playerStateCallback.onPlayerStateCallback(
						TmPlayerStatus.TmPlayerError,
						TMPCPlayer.TMPC_NO_HTTP_URL_FAILED, null);

			}
		} else if (nLocation == VcpDataEnum.RecordLocation.LOCATION_CLOUD) {
			recordMediaType = PLAY_CLOUD;
			// 云录像预览
			CloudRecordPlay cloudRcordPlay = new CloudRecordPlay();
			cloudRcordPlay.DevSN = sDevID;
			cloudRcordPlay.CameraId = String.valueOf(nCameraID);
			cloudRcordPlay.RecordId = sStartTime.replace(" ", "_") + "_"
					+ sEndTime.replace(" ", "_");
			this.mRecordId = sStartTime.replace(" ", "_") + "_"
					+ sEndTime.replace(" ", "_");
			// this.mRecordId = recordid;
			cloudRcordPlay.ServerId = serverId;
			this.mServerId = serverId;
			ProtocalData.mCloudRecordPlay = cloudRcordPlay;

			RecordInfo recordInfo = new RecordInfo();
			recordInfo.ContentId = sStartTime.replace(" ", "_") + "_"
					+ sEndTime.replace(" ", "_");
			recordInfo.BeginTime = sStartTime;
			recordInfo.EndTime = sEndTime;
			recordInfo.Location = nLocation;

			recordInfo.recordType = String
					.valueOf(VcpDataEnum.RecordType.RECORD_PLAN);
			ProtocalData.currentRecordFile = recordInfo;

			ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			try {
				protocalEngine
						.sendHttpRequest(MVPCommand.VCP_MEDIA_PLAY_CLOUD_RECORD);
				retCode = 1;
			} catch (Exception ex) {
				Log.d(TAG, "startRecordFilePlay--exception=" + ex.getMessage());
				playerStateCallback.onPlayerStateCallback(
						TmPlayerStatus.TmPlayerError,
						TMPCPlayer.TMPC_NO_HTTP_URL_FAILED, null);

			}

		}

		return retCode;
	}

	/**
	 * 播放告警视频
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param sDevID
	 * @param nCameraID
	 * @param sAlarmFileURL
	 *            告警文件URL
	 * @param nLocation
	 *            位置 1设备 2中心
	 * @param playerStateCallback
	 */
	public void startAlarmFilePlay(String sSessionID, String sOptID,
			String sDevID, int nCameraID, String sAlarmFileURL, int nLocation,
			IPlayerStateCallback playerStateCallback) {

		if (player != null) {
			player.set_BufferTime(nolivebufferTime);
			player.SetShouldBufferTime(nolivebufferTime);
		}
		this.playerStateCallback = playerStateCallback;
		try {

			ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			if (nLocation == VcpDataEnum.RecordLocation.LOCATION_DEVICE) {
				// 设备录像
				ProtocalData.currentSessionId = sSessionID;
				ProtocalData.operId = sOptID;
				ProtocalData.mCurrentDevNumber = sDevID;
				ProtocalData.mCurrentCameraId = String.valueOf(nCameraID);
				ProtocalData.mPlayRecordType = 2;
				RecordInfo recordInfo = new RecordInfo();
				int timeParamBeginIndex = sAlarmFileURL.indexOf("cid") + 4;// 取cid=后的值
				int specIndex = sAlarmFileURL.indexOf("|");
				String begTime = sAlarmFileURL.substring(timeParamBeginIndex,
						specIndex).replace(" ", "_");
				String endTime = sAlarmFileURL.substring(specIndex + 1,
						sAlarmFileURL.length()).replace(" ", "_");
				recordInfo.ContentId = begTime + "_" + endTime;
				recordInfo.BeginTime = begTime;
				recordInfo.EndTime = endTime;

				ProtocalData.currentRecordFile = recordInfo;

				protocalEngine
						.sendHttpRequest(MVPCommand.VCP_MEDIA_PLAY_RECORD);

			} else if (nLocation == VcpDataEnum.RecordLocation.LOCATION_CLOUD) {
				// 中心录像,采用http渐进式播放
				String playUrl = "";

				if (player != null) {

					String url = "";
					if (sAlarmFileURL != null) {

						url = sAlarmFileURL.substring(
								sAlarmFileURL.indexOf("url") + 4,
								sAlarmFileURL.length());
						playUrl = "http://" + MVPCommand.SERVER_IP
								+ MVPCommand.SERVER_PORT + "/fdl" + url;
						// LoggerUtil.d("告警中心播放地址" + playUrl);
						player.setDataSource(playUrl);
						player.start(0);
					}

				} else {
					playerStateCallback.onPlayerStateCallback(
							TmPlayerStatus.TmPlayerError,
							TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
				}

			}

		} catch (Exception ex) {
			Log.d(TAG, "startAlarmFilePlay exception=" + ex.getMessage());
			playerStateCallback.onPlayerStateCallback(
					TmPlayerStatus.TmPlayerError,
					TMPCPlayer.TMPC_MEDIA_SPEC_ERROR, null);
		}

	}

	/**
	 * 播放控制 (同步函数，无需异步处理)
	 * 
	 * @param playerCommand
	 *            控制命令
	 * @param nValue
	 *            拖动或速率 播放速率见PlaySpeed，拖动位置范围0-100
	 * @return 1成功 -1失败
	 */
	public int recordPlayControl(int playerCommand, int nValue) {
		int retCode = -1;
		Log.d(TAG, "recordPlayControl-->playerCommand=" + playerCommand
				+ ";nValue=" + nValue);
		if (player != null) {
			try {
				switch (playerCommand) {
				case PlayCommand.RecordPlay_Pause:
					// 暂停
					player.pause();
					break;
				case PlayCommand.RecordPlay_Resume:
					// 恢复
					player.start();
					break;
				case PlayCommand.RecordPlay_Speed:
					break;
				case PlayCommand.RecordPlay_DragPos:
					player.seekTo(nValue, 0);
					// player.start(nValue);
					break;
				case PlayCommand.RecordPlay_Stop:
					player.stop();
					break;

				default:
					break;
				}
				retCode = 1;
			} catch (Exception ex) {
				Log.d(TAG, "recordPlayControl exception=" + ex.getMessage());
				retCode = -1;
			}
		} else {
			retCode = -1;

		}

		return retCode;
	}

	/**
	 * 视频缩放与拖动
	 * 
	 * @param mode
	 *            操作模式 缩放OR拖动,取值为 ActionMode枚举的 ACTION_DRAG OR ACTION_ZOOM值
	 * @param scale
	 *            缩放比例
	 * @param scale_centerX
	 *            缩放时使用 缩放时的中心点 X坐标
	 * @param scale_centerY
	 *            缩放时使用 缩放时的中心点 Y坐标
	 * @param translate_dx
	 *            X坐标上移动的距离
	 * @param translate_dy
	 *            Y坐标上移动的距离
	 * @return
	 */
	public int setPlayerCameraViewInRect(ActionMode mode, float scale,
			float scale_centerX, float scale_centerY, float translate_dx,
			float translate_dy) {

		int retCode = -1;
		if (player != null) {
			if (mode == mode.ACTION_DRAG) {

				// 如果是拖动
				player.isScale = false;
				player.isTranslate = true;
				player.isTranslated = false;
				player.translate_dx = translate_dx;
				player.translate_dy = translate_dy;
				retCode = 1;

			} else if (mode == mode.ACTION_ZOOM) {

				// 缩放
				player.isScale = true;

				player.isTranslate = false;
				player.isScaled = false;

				player.scale_centerX = scale_centerX;
				player.scale_centerY = scale_centerY;
				player.setScaleT(scale);
				retCode = 1;

			}
		} else {
			retCode = -1;
		}

		return retCode;
	}

	/**
	 * 请求摄像头列表信息
	 */
	public void requestDevListInfo(String loginId, List<String> deviceIdList,
			IResultCallback retCallback) throws Exception {
		this.resultCallback = retCallback;

		LoginInfo loginInfo = new LoginInfo();
		loginInfo.LoginId = loginId;
		loginInfo.Dev_List = deviceIdList;
		ProtocalData.mLoginInfo = loginInfo;

		ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		protocalEngine.sendHttpRequest(MVPCommand.VCP_GET_DEVINFO);
	}

	/**
	 * 停止播放(同步函数，无需异步处理)
	 * 
	 * @param player
	 * @return
	 * @throws OperationException
	 */
	public int stopRecordPlay() {
		int retCode = -1;
		if (player != null) {
			try {
				player.stop();
				retCode = 1;
			} catch (OperationException e) {
				// TODO Auto-generated catch block
				retCode = -1;
				e.printStackTrace();
			}
		}
		return retCode;
	}

	/**
	 * 9.4.1按文件名下载录像计划录像(暂无使用)
	 * 
	 * @param sessionId
	 * @param operId
	 * @param devId
	 * @param nCameraId
	 * @param fileName
	 * @param location
	 * @param path
	 * @return
	 */
	private int startRecordFileDownload(String sSessionID, String sOptID,
			String sDevID, int nCameraId, String sFileName, int nLocation,
			String sDestFileName, IResultCallback retCallback) {
		int retCode = -1;
		this.resultCallback = retCallback;

		mNativeRecord = new NativeRecord();
		try {
			mHandler = new MainHandler();
			mNativeRecord.setHandler(mHandler);
			RecordDownLoad recordDownLoad = new RecordDownLoad();
			recordDownLoad.Range = 0;// 0 表示从下载时，不跳过录像内容s
			recordDownLoad.RecordId = sFileName;
			recordDownLoad.DevSN = sDevID;
			recordDownLoad.CameraId = String.valueOf(nCameraId);
			recordDownLoad.Type = 1;// 1 按文件名下载
			recordDownLoad.Location = nLocation;
			recordDownLoad.sDestFileName = sDestFileName;
			ProtocalData.mRecordDownLoad = recordDownLoad;

			ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine
					.sendHttpRequest(MVPCommand.VCP_GET_RECORD_DOWNLOAD_SERVER);

			retCode = 1;
		} catch (Exception ex) {
			retCode = -1;
			Log.d(TAG, "startRecordFileDownload exception:" + ex.getMessage());
		}

		return retCode;
	}

	/**
	 * 9.4.2按时间段下载录像
	 * 
	 * @param sessionId
	 * @param operId
	 * @param devId
	 * @param nCameraId
	 * @param startTime
	 * @param endTime
	 * @param location
	 * @param path
	 * @return
	 */
	public int startRecordTimeDownload(String sSessionID, String sOptID,
			String sDevID, int nCameraId, String sStartTime, String sEndTime,
			int nLocation, String sDestFileName, IResultCallback retCallback) {

		this.resultCallback = retCallback;
		try {
			mNativeRecord = new NativeRecord();
			mHandler = new MainHandler();
			mNativeRecord.setHandler(mHandler);
			RecordDownLoad recordDownLoad = new RecordDownLoad();
			recordDownLoad.Range = 0;// 0 表示从下载时，不跳过录像内容
			recordDownLoad.RecordId = sStartTime + "_" + sEndTime;
			recordDownLoad.DevSN = sDevID;
			recordDownLoad.CameraId = String.valueOf(nCameraId);
			recordDownLoad.Type = 2;// 2按时间段下载
			recordDownLoad.Location = nLocation;
			recordDownLoad.sDestFileName = sDestFileName;
			ProtocalData.mRecordDownLoad = recordDownLoad;

			ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine
					.sendHttpRequest(MVPCommand.VCP_GET_RECORD_DOWNLOAD_SERVER);
			retCode = 1;
		} catch (Exception ex) {
			Log.d(TAG, "startRecordTimeDownload exception=" + ex.getMessage());

			retCode = -1;
		}

		return retCode;
	}

	/**
	 * 告警录像下载
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param sDevID
	 * @param nCameraID
	 * @param sAlarmFileURL
	 *            告警录像URL
	 * @param sDestFileName
	 *            目标文件 如 /sdcard/a.mp4
	 * @param nLocation
	 *            位置 1设备 2中心
	 * @param callback
	 * @return
	 */
	public int startAlarmFileDownload(String sSessionID, String sOptID,
			String sDevID, int nCameraID, String sAlarmFileURL,
			String sDestFileName, int nLocation, IResultCallback callback) {
		int retCode = -1;
		this.resultCallback = callback;

		try {
			if (nLocation == VcpDataEnum.RecordLocation.LOCATION_DEVICE) {
				mNativeRecord = new NativeRecord();
				mHandler = new MainHandler();
				mNativeRecord.setHandler(mHandler);

				int timeParamBeginIndex = sAlarmFileURL.indexOf("cid") + 4;// 取cid=后的值
				int specIndex = sAlarmFileURL.indexOf("|");
				String begTime = sAlarmFileURL.substring(timeParamBeginIndex,
						specIndex).replace(" ", "_");
				String endTime = sAlarmFileURL.substring(specIndex + 1,
						sAlarmFileURL.length()).replace(" ", "_");

				RecordDownLoad recordDownLoad = new RecordDownLoad();
				recordDownLoad.Range = 0;// 0 表示从下载时，不跳过录像内容
				recordDownLoad.RecordId = begTime + "_" + endTime;
				recordDownLoad.DevSN = sDevID;
				recordDownLoad.CameraId = String.valueOf(nCameraID);
				recordDownLoad.Type = 2;// 2按时间段下载
				recordDownLoad.Location = VcpDataEnum.RecordLocation.LOCATION_DEVICE;
				recordDownLoad.sDestFileName = sDestFileName;
				ProtocalData.mRecordDownLoad = recordDownLoad;

				ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
				protocalEngine.addObserver(mProtocalDataCallback);
				protocalEngine
						.sendHttpRequest(MVPCommand.VCP_GET_RECORD_DOWNLOAD_SERVER);
			} else if (nLocation == VcpDataEnum.RecordLocation.LOCATION_CLOUD) {

				String videoUrl = "http://" + MVPCommand.SERVER_IP
						+ MVPCommand.SERVER_PORT + "/fdl" + sAlarmFileURL;

				String url = sAlarmFileURL.substring(
						sAlarmFileURL.indexOf("url") + 4,
						sAlarmFileURL.length());
				String playUrl = "http://" + MVPCommand.SERVER_IP
						+ MVPCommand.SERVER_PORT + "/fdl" + url;
				// LoggerUtil.d("告警中心播放地址" + playUrl);

				// mHandler = new MainHandler();
				// fileDownlad = new FileDownLoad();
				// fileDownlad.setHandler(mHandler);
				// // 开始下载
				// fileDownlad.execute(playUrl, sDestFileName);

				mHandler = new MainHandler();

				downLoadFile.setHandler(mHandler);
				downLoadFile.startdownload(playUrl, sDestFileName);

			}
			retCode = 1;

		} catch (Exception ex) {
			Log.d(TAG, "startAlarmFileDownload exception=" + ex.getMessage());
		}

		return retCode;

	}

	/**
	 * 查询告警视频，图片
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param sDevID
	 * @param nCameraID
	 * @param sStartTime
	 * @param sEndTime
	 * @param nAlarmType
	 *            告警类型 4是移动侦测，8是IO报警, 0是所有,
	 * @param AttachType
	 *            文件类型 0是所有，1是图片，2是视频文件
	 * @param nLocation
	 *            存储位置 1是设备，2是中心，0是所有
	 * @param callback
	 * @return
	 */
	public int FindAlarmFileInfo(String sSessionID, String sOptID,
			String sDevID, int nCameraID, String sStartTime, String sEndTime,
			int nAlarmType, int AttachType, int nLocation,
			IResultCallback callback) {

		int retCode = -1;
		this.resultCallback = callback;
		try {
			mAttachType = AttachType;
			QueryRecord queryRecord = new QueryRecord();
			queryRecord.BeginInex = 1;
			queryRecord.RecordNum = 1000;
			queryRecord.BeginTime = sStartTime;
			queryRecord.EndTime = sEndTime;
			queryRecord.OrderBy = "DESC";
			queryRecord.location = nLocation;
			queryRecord.devId = sDevID;
			List<String> eventList = new ArrayList<String>();
			if (nAlarmType == 0) {
				eventList.add(String
						.valueOf(VcpDataEnum.RecordType.RECORD_MDETEION_ALARM));// 移动侦测
				eventList.add(String
						.valueOf(VcpDataEnum.RecordType.RECORD_SENCE_ALARM));// IO告警
				eventList.add(String
						.valueOf(VcpDataEnum.RecordType.RECORD_AUDIO_ALARM));// 音频告警

			} else {
				eventList.add(String.valueOf(nAlarmType));// 告警的各种类型
			}

			queryRecord.EVENT_LIST = eventList;// 录像类型
			queryRecord.CAMERA_ID = String.valueOf(nCameraID);

			if (nLocation == VcpDataEnum.RecordLocation.LOCATION_ALL) {
				// type=1 表示请求服务器同时查询设备录像和云录像
				queryRecord.Type = 1;
			}
			ProtocalData.mQueryRecord = queryRecord;

			ProtocalData.currentSessionId = sSessionID;
			ProtocalData.operId = sOptID;
			ProtocalData.mCurrentDevNumber = sDevID;
			ProtocalData.mCurrentCameraId = String.valueOf(nCameraID);
			ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);

			if (nLocation == VcpDataEnum.RecordLocation.LOCATION_DEVICE) {
				// 设备告警录像
				protocalEngine
						.sendHttpRequest(MVPCommand.VCP_QUERY_DEVICE_ALARM_FILE);

			} else if (nLocation == VcpDataEnum.RecordLocation.LOCATION_CLOUD) {
				// 中心服务器告警录像
				protocalEngine
						.sendHttpRequest(MVPCommand.VCP_QUERY_CLOUD_ALARM_FILE);
			} else if (nLocation == VcpDataEnum.RecordLocation.LOCATION_ALL) {
				// 所有告警录像
				protocalEngine
						.sendHttpRequest(MVPCommand.VCP_QUERY_CLOUD_ALARM_FILE);
			}

		} catch (Exception ex) {
			Log.d(TAG, "FindAlarmFileInfo ex=" + ex.getMessage());
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 按报警ID查询
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param sDevID
	 * @param nCameraID
	 * @param alarmIDList
	 * @param callback
	 * @return
	 */
	public int FindAlarmFileInfoById(String sSessionID, String sOptID,
			String sDevID, int nCameraID, ArrayList<String> alarmIDList,
			int type, IResultCallback callback) {

		int retCode = 1;
		this.resultCallback = callback;
		AlarmAttachQuery alarmAttachQuery = new AlarmAttachQuery();
		alarmAttachQuery.DevSN = sDevID;
		alarmAttachQuery.CameraId = String.valueOf(nCameraID);
		alarmAttachQuery.PageNo = String.valueOf(1);
		alarmAttachQuery.PageSize = 1000;
		alarmAttachQuery.Type = type;

		String mAlarmID = "";
		if (null != alarmIDList) {
			for (int i = 0; i < alarmIDList.size(); i++) {

				mAlarmID += alarmIDList.get(i) + ",";

			}
			String submAlarmID = mAlarmID.substring(0, mAlarmID.length() - 1);

			alarmAttachQuery.AlarmID = submAlarmID;
		} else {
			alarmAttachQuery.AlarmID = mAlarmID;
		}
		ProtocalData.mAlarmAttachReq = alarmAttachQuery;
		ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		protocalEngine.addObserver(mProtocalDataCallback);

		protocalEngine.sendHttpRequest(MVPCommand.VCP_QUERY_ALARM_FILE_BY_ID);
		// 按报警id
		return retCode;
	}

	/**
	 * 下载告警图片
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param sDevID
	 * @param nCameraID
	 * @param sFileName
	 *            图片ID
	 * @param sDestFileName
	 *            图片下载至SD卡后的完整路径 如 sdcard/a.jpg
	 * @param String
	 *            resolution 分辨率 如果是完整截图传入空字符 缩略图传入分辨率 如128x128
	 * @return
	 */
	private String startAttachPictureFileDownload(String sSessionID,
			String sOptID, String sDevID, String nCameraID, String sFileName,
			String sDestFileName, String resolution,
			OnDownLoadProcessListener downLoadListener) {

		String destPicSrc = "";
		try {
			String serverIP = MVPCommand.SERVER_IP;
			String serverPort = MVPCommand.SERVER_PORT;
			String url = "";
			if (resolution == null || resolution.equals("")) {
				url = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.ALARM_PICTURE_DOWNLOAD_URL + "?url="
						+ sFileName;
			} else {
				url = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.ALARM_PICTURE_DOWNLOAD_URL + "?url="
						+ sFileName + "&spec=" + resolution;
			}
			ProtocalData.mCurrentDevNumber = sDevID;
			ProtocalData.mCurrentCameraId = nCameraID;

			DownImg downImg = new DownImg(downLoadListener);
			destPicSrc = downImg.downLoadFile(url, sDestFileName);

		} catch (Exception ex) {
			downLoadListener.onDownLoadResponse(
					OnDownLoadProcessListener.DOWNLOAD_FAILED, "");
			Log.d(TAG,
					"startPictureFileDownload-->exception=" + ex.getMessage());
		}
		return destPicSrc;
	}

	/**
	 * 视频预览缩略图下载
	 * 
	 * @param type
	 *            视频类型 PreviewType.TmPreviewLive(实时视频),
	 *            PreviewType.TmPreviewDevRecord (设备录像),
	 *            PreviewType.TmPreviewCloudRecord(云录像)
	 * @param previewBase
	 *            视频预览图下载的基础信息,根据Type传入DevRecordPreview
	 *            ,LivePreview,CloudRecordPreview中的一种
	 * @param sDestFileName
	 *            下载后的完整文件名 如 sdcard/a.jpg
	 * @param downLoadListener
	 *            下载回调接口
	 * @return
	 */
	private String startDownloadJpgFromVideo(PreviewType type,
			PreviewBase previewBase, String sDestFileName,
			OnDownLoadProcessListener downLoadListener) {
		String destPicSrc = "";
		try {

			if (thumbnailServer == null || thumbnailServer.host.equals("")) {

				downLoadListener.onDownLoadResponse(
						OnDownLoadProcessListener.DOWNLOAD_FAILED, "");

			} else {

				String httpUrl = "http://" + thumbnailServer.host + ":"
						+ thumbnailServer.port + "";
				if (type == type.TmPreviewLive) {
					// 视频预览图下载
					LivePreview livePreview = (LivePreview) previewBase;
					httpUrl = httpUrl + "/iframe?Type=0?Devid="
							+ livePreview.devId + "&Cameraid="
							+ livePreview.cameraId + "&Streamid="
							+ livePreview.streamId + "";

				} else if (type == type.TmPreviewDevRecord) {
					// 设备录像预览图下载
					DevRecordPreview devRecordPreview = (DevRecordPreview) previewBase;
					httpUrl = httpUrl + "/iframe?Type=0?Devid="
							+ devRecordPreview.devId + "&Cameraid="
							+ devRecordPreview.cameraId
							+ "&Streamid=0&Filename="
							+ devRecordPreview.Filename + "&Timestamp="
							+ devRecordPreview.Timestamp + "";

				} else if (type == type.TmPreviewCloudRecord) {
					// 云录像预览图下载
					CloudRecordPreview cloudRecordPreview = (CloudRecordPreview) previewBase;
					httpUrl = httpUrl + "/iframe?Type=0?Devid="
							+ cloudRecordPreview.devId + "&Cameraid="
							+ cloudRecordPreview.cameraId
							+ "&Streamid=0&Timestamp="
							+ cloudRecordPreview.Timestamp + "&ServerId="
							+ cloudRecordPreview.ServerId + "&Filename="
							+ cloudRecordPreview.recordId;

				}
				DownImg downImg = new DownImg(downLoadListener);
				destPicSrc = downImg.downLoadFile(httpUrl, sDestFileName);
			}

		} catch (Exception ex) {
			downLoadListener.onDownLoadResponse(
					OnDownLoadProcessListener.DOWNLOAD_FAILED, "");
			Log.d(TAG,
					"startDownloadJpgFromVideo-->exception=" + ex.getMessage());
		}

		return destPicSrc;
	}

	/**
	 * 根据预览类型获取对应图片下载地址
	 * 
	 * @param type
	 *            预览类型 PreviewType.TmPreviewLive(实时视频),
	 *            PreviewType.TmPreviewDevRecord (设备录像),
	 *            PreviewType.TmPreviewCloudRecord(云录像),
	 *            PreviewType.TmPreviewImageAlarm
	 *            (报警图片预览),如果是告警图片缩略图，AlarmImagePreview
	 *            .width,AlarmImagePreview.height传入0
	 * @param previewBase
	 *            视频预览图下载的基础信息,根据Type传入DevRecordPreview
	 *            ,LivePreview,CloudRecordPreview,AlarmImagePreview中的一种
	 * @return 图片下载地址 http://.....
	 */
	public String makeUrlofType(PreviewType type, PreviewBase previewBase) {
		String httpUrl = "";
		try {

			if (previewBase == null) {
				httpUrl = "";
			} else {
				// httpUrl = "http://" + thumbnailServer.host + ":"
				// + thumbnailServer.port + "";
				httpUrl = "http://120.197.98.61:9900";
				if (type == type.TmPreviewImageAlarm) {
					AlarmImagePreview alarmImagePreview = (AlarmImagePreview) previewBase;
					// 告警图片URL
					String serverIP = MVPCommand.SERVER_IP;
					String serverPort = MVPCommand.SERVER_PORT;

					if (alarmImagePreview.height == 0
							|| alarmImagePreview.width == 0) {
						httpUrl = httpUrl + MVPCommand.HTTP + serverIP
								+ serverPort
								+ MVPCommand.ALARM_PICTURE_DOWNLOAD_URL
								+ "?url=" + alarmImagePreview.sFileName;
					} else {
						httpUrl = MVPCommand.HTTP + serverIP + serverPort
								+ MVPCommand.ALARM_PICTURE_DOWNLOAD_URL
								+ "?url=" + alarmImagePreview.sFileName
								+ "&spec=" + alarmImagePreview.width + "x"
								+ alarmImagePreview.height;
					}
				} else if (type == type.TmPreviewLive) {
					// 视频预览图URL
					LivePreview livePreview = (LivePreview) previewBase;
					httpUrl = httpUrl + "/iframe?Type=0?Devid="
							+ livePreview.devId + "&Cameraid="
							+ livePreview.cameraId + "&Streamid="
							+ livePreview.streamId + "";

				} else if (type == type.TmPreviewDevRecord) {
					// 设备录像预览图URL
					DevRecordPreview devRecordPreview = (DevRecordPreview) previewBase;
					httpUrl = httpUrl + "/iframe?Type=0?Devid="
							+ devRecordPreview.devId + "&Cameraid="
							+ devRecordPreview.cameraId
							+ "&Streamid=0&Filename="
							+ devRecordPreview.Filename + "&Timestamp="
							+ devRecordPreview.Timestamp + "";

				} else if (type == type.TmPreviewCloudRecord) {
					// 云录像预览图URL
					CloudRecordPreview cloudRecordPreview = (CloudRecordPreview) previewBase;
					httpUrl = httpUrl + "/iframe?Type=0&Devid="
							+ cloudRecordPreview.devId + "&Cameraid="
							+ cloudRecordPreview.cameraId
							+ "&Streamid=0&Timestamp="
							+ cloudRecordPreview.Timestamp + "&ServerId="
							+ cloudRecordPreview.ServerId + "&Filename="
							+ cloudRecordPreview.recordId;

				}
			}

		} catch (Exception ex) {
			httpUrl = "";
			Log.d(TAG, "makeUrlofType-->exception=" + ex.getMessage());
		}

		return httpUrl;
	}

	/**
	 * 获取实时视频未预览缩略图URL
	 * 
	 * @param devId
	 *            设备号
	 * @param cameraId
	 *            镜头ID
	 * @param Streamid
	 *            流id
	 * @return 实时视频未预览缩略图URL
	 */
	public String getRealURLPictrue(String devId, int cameraId) {

		if (thumbnailServer == null) {
			return "";
		}
		String previewUrl = "http://" + thumbnailServer.host + ":"
				+ thumbnailServer.port + "/iframe?Type=0&Devid=" + devId
				+ "&Cameraid=" + cameraId + "&Streamid=" + 1;
		return previewUrl;
	}

	/**
	 * 获取录像录像拖动时缩略图URL
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param nSeekTime
	 * @return URL
	 */
	public String getPlayRecordURLPictrue(int nSeekTime) {

		if (thumbnailServer == null) {
			return "";
		}
		String httpUrl = "http://" + thumbnailServer.host + ":"
				+ thumbnailServer.port + "";
		if (recordMediaType == PLAY_DEVICE) {
			// 如果操作的是设备录像
			httpUrl = httpUrl + "/iframe?Type=1&Devid="
					+ ProtocalData.mCurrentDevNumber + "&Cameraid="
					+ ProtocalData.mCurrentCameraId + "&Streamid=0&Filename="
					+ this.mRecordId + "&Timestamp=" + nSeekTime + "";

		} else if (recordMediaType == PLAY_CLOUD) {
			// 如果操作的是云录像
			httpUrl = httpUrl + "/iframe?Type=2&Devid="
					+ ProtocalData.mCurrentDevNumber + "&Cameraid="
					+ ProtocalData.mCurrentCameraId + "&Streamid=1&Filename="
					+ this.mRecordId + "&Timestamp=" + nSeekTime + "&ServerId="
					+ this.mServerId;

			LoggerUtil.d("显示代码" + mRecordId);

		}

		return httpUrl;

	}

	/*
	 * 对TMPC类中的回调接口进行实现体注册
	 */
	public void setTMPCPlayerCallback(Object oc) {

		setTmpcPlayerCallbackImpl(oc);
	}

	// public void setTMPCPlayerCallback(Fragment fragment) {
	//
	// setTmpcPlayerCallbackImpl(fragment);
	// }

	private void setTmpcPlayerCallbackImpl(Object callback) {
		player.setOnCompletionListener((callback instanceof OnCompletionListener) ? ((OnCompletionListener) callback)
				: null);
		player.setOnVideoSizeChangedListener((callback instanceof OnVideoSizeChangedListener) ? ((OnVideoSizeChangedListener) callback)
				: null);
		player.setScreenOnWhilePlaying(true);
		player.setOnSeekCompleteListener((callback instanceof OnSeekCompleteListener) ? ((OnSeekCompleteListener) callback)
				: null);
		player.setOnPreparedListener((callback instanceof OnPreparedListener) ? ((OnPreparedListener) callback)
				: null);
		player.setOnRecodeListener((callback instanceof OnRecodeListener) ? ((OnRecodeListener) callback)
				: null);
		playerStateCallback = (callback instanceof IPlayerStateCallback) ? ((IPlayerStateCallback) callback)
				: null;
	}

	/**
	 * 停止下载
	 * 
	 * @param downLoad
	 * @return 1已成功停止 2 停止失败
	 */
	public int stopRecordDownload() {
		int retCode = 0;
		try {

			Log.d(TAG,
					"stopRecordDownload"
							+ String.valueOf(System.currentTimeMillis()));
			if (taskForDownLoadPercent != null) {
				taskForDownLoadPercent.cancel();
				taskForDownLoadPercent = null;
			}

			if (timerGetDownLoadPercent != null) {

				timerGetDownLoadPercent.cancel();
				timerGetDownLoadPercent = null;
			}

			if (mNativeRecord != null) {
				mNativeRecord.stopDownload();// 下载完成，停止
				mNativeRecord = null;
			}

			downLoadFile.stopdownload();

			Log.d(TAG,
					"stopRecordDownload"
							+ String.valueOf(System.currentTimeMillis()));
			retCode = 1;
		} catch (Exception ex) {
			Log.d(TAG, "stopRecordDownload() exception=" + ex.getMessage());
			retCode = -1;
		}

		return retCode;
	}

	/**
	 * 获取设备信息,参数
	 */
	public int getDeviceInfo(String sessionId, String operId,
			CommInfo commInfo, IResultCallback resultCback) throws Exception {
		this.resultCallback = resultCback;
		int retCode = -1;
		ProtocalData.currentSessionId = sessionId;
		ProtocalData.operId = operId;
		CommHeader cmdHeader = commInfo.commHeader;
		ProtocalData.mCameraParamCategoryInfo = "ALL";
		ProtocalEngine protocalEngine = null;

		switch (cmdHeader.nCommand) {
		case DevCommand.GET_DEVICE_INFO:
			// 获取设备信息，包括镜头，码流信息
			// String devIdStr = (String) commInfo.pData;
			// List<String> deviceIdList = new ArrayList<String>();
			// if (devIdStr.length() > 0) {
			//
			// String[] deviceIdArray = devIdStr.split(",");
			//
			// for (int i = 0; i < deviceIdArray.length; i++) {
			//
			// deviceIdList.add(deviceIdArray[i]);
			// }
			// }
			ArrayList<String> devIdStr = (ArrayList<String>) commInfo.pData;
			List<String> deviceIdList = devIdStr;

			LoginInfo loginInfo = new LoginInfo();
			loginInfo.Dev_List = deviceIdList;
			ProtocalData.mLoginInfo = loginInfo;
			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_GET_DEVINFO);

			break;
		case DevCommand.GET_DEVICE_PARAM_IFNO:
			DeviceParam deviceParam = (DeviceParam) commInfo.pData;
			ProtocalData.mCurrentDevNumber = deviceParam.sDeviceID;
			ProtocalData.mCurrentCameraId = String
					.valueOf(deviceParam.mcamerid);
			// 获取设备参数
			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_QUERY_CAMERA_PARAM);
			break;

		case DevCommand.GET_DEVICE_VERSION_INFO:

			// 获取设备软件版本号
			DeviceVersion deviceVersion = (DeviceVersion) commInfo.pData;
			ProtocalData.mCurrentDevNumber = deviceVersion.sDeviceID;
			ProtocalData.mCurrentCameraId = String
					.valueOf(deviceVersion.scamerid);
			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_GET_DEV_VERSOION);
			break;
		case DevCommand.GET_DEVICE_RECORD_PLAN:
		case DevCommand.GET_DEVICE_ALARM_PLAN:

			if (cmdHeader.nCommand == DevCommand.GET_DEVICE_RECORD_PLAN) {
				ProtocalData.mDevPlanCategoryInfo = "RecordPlan";
			} else if (cmdHeader.nCommand == DevCommand.GET_DEVICE_ALARM_PLAN) {
				ProtocalData.mDevPlanCategoryInfo = "AlarmPlan";
			}
			QueryDevicePlan devicePlan = (QueryDevicePlan) commInfo.pData;
			ProtocalData.mCurrentDevNumber = devicePlan.sDeviceID;
			ProtocalData.mCurrentCameraId = String.valueOf(devicePlan.cameraID);
			// 获取设备录像计划，报警计划
			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_GET_DEV_PLAN);

			break;
		// 设备能力查询
		case DevCommand.GET_DEVICE_ABILITY:
			DeviceAbility deviceAbility = (DeviceAbility) commInfo.pData;

			DeviceAbilityInfo deviceAbilityInfo = new DeviceAbilityInfo();
			deviceAbilityInfo.sDeviceID = deviceAbility.sDeviceID;
			ProtocalData.mDeviceAbility = deviceAbilityInfo;

			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_DEVICEABILITY_QUERY);
			break;
		case DevCommand.GET_CLOUD_RECORD_SWITCH:
			// 获取云录像开关

			DeviceSwitchInfo switchInfo = (DeviceSwitchInfo) commInfo.pData;

			DeviceSwitchInfoCfg switchInfoCfg = new DeviceSwitchInfoCfg();
			switchInfoCfg.devID = switchInfo.devID;
			switchInfoCfg.nSwitchType = VcpDataEnum.SwitchType.SWITCH_CLOUDRECORD;// 云录像开关
			ProtocalData.mDeviceSwitchCfg = switchInfoCfg;

			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_CLOUD_RECORDGET_FLAG);

			break;

		case DevCommand.GET_ALARM_SWITCH:
			// 获取报警开关
			if (cmdHeader.nCommand == DevCommand.GET_ALARM_SWITCH) {
				ProtocalData.mDevPlanCategoryInfo = "SwitchInfo";
			}
			DeviceSwitchInfo alarmSwitchInfo = (DeviceSwitchInfo) commInfo.pData;
			ProtocalData.mCurrentDevNumber = alarmSwitchInfo.devID;
			ProtocalData.mCurrentCameraId = String
					.valueOf(alarmSwitchInfo.cameraID);
			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_ALARM_SWITCH_GET);

			break;

		default:
			break;
		}

		return retCode;
	}

	/**
	 * 设置设备参数
	 * 
	 * @param sSessionID
	 * @param sOptID
	 * @param commInfo
	 * @return
	 */
	public int setDeviceInfo(String sSessionID, String sOptID,
			CommInfo commInfo, IResultCallback retCallback) throws Exception {
		this.resultCallback = retCallback;
		int retCode = -1;
		ProtocalData.currentSessionId = sSessionID;
		ProtocalData.operId = sOptID;
		ProtocalEngine protocalEngine = null;
		switch (commInfo.commHeader.nCommand) {
		case DevCommand.SET_DEVICE_PARAM_IFNO_PASSWORD:
			// 设置密码
			CfgDevPwd pwdData = (CfgDevPwd) commInfo.pData;

			ProtocalData.mCurrentDevNumber = pwdData.sDeviceID;
			ProtocalData.mCurrentCameraId = String.valueOf(pwdData.cameraId);
			SecurityInfo securityInfo = new SecurityInfo();
			securityInfo.oldPwd = pwdData.sOldPasswd;
			securityInfo.newPwd = pwdData.sNewPasswd;
			securityInfo.cameraId = pwdData.cameraId;
			// securityInfo.cameraId = pwdData.cameraId == 0 ? 1
			// : pwdData.cameraId;
			// ProtocalData.mCurrentCameraId = String
			// .valueOf(securityInfo.cameraId == 0 ? 1
			// : securityInfo.cameraId);

			ProtocalData.mDeviceSecurityInfo = securityInfo;

			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine
					.sendHttpRequest(MVPCommand.VCP_SET_CAMERA_PARAM_PASSWORD);

			break;

		case DevCommand.SET_DEVICE_PARAM_IFNO_OSD:
			Osdinfo osdinfo = (Osdinfo) commInfo.pData;
			ProtocalData.mCurrentDevNumber = osdinfo.devId;
			ProtocalData.mCurrentCameraId = String.valueOf(osdinfo.cameraId);

			Osddata osddata = new Osddata();
			osddata.devId = osdinfo.devId;
			osddata.cameraId = osdinfo.cameraId;
			osddata.Name = osdinfo.Name;
			osddata.Flag = osdinfo.Flag;

			ProtocalData.osddata = osddata;

			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_SET_CAMERA_PARAM_OSD);

			break;
		case DevCommand.SET_DEVICE_RECORD_PLAN:
		case DevCommand.SET_DEVICE_ALARM_PLAN:

			// 设置设备计划
			DevicePlan devPlan = (DevicePlan) commInfo.pData;
			if (devPlan != null) {
				ProtocalData.mCurrentDevNumber = devPlan.sDeviceID;
				ProtocalData.mCurrentCameraId = String
						.valueOf(devPlan.cameraID == 0 ? 1 : devPlan.cameraID);
			}
			PlanList planList = new PlanList();
			planList.planList = new ArrayList<DevPlan>();

			// planList.type = devPlan.nPlanType == 1 ? "RecordPlan" :
			// "AlarmPlan";
			if (commInfo.commHeader.nCommand == DevCommand.SET_DEVICE_RECORD_PLAN) {

				planList.type = "RecordPlan";
			} else if (commInfo.commHeader.nCommand == DevCommand.SET_DEVICE_ALARM_PLAN) {
				planList.type = "AlarmPlan";
			}

			WeekInfo weekInfo = devPlan.weekPlan;
			DevPlan onePlan = new DevPlan();
			onePlan.pid = weekInfo.nPlanID;
			onePlan.enable = weekInfo.bEnable;
			onePlan.period = weekInfo.sWeekFlag;
			onePlan.cycle = weekInfo.bCycle;

			onePlan.week1 = String.valueOf(weekInfo.sWeek[0]);
			onePlan.week2 = String.valueOf(weekInfo.sWeek[1]);
			onePlan.week3 = String.valueOf(weekInfo.sWeek[2]);
			onePlan.week4 = String.valueOf(weekInfo.sWeek[3]);
			onePlan.week5 = String.valueOf(weekInfo.sWeek[4]);
			onePlan.week6 = String.valueOf(weekInfo.sWeek[5]);
			onePlan.week7 = String.valueOf(weekInfo.sWeek[6]);
			planList.planList.add(onePlan);

			ProtocalData.mDevPlanList = planList;

			// 发送设置网络请求

			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_SET_DEV_PLAN);

			break;

		case DevCommand.SET_CLOUD_RECORD_SWITCH:
			// 中心录像开关
			DeviceSwitchInfo switchInfo = (DeviceSwitchInfo) commInfo.pData;

			DeviceSwitchInfoCfg switchInfoCfg = new DeviceSwitchInfoCfg();
			switchInfoCfg.devID = switchInfo.devID;
			switchInfoCfg.nSwitchFlag = switchInfo.nSwitchFlag;
			switchInfoCfg.nSwitchType = VcpDataEnum.SwitchType.SWITCH_CLOUDRECORD;// 2表示中心录像开关
			switchInfoCfg.cameraID = switchInfo.cameraID == 0 ? 1
					: switchInfo.cameraID;
			ProtocalData.mDeviceSwitchCfg = switchInfoCfg;
			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_CLOUD_RECORDSET_FLAG);

			break;
		case DevCommand.SET_ALARM_SWITCH:
			// 报警开关(一键撤防 布防开关)
			DeviceSwitchInfo alarmSwitchInfo = (DeviceSwitchInfo) commInfo.pData;
			DeviceSwitchInfoCfg alarmSwitchInfoCfg = new DeviceSwitchInfoCfg();
			alarmSwitchInfoCfg.devID = alarmSwitchInfo.devID;
			alarmSwitchInfoCfg.nSwitchFlag = alarmSwitchInfo.nSwitchFlag;
			alarmSwitchInfoCfg.nSwitchType = VcpDataEnum.SwitchType.SWITCH_ALARM;// 1表示设置报警开关
			alarmSwitchInfoCfg.cameraID = alarmSwitchInfo.cameraID;
			ProtocalData.mDeviceSwitchCfg = alarmSwitchInfoCfg;
			protocalEngine = ProtocalEngine.getInstance();
			protocalEngine.addObserver(mProtocalDataCallback);
			protocalEngine.sendHttpRequest(MVPCommand.VCP_ALARM_SWITCH_SET);
			break;

		default:
			break;
		}

		return retCode;

	}

	/**
	 * 取消请求回调
	 */
	public void cancelRequest() {

		if (mProtocalDataCallback != null) {
			mProtocalDataCallback.isCancel(true);
		}
	}

	/*
	 * 处理网络请求后得到数据 的回调函数
	 */
	private class ProtocalDataCallback implements IProtocalInterface {

		private boolean isCancel = false;

		@Override
		public void onProtocalNotifycation(int cmd, int errcode) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onProtocalNotifycation isCancel=" + isCancel);
			if (isCancel) {
				return;
			}
			Log.d(TAG, "onProtocalNotifycation cmd=" + cmd + ";errcode="
					+ errcode);
			switch (cmd) {

			case MVPCommand.VCP_SET_SERVERINFO:
				if (resultCallback != null) {

					resultCallback.ResultCallbackFun(
							OptResultType.Result_SetServerInfo, 1, null, 0);
				}
				break;
			case MVPCommand.VCP_GET_DEVINFO:
				// 获取设备信息
				if (errcode == 200 && ProtocalData.mDeviceList != null) {

					// 取结果后封装成对外的数据结构
					List<DeviceInfo> deviceInfoList = fillDevicesInfo(ProtocalData.mDeviceList);
					if (deviceInfoList != null) {
						CommHeader header = new CommHeader(
								DevCommand.GET_DEVICE_INFO);
						CommInfo commInfo = new CommInfo(header, deviceInfoList);
						resultCallback.ResultCallbackFun(
								OptResultType.Result_GetDeviceInfo, 1,
								commInfo, 0);

						// 保存获取到的视频预览缩略图需要的服务器信息

						thumbnailServer = ProtocalData.mThumbnailServer;

						if (isdebug) {
							thumbnailServer = TranIpAndPort
									.testtranip(ProtocalData.mThumbnailServer);
						}

					} else {
						resultCallback
								.ResultCallbackFun(
										OptResultType.Result_GetDeviceInfo, -1,
										null, 0);
					}

				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_GetDeviceInfo, -1, null, 0);
				}

				break;

			case MVPCommand.VCP_MEDIA_PLAY:
				// 如果是获取视频播放IP 和PORT
				if (errcode == 0 || errcode == 200) {
					// 拼接URL
					String url = constractMediaUrl(PLAY_TYPE_MONITOR);
					if (url != null && !url.equals("")) {
						try {
							if (player != null) {
								player.setDataSource(url);
								player.start();
							} else {
								playerStateCallback.onPlayerStateCallback(
										TmPlayerStatus.TmPlayerError,
										TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
							}

						} catch (OperationException e) {
							// TODO: handle exception
							playerStateCallback.onPlayerStateCallback(
									TmPlayerStatus.TmPlayerError,
									TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							playerStateCallback.onPlayerStateCallback(
									TmPlayerStatus.TmPlayerError,
									TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
							e.printStackTrace();
						}
					} else {
						playerStateCallback.onPlayerStateCallback(
								TmPlayerStatus.TmPlayerError,
								TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
					}

				} else {
					playerStateCallback.onPlayerStateCallback(
							TmPlayerStatus.TmPlayerError,
							TMPCPlayer.TMPC_NO_HTTP_URL_FAILED, null);

				}
				break;
			case MVPCommand.VCP_MEDIA_AUTH:
				// 云台鉴权
				if (ProtocalData.Haspivilege) {
					cameraPtzControl();
				} else {
					if (resultCallback != null) {
						resultCallback.ResultCallbackFun(
								OptResultType.Result_PtzControl, -1, null, 0);
					}
				}
				break;
			case MVPCommand.VCP_CAMERA_CTRL:
				// 云台控制
				if (errcode == 200) {
					// 操作成功
					if (resultCallback != null) {
						resultCallback.ResultCallbackFun(
								OptResultType.Result_PtzControl, 1, null, 0);
					}
				} else {
					// 操作失败
					if (resultCallback != null) {
						resultCallback.ResultCallbackFun(
								OptResultType.Result_PtzControl, -1, null, 0);
					}
				}
				break;
			case MVPCommand.VCP_PRE_SET:
				// 预设位操作
				if (errcode == 0 || errcode == 200) {
					// 操作成功

					if (resultCallback != null) {
						resultCallback.ResultCallbackFun(
								OptResultType.Result_PresetControl, 1, null, 0);
					}
				} else {
					// 操作失败
					if (resultCallback != null) {
						resultCallback
								.ResultCallbackFun(
										OptResultType.Result_PresetControl, -1,
										null, 0);
					}
				}
				break;
			case MVPCommand.VCP_QUERY_RECORD:
			case MVPCommand.VCP_GET_CLOUDRECORD:
				// 获取设备录像,云录像,所有录像
				List<RecordInfo> recordInfoList = ProtocalData.mQueryRecord.RecordInfos;
				try {
					// 将结果赋予RecordResult
					if (recordInfoList != null) {
						if (ProtocalData.mQueryRecord != null) {

							CommHeader cmdHeader = new CommHeader(
									OptResultType.Result_FindRecordInfo);
							List<RecordResult> recordResultList = fillRecordResultList(recordInfoList);
							if (recordResultList != null) {
								CommInfo commData = new CommInfo(cmdHeader,
										recordResultList);
								if (resultCallback != null) {
									// // 1表示成功
									resultCallback
											.ResultCallbackFun(
													OptResultType.Result_FindRecordInfo,
													1, commData, 0);
								}
							} else {
								resultCallback.ResultCallbackFun(
										OptResultType.Result_FindRecordInfo,
										-1, null, 0);
							}

						}
					} else {
						// 获取数据失败
						resultCallback.ResultCallbackFun(
								OptResultType.Result_FindRecordInfo, -1, null,
								0);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.d(TAG,
							"onProtocalNotifycation-->MVPCommand.VCP_QUERY_RECORD exception:"
									+ e.getMessage());
					if (resultCallback != null) {
						resultCallback.ResultCallbackFun(
								OptResultType.Result_FindRecordInfo, -1, null,
								0);
					}
				}

				break;

			case MVPCommand.VCP_MEDIA_PLAY_RECORD:
				// 播放设备录像

				if (errcode == 200) {
					// 拼接URL
					// String url2 = constractMediaUrl(PLAY_TYPE_RECORDFILE);
					String url2 = constractMediaUrl(2);
					// LoggerUtil.d("计划录像设备录像播放地址=" + url2);
					if (url2 != null && !url2.equals("")) {
						Log.d(TAG,
								"onProtocalNotifycation-->MVPCommand.VCP_MEDIA_PLAY_RECORD-->url2="
										+ url2);
						Log.d(TAG,
								"onProtocalNotifycation-->MVPCommand.VCP_MEDIA_PLAY_RECORD-->player is null="
										+ String.valueOf(player == null));
						try {
							if (player != null) {
								player.setDataSource(url2);
								player.start(recordCurrentPlayDuration);
							} else {
								playerStateCallback.onPlayerStateCallback(
										TmPlayerStatus.TmPlayerError,
										TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
							}

						} catch (OperationException e) {
							// TODO: handle exception
							playerStateCallback.onPlayerStateCallback(
									TmPlayerStatus.TmPlayerError,
									TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							playerStateCallback.onPlayerStateCallback(
									TmPlayerStatus.TmPlayerError,
									TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
							e.printStackTrace();
						}
					} else {
						playerStateCallback.onPlayerStateCallback(
								TmPlayerStatus.TmPlayerError,
								TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
					}

				} else {
					playerStateCallback.onPlayerStateCallback(
							TmPlayerStatus.TmPlayerError,
							TMPCPlayer.TMPC_NO_HTTP_URL_FAILED, null);
				}
				break;

			case MVPCommand.VCP_MEDIA_PLAY_CLOUD_RECORD:
				// 播放中心录像
				if (errcode == 200) {
					// 拼接URL
					if (ProtocalData.mCloudRecordPlay != null) {
						ProtocalData.mCurStream = new Stream();
						ProtocalData.mCurStream.ip = ProtocalData.mCloudRecordPlay.ServerIP;
						if (ProtocalData.mCloudRecordPlay.ServerPort != null) {
							ProtocalData.mCurStream.port = Integer
									.parseInt(ProtocalData.mCloudRecordPlay.ServerPort);
						}

					}
					// String url2 = constractMediaUrl(PLAY_TYPE_RECORDFILE);

					String url2 = constractMediaUrl(2);

					// LoggerUtil.d("计划录像中心录像播放地址=" + url2);
					if (url2 != null && !url2.equals("")) {
						Log.d(TAG,
								"onProtocalNotifycation-->MVPCommand.VCP_MEDIA_PLAY_RECORD-->url2="
										+ url2);
						try {
							Log.d(TAG,
									"onProtocalNotifycation-->MVPCommand.VCP_MEDIA_PLAY_RECORD-->player is null="
											+ String.valueOf(player == null));
							if (player != null) {
								player.setDataSource(url2);
								player.start(recordCurrentPlayDuration);
							} else {
								playerStateCallback.onPlayerStateCallback(
										TmPlayerStatus.TmPlayerError,
										TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						playerStateCallback.onPlayerStateCallback(
								TmPlayerStatus.TmPlayerError,
								TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
					}

				} else {
					playerStateCallback.onPlayerStateCallback(
							TmPlayerStatus.TmPlayerError,
							TMPCPlayer.TMPC_NO_HTTP_URL_FAILED, null);
				}
				break;
			case MVPCommand.VCP_QUERY_CAMERA_PARAM:
				// 获取设备参数
				if (errcode == 200) {

					CameraParam param = ProtocalData.mCameraParam;
					// 将设备参数 封装成DeviceParam返回
					CameraOSDInfo osdInfo = null;
					if (null != param) {
						osdInfo = param.cameraOsdInfo;
					} else {
						osdInfo = null;
					}

					if (osdInfo != null) {
						DeviceParam deviceParam = new DeviceParam();
						deviceParam.nOSD = osdInfo.Flag;
						deviceParam.sDeviceName = osdInfo.Name;
						if (resultCallback != null) {
							CommHeader cmdHeader = new CommHeader(
									DevCommand.GET_DEVICE_PARAM_IFNO);
							Object data = deviceParam;
							CommInfo commInfo = new CommInfo(cmdHeader, data);

							resultCallback.ResultCallbackFun(
									OptResultType.Result_GetDeviceInfo, 1,
									commInfo, 0);
						}

					} else {
						resultCallback
								.ResultCallbackFun(
										OptResultType.Result_GetDeviceInfo, -1,
										null, 0);
					}

				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_GetDeviceInfo, -1, null, 0);
				}
				break;
			case MVPCommand.VCP_GET_DEV_VERSOION:
				// 获取设备软件版本号
				if (errcode == 200) {

					CameraParam param = ProtocalData.mCameraParam;
					// 将设备版本号 封装成DeviceVersion返回

					MyCamera baseInfo = param.cameraBaseInfo;
					if (baseInfo != null) {
						DeviceVersion deviceVersion = new DeviceVersion();
						deviceVersion.sSoftVersion = baseInfo.Ver;

						if (resultCallback != null) {
							CommHeader cmdHeader = new CommHeader(
									DevCommand.GET_DEVICE_VERSION_INFO);
							Object data = deviceVersion;
							CommInfo commInfo = new CommInfo(cmdHeader, data);

							resultCallback.ResultCallbackFun(
									OptResultType.Result_GetDeviceInfo, 1,
									commInfo, 0);
						}

					} else {
						resultCallback
								.ResultCallbackFun(
										OptResultType.Result_GetDeviceInfo, -1,
										null, 0);
					}

				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_GetDeviceInfo, -1, null, 0);
				}
				break;
			case MVPCommand.VCP_SET_CAMERA_PARAM_PASSWORD:
				// 设置设备参数
				if (errcode == 0 || errcode == 200) {

					// 设置成功
					if (resultCallback != null) {
						resultCallback.ResultCallbackFun(
								OptResultType.Result_SetDeviceInfo, 1, null, 0);
					}
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_SetDeviceInfo, -1, null, 0);
				}
				break;

			case MVPCommand.VCP_SET_CAMERA_PARAM_OSD:
				// 设置设备参数
				if (errcode == 0 || errcode == 200) {

					// 设置成功
					if (resultCallback != null) {
						resultCallback.ResultCallbackFun(
								OptResultType.Result_SetDeviceInfo, 1, null, 0);
					}
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_SetDeviceInfo, -1, null, 0);
				}
				break;
			case MVPCommand.VCP_GET_DEV_PLAN:
				// 获取设备录像/告警计划

				if (errcode == 200) {

					PlanList devPlanList = ProtocalData.mDevPlanList;
					if (devPlanList != null) {
						CommHeader cmdHeader = new CommHeader();
						if (devPlanList.type.equals("AlarmPlan")) {
							cmdHeader.nCommand = DevCommand.GET_DEVICE_ALARM_PLAN;
						} else if (devPlanList.type.equals("RecordPlan")) {
							cmdHeader.nCommand = DevCommand.GET_DEVICE_RECORD_PLAN;
						}
						List<WeekInfo> devWeekPlanList = fillDevPlanList(devPlanList);

						// for (int i = 0; i < devWeekPlanList.size(); i++) {
						// for (int j = 0; j <
						// devWeekPlanList.get(i).sWeek.length; j++) {
						// // //LoggerUtil.d("显示DEMO---j"
						// // + j
						// // + "--"
						// // + new String(devWeekPlanList
						// // .get(i).sWeek[j]));
						// }
						// }

						CommInfo comInfo = new CommInfo(cmdHeader,
								devWeekPlanList);

						resultCallback.ResultCallbackFun(
								OptResultType.Result_GetDeviceInfo, 1, comInfo,
								0);

					} else {
						resultCallback
								.ResultCallbackFun(
										OptResultType.Result_GetDeviceInfo, -1,
										null, 0);
					}
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_GetDeviceInfo, -1, null, 0);
				}
				break;
			case MVPCommand.VCP_SET_DEV_PLAN:
				// 设置设备计划
				if (errcode == 200) {
					// 设置成功
					resultCallback.ResultCallbackFun(
							OptResultType.Result_SetDeviceInfo, 1, null, 0);
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_SetDeviceInfo, -1, null, 0);
				}
				break;
			case MVPCommand.VCP_GET_SOUNDTALK_SERVER:
				// 获取语音对讲需要的服务器信息
				if (errcode == 200
						&& (ProtocalData.mSoundTalkServer.StreamIP != null && !ProtocalData.mSoundTalkServer.StreamIP
								.equals(""))) {
					startTalk();
				} else {
					Message msg = mHandler.obtainMessage();
					msg.what = TalkVoiceStatus.TM_AUDIOTALK_TRANSFER_CONNECT_ERROR;
					mHandler.sendMessage(msg);
				}
				break;

			case MVPCommand.VCP_GET_RECORD_DOWNLOAD_SERVER:
				// 获取录像下载需要的服务器信息
				if (errcode == 200
						&& (ProtocalData.mRecordDownLoad.StreamIP != null && !ProtocalData.mRecordDownLoad.StreamIP
								.equals(""))) {
					startRecordDownLoad();
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_StartRecordFileDownload,
							DownLoadStatus.TM_RECORD_DOWNLOAD_FAILED, null,
							DownLoadStatus.TM_RECORD_DOWNLOAD_FAILED);
				}
				break;
			case MVPCommand.VCP_CLOUD_RECORD_DEL:
				// 云录像删除
				if (errcode == 200) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_DelRecord, 1, null, 0);
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_DelRecord, -1, null, 0);
				}
				break;
			case MVPCommand.VCP_RECORD_DEL:
				// 设备录像删除
				if (errcode == 200) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_DelRecord, 1, null, 0);
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_DelRecord, -1, null, 0);
				}
				break;

			case MVPCommand.VCP_CLOUD_RECORDSET_FLAG:
				// 中心录像开关设置
				if (errcode == 200) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_SetDeviceInfo, 1, null, 0);
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_SetDeviceInfo, -1, null, 0);
				}
				break;

			case MVPCommand.VCP_CLOUD_RECORDGET_FLAG:
				// 中心录像开关获取
				if (errcode == 200 && ProtocalData.mDeviceSwitchCfg != null) {
					DeviceSwitchInfo cloudSwitchCfg = new DeviceSwitchInfo();
					cloudSwitchCfg.devID = ProtocalData.mDeviceSwitchCfg.devID;
					cloudSwitchCfg.nSwitchFlag = ProtocalData.mDeviceSwitchCfg.nSwitchFlag;
					cloudSwitchCfg.nSwitchType = VcpDataEnum.SwitchType.SWITCH_CLOUDRECORD;

					CommHeader header = new CommHeader(
							DevCommand.GET_CLOUD_RECORD_SWITCH);

					CommInfo commInfo = new CommInfo(header, cloudSwitchCfg);
					resultCallback.ResultCallbackFun(
							OptResultType.Result_GetDeviceInfo, 1, commInfo, 0);
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_GetDeviceInfo, -1, null, 0);
				}
				break;
			case MVPCommand.VCP_DEVICEABILITY_QUERY:
				// 设备能力查询
				if (errcode == 200 && ProtocalData.mDeviceAbility != null) {

					DeviceAbility deviceAbility = constractDeviceAbilityData(ProtocalData.mDeviceAbility);

					CommHeader header = new CommHeader();
					header.nCommand = DevCommand.GET_DEVICE_ABILITY;
					CommInfo commInfo = new CommInfo(header, deviceAbility);
					resultCallback.ResultCallbackFun(
							OptResultType.Result_GetDeviceInfo, 1, commInfo, 0);
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_GetDeviceInfo, -1, null, 0);
				}
				break;

			case MVPCommand.VCP_ALARM_SWITCH_SET:
				// 报警开关设置
				if (errcode == 200) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_SetDeviceInfo, 1, null, 0);
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_SetDeviceInfo, -1, null, 0);
				}
				break;
			case MVPCommand.VCP_ALARM_SWITCH_GET:
				// 报警开关查询
				if (errcode == 200 && ProtocalData.mCameraParam != null) {

					CameraSwitchInfo switchInfo = ProtocalData.mCameraParam.cameraSwitchInfo;
					if (switchInfo != null) {

						DeviceSwitchInfo deviceSwitchInfo = new DeviceSwitchInfo();
						deviceSwitchInfo.nSwitchType = VcpDataEnum.SwitchType.SWITCH_ALARM;// 1为报警开关
						deviceSwitchInfo.nSwitchFlag = switchInfo.Guard;

						CommHeader header = new CommHeader(
								DevCommand.GET_ALARM_SWITCH);
						CommInfo commInfo = new CommInfo(header,
								deviceSwitchInfo);

						resultCallback.ResultCallbackFun(
								OptResultType.Result_GetDeviceInfo, 1,
								commInfo, 0);

					} else {
						resultCallback
								.ResultCallbackFun(
										OptResultType.Result_GetDeviceInfo, -1,
										null, 0);
					}

				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_GetDeviceInfo, -1, null, 0);
				}
				break;

			case MVPCommand.VCP_QUERY_DEVICE_ALARM_FILE:
			case MVPCommand.VCP_QUERY_CLOUD_ALARM_FILE:
				// 设备上告警文件查询

				if (errcode == 200) {
					List<RecordInfo> alarmRecordList = ProtocalData.mQueryRecord.RecordInfos;
					if (alarmRecordList != null) {

						List<AlarmFileURLResult> alarmFileList = constractAlarmFileResult(alarmRecordList);
						if (alarmFileList != null) {

							CommHeader header = new CommHeader(
									OptResultType.Result_FindAlarmFileInfo);
							CommInfo commInfo = new CommInfo(header,
									alarmFileList);
							resultCallback.ResultCallbackFun(
									OptResultType.Result_FindAlarmFileInfo, 1,
									commInfo, 0);

						} else {
							resultCallback.ResultCallbackFun(
									OptResultType.Result_FindAlarmFileInfo, -1,
									null, 0);
						}

					} else {
						CommHeader header = new CommHeader(
								OptResultType.Result_FindAlarmFileInfo);
						CommInfo commInfo = new CommInfo(header,
								new ArrayList<AlarmFileURLResult>());
						resultCallback.ResultCallbackFun(
								OptResultType.Result_FindAlarmFileInfo, 1,
								commInfo, 0);
					}
				} else {
					resultCallback
							.ResultCallbackFun(
									OptResultType.Result_FindAlarmFileInfo, -1,
									null, 0);
				}

				break;

			case MVPCommand.VCP_QUERY_ALARM_FILE_BY_ID:
				// 按报警id

				if (errcode == 200) {
					List<AlarmGuardEventInfo> alarmGuardEventInfos = ProtocalData.mAlarmAttachList;

					String httpUrlHeader = "http://" + MVPCommand.SERVER_IP
							+ "" + MVPCommand.SERVER_PORT + "/spi/image?url=";

					if (null != alarmGuardEventInfos) {
						List<AlarmFileURLResult> alarmFileList = new ArrayList<AlarmFileURLResult>();

						for (int i = 0; i < alarmGuardEventInfos.size(); i++) {
							AlarmFileURLResult alarmFileURLResult = new AlarmFileURLResult();
							alarmFileURLResult.nFileType = alarmGuardEventInfos
									.get(i).Type;
							if (alarmFileURLResult.nFileType == 1) {
								alarmFileURLResult.sPictureURL = httpUrlHeader
										+ alarmGuardEventInfos.get(i).AttchURL;
							} else if (alarmFileURLResult.nFileType == 2) {
								alarmFileURLResult.sRecordURL = httpUrlHeader
										+ alarmGuardEventInfos.get(i).AttchURL;
							}
							alarmFileURLResult.sBeginTime = alarmGuardEventInfos
									.get(i).BeginTime;
							alarmFileURLResult.nLocation = 2;
							alarmFileList.add(alarmFileURLResult);

						}

						CommHeader header = new CommHeader(
								OptResultType.Result_FindAlarmFileInfo);
						CommInfo commInfo = new CommInfo(header, alarmFileList);
						resultCallback.ResultCallbackFun(
								OptResultType.Result_FindAlarmFileInfo, 1,
								commInfo, 0);

					} else {
						// 无数据返回
						CommHeader header = new CommHeader(
								OptResultType.Result_FindAlarmFileInfo);
						CommInfo commInfo = new CommInfo(header,
								new ArrayList<AlarmFileURLResult>());
						resultCallback.ResultCallbackFun(
								OptResultType.Result_FindAlarmFileInfo, 1,
								commInfo, 0);
					}

				} else {
					resultCallback
							.ResultCallbackFun(
									OptResultType.Result_FindAlarmFileInfo, -1,
									null, 0);
				}

				break;

			case MVPCommand.VCP_GET_SERVER_INFO:
				// // 调试
				// errcode = 200;
				if (errcode == 0 || errcode == 200) {
					// 拼接URL
					String url = getp2pPlayurl(isp2plive);
					if (url != null && !url.equals("")) {
						try {
							if (player != null) {

								// player.setDataSource("tp2p://192.168.7.104:7000/27/live/cam_"
								// + ProtocalData.mCurrentCameraId
								// + "_"
								// + ProtocalData.mCurrentStreamId
								// + "?selfid=" + ProtocalData.client_id);
								player.setDataSource(url);
								player.start();
							} else {
								playerStateCallback.onPlayerStateCallback(
										TmPlayerStatus.TmPlayerError,
										TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
							}

						} catch (OperationException e) {
							// TODO: handle exception
							playerStateCallback.onPlayerStateCallback(
									TmPlayerStatus.TmPlayerError,
									TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							playerStateCallback.onPlayerStateCallback(
									TmPlayerStatus.TmPlayerError,
									TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
							e.printStackTrace();
						}
					} else {
						playerStateCallback.onPlayerStateCallback(
								TmPlayerStatus.TmPlayerError,
								TMPCPlayer.TMPC_NO_PLAY_OBJECT, null);
					}

				} else {

					// LoggerUtil.d("P2P错误信息" + "P2PHttp请求Code=" + errcode
					// + "Sdk_nConnType" + Sdk_nConnType);

					if (Sdk_nConnType == PLAY_MODEL_AUTO_CHOOSE) {

						startRtsLivePlay(mplaySaveAutoInfo.sSessionID,
								mplaySaveAutoInfo.sOptID,
								mplaySaveAutoInfo.sDevID,
								mplaySaveAutoInfo.nCameraID,
								mplaySaveAutoInfo.nStreamType,
								mplaySaveAutoInfo.mplayerStateCallback);
						Sdk_nConnType = PLAY_MODEL_RTSP_TRANSIT;

						// LoggerUtil.d("P2P错误信息" + "进入中转模式Sdk_nConnType"
						// + Sdk_nConnType);

					} else if (Sdk_nConnType == PLAY_MODEL_P2P) {
						playerStateCallback.onPlayerStateCallback(
								TmPlayerStatus.TmPlayerError,
								TMPCPlayer.TMPC_NO_P2P_HTTP_URL_FAILED, null);
					}

				}
				break;

			case MVPCommand.VCP_RESTART_DEVICE:
				if (errcode == 0 || errcode == 200) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_ReStartDevice, 1, null, 0);
				} else {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_ReStartDevice, -1, null, 0);
				}
				break;

			default:
				break;
			}
		}

		@Override
		public void isCancel(boolean isCancel) {
			// TODO Auto-generated method stub
			this.isCancel = isCancel;

		}
	};

	/**
	 * 播放视频
	 * 
	 * @param url
	 */
	private void startPlay(String url, int operType) {
		if (player != null) {
			try {
				player.setDataSource(url);
				player.start();
				if (resultCallback != null) {
					// 1标示启动播放成功
					resultCallback.ResultCallbackFun(operType, 1, null, 0);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				if (resultCallback != null) {

					// -1标示启动播放失败
					resultCallback.ResultCallbackFun(operType, -1, null, 0);
				}
			}
		} else {
			// -1标示启动播放失败
			resultCallback.ResultCallbackFun(operType, -1, null, 0);
		}
	}

	/**
	 * 云台控制
	 * 
	 * @author Administrator
	 * 
	 */
	private void cameraPtzControl() {
		ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		mProtocalDataCallback.isCancel = false;
		protocalEngine.addObserver(mProtocalDataCallback);
		protocalEngine.sendHttpRequest(MVPCommand.VCP_CAMERA_CTRL);
	}

	/**
	 * 封装设备信息，码流，镜头数据
	 */
	private List<DeviceInfo> fillDevicesInfo(List<Device> devices) {

		List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();
		for (int i = 0; i < devices.size(); i++) {
			Device device = devices.get(i);
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.sDeviceID = device.DeviceId;
			deviceInfo.status = Integer.parseInt(device.RunStatus);
			deviceInfo.type = device.Type;
			deviceInfo.p2pID = device.P2PId;

			if (device.cameraList != null) {
				List<CameraInfo> cameraInfoList = new ArrayList<CameraInfo>();
				for (MyCamera camera : device.cameraList) {

					CameraInfo cameraInfo = new CameraInfo();
					cameraInfo.cameraId = Integer.parseInt(camera.CameraId);
					cameraInfo.PTZType = camera.PTZType;
					cameraInfoList.add(cameraInfo);

					DevicStreamInfo streamInfo = new DevicStreamInfo();
					streamInfo.sDeviceID = device.DeviceId;
					for (int k = 0; k < camera.Stream_List.size(); k++) {

						Stream stream = camera.Stream_List.get(k);
						if (stream.ID.equals("0")) {
							streamInfo.nStreamMain = 1;
						}
						if (stream.ID.equals("1")) {
							streamInfo.nSubStream1 = 1;
						}
						if (stream.ID.equals("2")) {
							streamInfo.nSubStream2 = 1;
						}
						if (stream.ID.equals("3")) {
							streamInfo.nSubStream3 = 1;
						}

					}
					cameraInfo.devicStreamInfo = streamInfo;

				}
				deviceInfo.cameraInfoList = cameraInfoList;

			}

			deviceList.add(deviceInfo);

		}
		return deviceList;
	}

	/**
	 * 填充录像记录集合
	 * 
	 * @author Administrator
	 * 
	 */
	private List<RecordResult> fillRecordResultList(
			List<RecordInfo> recordInfoList) {

		List<RecordResult> resultList = new ArrayList<RecordResult>();

		for (int i = 0; i < recordInfoList.size(); i++) {
			RecordResult resultObj = new RecordResult();
			RecordInfo recordInfo = recordInfoList.get(i);

			resultObj.nRecordFrom = recordInfo.Location;
			int recordType = 0;
			if (recordInfo.recordType != null) {
				recordType = Integer.parseInt(recordInfo.recordType);
			}
			resultObj.nRecordType = recordType;

			resultObj.sFileName = recordInfo.ContentId;
			resultObj.sRecordID = recordInfo.ContentId;

			resultObj.nSize = recordInfo.ContentSize;

			resultObj.sBeginTime = recordInfo.BeginTime;
			resultObj.sEndTime = recordInfo.EndTime;

			resultObj.sServerID = recordInfo.ServerId;

			resultList.add(resultObj);
		}
		return resultList;

	}

	/**
	 * 填充录像时间段片段集合
	 * 
	 * @author Administrator
	 * 
	 */
	private RecordTimeResultInfo fillRecordTimeResultList(
			List<RecordInfo> recordInfoList) {

		RecordTimeResultInfo fileList = new RecordTimeResultInfo();
		fileList.arryRedInfo = new ArrayList<RecordTimeResult>();
		for (int i = 0; i < recordInfoList.size(); i++) {
			RecordTimeResult recordTimeObj = new RecordTimeResult();
			RecordInfo recordInfo = recordInfoList.get(i);
			int recordType = 0;
			if (recordInfo.recordType != null) {
				recordType = Integer.parseInt(recordInfo.recordType);
			}
			recordTimeObj.nRecordType = recordType;
			recordTimeObj.sBeginTime = recordInfo.BeginTime;
			recordTimeObj.sEndTime = recordInfo.EndTime;

			recordTimeObj.serverId = recordInfo.ServerId;
			recordTimeObj.recordId = recordInfo.ContentId;

			fileList.arryRedInfo.add(recordTimeObj);
		}
		return fileList;

	}

	/**
	 * 填充设备计划集合
	 */
	public List<WeekInfo> fillDevPlanList(PlanList planList) {

		List<WeekInfo> devWeekPlanList = new ArrayList<WeekInfo>();
		if (planList != null) {

			for (int i = 0; i < planList.planList.size(); i++) {
				char[][] sWeek = new char[7][24];
				DevPlan plan = planList.planList.get(i);
				WeekInfo weekPlan = new WeekInfo();
				weekPlan.nPlanID = plan.pid;
				weekPlan.bEnable = plan.enable;
				weekPlan.bCycle = plan.cycle;
				weekPlan.sWeekFlag = plan.period;

				if (plan.week1 != null) {
					sWeek[0] = plan.week1.toCharArray();
				}

				if (plan.week2 != null) {
					sWeek[1] = plan.week2.toCharArray();
				}
				if (plan.week3 != null) {
					sWeek[2] = plan.week3.toCharArray();
				}
				if (plan.week4 != null) {
					sWeek[3] = plan.week4.toCharArray();
				}
				if (plan.week5 != null) {
					sWeek[4] = plan.week5.toCharArray();
				}
				if (plan.week6 != null) {
					sWeek[5] = plan.week6.toCharArray();
				}
				if (plan.week7 != null) {
					sWeek[6] = plan.week7.toCharArray();
				}

				// ///////////////////////////////
				// char[] week1 = plan.week1 != null ? plan.week1.toCharArray()
				// : null;
				// char[] week2 = plan.week2 != null ? plan.week2.toCharArray()
				// : null;
				// char[] week3 = plan.week3 != null ? plan.week3.toCharArray()
				// : null;
				// char[] week4 = plan.week4 != null ? plan.week4.toCharArray()
				// : null;
				// char[] week5 = plan.week5 != null ? plan.week5.toCharArray()
				// : null;
				// char[] week6 = plan.week6 != null ? plan.week6.toCharArray()
				// : null;
				// char[] week7 = plan.week7 != null ? plan.week7.toCharArray()
				// : null;
				//
				// for (int k = 0; k < weekPlan.sWeekFlag.length(); k++) {
				// switch (k) {
				// case 0:
				// sWeek[0] = week1;
				// LoggerUtil.d("进入列表信息报警信息列数i" + i + "------week1="
				// + new String(sWeek[k]) + "-----显示k=" + k);
				// break;
				// case 1:
				// sWeek[1] = week2;
				// LoggerUtil.d("进入列表信息报警信息列数i" + i + "------week2="
				// + new String(sWeek[k]) + "显示k=" + k);
				// break;
				// case 2:
				// sWeek[2] = week3;
				// LoggerUtil.d("进入列表信息报警信息列数i" + i + "------week3="
				// + new String(sWeek[k]) + "显示k=" + k);
				// break;
				// case 3:
				// sWeek[3] = week4;
				// LoggerUtil.d("进入列表信息报警信息列数i" + i + "------week4="
				// + new String(sWeek[k]) + "显示k=" + k);
				// break;
				// case 4:
				// sWeek[4] = week5;
				// LoggerUtil.d("进入列表信息报警信息列数i" + i + "------week5="
				// + new String(sWeek[k]) + "显示k=" + k);
				// break;
				// case 5:
				// sWeek[5] = week6;
				// LoggerUtil.d("进入列表信息报警信息列数i" + i + "------week6="
				// + new String(sWeek[k]) + "显示k=" + k);
				// break;
				// case 6:
				// sWeek[6] = week7;
				// LoggerUtil.d("进入列表信息报警信息列数i" + i + "------week7="
				// + new String(sWeek[k]) + "显示k=" + k);
				//
				// break;
				//
				// default:
				// break;
				// }
				//
				// }
				weekPlan.sWeek = sWeek;

				devWeekPlanList.add(weekPlan);

			}
		}
		return devWeekPlanList;
	}

	/**
	 * 
	 * @param type
	 *            播放类型 1 实时视频 2播录像
	 * @return
	 */
	public String constractMediaUrl(int type) {
		String playUrl = "";
		long phoneTime = System.currentTimeMillis() / 1000;// 当前手机时间
		Log.i("", "mTimeOffset:" + ProtocalData.mTimeOffset);
		long calibrateTime = phoneTime - ProtocalData.mTimeOffset;// 校准后的时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 时间格式
		Date dt = new Date(calibrateTime * 1000);// 按照时间格式生成字符串
		String ts = sdf.format(dt);
		String ip = ProtocalData.mCurStream.ip;
		int port = ProtocalData.mCurStream.port;

		if (isdebug) {
			Stream stream = TranIpAndPort.testtranip(ProtocalData.mCurStream);
			ip = stream.ip;
			port = stream.port;

			if (recordMediaType == PLAY_CLOUD && type == 2) {

				CloudRecordPlay cloudRecordPlay = TranIpAndPort
						.testtranip(ProtocalData.mCloudRecordPlay);
				ip = cloudRecordPlay.ServerIP;
				port = Integer.valueOf(cloudRecordPlay.ServerPort);
			}

		}

		String content_url = "";
		if (type == 1) {

			content_url = "/" + ProtocalData.mCurrentDevNumber + "_"
					+ ProtocalData.mCurrentCameraId + "?stype=live&streamid="
					+ ProtocalData.mCurrentStreamId + "&sid=" + "" + "&ts="
					+ ts;

		} else if (type == 2) {

			if (playType == PLAY_TYPE_RECORDFILE) {
				// 播放录像文件
				content_url = "/" + ProtocalData.mCurrentDevNumber + "_"
						+ ProtocalData.mCurrentCameraId + "?stype=vodfile&cid="
						+ ProtocalData.currentRecordFile.ContentId + "&sid="
						+ ProtocalData.mDevSessionId + "&ts=" + ts;
			} else {

				// 按时间段范围播放

				content_url = "/"
						+ ProtocalData.mCurrentDevNumber
						+ "_"
						+ ProtocalData.mCurrentCameraId
						+ "?stype=vodtime&cid="
						+ ProtocalData.currentRecordFile.BeginTime.replace(" ",
								"_")
						+ "_"
						+ ProtocalData.currentRecordFile.EndTime.replace(" ",
								"_") + "&sid=" + ProtocalData.mDevSessionId
						+ "&ts=" + ts;
				// LoggerUtil.d("计划录像拼装=" + content_url);

			}

		}
		String nonce = "";
		// md5加密
		String md5auth = "";
		try {
			md5auth = AESForC.getMD5String(content_url + "&nonce=" + nonce);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (isTempUrl) {
			playUrl = "rtsp://120.197.98.54:554/bq_rtsp_240_xdcxj_cs1.mp4";
		} else {
			// 最终url
			playUrl = "rtsp://" + ip + ":" + port + content_url + "&auth="
					+ md5auth;

			// LoggerUtil.d("计划录像拼装最终=" + playUrl);
		}

		// "rtsp://192.168.3.81:554/gq_rtsp_400_xdcxj_cs1.mp4";//
		// rtsp://192.168.3.81:554/bq_rtsp_240_yqzgdnh_cs1.mp4
		// rtsp://120.197.98.54:554/gq_rtsp_400_xdcxj_cs4.mp4
		// http://192.168.3.177:2800/fdl/20141025/86000123456884846234123456789007/alarm/86000123456884846234123456789007_1_99_20141025112509/1414207819233.mp4
		// "http://192.168.3.177:2800/fdl/20141025/86000123456884846234123456789007/alarm/86000123456884846234123456789007_1_99_20141025112509/1414207819233.mp4";
		return playUrl;
	}

	/**
	 * 设备能力查询结果封装
	 */
	private DeviceAbility constractDeviceAbilityData(DeviceAbilityInfo info) {

		DeviceAbility deviceAbility = new DeviceAbility();

		deviceAbility.nAlarmPicture = info.nAlarmPicture;
		deviceAbility.nAlarmRecord = info.nAlarmRecord;
		deviceAbility.nAlarmReport = info.nAlarmReport;
		deviceAbility.nAudioTalk = info.nAudioTalk;
		deviceAbility.nFileDownload = info.nFileDownload;
		deviceAbility.nFilePlay = info.nFilePlay;
		deviceAbility.nFileQuery = info.nFileQuery;
		deviceAbility.nParamConfig = info.nParamConfig;
		deviceAbility.nPtz = info.nPtz;
		deviceAbility.nRealPlay = info.nRealPlay;
		deviceAbility.sDeviceID = info.sDeviceID;

		return deviceAbility;
	}

	/**
	 * 告警附件数据返回封装
	 */
	private List<AlarmGuardEvent> constractAlarmAttachData(
			List<AlarmGuardEventInfo> infoList) {

		List<AlarmGuardEvent> alarmAttachList = new ArrayList<AlarmGuardEvent>();

		for (AlarmGuardEventInfo attach : infoList) {

			AlarmGuardEvent alarmAttach = new AlarmGuardEvent();
			alarmAttach.BeginTime = attach.BeginTime;
			alarmAttach.Type = attach.Type;
			alarmAttach.AttchURL = attach.AttchURL;

			alarmAttachList.add(alarmAttach);
		}

		return alarmAttachList;
	}

	/**
	 * 告警文件数据返回封装
	 */

	private List<AlarmFileURLResult> constractAlarmFileResult(
			List<RecordInfo> recordList) {

		List<AlarmFileURLResult> alarmFileURLResultList = new ArrayList<AlarmFileURLResult>();

		for (int i = 0; i < recordList.size(); i++) {

			AlarmFileURLResult fileResult = null;
			RecordInfo recordInfo = recordList.get(i);
			int type = recordInfo.Location;
			if (type == VcpDataEnum.RecordLocation.LOCATION_DEVICE
					&& mAttachType != VcpDataEnum.AttachType.ALARM_PIC) {

				fileResult = new AlarmFileURLResult();
				fileResult.nLocation = VcpDataEnum.RecordLocation.LOCATION_DEVICE;
				fileResult.nFileType = VcpDataEnum.AttachType.ALARM_VIDEO;
				fileResult.nAlarmType = Integer.parseInt(recordInfo.recordType);
				fileResult.sBeginTime = recordInfo.BeginTime;

				// String picUrl = "/" + ProtocalData.mCurrentDevNumber + "_"
				// + ProtocalData.mCurrentCameraId + "?stype=vodtime&cid="
				// + recordInfo.BeginTime.replace(" ", "_") + "_"
				// + recordInfo.EndTime.replace(" ", "_");
				if (thumbnailServer != null) {
					String httpHeader = "http://" + thumbnailServer.host + ":"
							+ thumbnailServer.port;
					fileResult.sPictureURL = httpHeader
							+ "/iframe?Type=1&Devid="
							+ ProtocalData.mCurrentDevNumber + "&Cameraid="
							+ ProtocalData.mCurrentCameraId
							+ "&StreamId=0&Filename="
							+ recordInfo.BeginTime.replace(" ", "_") + "_"
							+ recordInfo.EndTime.replace(" ", "_")
							+ "&Timestamp=0";
				}

				fileResult.sRecordURL = "/" + ProtocalData.mCurrentDevNumber
						+ "_" + ProtocalData.mCurrentCameraId
						+ "?stype=vodtime&cid="
						+ recordInfo.BeginTime.replace(" ", "_") + "|"
						+ recordInfo.EndTime.replace(" ", "_");

				Date begTime = null;
				Date endTime = null;
				try {
					begTime = Util.parseDate(recordInfo.BeginTime);
					endTime = Util.parseDate(recordInfo.EndTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long temp = 0;
				if (begTime != null && endTime != null) {
					temp = endTime.getTime() - begTime.getTime();
				}
				// 时长
				int durationSeconds = (int) (temp / 1000);
				fileResult.nTimeSize = durationSeconds;
				alarmFileURLResultList.add(fileResult);
			} else if (type == VcpDataEnum.RecordLocation.LOCATION_CLOUD) {
				// 云服务器上 告警文件
				String httpUrlHeader = "http://" + MVPCommand.SERVER_IP + ""
						+ MVPCommand.SERVER_PORT + "/spi/image?url=";
				// String httpUrlHeader_Video = "http://" + MVPCommand.SERVER_IP
				// + ""
				// + MVPCommand.SERVER_PORT + "/fdl";
				for (int k = 0; k < recordInfo.alarmAttachList.size(); k++) {
					AlarmGuardEventInfo alarmAttachInfo = recordInfo.alarmAttachList
							.get(k);
					if (mAttachType == VcpDataEnum.AttachType.ALARM_ALL) {
						fileResult = new AlarmFileURLResult();
						fileResult.nLocation = VcpDataEnum.RecordLocation.LOCATION_CLOUD;
						fileResult.nFileType = alarmAttachInfo.Type;
						fileResult.nTimeSize = 15;
						fileResult.sBeginTime = alarmAttachInfo.BeginTime;
						fileResult.nAlarmType = Integer
								.parseInt(recordInfo.recordType);
						if (alarmAttachInfo.Type == VcpDataEnum.AttachType.ALARM_PIC) {
							// 图片
							fileResult.sPictureURL = httpUrlHeader
									+ alarmAttachInfo.AttchURL;
						} else if (alarmAttachInfo.Type == VcpDataEnum.AttachType.ALARM_VIDEO) {
							// 视频
							fileResult.sRecordURL = httpUrlHeader
									+ alarmAttachInfo.AttchURL;
						}
						alarmFileURLResultList.add(fileResult);
					} else if (mAttachType == VcpDataEnum.AttachType.ALARM_VIDEO) {
						// 如果只是查询告警视频
						if (alarmAttachInfo.Type == VcpDataEnum.AttachType.ALARM_VIDEO) {
							fileResult = new AlarmFileURLResult();
							fileResult.nLocation = VcpDataEnum.RecordLocation.LOCATION_CLOUD;
							fileResult.nFileType = alarmAttachInfo.Type;
							fileResult.nTimeSize = 15;
							fileResult.sBeginTime = alarmAttachInfo.BeginTime;
							fileResult.nAlarmType = Integer
									.parseInt(recordInfo.recordType);
							fileResult.sRecordURL = httpUrlHeader
									+ alarmAttachInfo.AttchURL;
							alarmFileURLResultList.add(fileResult);

						}
					} else if (mAttachType == VcpDataEnum.AttachType.ALARM_PIC) {
						// 如果只是查询告警图片
						if (alarmAttachInfo.Type == VcpDataEnum.AttachType.ALARM_PIC) {
							fileResult = new AlarmFileURLResult();
							fileResult.nLocation = VcpDataEnum.RecordLocation.LOCATION_CLOUD;
							fileResult.nFileType = alarmAttachInfo.Type;
							fileResult.nTimeSize = 15;
							fileResult.sBeginTime = alarmAttachInfo.BeginTime;
							fileResult.nAlarmType = Integer
									.parseInt(recordInfo.recordType);
							fileResult.sPictureURL = httpUrlHeader
									+ alarmAttachInfo.AttchURL;
							alarmFileURLResultList.add(fileResult);

						}
					}

				}

			}
		}
		return alarmFileURLResultList;
	}

	/**
	 * 开始语音传输
	 */
	private void startTalk() {
		String serverIp = ProtocalData.mSoundTalkServer.StreamIP;
		String port = ProtocalData.mSoundTalkServer.StreamPort;

		if (isdebug) {
			SoundTalkServer soundTalkServer = TranIpAndPort
					.testtranip(ProtocalData.mSoundTalkServer);
			serverIp = soundTalkServer.StreamIP;
			port = soundTalkServer.StreamPort;

		}

		String phoneparam = "devid=" + ProtocalData.mCurrentDevNumber
				+ "&cameraid=" + ProtocalData.mCurrentCameraId + "&server_ip="
				+ serverIp + "&server_port=" + port + "&";

		Log.d(TAG, "startTalk()-->phoneparam=" + phoneparam);

		mNativeSupport.setPhoneParam(phoneparam);
		mNativeSupport.initTalkVoice();

	}

	/**
	 * 开始下载录像
	 */
	private void startRecordDownLoad() {

		Log.d("进入底层下载", "底层下载开始" + "Start");
		String serverIp = ProtocalData.mRecordDownLoad.StreamIP;
		int port = ProtocalData.mRecordDownLoad.StreamPort;

		if (isdebug) {
			RecordDownLoad recordDownLoad = TranIpAndPort
					.testtranip(ProtocalData.mRecordDownLoad);
			serverIp = recordDownLoad.StreamIP;
			port = recordDownLoad.StreamPort;
		}

		String devSessionId = ProtocalData.mRecordDownLoad.DevSessionId;
		String destPath = ProtocalData.mRecordDownLoad.sDestFileName;//
		// String
		// phoneparam="Recordpath=/sdcard/record.mp4&DevSessionID=00008888&server_ip=192.168.3.250&server_port=18681&";
		String phoneParam = "Recordpath=" + destPath + "&DevSessionID="
				+ devSessionId + "&server_ip=" + serverIp + "&server_port="
				+ port + "&";

		Log.d(TAG, "startRecordDownLoad()-->phoneparam=" + phoneParam);

		mNativeRecord.setPhoneParam(phoneParam);
		mNativeRecord.startDownload();
	}

	@Override
	public boolean onError(TMPCPlayer player, int what, int extra) {
		// TODO Auto-generated method stub

		if (Sdk_nConnType == PLAY_MODEL_AUTO_CHOOSE) {
			// 不向上层上层发送
			// LoggerUtil.d("自动模式错误信息" + "播放库返回错误码=" + what + "Sdk_nConnType="
			// + Sdk_nConnType);
			startRtsLivePlay(mplaySaveAutoInfo.sSessionID,
					mplaySaveAutoInfo.sOptID, mplaySaveAutoInfo.sDevID,
					mplaySaveAutoInfo.nCameraID, mplaySaveAutoInfo.nStreamType,
					mplaySaveAutoInfo.mplayerStateCallback);
			Sdk_nConnType = PLAY_MODEL_RTSP_TRANSIT;

		} else if (Sdk_nConnType == PLAY_MODEL_P2P) {
			if (playerStateCallback != null) {
				// LoggerUtil.d("P2P模式错误信息" + "播放库返回错误码=" + what
				// + "Sdk_nConnType=" + Sdk_nConnType);
				playerStateCallback.onPlayerStateCallback(
						TmPlayerStatus.TmPlayerError, what, null);
			}

		} else if (Sdk_nConnType == PLAY_MODEL_RTSP_TRANSIT) {
			if (playerStateCallback != null) {
				// LoggerUtil.d("中转模式错误信息" + "播放库返回错误码=" + what +
				// "Sdk_nConnType="
				// + Sdk_nConnType);
				playerStateCallback.onPlayerStateCallback(
						TmPlayerStatus.TmPlayerError, what, null);
			}
		} else {
			if (playerStateCallback != null) {

				playerStateCallback.onPlayerStateCallback(
						TmPlayerStatus.TmPlayerError, what, null);
			}
		}

		return false;
	}

	@Override
	public void onBufferingUpdate(TMPCPlayer player, int percent) {
		// TODO Auto-generated method stub
		// Log.d(TAG, "onBufferingUpdate()...percent=" + percent);
		if (playerStateCallback != null) {

			playerStateCallback.onPlayerStateCallback(
					TmPlayerStatus.TmPlayerBuffering, percent, null);
		}

	}

	@Override
	public void onBufferingBegin(TMPCPlayer player) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onBufferingBegin()...");
		Log.d(TAG, "onBufferingBegin()-->isMute=" + isMute);
		if (playerStateCallback != null) {

			if (isMute) {
				// 静音
				if (player != null) {
					player.nativeSetAudioMute(1);
				}
			} else {
				// 非静音
				if (player != null) {
					player.nativeSetAudioMute(0);
				}

			}
			playerStateCallback.onPlayerStateCallback(
					TmPlayerStatus.TmPlayerMediaInfo, 0, null);
		}

	}

	@Override
	public void onBufferingComplete(TMPCPlayer player) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onBufferingComplete()...");
		if (playerStateCallback != null) {

			playerStateCallback.onPlayerStateCallback(
					TmPlayerStatus.TmPlyaerPlayStart, 100, null);
		}

	}

	@Override
	public void onCompletion(TMPCPlayer player) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCompletion()...");
		if (playerStateCallback != null) {

			playerStateCallback.onPlayerStateCallback(
					TmPlayerStatus.TmPlayerPlayFinish, 1, null);
		}
	}

	/*
	 * 获取下载进度 接口
	 */
	public interface IRecordPosChanged {
		/*
		 * 获取进度位置
		 */
		int RecordPosFun(int nPos);

	}

	// 业务操作结果回调接口
	public interface IResultCallback {
		/**
		 * 
		 * @param nCommnd
		 *            业务操作命令号
		 * @param nResult
		 *            操作结果 1 成功 -1失败
		 * @param pInfo
		 *            操作返回的数据
		 * @param nContext
		 *            扩展字段
		 */
		void ResultCallbackFun(int nCommnd, int nResult, CommInfo pInfo,
				int nContext);
	}

	/**
	 * 播放器状态回调接口
	 */
	public interface IPlayerStateCallback extends IResultCallback {
		/**
		 * 
		 * @param code
		 *            播放器状态 参考TmPlayerStatus
		 * @param param
		 *            code= TmPlayerError时，错误描述,缓冲时，传递缓冲进度值
		 * @param info
		 *            扩展字段
		 */
		void onPlayerStateCallback(int code, int param, Object info);
	}

	/**
	 * 录像开始/结束客户端回调接口
	 */
	public interface IRecordingStateCallback {

		/**
		 * 开始录像
		 */
		void onRecordStarted();

		/**
		 * 录像结束
		 */
		void onRecordStoped();
	}

	private class MainHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// Log.d(TAG, "mHandler msg what=" + msg.what + ";msg obj=" +
			// msg.obj);
			switch (msg.what) {
			case NativeSupport.TM_AUDIOTALK_TRANSFER_CONNECT_ERROR:
				// 语音对讲传输连接失败
				Log.d(TAG,
						"NativeSupport.TM_AUDIOTALK_TRANSFER_CONNECT_ERROR....");

				// LoggerUtil.d("对讲语音连接错误TM_AUDIOTALK_TRANSFER_CONNECT_ERROR=1000");
				if (resultCallback != null) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_StartTalkVoice,
							NativeSupport.TM_AUDIOTALK_TRANSFER_CONNECT_ERROR,
							null,
							NativeSupport.TM_AUDIOTALK_TRANSFER_CONNECT_ERROR);
				}
				break;
			case NativeSupport.TM_AUIOTALK_TRANSFER_CONNECT_OK:
				Log.d(TAG, "NativeSupport.TM_AUIOTALK_TRANSFER_CONNECT_OK....");
				// 连接OK
				// LoggerUtil.d("对讲语音连接成功TM_AUIOTALK_TRANSFER_CONNECT_OK=1001");
				if (resultCallback != null) {
					resultCallback
							.ResultCallbackFun(
									OptResultType.Result_StartTalkVoice,
									NativeSupport.TM_AUIOTALK_TRANSFER_CONNECT_OK,
									null,
									NativeSupport.TM_AUIOTALK_TRANSFER_CONNECT_OK);
				}

				break;
			case NativeSupport.TM_AUDIOTALK_TRANSFER_RECONNECT:
				Log.d(TAG, "NativeSupport.TM_AUDIOTALK_TRANSFER_RECONNECT....");
				// 重连

				// LoggerUtil.d("对讲语音重连TM_AUDIOTALK_TRANSFER_RECONNECT=1002");
				if (resultCallback != null) {
					resultCallback
							.ResultCallbackFun(
									OptResultType.Result_StartTalkVoice,
									NativeSupport.TM_AUDIOTALK_TRANSFER_RECONNECT,
									null,
									NativeSupport.TM_AUDIOTALK_TRANSFER_RECONNECT);
				}
				break;
			case NativeSupport.TM_AUDIOTALK_TRANSFER_GETDEVAUDIOINFO:
				// //获取设备支持的音频信息
				Log.d(TAG,
						"NativeSupport.TM_AUDIOTALK_TRANSFER_GETDEVAUDIOINFO....");

				// LoggerUtil.d("对讲语音获取设备支持的音频信息TM_AUDIOTALK_TRANSFER_GETDEVAUDIOINFO=1007");

				String devAudioInfo = mNativeSupport.getDevAudioInfo();

				Log.d(TAG, "startTalk()-->devAudioInfo=" + devAudioInfo);
				// devAudioInfo = "AAC/44100";
				// AMR/8000
				// added it by ihappy for change audio encoder from amr to aac
				int sampleRate = 44100;

				Log.d("显示错误", devAudioInfo);
				if (devAudioInfo != null && !devAudioInfo.equals("")) {

					if (devAudioInfo.indexOf("/") > 0) {

						String subString = devAudioInfo.substring(
								devAudioInfo.indexOf("/") + 1,
								devAudioInfo.length());
						Log.d("subString", subString.replaceAll("", ""));

						sampleRate = Integer.parseInt(subString.replaceAll("",
								""));
					}
				}
				Log.d(TAG, "startTalk()-->sampleRate=" + sampleRate);
				customRecord = new CustomRecord(sampleRate, 1);
				// added it by ihappy for change audio encoder from amr to aac
				customRecord.SetBufferSize(1024);
				customRecord.setCallback(mAudioWriteFrameCallback);
				// 开始传送音频数据
				customRecord.resume();
				resultCallback.ResultCallbackFun(
						OptResultType.Result_StartTalkVoice,
						NativeSupport.TM_AUDIOTALK_TRANSFER_GETDEVAUDIOINFO,
						null,
						NativeSupport.TM_AUDIOTALK_TRANSFER_GETDEVAUDIOINFO);

				break;
			case NativeSupport.TM_AUIOTALK_TRANSFER_TIMEOUT:
				// //传输超时
				Log.d(TAG, "NativeSupport.TM_AUIOTALK_TRANSFER_TIMEOUT....");

				// LoggerUtil.d("对讲语音传输超时TM_AUIOTALK_TRANSFER_TIMEOUT=1006");
				if (resultCallback != null) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_StartTalkVoice,
							NativeSupport.TM_AUIOTALK_TRANSFER_TIMEOUT, null,
							NativeSupport.TM_AUIOTALK_TRANSFER_TIMEOUT);
				}

				break;

			case NativeRecord.TM_RECORD_DOWNLOAD_CONNECT_OK:
				// 下载连接成功
				Log.d(TAG, "NativeRecord.TM_RECORD_DOWNLOAD_CONNECT_OK....");
				if (resultCallback != null) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_StartRecordFileDownload,
							DownLoadStatus.TM_RECORD_DOWNLOADING, new CommInfo(
									null, 0), 0);
				}
				break;
			case NativeRecord.TM_RECORD_DOWNLOAD_CONNECT_ERROR:
				// 下载连接失败
				Log.d(TAG, "NativeRecord.TM_RECORD_DOWNLOAD_CONNECT_ERROR....");
				if (resultCallback != null) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_StartRecordFileDownload,
							DownLoadStatus.TM_RECORD_DOWNLOADING, new CommInfo(
									null, 0), 0);
				}
				break;
			case NativeRecord.TM_RECORD_DOWNLOAD_RECONNECT:
				Log.d(TAG, "NativeRecord.TM_RECORD_DOWNLOAD_RECONNECT....");
				if (resultCallback != null) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_StartRecordFileDownload,
							DownLoadStatus.TM_RECORD_DOWNLOADING, new CommInfo(
									null, 0), 0);
				}
				// 下载正在重连
				break;
			case NativeRecord.TM_RECORD_DOWNLOAD_BEGIN_ERROR:
				// 开始下载失败
				Log.d(TAG, "NativeRecord.TM_RECORD_DOWNLOAD_BEGIN_ERROR....");
				if (resultCallback != null) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_StartRecordFileDownload,
							DownLoadStatus.TM_RECORD_DOWNLOADING, new CommInfo(
									null, 0), 0);
				}
				break;
			case NativeRecord.TM_RECORD_DOWNLOAD_TIMEOUT:
			case DownLoadStatus.TM_RECORD_DOWNLOAD_FAILED:
				// 连接失败，超时
				Log.d(TAG, "NativeRecord.TM_RECORD_DOWNLOAD_TIMEOUT....");

				// LoggerUtil.d("定时器准备关闭，收到下载超时");
				if (mNativeRecord != null) {
					mNativeRecord.stopDownload();// 下载完成，停止
				}

				if (timerGetDownLoadPercent != null) {

					timerGetDownLoadPercent.cancel();
					timerGetDownLoadPercent = null;
				}
				if (taskForDownLoadPercent != null) {

					taskForDownLoadPercent.cancel();
					taskForDownLoadPercent = null;
				}

				if (resultCallback != null) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_StartRecordFileDownload,
							DownLoadStatus.TM_RECORD_DOWNLOAD_FAILED, null,
							DownLoadStatus.TM_RECORD_DOWNLOAD_FAILED);
				}
				break;
			case NativeRecord.TM_RECORD_DOWNLOAD_OK:
			case DownLoadStatus.TM_RECORD_DOWNLOAD_OK:
				// 完成后，传递目标路径

				Log.d("打印成功", "下载状态:" + msg.what + "");
				// String destPath = (String) msg.obj;
				CommHeader header1 = new CommHeader();
				CommInfo info1 = new CommInfo(header1, "");
				// 下载完成
				Log.d(TAG, "NativeRecord.TM_RECORD_DOWNLOAD_OK....");
				if (mNativeRecord != null) {
					mNativeRecord.stopDownload();// 下载完成，停止
				}
				if (timerGetDownLoadPercent != null) {

					timerGetDownLoadPercent.cancel();
					timerGetDownLoadPercent = null;
				}
				if (taskForDownLoadPercent != null) {

					taskForDownLoadPercent.cancel();
					taskForDownLoadPercent = null;
				}
				if (resultCallback != null) {
					resultCallback.ResultCallbackFun(
							OptResultType.Result_StartRecordFileDownload,
							DownLoadStatus.TM_RECORD_DOWNLOAD_OK, info1,
							DownLoadStatus.TM_RECORD_DOWNLOAD_OK);
				}
				break;
			case NativeRecord.TM_RECORD_DOWNLOAD_GETTOTALLENGTH:
				// 循环从底层获取下载进度
				if (mNativeRecord != null) {
					// refreshDownLoadProgress.start();
					if (timerGetDownLoadPercent == null) {

						timerGetDownLoadPercent = new Timer();
					}
					if (taskForDownLoadPercent == null) {
						currentDownLoadPercent = 0;
						isStop = false;
						taskForDownLoadPercent = new TaskForDownLoadPercent();
					}
					timerGetDownLoadPercent.schedule(taskForDownLoadPercent,
							500, 1000);
				}

				break;
			case DownLoadStatus.TM_RECORD_DOWNLOADING:
				// 对外暴露下载进度
				Integer percent = (Integer) msg.obj;
				int percent1 = msg.arg1;
				Log.d("底层打印", "下载进度显示" + percent);
				CommHeader header = new CommHeader();
				CommInfo info = new CommInfo(header, percent1);
				if (resultCallback != null) {
					resultCallback
							.ResultCallbackFun(
									OptResultType.Result_StartRecordFileDownload,
									DownLoadStatus.TM_RECORD_DOWNLOADING, info,
									percent);
				}
				break;

			default:
				break;
			}
		}
	}

	private Timer timerGetDownLoadPercent = null;
	private TaskForDownLoadPercent taskForDownLoadPercent;
	private int currentDownLoadPercent = 0;
	private boolean isStop = false;

	private class TaskForDownLoadPercent extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				if (!isStop) {

					//
					// if (mNativeRecord != null) {
					// int downLoadPercent = mNativeRecord
					// .nativeGetDownloadPercent();
					// currentDownLoadPercent = downLoadPercent;
					// Log.d(TAG,
					// "taskForDownLoadPercent-->TM_RECORD_DOWNLOAD_GETTOTALLENGTH-->downLoadPercent="
					// + downLoadPercent);
					//
					// Message msg = mHandler.obtainMessage();
					// msg.what = DownLoadStatus.TM_RECORD_DOWNLOADING;
					// msg.obj = currentDownLoadPercent;
					// mHandler.sendMessage(msg);
					// }
					//
					// if (currentDownLoadPercent >= 100) {
					//
					// Message msg = mHandler.obtainMessage();
					// msg.what = NativeRecord.TM_RECORD_DOWNLOAD_OK;
					// msg.obj = String.valueOf(100);
					// mHandler.sendMessage(msg);
					// }
					if (mNativeRecord != null) {
						// LoggerUtil.d("定时器运行了一次");

						int downLoadPercent = mNativeRecord
								.nativeGetDownloadPercent();
						currentDownLoadPercent = downLoadPercent;
						Log.d(TAG,
								"taskForDownLoadPercent-->TM_RECORD_DOWNLOAD_GETTOTALLENGTH-->downLoadPercent="
										+ downLoadPercent);
						Log.d(TAG,
								"下载进度显示-->TM_RECORD_DOWNLOAD_GETTOTALLENGTH-->downLoadPercent="
										+ downLoadPercent);
						Log.d(TAG, "下载进度显示-->currentDownLoadPercent"
								+ currentDownLoadPercent);

						if (currentDownLoadPercent > 100) {
							//
							// Message msg = mHandler.obtainMessage();
							// msg.what = NativeRecord.TM_RECORD_DOWNLOAD_OK;
							// msg.obj = String.valueOf(100);
							// msg.arg1 = 100;
							// mHandler.sendMessage(msg);
						} else {

							if (currentDownLoadPercent < 0) {
								currentDownLoadPercent = 0;
							}

							Message msg = mHandler.obtainMessage();
							msg.what = DownLoadStatus.TM_RECORD_DOWNLOADING;
							msg.arg1 = currentDownLoadPercent;
							msg.obj = currentDownLoadPercent;
							mHandler.sendMessage(msg);
						}
					}
				}

			} catch (Exception ex) {
				Log.d(TAG,
						"RefreshDownLoadProgress-->mNativeRecord.nativeGetDownloadPercent exception="
								+ ex.getMessage());
			}

		}

		@Override
		public boolean cancel() {
			// TODO Auto-generated method stub
			isStop = true;
			return super.cancel();
		}

	};

	/**
	 * 写音频数据回调函数
	 */
	private final AudioWriteFrameCallback mAudioWriteFrameCallback = new AudioWriteFrameCallback();

	private final class AudioWriteFrameCallback implements
			CustomRecord.AudioWriteFrameCallback {
		public void Write(byte[] data) {
			if (mNativeSupport != null)
				mNativeSupport.audioRecordCallback(data);
		}
	}

	@Override
	public void onTMPCRecodeStarted() {
		// TODO Auto-generated method stub
		// 录像开始
		Log.d(TAG, "record...onTMPCRecodeStarted....");
		if (recordCallback != null) {

			recordCallback.onRecordStarted();
		}

	}

	@Override
	public void onTMPCRecodeStoped() {
		// TODO Auto-generated method stub
		// 录像停止
		Log.d(TAG, "record....onTMPCRecodeStoped.....");

		if (recordCallback != null) {

			recordCallback.onRecordStoped();
		}
	}

	public TMPCPlayer getPlayer() {
		return player;

	}

	/**
	 * @description 暂停视频
	 * @author zhaosl
	 * @update 2013-6-19 下午4:17:52
	 */
	public void doPause() {
		if (player == null) {
			return;
		}

		if (player != null
				&& (player.getTPlayerState() > TMPCPlayer.PLAYER_STOPED)) {
			try {
				if (player.isPausable()) {
					Log.d(TAG, "surface: player.pause();");
					player.pause();
					// setPromptMsg(ConstantData.STR_PAUSED,
					// ConstantData.TIME_FOREVER, false);
				} else {
					Log.d(TAG, "surface: player.stop();");
					player.stop();
					// setPromptMsg(ConstantData.STR_STOPED,
					// ConstantData.TIME_FOREVER, false);
				}
				// pauseImgBtn.setBackgroundResource(R.drawable.btn_play_selector);
				// guideBgLayout.setVisibility(View.GONE);
			} catch (Exception e) {
				// setPromptMsg(ConstantData.STR_ERROR,
				// ConstantData.TIME_FOREVER,
				// false);
				e.printStackTrace();
			}
		} else {
			if (player != null
					&& (player.getTPlayerState() > TMPCPlayer.PLAYER_STOPED)) {

			}
		}
	}

	/**
	 * @description 播放视频
	 * 
	 */
	public void doPlay() {
		if (player != null
				&& (player.getTPlayerState() == TMPCPlayer.PLAYER_PAUSED || player
						.getTPlayerState() == TMPCPlayer.PLAYER_STOPED)) {
			if (player != null && !player.isPlaying()) {
				// pauseImgBtn
				// .setBackgroundResource(R.drawable.btn_pause_selector);
				if (player.getTPlayerState() <= TMPCPlayer.PLAYER_STOPED) {
					// //dostart
					try {
						player.start();
					} catch (OperationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {

						player.start();
						// setPromptMsg(ConstantData.STR_PREPARE,
						// ConstantData.TIME_FOREVER, true);
					} catch (Exception e) {
						// setPromptMsg(ConstantData.STR_ERROR,
						// ConstantData.TIME_FOREVER, true);
						e.printStackTrace();
					}
				}
			}
		} else {
			if (player != null
					&& player.getTPlayerState() == TMPCPlayer.PLAYER_BUFFERING) {

			}
		}
	}

	/**
	 * @description 播放视频带url
	 * 
	 */
	public void doPlay(String url, IPlayerStateCallback mplayerStateCallback) {
		this.playerStateCallback = mplayerStateCallback;
		if (player != null
				&& (player.getTPlayerState() == TMPCPlayer.PLAYER_PAUSED || player
						.getTPlayerState() == TMPCPlayer.PLAYER_STOPED)) {
			if (player != null && !player.isPlaying()) {
				// pauseImgBtn
				// .setBackgroundResource(R.drawable.btn_pause_selector);
				if (player.getTPlayerState() <= TMPCPlayer.PLAYER_STOPED) {
					// //dostart
					try {
						player.setDataSource(url);
						player.start();
					} catch (OperationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {

						player.start();
						// setPromptMsg(ConstantData.STR_PREPARE,
						// ConstantData.TIME_FOREVER, true);
					} catch (Exception e) {
						// setPromptMsg(ConstantData.STR_ERROR,
						// ConstantData.TIME_FOREVER, true);
						e.printStackTrace();
					}
				}
			}
		} else {
			if (player != null
					&& player.getTPlayerState() == TMPCPlayer.PLAYER_BUFFERING) {

			}
		}
	}

	/**
	 * 
	 * @param url
	 *            下载地址
	 * @param target
	 *            下载的位置
	 */
	public void startHttpDownloadImage(String url, String target,
			IResultCallback callback) {

		this.resultCallback = callback;
		mHandler = new MainHandler();
		downLoadFile.setHandler(mHandler);
		downLoadFile.startdownload(url, target);

	}

	/**
	 * 停止下载图片
	 */
	public void stopHttpDownloadImage() {
		downLoadFile.stopdownload();

	}

	/**
	 * 原始尺寸
	 * 
	 * @param surfaceView
	 * @param playerLayout
	 * @param mscreenWidth
	 * @param mscreenHeight
	 */
	private void setUserPicturesizeToOriginalsize(SurfaceView surfaceView,
			RelativeLayout playerLayout, int mscreenWidth, int mscreenHeight) {

		int screenWidth = mscreenWidth;
		int screenHeight = mscreenHeight;
		int w = player.getVideoWidth();
		int h = player.getVideoHeight();

		int tw = w;
		int th = h;
		if (tw > screenWidth) {
			tw = screenWidth;
			th = h * screenWidth / w;
		}
		surfaceView.getHolder().setFixedSize(w, h);
		RelativeLayout.LayoutParams params = null;
		if (params == null) {
			params = new RelativeLayout.LayoutParams(tw, th);
		}
		params.leftMargin = (screenWidth - tw) / 2;
		params.topMargin = (screenHeight - th) / 2;
		surfaceView.setLayoutParams(params);
		playerLayout.updateViewLayout(surfaceView, params);
	}

	private void setUserFullScreen(SurfaceView surfaceView,
			RelativeLayout playerLayout, int mscreenWidth, int mscreenHeight) {

		setUserPicturesizeToOriginalsize(surfaceView, playerLayout,
				mscreenWidth, mscreenHeight);
		playerLayout.updateViewLayout(surfaceView,
				new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT));
	}

	public void setPicturesizeToOriginalsize(SurfaceView surfaceView,
			RelativeLayout playerLayout, int width, int height,
			int mscreenWidth, int mscreenHeight) {

		int screenWidth = mscreenWidth;
		int screenHeight = mscreenHeight;
		int w = width;
		int h = height;

		int tw = w;
		int th = h;
		if (tw > screenWidth) {
			tw = screenWidth;
			th = h * screenWidth / w;
		}
		surfaceView.getHolder().setFixedSize(w, h);
		RelativeLayout.LayoutParams params = null;
		if (params == null) {
			params = new RelativeLayout.LayoutParams(tw, th);
		}
		params.leftMargin = (screenWidth - tw) / 2;
		params.topMargin = (screenHeight - th) / 2;
		surfaceView.setLayoutParams(params);
		playerLayout.updateViewLayout(surfaceView, params);
	}

	public void setFullScreen(SurfaceView surfaceView,
			RelativeLayout playerLayout, int width, int height,
			int mscreenWidth, int mscreenHeight) {

		setPicturesizeToOriginalsize(surfaceView, playerLayout, width, height,
				mscreenWidth, mscreenHeight);
		playerLayout.updateViewLayout(surfaceView,
				new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT));
	}

	/**
	 * 设置到哪一点 播放
	 * 
	 * @param videoSeekPrg
	 */
	public void doPlayseekto(int videoSeekPrg) {

		/**
		 * 保证用户无法拖动到最后一秒，从而导致播放器自动退出
		 */
		if (videoSeekPrg == player.getDuration()) {
			videoSeekPrg = videoSeekPrg - 1 * 60 * 1000;
		}
		try {
			if (player.getTPlayerState() == TMPCPlayer.PLAYER_PLAYING) {
				// player.seekTo(videoSeekPrg);
				player.seekTo(videoSeekPrg, 0);
			} else if (player.getTPlayerState() == TMPCPlayer.PLAYER_PAUSED
					|| player.getTPlayerState() == TMPCPlayer.PLAYER_STOPED) {
				player.start(videoSeekPrg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 设置 播放速度
	 * 
	 * @param num分子
	 * @param den分母
	 *            分子大于分母 加速播放
	 * @return
	 */
	public void SetPlaySpeed(int num, int den) {

		player.setPlaySpeed(num, den);

	}

	/**
	 * 取消HTTP请求操作
	 */
	public void cancelCommond() {
		ProtocalEngine protocalEngine = ProtocalEngine.getInstance();
		protocalEngine.cancelAllHttp();

	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}

		return null;
	}

	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 获取本机外网IP地址
	 * 
	 * @param context
	 * @return IP地址
	 */
	public static String getWaiWangIP() {
		String IP = "";
		try {
			String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
			URL url = new URL(address);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setUseCaches(false);

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream in = connection.getInputStream();

				// 将流转化为字符串
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));

				String tmpString = "";
				StringBuilder retJSON = new StringBuilder();
				while ((tmpString = reader.readLine()) != null) {
					retJSON.append(tmpString + "\n");
				}

				JSONObject jsonObject = new JSONObject(retJSON.toString());
				String code = jsonObject.getString("code");
				if (code.equals("0")) {
					JSONObject data = jsonObject.getJSONObject("data");
					// IP = data.getString("ip") + "(" +
					// data.getString("country")
					// + data.getString("area") + "区"
					// + data.getString("region") + data.getString("city")
					// + data.getString("isp") + ")";

					IP = data.getString("ip");

					Log.e("提示", "您的IP地址是：" + IP);
				} else {
					IP = "";
					Log.e("提示", "IP接口异常，无法获取IP地址！");
				}
			} else {
				IP = "";
				Log.e("提示", "网络连接异常，无法获取IP地址！");
			}
		} catch (Exception e) {
			IP = "";
			Log.e("提示", "获取IP地址时出现异常，异常信息是：" + e.toString());
		}
		return IP;
	}

	public String getVcpPlayerVersion() {

		return TMPCPlayer.getPlayerVersion();

	}

	public void SetDisplayOutside(boolean isoutside) {

		player.SetDisplayOutside(isoutside);// false 内显 true 外显
	}

}
