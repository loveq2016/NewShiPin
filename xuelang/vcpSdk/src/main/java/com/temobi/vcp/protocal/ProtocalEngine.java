package com.temobi.vcp.protocal;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;

import com.temobi.android.volley.AuthFailureError;
import com.temobi.android.volley.Request;
import com.temobi.android.volley.Request.Method;
import com.temobi.android.volley.Response;
import com.temobi.android.volley.Response.Listener;
import com.temobi.android.volley.VolleyError;
import com.temobi.android.volley.manager.RequestManager;
import com.temobi.android.volley.manager.TemobiHttp;
import com.temobi.android.volley.toolbox.StringRequest;
import com.temobi.vcp.httpclienttool.HttpClientTool;
import com.temobi.vcp.protocal.data.ProtocalData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Html.TagHandler;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class ProtocalEngine {

	private static String TAG = "ProtocalEngine";
	// 服务器返回数据
	private byte[] responseData = null;
	// 观察者
	private Vector<IProtocalInterface> obs = new Vector<IProtocalInterface>();
	// 单例模式
	private static ProtocalEngine INSTANCE = null;
	// http请求收发类
	private HttpManager mHttpManager = new HttpManager();
	// 异步task:发送心跳外请求
	private HttpTask mHttpTask = new HttpTask();
	// 请求xml组织类
	private XmlAssembler mXmlAssembler = new XmlAssembler();
	// xml解析类
	private XmlParser mXmlParser = new XmlParser();
	// 发送命令的条数
	private int mCommandCount;
	// 当前命令值
	private int mCurCommand;
	// 心跳异步任务
	private HeartBeatTask mHeartBeatTask = new HeartBeatTask();
	private MyHander mHeartBeatHander = new MyHander();
	// 服务器心跳返回数据
	private byte[] HBresponseData = null;
	// 心跳时间间隔(单位毫秒)
	private int mTimeInterval;
	// 心跳失败次数(连续心跳失败3次以上表示心跳已经断开,通知UI重新登录)
	private int mHeartBeatErrCount;
	private final int MAX_ERR_COUNT = 3;

	private HttpClientTool httpClientTool = new HttpClientTool();

	private ProtocalEngine() {
		mCommandCount = 0;
		mCurCommand = 0;
		temobiHttp = TemobiHttp.getInstance();

		httpClientTool.getHttpClient();

	}

	// get ProtocalEngine's Instance
	public static ProtocalEngine getInstance() {

		if (INSTANCE == null) {
			synchronized (ProtocalEngine.class) {
				if (INSTANCE == null) {
					INSTANCE = new ProtocalEngine();
				}
			}
		}
		return INSTANCE;

	}

	// add a observer
	public void addObserver(IProtocalInterface obs) {

		if (this.obs != null) {
			this.obs.clear();
		}
		obs.isCancel(false);
		this.obs.add(obs);
	}

	// remove a observer
	public void delObserver(IProtocalInterface obs) {
		this.obs.remove(obs);
	}

	// notify all observers
	protected void notifyObserver(int cmd, int errcode) {
		if (obs != null && obs.size() > 0) {
			for (IProtocalInterface o : obs) {
				o.onProtocalNotifycation(cmd, errcode);
			}
		}

	}

	// start heart beat
	public void startHeartBeat() {
		if (null == ProtocalData.mLoginInfo) {
			return;
		}
		Log.i(TAG, "HeartbeatTime:" + ProtocalData.mLoginInfo.HeartbeatTime);
		mHeartBeatErrCount = 0;
		mTimeInterval = ProtocalData.mLoginInfo.HeartbeatTime * 1000;
		// mTimeInterval = 10000;//测试，先写10s,服务器返回时间间隔有问题
		sendMsg(mHeartBeatHander.SEND_HEARTBEAT, mTimeInterval);
	}

	// stop heart beat
	public void stopHeartBeat() {
		mHeartBeatTask.cancel(true);
	}

	// send heart beat
	private void sendHeartBeat() {

		if (mHeartBeatTask != null) {
			mHeartBeatTask.cancel(true);
			mHeartBeatTask = null;
		}
		mHeartBeatTask = new HeartBeatTask();

		String reqString = mXmlAssembler.assembleHeartBeatXml(mCommandCount);
		if (null == reqString) {

		} else {
			String urlPath = MVPCommand.HTTP + MVPCommand.SERVER_IP
					+ MVPCommand.SERVER_PORT + MVPCommand.HEAETBEAT_URL;
			mHeartBeatTask.execute(urlPath, reqString);
		}
	}

	// cancel a request
	public void cancelRequest() {
		if (mHttpTask != null) {
			mHttpTask.cancel(true);
			mHttpTask = null;
		}
	}

	// send request to server
	public boolean sendHttpRequest(int cmd) {
		boolean ret = false;
		mCommandCount++;
		String reqString;
		String urlPath;
		String serverIP;
		if (MVPCommand.INPUT_IP.length() <= 0) {
			serverIP = MVPCommand.SERVER_IP;
		} else {
			serverIP = MVPCommand.INPUT_IP;
		}
		String serverPort = MVPCommand.SERVER_PORT;
		switch (cmd) {
		case MVPCommand.VCP_GET_DEVINFO:
			reqString = mXmlAssembler.assembleGetDevInfoXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.GET_DEVINFO_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_QUERY_CAMERA_PARAM:
		case MVPCommand.VCP_GET_DEV_VERSOION:
			reqString = mXmlAssembler.assembleGetDevParamXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.GET_CAMERA_PARAM_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_SET_CAMERA_PARAM:
			reqString = mXmlAssembler
					.assembleSetDevParamPassWordXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.SET_CAMERA_PARAM_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_SET_CAMERA_PARAM_PASSWORD:
			reqString = mXmlAssembler
					.assembleSetDevParamPassWordXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.SET_CAMERA_PARAM_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_SET_CAMERA_PARAM_OSD:

			reqString = mXmlAssembler.assembleSetDevParamOSDXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.SET_CAMERA_PARAM_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_HEARTBEAT:
			reqString = mXmlAssembler.assembleHeartBeatXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.HEAETBEAT_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_MEDIA_PLAY:
			// Test
			reqString = mXmlAssembler.assembleMediaPlayXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.MEDIA_PLAY_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_MEDIA_QUIT:
			reqString = mXmlAssembler.assembleMediaQuitXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.MEDIA_QUIT_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_MODIFY_PWD:
			reqString = mXmlAssembler.assembleModifyPwdXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.MODIFY_PWD_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_MEDIA_AUTH:
			reqString = mXmlAssembler
					.assembleMediaAuthRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.MEDIA_AUTH_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_CAMERA_CTRL:
			reqString = mXmlAssembler
					.assembleCameraCtrlRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.CAMERA_CTRL_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_RTSP_URL:
			reqString = mXmlAssembler.assembleRtspUrlRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.GET_RTSP_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_CAMERA_LIST:
			reqString = mXmlAssembler.assembleBindIdRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.CAMERA_LIST_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_QUERY_RECORD:
			reqString = mXmlAssembler
					.assembleQueryRecordRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.QUERY_RECORD_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_GET_CLOUDRECORD:
			reqString = mXmlAssembler
					.assembleQueryCloudRecordRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.QUERY_CLOUD_RECORD;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_TERMINAL_STATUS:
			reqString = mXmlAssembler
					.assembleTerminalDeviceRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.TERMINAL_STATUS_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_MEDIA_PLAY_RECORD:
			reqString = mXmlAssembler.assembleRecordPlayXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.RECORD_PLAY_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_GET_DEV_PLAN:
			reqString = mXmlAssembler
					.assembleQueryDevPlanRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.DEV_PLAN_QUERY;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_SET_DEV_PLAN:
			// 设置设备计划
			reqString = mXmlAssembler
					.assembleSetDevPlanRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.DEV_PLAN_SET;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_GET_SOUNDTALK_SERVER:
			// 获取语音对讲需要服务器信息
			reqString = mXmlAssembler
					.assembleSoundTalkRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.GET_SOUNDTALK_SERVER;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_GET_RECORD_DOWNLOAD_SERVER:
			// 获取下载录像需要服务器信息
			reqString = mXmlAssembler
					.assembleRecordDownLoadRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.GET_RECORDDOWNLOAD_SERVER;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_MEDIA_PLAY_CLOUD_RECORD:
			// 云录像预览
			reqString = mXmlAssembler.assembleCloudRecordPlayXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.PLAY_CLOUD_RECORD;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_CLOUD_RECORD_DEL:
			// 删除云录像
			reqString = mXmlAssembler
					.assembleDelCloudRecordRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.DEL_CLOUD_RECORD_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_RECORD_DEL:
			// 删除设备录像
			reqString = mXmlAssembler
					.assembleDelDeviceRecordRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.DEL_DEVICERECORD_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_DEVICEABILITY_QUERY:
			// 设备能力查询
			reqString = mXmlAssembler.assembleAbilityRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.GET_DEVICE_ABILITY;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_CLOUD_RECORDSET_FLAG:
			// 中心录像开关 设置
			reqString = mXmlAssembler
					.assembleCloudRecordSetFlagRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.CLOUD_RECORDSET_FLAG_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_CLOUD_RECORDGET_FLAG:
			// 中心录像开关 查询
			reqString = mXmlAssembler
					.assembleCloudRecordGetFlagRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.CLOUD_RECORDGET_FLAG_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_QUERY_ALARM_ATTACH:
			// 获取报警附件
			reqString = mXmlAssembler
					.assembleAlarmAttchRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.ALARM_ATTACH_QUERY_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_QUERY_ALARM_FILE_BY_ID:

			reqString = mXmlAssembler
					.assembleAlarmAttchByIdRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.ALARM_ATTACH_QUERY_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_ALARM_SWITCH_SET:
			// 报警开关设置
			reqString = mXmlAssembler
					.assembleSetDevAlarmSwitchXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.SET_CAMERA_PARAM_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_ALARM_SWITCH_GET:
			// 报警开关获取
			reqString = mXmlAssembler
					.assembleGetDevAlarmSwitchXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.GET_CAMERA_PARAM_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_QUERY_DEVICE_ALARM_FILE:
			// 设备上告警文件查询
			reqString = mXmlAssembler
					.assembleQueryRecordRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.QUERY_RECORD_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;
		case MVPCommand.VCP_QUERY_CLOUD_ALARM_FILE:
			// 中心服务器上告警文件查询
			reqString = mXmlAssembler
					.assembleQueryCloudRecordRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.QUERY_CLOUD_RECORD;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}
			break;

		case MVPCommand.VCP_GET_SERVER_INFO:
			// 获取服务器信息(p2p)
			reqString = mXmlAssembler
					.assembleGetServerInfoRequestXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.GET_SERVER_INFO_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}

			break;

		case MVPCommand.VCP_RESTART_DEVICE:
			// 重启设备
			reqString = mXmlAssembler.assembleReStartDeviceXml(mCommandCount);
			if (null == reqString) {
				ret = false;
			} else {
				urlPath = MVPCommand.HTTP + serverIP + serverPort
						+ MVPCommand.RESTART_DEVICE_URL;
				executeRequest(cmd, urlPath, reqString);
				ret = true;
			}

			break;
		default:
			break;
		}
		return ret;
	}

	void executeRequest(int curCmd, String path, String posData) {
		if (mHttpTask != null) {
			mHttpTask.cancel(true);
			mHttpTask = null;
		}
		mHttpTask = new HttpTask();
		mHttpTask.execute(path, posData);
		mCurCommand = curCmd;
	}

	private TemobiHttp temobiHttp;

	void executeRequest2(int curCmd, String path, String postData) {

		mCurCommand = curCmd;
		// TODO Auto-generated method stub
		String sessionId = null;
		if (ProtocalData.mLoginInfo == null) {
			sessionId = "";
		} else {
			sessionId = ProtocalData.currentSessionId;
		}
		String devId;
		if (ProtocalData.mCurrentDevNumber != null) {
			devId = ProtocalData.mCurrentDevNumber;
		} else {
			devId = "";
		}
		String cameraId = null;
		if (ProtocalData.mCurrentCameraId != null) {
			cameraId = ProtocalData.mCurrentCameraId;
		} else {
			cameraId = "";
		}
		String operId = null;
		if (ProtocalData.operId != null) {
			operId = ProtocalData.operId;
		} else {
			operId = "";
		}
		Log.d(TAG, "doInBackground devId=" + devId + ";cameraId=" + cameraId);

		temobiHttp.doPost(path, postData, sessionId, devId, cameraId, operId,
				listener, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

						notifyObserver(mCurCommand, -1);

						LoggerUtil.d("Temobihttp失败" + error.getMessage());

					}

				});

	}

	Response.Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			boolean result = true;
			try {

				// LoggerUtil.d("Temobihttp返回" + response);
				responseData = response.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int errcode = -1;

			switch (mCurCommand) {
			case MVPCommand.VCP_GET_DEVINFO:
				errcode = mXmlParser.parseGetDevInfoResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_QUERY_CAMERA_PARAM:
			case MVPCommand.VCP_GET_DEV_VERSOION:
				errcode = mXmlParser.parseGetCameraParamResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_SET_CAMERA_PARAM:
				errcode = mXmlParser.parseDeviceParamSetPasswordResponse(
						result, responseData);
				break;
			case MVPCommand.VCP_SET_CAMERA_PARAM_PASSWORD:
				errcode = mXmlParser.parseDeviceParamSetPasswordResponse(
						result, responseData);
				break;

			case MVPCommand.VCP_SET_CAMERA_PARAM_OSD:
				errcode = mXmlParser.parseDeviceParamSetOSDResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_MEDIA_PLAY:
				errcode = mXmlParser.parseMediaPlayResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_MEDIA_QUIT:
				errcode = mXmlParser.parseMediaQuitResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_MODIFY_PWD:
				errcode = mXmlParser.parseSimpleResponse(result, responseData);
				break;

			case MVPCommand.VCP_HEARTBEAT:
				errcode = mXmlParser.parseSimpleResponse(result, responseData);
				break;
			case MVPCommand.VCP_MEDIA_AUTH:
				errcode = mXmlParser.parseMediaAuthResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_CAMERA_CTRL:
				errcode = mXmlParser.parseCameraCtrlResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_RTSP_URL:
				errcode = mXmlParser.parseRtspUrlResponse(result, responseData);
				break;
			case MVPCommand.VCP_CAMERA_LIST:
				errcode = mXmlParser.parseBindIdResponse(result, responseData);
				break;
			case MVPCommand.VCP_QUERY_RECORD:
				errcode = mXmlParser.parseQueryRecordResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_TERMINAL_STATUS:
				errcode = mXmlParser.parseTerminalDeviceResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_MEDIA_PLAY_RECORD:
				errcode = mXmlParser.parseRecordPlayResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_GET_CLOUDRECORD:
				errcode = mXmlParser.parseQueryCloudRecordResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_GET_DEV_PLAN:
				errcode = mXmlParser.parseQueryDevPlanResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_SET_DEV_PLAN:
				errcode = mXmlParser.parseSetDevPlanResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_GET_SOUNDTALK_SERVER:
				errcode = mXmlParser.parseSoundServerResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_GET_RECORD_DOWNLOAD_SERVER:
				errcode = mXmlParser.parseRecordDownLoadServerResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_MEDIA_PLAY_CLOUD_RECORD:
				errcode = mXmlParser.parseCloudRecordPlayResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_CLOUD_RECORD_DEL:
				// 云 录像删除
				errcode = mXmlParser.parseDelCloudRecordResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_RECORD_DEL:
				// 设备 录像删除
				errcode = mXmlParser.parseDelDeviceRecordResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_DEVICEABILITY_QUERY:
				// 设备能力查询
				errcode = mXmlParser.parseDeviceAblityResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_CLOUD_RECORDSET_FLAG:
				// 中心录像开关设置
				errcode = mXmlParser.parseCloudRecordFlagResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_CLOUD_RECORDGET_FLAG:
				// 中心录像开关查询
				errcode = mXmlParser.parseGetCloudRecordFlagResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_QUERY_ALARM_ATTACH:
				// 告警附件查询
				errcode = mXmlParser.parseAlarmAttchResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_QUERY_ALARM_FILE_BY_ID:
				// 按报警ID查询
				errcode = mXmlParser.parseAlarmAttchResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_ALARM_SWITCH_SET:
				// 告警开关设置
				errcode = mXmlParser.parseAlarmSwitchSetResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_ALARM_SWITCH_GET:
				// 告警开关查询
				errcode = mXmlParser.parseGetCameraParamResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_QUERY_DEVICE_ALARM_FILE:
				// 设备上告警文件查询
				errcode = mXmlParser.parseQueryRecordResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_QUERY_CLOUD_ALARM_FILE:
				// 云服务器上告警文件查询
				errcode = mXmlParser.parseQueryCloudRecordResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_GET_SERVER_INFO:
				// 获取服务器IP
				errcode = mXmlParser.parseGetServerInfoResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_RESTART_DEVICE:
				errcode = mXmlParser.parseReStartDeviceResponse(result,
						responseData);
				break;

			default:
				break;
			}
			notifyObserver(mCurCommand, errcode);

		}

	};

	class HttpTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			String sessionId = null;
			if (ProtocalData.mLoginInfo == null) {
				sessionId = "";
			} else {
				sessionId = ProtocalData.currentSessionId;
			}
			String devId;
			if (ProtocalData.mCurrentDevNumber != null) {
				devId = ProtocalData.mCurrentDevNumber;
			} else {
				devId = "";
			}
			String cameraId = null;
			if (ProtocalData.mCurrentCameraId != null) {
				cameraId = ProtocalData.mCurrentCameraId;
			} else {
				cameraId = "";
			}
			String operId = null;
			if (ProtocalData.operId != null) {
				operId = ProtocalData.operId;
			} else {
				operId = "";
			}
			Log.d(TAG, "doInBackground devId=" + devId + ";cameraId="
					+ cameraId);
//			responseData = mHttpManager.doPost(params[0], params[1].getBytes(),
//					sessionId, devId, cameraId, operId);
			// /test
			String testString = httpClientTool.doPost(params[0], params[1],
					sessionId, devId, cameraId, operId);
			try {
				responseData = testString.getBytes("UTF8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// /test
			if (responseData == null || responseData.length <= 0) {
				Log.e(TAG, "responseData is null");
				return false;
			} else {
				return true;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			int errcode = -1;
			switch (mCurCommand) {
			case MVPCommand.VCP_GET_DEVINFO:
				errcode = mXmlParser.parseGetDevInfoResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_QUERY_CAMERA_PARAM:
			case MVPCommand.VCP_GET_DEV_VERSOION:
				errcode = mXmlParser.parseGetCameraParamResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_SET_CAMERA_PARAM:
				errcode = mXmlParser.parseDeviceParamSetPasswordResponse(
						result, responseData);
				break;
			case MVPCommand.VCP_SET_CAMERA_PARAM_PASSWORD:
				errcode = mXmlParser.parseDeviceParamSetPasswordResponse(
						result, responseData);
				break;

			case MVPCommand.VCP_SET_CAMERA_PARAM_OSD:
				errcode = mXmlParser.parseDeviceParamSetOSDResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_MEDIA_PLAY:
				errcode = mXmlParser.parseMediaPlayResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_MEDIA_QUIT:
				errcode = mXmlParser.parseMediaQuitResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_MODIFY_PWD:
				errcode = mXmlParser.parseSimpleResponse(result, responseData);
				break;

			case MVPCommand.VCP_HEARTBEAT:
				errcode = mXmlParser.parseSimpleResponse(result, responseData);
				break;
			case MVPCommand.VCP_MEDIA_AUTH:
				errcode = mXmlParser.parseMediaAuthResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_CAMERA_CTRL:
				errcode = mXmlParser.parseCameraCtrlResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_RTSP_URL:
				errcode = mXmlParser.parseRtspUrlResponse(result, responseData);
				break;
			case MVPCommand.VCP_CAMERA_LIST:
				errcode = mXmlParser.parseBindIdResponse(result, responseData);
				break;
			case MVPCommand.VCP_QUERY_RECORD:
				errcode = mXmlParser.parseQueryRecordResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_TERMINAL_STATUS:
				errcode = mXmlParser.parseTerminalDeviceResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_MEDIA_PLAY_RECORD:
				errcode = mXmlParser.parseRecordPlayResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_GET_CLOUDRECORD:
				errcode = mXmlParser.parseQueryCloudRecordResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_GET_DEV_PLAN:
				errcode = mXmlParser.parseQueryDevPlanResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_SET_DEV_PLAN:
				errcode = mXmlParser.parseSetDevPlanResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_GET_SOUNDTALK_SERVER:
				errcode = mXmlParser.parseSoundServerResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_GET_RECORD_DOWNLOAD_SERVER:
				errcode = mXmlParser.parseRecordDownLoadServerResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_MEDIA_PLAY_CLOUD_RECORD:
				errcode = mXmlParser.parseCloudRecordPlayResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_CLOUD_RECORD_DEL:
				// 云 录像删除
				errcode = mXmlParser.parseDelCloudRecordResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_RECORD_DEL:
				// 设备 录像删除
				errcode = mXmlParser.parseDelDeviceRecordResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_DEVICEABILITY_QUERY:
				// 设备能力查询
				errcode = mXmlParser.parseDeviceAblityResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_CLOUD_RECORDSET_FLAG:
				// 中心录像开关设置
				errcode = mXmlParser.parseCloudRecordFlagResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_CLOUD_RECORDGET_FLAG:
				// 中心录像开关查询
				errcode = mXmlParser.parseGetCloudRecordFlagResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_QUERY_ALARM_ATTACH:
				// 告警附件查询
				errcode = mXmlParser.parseAlarmAttchResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_QUERY_ALARM_FILE_BY_ID:
				// 按报警ID查询
				errcode = mXmlParser.parseAlarmAttchResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_ALARM_SWITCH_SET:
				// 告警开关设置
				errcode = mXmlParser.parseAlarmSwitchSetResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_ALARM_SWITCH_GET:
				// 告警开关查询
				errcode = mXmlParser.parseGetCameraParamResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_QUERY_DEVICE_ALARM_FILE:
				// 设备上告警文件查询
				errcode = mXmlParser.parseQueryRecordResponse(result,
						responseData);
				break;
			case MVPCommand.VCP_QUERY_CLOUD_ALARM_FILE:
				// 云服务器上告警文件查询
				errcode = mXmlParser.parseQueryCloudRecordResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_GET_SERVER_INFO:
				// 获取服务器IP
				errcode = mXmlParser.parseGetServerInfoResponse(result,
						responseData);
				break;

			case MVPCommand.VCP_RESTART_DEVICE:
				errcode = mXmlParser.parseReStartDeviceResponse(result,
						responseData);
				break;

			default:
				break;
			}
			notifyObserver(mCurCommand, errcode);
		}
	}

	public void cancelAllHttp() {
		if (temobiHttp != null) {
			temobiHttp.cancelAll();
		}
	}

	// 发送心跳的异步task
	class HeartBeatTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			String sessionId = null;
			if (ProtocalData.currentSessionId != null) {
				sessionId = ProtocalData.currentSessionId;
			} else {
				sessionId = "";
			}
			String cameraId;
			if (ProtocalData.mCurrentCameraId != null) {
				cameraId = ProtocalData.mCurrentCameraId;
			} else {
				cameraId = "";
			}
			String devId;
			if (ProtocalData.mCurrentDevNumber != null) {
				devId = ProtocalData.mCurrentDevNumber;
			} else {
				devId = "";
			}
			String operId = null;
			if (ProtocalData.operId != null) {
				operId = ProtocalData.operId;
			} else {
				operId = "";
			}

			HBresponseData = mHttpManager.doPost(params[0],
					params[1].getBytes(), sessionId, devId, cameraId, operId);
			if (HBresponseData == null || HBresponseData.length <= 0) {
				Log.e(TAG, "HBresponseData is null");
				return false;
			} else {
				return true;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			int errcode = mXmlParser
					.parseSimpleResponse(result, HBresponseData);
			Log.i(TAG, "YJT_HEARTBEAT errcode:" + errcode);
			if (errcode == 0) {
				sendMsg(mHeartBeatHander.SEND_HEARTBEAT, mTimeInterval);
				mHeartBeatErrCount = 0;
			} else {
				mHeartBeatErrCount++;
				if (mHeartBeatErrCount >= MAX_ERR_COUNT) {
					// 通知UI心跳失败
					notifyObserver(MVPCommand.VCP_HEARTBEAT, errcode);
				} else {
					sendMsg(mHeartBeatHander.SEND_HEARTBEAT, mTimeInterval);
				}
			}
		}

	}

	// 发消息
	private void sendMsg(int what, int delay) {
		Message msg = new Message();
		msg.what = what;
		mHeartBeatHander.sendMessageDelayed(msg, delay);
	}

	// handler
	class MyHander extends Handler {
		public final int SEND_HEARTBEAT = 1;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SEND_HEARTBEAT:
				sendHeartBeat();
				break;
			default:
				break;
			}
		}
	}

}
