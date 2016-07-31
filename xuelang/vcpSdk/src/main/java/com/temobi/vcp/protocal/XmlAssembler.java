package com.temobi.vcp.protocal;

import com.temobi.vcp.protocal.data.DevPlan;
import com.temobi.vcp.protocal.data.ProtocalData;
import com.temobi.vcp.protocal.data.RecordInfo;

import android.os.Build;
import android.util.Log;

public class XmlAssembler {
	private static String TAG = "XmlAssembler";

	public String assembleModifyPwdXml(int cmdCount) {
		if (ProtocalData.mModifyPwd == null || ProtocalData.mLoginInfo == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"PasswordModifyRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<PasswordModifyRequest>\n");
			reqbuf.append("		<SessionId>" + ProtocalData.mLoginInfo.SessionId
					+ "</SessionId>\n");
			reqbuf.append("		<OldPassword>"
					+ ProtocalData.mModifyPwd.OldPassword + "</OldPassword>\n");
			reqbuf.append("		<NewPassword>"
					+ ProtocalData.mModifyPwd.NewPassword + "</NewPassword>\n");
			reqbuf.append("     <Enc_Mode>" + ProtocalData.mModifyPwd.Enc_Mode
					+ "</Enc_Mode>\n");
			reqbuf.append("	</PasswordModifyRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "assembleModifyPwdXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	public String assembleGetDevInfoXml(int cmdCount) {
		if (ProtocalData.mLoginInfo.Dev_List == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"DevInfoRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<DevInfoRequest>\n");

			reqbuf.append("<DevSN>");

			String deviceIdStr = "";
			for (int i = 0; i < ProtocalData.mLoginInfo.Dev_List.size(); i++) {
				deviceIdStr += ProtocalData.mLoginInfo.Dev_List.get(i) + ",";

			}
			reqbuf.append(deviceIdStr.substring(0, deviceIdStr.length() - 1));
			reqbuf.append("</DevSN>\n");
			reqbuf.append("	</DevInfoRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "assembleGetDevInfoXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/*
	 * 获取设备参数
	 */
	public String assembleGetDevParamXml(int cmdCount) {
		if (ProtocalData.mCameraParamCategoryInfo == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"GET_DEV_CONFIG\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<CategoryInfo Value=\""
					+ ProtocalData.mCameraParamCategoryInfo + "\"/>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "assembleGetDevParamXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 设置设备密码
	 * 
	 * @param cmdCount
	 * @return
	 */
	public String assembleSetDevParamPassWordXml(int cmdCount) {
		if (ProtocalData.mDeviceSecurityInfo == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"SET_DEV_CONFIG\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<SecurityInfo oldPwd=\""
					+ ProtocalData.mDeviceSecurityInfo.oldPwd + "\" newPwd=\""
					+ ProtocalData.mDeviceSecurityInfo.newPwd + "\"/>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleSetDevParamPassWordXml reqbuf="
							+ reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 设置设备OSD信息
	 * 
	 * @param cmdCount
	 * @return
	 */
	public String assembleSetDevParamOSDXml(int cmdCount) {
		if (ProtocalData.osddata == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"ChangeDevParamRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<OSDInfo Name=\"" + ProtocalData.osddata.Name
					+ "\" Flag=\"" + ProtocalData.osddata.Flag + "\"/>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "assembleSetDevOSDXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 一键设防，布防设置(报警开关)
	 * 
	 * @param cmdCount
	 * @return
	 */
	public String assembleSetDevAlarmSwitchXml(int cmdCount) {
		if (ProtocalData.mDeviceSwitchCfg == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"SET_DEV_CONFIG\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<SwitchInfo Guard=\""
					+ ProtocalData.mDeviceSwitchCfg.nSwitchFlag + "\"/>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleSetDevAlarmSwitchXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 一键设防，布防查询(报警开关)
	 * 
	 * @param cmdCount
	 * @return
	 */
	public String assembleGetDevAlarmSwitchXml(int cmdCount) {
		if (ProtocalData.mDevPlanCategoryInfo == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"GET_DEV_CONFIG\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<CategoryInfo Value=\""
					+ ProtocalData.mDevPlanCategoryInfo + "\"/>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleGetDevAlarmSwitchXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/*
	 * 视频实时预览请求
	 */
	public String assembleMediaPlayXml(int cmdCount) {
		if (ProtocalData.mCurrentDevNumber == null
				|| ProtocalData.mCurrentCameraId == null
				|| ProtocalData.mCurrentStreamId == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"MediaPlaytRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<MediaPlaytRequest>\n");
			reqbuf.append("  <DevSN>" + ProtocalData.mCurrentDevNumber
					+ "</DevSN>\n");
			reqbuf.append("<CameraId>" + ProtocalData.mCurrentCameraId
					+ "</CameraId>\n");
			reqbuf.append("<StreamType>" + ProtocalData.mCurrentStreamId
					+ "</StreamType>\n");
			reqbuf.append("	</MediaPlaytRequest>\n");
			reqbuf.append("</Message>\n");
			//Log.i(TAG, "assembleMediaPlayXml reqbuf=" + reqbuf.toString());
			LoggerUtil.d("assembleMediaPlayXml" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/*
	 * 视频实时预览退出
	 */
	public String assembleMediaQuitXml(int cmdCount) {
		if (ProtocalData.mCurrentDevNumber == null
				|| ProtocalData.mCurrentCameraId == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"MediaQuitRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<MediaQuitRequest>\n");
			reqbuf.append("<DevSN>" + ProtocalData.mCurrentDevNumber
					+ "</DevSN>\n");
			reqbuf.append("<CameraId>" + ProtocalData.mCurrentCameraId
					+ "</CameraId>\n");
			if (ProtocalData.mCurrentStreamId == null
					|| ProtocalData.mCurrentStreamId.equals("")) {
				reqbuf.append("<StreamType>1</StreamType>\n");
			} else {
				reqbuf.append("<StreamType>" + ProtocalData.mCurrentStreamId
						+ "</StreamType>\n");
			}

			reqbuf.append("</MediaQuitRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "assembleMediaQuitXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/*
	 * 设备录像预览请求
	 */
	public String assembleRecordPlayXml(int cmdCount) {
		if (ProtocalData.mCurrentDevNumber == null
				|| ProtocalData.mCurrentCameraId == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"RecordPlayRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<RecordPlayRequest>\n");
			reqbuf.append("<RecordId>"
					+ ProtocalData.currentRecordFile.ContentId
					+ "</RecordId>\n");
			reqbuf.append("  <DevSN>" + ProtocalData.mCurrentDevNumber
					+ "</DevSN>\n");
			reqbuf.append("<CameraId>" + ProtocalData.mCurrentCameraId
					+ "</CameraId>\n");
			reqbuf.append("<Range>0</Range>\n");
			reqbuf.append("<PlayType>" + ProtocalData.mPlayRecordType
					+ "</PlayType>\n");
			reqbuf.append("	</RecordPlayRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "assembleRecordPlayXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/*
	 * 云录像预览请求
	 */
	public String assembleCloudRecordPlayXml(int cmdCount) {
		if (ProtocalData.mCloudRecordPlay == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"CloundRecordPlayRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<CloundRecordPlayRequest>\n");
			reqbuf.append("<RecordId>" + ProtocalData.mCloudRecordPlay.RecordId
					+ "</RecordId>\n");
			reqbuf.append("  <DevSN>" + ProtocalData.mCloudRecordPlay.DevSN
					+ "</DevSN>\n");
			reqbuf.append("<CameraId>" + ProtocalData.mCloudRecordPlay.CameraId
					+ "</CameraId>\n");
			reqbuf.append("<ServerId>" + ProtocalData.mCloudRecordPlay.ServerId
					+ "</ServerId>\n");

			reqbuf.append("	</CloundRecordPlayRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "assembleCloudRecordPlayXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	public String assembleHeartBeatXml(int cmdCount) {
		if (ProtocalData.mLoginInfo == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"HeartbeatRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<HeartbeatRequest>\n");
			reqbuf.append("		<SessionId>" + ProtocalData.mLoginInfo.SessionId
					+ "</SessionId>\n");
			reqbuf.append("	</HeartbeatRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "assembleHeartBeatXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	public String assembleRtspUrlRequestXml(int cmdCount) {
		if (ProtocalData.mRtspUrlRequest == null
				|| ProtocalData.mLoginInfo == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"RtspUrlRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<RtspUrlRequest>\n");
			reqbuf.append("		<SessionId>" + ProtocalData.mLoginInfo.SessionId
					+ "</SessionId>\n");
			reqbuf.append("		<LogonId>" + ProtocalData.mLoginInfo.LoginId
					+ "</LogonId>\n");
			reqbuf.append("		<CameraId>"
					+ ProtocalData.mRtspUrlRequest.CameraId + "</CameraId>\n");
			reqbuf.append("	</RtspUrlRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "assembleRtspUrlRequestXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	public String assembleBindIdRequestXml(int cmdCount) {
		if (ProtocalData.mLoginInfo == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"BindIdRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<BindIdRequest>\n");
			reqbuf.append("		<SessionId>" + ProtocalData.mLoginInfo.SessionId
					+ "</SessionId>\n");
			reqbuf.append("		<LogonId>" + ProtocalData.mLoginInfo.LoginId
					+ "</LogonId>\n");
			reqbuf.append("	</BindIdRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "assembleBindIdRequestXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/*
	 * 设备云台鉴权请求
	 */
	public String assembleMediaAuthRequestXml(int cmdCount) {
		if (ProtocalData.mCurrentDevNumber == null
				|| ProtocalData.mCurrentCameraId == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"MediaAuthRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<MediaAuthRequest>\n");
			reqbuf.append("		<DevSN>" + ProtocalData.mCurrentDevNumber
					+ "</DevSN>\n");
			reqbuf.append("		<CameraId>" + ProtocalData.mCurrentCameraId
					+ "</CameraId>\n");
			reqbuf.append("	</MediaAuthRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleMediaAuthRequestXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	// 6.13. 宜居通摄像头云台控制消息
	public String assembleCameraCtrlRequestXml(int cmdCount) {
		if (ProtocalData.mCameraCtrl == null) {
			return null;// assemble xml fail
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header MsgType=\"MSG_PTZ_SET_REQ\" MsgSeq=\""
					+ cmdCount + "\"/>\n");

			reqbuf.append("<PTZ ScId=\"" + ProtocalData.mCameraCtrl.devId
					+ "\" CameraId=\"" + ProtocalData.mCameraCtrl.CameraId
					+ "\" OpCode=\"" + ProtocalData.mCameraCtrl.OpCode
					+ "\" Param1=\"" + ProtocalData.mCameraCtrl.Param1
					+ "\" Param2=\"" + ProtocalData.mCameraCtrl.Param2
					+ "\"/>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleCameraCtrlRequestXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	// 摄像头录像查询消息
	public String assembleQueryRecordRequestXml(int cmdCount) {
		if (null == ProtocalData.mQueryRecord) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"2.0\">\n");
			reqbuf.append("	<Header  MsgType=\"GET_DEV_RECORDS\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("<PageInfo BeginIndex=\""
					+ ProtocalData.mQueryRecord.BeginInex + "\" RecordNum=\""
					+ ProtocalData.mQueryRecord.RecordNum + "\"/>\n");
			reqbuf.append("<CameraId>" + ProtocalData.mQueryRecord.CAMERA_ID
					+ "</CameraId>\n");
			reqbuf.append("<RecordQueryInfo BeginTime=\""
					+ ProtocalData.mQueryRecord.BeginTime + "\" EndTime=\""
					+ ProtocalData.mQueryRecord.EndTime + "\" OrderBy=\""
					+ ProtocalData.mQueryRecord.OrderBy + "\">\n");

			reqbuf.append("<RecordTypeList>\n");
			for (int i = 0; i < ProtocalData.mQueryRecord.EVENT_LIST.size(); i++) {
				reqbuf.append("<RecordType>"
						+ ProtocalData.mQueryRecord.EVENT_LIST.get(i)
						+ "</RecordType>\n");
			}
			reqbuf.append("</RecordTypeList>\n");
			reqbuf.append("</RecordQueryInfo>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleQueryRecordRequestXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 获取云录像列表
	 * 
	 * @param cmdCount
	 * @return
	 */

	public String assembleQueryCloudRecordRequestXml(int cmdCount) {
		if (null == ProtocalData.mQueryRecord) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header MsgType=\"QueryCloudRecordRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("<QueryCloudRecordRequest>\n");
			reqbuf.append("<DevSN>" + ProtocalData.mQueryRecord.devId
					+ "</DevSN>\n");
			reqbuf.append("<CameraId>" + ProtocalData.mQueryRecord.CAMERA_ID
					+ "</CameraId>\n");
			reqbuf.append("<PageNo>" + ProtocalData.mQueryRecord.BeginInex
					+ "</PageNo>\n");
			reqbuf.append("<PageSize>" + ProtocalData.mQueryRecord.RecordNum
					+ "</PageSize>\n");
			reqbuf.append("<BeginTime>" + ProtocalData.mQueryRecord.BeginTime
					+ "</BeginTime>\n");
			reqbuf.append("<EndTime>" + ProtocalData.mQueryRecord.EndTime
					+ "</EndTime>\n");
			String recordType = "";
			for (int i = 0; i < ProtocalData.mQueryRecord.EVENT_LIST.size(); i++) {

				recordType += ProtocalData.mQueryRecord.EVENT_LIST.get(i) + ",";
			}
			recordType = recordType.substring(0, recordType.length() - 1);
			reqbuf.append("<RecordType>" + recordType + "</RecordType>\n");

			reqbuf.append("<Type>" + ProtocalData.mQueryRecord.Type
					+ "</Type>\n");

			reqbuf.append("</QueryCloudRecordRequest>\n");

			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleQueryCloudRecordRequestXml reqbuf="
							+ reqbuf.toString());
			return reqbuf.toString();
		}

	}

	/**
	 * 获取设备计划
	 * 
	 * @param cmdCount
	 * @return
	 */

	public String assembleQueryDevPlanRequestXml(int cmdCount) {
		if (null == ProtocalData.mDevPlanCategoryInfo) {
			return null;
		} else {

			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header MsgType=\"GET_DEV_PLAN\" MsgSeq=\""
					+ cmdCount + "\"/>\n");

			reqbuf.append("<CategoryInfo Value=\""
					+ ProtocalData.mDevPlanCategoryInfo + "\"/>\n");

			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleQueryDevPlanRequestXml reqbuf="
							+ reqbuf.toString());
			return reqbuf.toString();
		}

	}

	/**
	 * 设置设备计划
	 * 
	 * @param cmdCount
	 * @return
	 */
	public String assembleSetDevPlanRequestXml(int cmdCount) {
		if (null == ProtocalData.mDevPlanList) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header MsgType=\"SET_DEV_PLAN\" MsgSeq=\""
					+ cmdCount + "\"/>\n");

			reqbuf.append("<PlanList Type=\"" + ProtocalData.mDevPlanList.type
					+ "\">\n");

			for (int i = 0; i < ProtocalData.mDevPlanList.planList.size(); i++) {
				DevPlan devPlan = ProtocalData.mDevPlanList.planList.get(i);

				reqbuf.append("<Plan PID=\"" + devPlan.pid + "\" Enable=\""
						+ devPlan.enable + "\" Cycle=\"" + devPlan.cycle
						+ "\" Period=\"" + devPlan.period + "\">\n");
				reqbuf.append(" <WeekInfo>");
				reqbuf.append("<Week1>" + devPlan.week1 + "</Week1>\n"
						+ "<Week2>" + devPlan.week2 + "</Week2>\n" + "<Week3>"
						+ devPlan.week3 + "</Week3>\n" + "<Week4>"
						+ devPlan.week4 + "</Week4>\n" + "<Week5>"
						+ devPlan.week5 + "</Week5>\n" + "<Week6>"
						+ devPlan.week6 + "</Week6>\n" + "<Week7>"
						+ devPlan.week7 + "</Week7>\n");
				reqbuf.append(" </WeekInfo>");
				reqbuf.append("</Plan>");

			}
			reqbuf.append("</PlanList>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleSetDevPlanRequestXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}

	}

	public String assembleSetAlarmRequestXml(int cmdCount) {
		if (null == ProtocalData.mSetAlarm) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"SetAlarmRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<SetAlarmRequest>\n");
			reqbuf.append("		<SessionId>" + ProtocalData.mLoginInfo.SessionId
					+ "</SessionId>\n");
			reqbuf.append("		<Type>" + ProtocalData.mSetAlarm.Type
					+ "</Type>\n");
			reqbuf.append("		<Status>" + ProtocalData.mSetAlarm.Status
					+ "</Status>\n");
			reqbuf.append("		<Name>" + ProtocalData.mSetAlarm.Name
					+ "</Name>\n");
			reqbuf.append("	</SetAlarmRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "SetAlarmRequest reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	public String assembleTerminalDeviceRequestXml(int cmdCount) {
		if (null == ProtocalData.mTerminalDevice) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"TerminalDeviceRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<TerminalDeviceRequest>\n");
			reqbuf.append("		<SessionId>" + ProtocalData.mLoginInfo.SessionId
					+ "</SessionId>\n");
			reqbuf.append("		<Type>" + ProtocalData.mTerminalDevice.Type
					+ "</Type>\n");
			reqbuf.append("	</TerminalDeviceRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleTerminalDeviceRequestXml reqbuf="
							+ reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 请求语音对讲需要的服务器信息
	 */
	public String assembleSoundTalkRequestXml(int cmdCount) {
		if (ProtocalData.mCurrentDevNumber == null
				|| ProtocalData.mCurrentCameraId == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"SoundTalkServerRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<SoundTalkServerRequest>\n");
			reqbuf.append("		<DevSN>" + ProtocalData.mCurrentDevNumber
					+ "</DevSN>\n");
			reqbuf.append("		<ChannelNo>" + ProtocalData.mCurrentCameraId
					+ "</ChannelNo>\n");
			reqbuf.append("	</SoundTalkServerRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleSoundTalkRequestXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 请求下载录像需要的服务器信息
	 */
	public String assembleRecordDownLoadRequestXml(int cmdCount) {
		if (ProtocalData.mRecordDownLoad == null) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"RecordDownRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<RecordDownRequest>\n");
			reqbuf.append("<RecordId>" + ProtocalData.mRecordDownLoad.RecordId
					+ "</RecordId>\n");
			reqbuf.append("<Range>" + ProtocalData.mRecordDownLoad.Range
					+ "</Range>\n");
			reqbuf.append("<DevSN>" + ProtocalData.mRecordDownLoad.DevSN
					+ "</DevSN>\n");
			reqbuf.append("<CameraId>" + ProtocalData.mRecordDownLoad.CameraId
					+ "</CameraId>\n");
			reqbuf.append("<Type>" + ProtocalData.mRecordDownLoad.Type
					+ "</Type>\n");
			reqbuf.append("<Location>" + ProtocalData.mRecordDownLoad.Location
					+ "</Location>\n");
			reqbuf.append("	</RecordDownRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleRecordDownLoadRequestXml reqbuf="
							+ reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 删除中心录像文件
	 */
	public String assembleDelCloudRecordRequestXml(int cmdCount) {
		if (ProtocalData.mRecordList == null) {
			return null;
		} else {
			String recordId = "";
			for (RecordInfo record : ProtocalData.mRecordList) {
				recordId = recordId + record.ContentId + ",";
			}
			recordId = recordId.substring(0, recordId.length() - 1);
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"DelRecordFileInfoRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<DelRecordFileInfoRequest>\n");
			reqbuf.append("		<DevSN>" + ProtocalData.mCurrentDevNumber
					+ "</DevSN>\n");
			reqbuf.append("		<CameraId>" + ProtocalData.mCurrentCameraId
					+ "</CameraId>\n");
			reqbuf.append("		<RecordId>" + recordId + "</RecordId>\n");
			reqbuf.append("		<Type>"
					+ ProtocalData.mRecordList.get(0).returnType + "</Type>\n");
			reqbuf.append("		<Location>"
					+ ProtocalData.mRecordList.get(0).Location
					+ "</Location>\n");
			reqbuf.append("	</DelRecordFileInfoRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleDelCloudRecordRequestXml reqbuf="
							+ reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 删除设备录像文件
	 */
	public String assembleDelDeviceRecordRequestXml(int cmdCount) {
		if (ProtocalData.mRecordList == null) {
			return null;
		} else {

			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"DEL_DEV_RECORDS\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<RecordList>\n");

			for (int i = 0; i < ProtocalData.mRecordList.size(); i++) {
				reqbuf.append("	<ContentId>"
						+ ProtocalData.mRecordList.get(i).ContentId
						+ "</ContentId>\n");
			}

			reqbuf.append("	</RecordList>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleDelDeviceRecordRequestXml reqbuf="
							+ reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 设备能力查询
	 */
	public String assembleAbilityRequestXml(int cmdCount) {
		if (ProtocalData.mDeviceAbility == null) {
			return null;
		} else {

			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"GetDeviceFunInfoRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<GetDeviceFunInfoRequest>\n");
			reqbuf.append("		<DevSN>" + ProtocalData.mDeviceAbility.sDeviceID
					+ "</DevSN>\n");
			reqbuf.append("	</GetDeviceFunInfoRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG, "assembleAbilityRequestXml reqbuf=" + reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 中心录像开关设置
	 */
	public String assembleCloudRecordSetFlagRequestXml(int cmdCount) {
		if (ProtocalData.mDeviceSwitchCfg == null) {
			return null;
		} else {

			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"SetCloudRecordInfoRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<SetCloudRecordInfoRequest>\n");
			reqbuf.append("		<DevSN>" + ProtocalData.mDeviceSwitchCfg.devID
					+ "</DevSN>\n");
			reqbuf.append("		<Flag>"
					+ ProtocalData.mDeviceSwitchCfg.nSwitchFlag + "</Flag>\n");
			reqbuf.append("	</SetCloudRecordInfoRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleCloudRecordSetFlagRequestXml reqbuf="
							+ reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 中心录像开关查询
	 */
	public String assembleCloudRecordGetFlagRequestXml(int cmdCount) {
		if (ProtocalData.mDeviceSwitchCfg == null) {
			return null;
		} else {

			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"GetCloudRecordInfoRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<GetCloudRecordInfoRequest>\n");
			reqbuf.append("		<DevSN>" + ProtocalData.mDeviceSwitchCfg.devID
					+ "</DevSN>\n");

			reqbuf.append("	</GetCloudRecordInfoRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleCloudRecordGetFlagRequestXml reqbuf="
							+ reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 报警附件查询接口
	 */
	public String assembleAlarmAttchRequestXml(int cmdCount) {
		if (ProtocalData.mAlarmAttachReq == null) {
			return null;
		} else {

			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"QueryAlarmAttachListRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<QueryAlarmAttachListRequest>\n");
			reqbuf.append("		<DevSN>" + ProtocalData.mAlarmAttachReq.DevSN
					+ "</DevSN>\n");
			reqbuf.append("		<CameraId>"
					+ ProtocalData.mAlarmAttachReq.CameraId + "</CameraId>\n");

			reqbuf.append("		<PageNo>" + ProtocalData.mAlarmAttachReq.PageNo
					+ "</PageNo>\n");

			reqbuf.append("		<PageSize>"
					+ ProtocalData.mAlarmAttachReq.PageSize + "</PageSize>\n");

			reqbuf.append("		<BeginTime>"
					+ ProtocalData.mAlarmAttachReq.BeginTime + "</BeginTime>\n");

			reqbuf.append("		<EndTime>" + ProtocalData.mAlarmAttachReq.EndTime
					+ "</EndTime>\n");

			reqbuf.append("		<AlarmID>" + ProtocalData.mAlarmAttachReq.AlarmID
					+ "</AlarmID>\n");
			reqbuf.append("		<Type>" + ProtocalData.mAlarmAttachReq.Type
					+ "</Type>\n");

			reqbuf.append("	</QueryAlarmAttachListRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleAlarmPictureRequestXml reqbuf="
							+ reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 按报警id查询接口
	 */
	public String assembleAlarmAttchByIdRequestXml(int cmdCount) {
		if (ProtocalData.mAlarmAttachReq == null) {
			return null;
		} else {

			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header  MsgType=\"QueryAlarmAttachListRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("	<QueryAlarmAttachListRequest>\n");
			reqbuf.append("		<DevSN>" + ProtocalData.mAlarmAttachReq.DevSN
					+ "</DevSN>\n");
			reqbuf.append("		<CameraId>"
					+ ProtocalData.mAlarmAttachReq.CameraId + "</CameraId>\n");

			reqbuf.append("		<PageNo>" + ProtocalData.mAlarmAttachReq.PageNo
					+ "</PageNo>\n");

			reqbuf.append("		<PageSize>"
					+ ProtocalData.mAlarmAttachReq.PageSize + "</PageSize>\n");

			reqbuf.append("		<AlarmID>" + ProtocalData.mAlarmAttachReq.AlarmID
					+ "</AlarmID>\n");
			reqbuf.append("		<Type>" + ProtocalData.mAlarmAttachReq.Type
					+ "</Type>\n");

			reqbuf.append("	</QueryAlarmAttachListRequest>\n");
			reqbuf.append("</Message>\n");
			Log.i(TAG,
					"assembleAlarmPictureRequestXml reqbuf="
							+ reqbuf.toString());
			return reqbuf.toString();
		}
	}

	/**
	 * 获取服务器信息（P2P）
	 * 
	 * @param cmdCount
	 * @return
	 */

	public String assembleGetServerInfoRequestXml(int cmdCount) {
		if (null == ProtocalData.serverInfo) {
			return null;
		} else {
			StringBuffer reqbuf = new StringBuffer();
			reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			reqbuf.append("<Message Version=\"1.0\">\n");
			reqbuf.append("	<Header MsgType=\"ServerInfoRequest\" MsgSeq=\""
					+ cmdCount + "\"/>\n");
			reqbuf.append("<ServerInfoRequest>\n");
			reqbuf.append("<ServerType>" + ProtocalData.serverInfo.ServerType
					+ "</ServerType>\n");
			reqbuf.append("<DevSN>" + ProtocalData.serverInfo.DevSN
					+ "</DevSN>\n");

			reqbuf.append("</ServerInfoRequest>\n");

			reqbuf.append("</Message>\n");
			Log.i(TAG, "获取服务器信息=assembleGetServerInfoRequestXml reqbuf="
					+ reqbuf.toString());
			return reqbuf.toString();
		}

	}

	/**
	 * 设备重启组装XML
	 * 
	 * @param cmdCount
	 * @return
	 */
	public String assembleReStartDeviceXml(int cmdCount) {

		StringBuffer reqbuf = new StringBuffer();
		reqbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		reqbuf.append("<Message Version=\"1.0\">\n");
		reqbuf.append("	<Header MsgType=\"NOTIFY_DEV_RESTART\" MsgSeq=\""
				+ cmdCount + "\"/>\n");

		reqbuf.append("</Message>\n");
		Log.i(TAG, "重启设备=assembleReStartDeviceXml reqbuf=" + reqbuf.toString());
		return reqbuf.toString();
	}

}
