package com.temobi.vcp.protocal;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.AlreadyConnectedException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.temobi.vcp.protocal.data.AlarmGuardEventInfo;
import com.temobi.vcp.protocal.data.CameraDiskInfo;
import com.temobi.vcp.protocal.data.CameraNetInfo;
import com.temobi.vcp.protocal.data.CameraOSDInfo;
import com.temobi.vcp.protocal.data.CameraParam;
import com.temobi.vcp.protocal.data.CameraSwitchInfo;
import com.temobi.vcp.protocal.data.DevPlan;
import com.temobi.vcp.protocal.data.Device;
import com.temobi.vcp.protocal.data.DeviceAbilityInfo;
import com.temobi.vcp.protocal.data.DeviceSwitchInfoCfg;
import com.temobi.vcp.protocal.data.MyCamera;
import com.temobi.vcp.protocal.data.Picture;
import com.temobi.vcp.protocal.data.PlanList;
import com.temobi.vcp.protocal.data.ProtocalData;
import com.temobi.vcp.protocal.data.RecordInfo;
import com.temobi.vcp.protocal.data.ServerInfo;
import com.temobi.vcp.protocal.data.SoundTalkServer;
import com.temobi.vcp.protocal.data.Stream;
import com.temobi.vcp.protocal.data.ThumbnailServer;

import android.util.Log;
import android.util.Xml;

public class XmlParser {

	private static String TAG = "XmlParser";

	int parseRtspUrlResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseRtspUrlResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "RtspUrlResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "RtspUrlResponse errcode=" + retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "RtspUrlResponse retDesc=" + retDesc);
						} else if (parser.getName().equals("RtspUrlResponse")) {

						} else if (parser.getName().equals("PlayUrl")) {
							eventType = parser.next();
							String PlayUrl = parser.getText();
							ProtocalData.mRtspUrlRequest.PlayUrl = PlayUrl;
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	int parseGetDevInfoResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseGetDevInfoResponse");
		int retCode = -1;
		Log.i(TAG, "parseGetDevInfoResponse");

		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseGetDevInfoResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				List<Device> deviceList = null;
				Device device = null;
				MyCamera camera = null;
				List<MyCamera> cameraList = null;
				Stream stream = null;
				ThumbnailServer thumbServer = null;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						cameraList = new ArrayList<MyCamera>();
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseGetDevInfoResponse errcode="
									+ retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "parseGetDevInfoResponse retDesc="
									+ retDesc);

						} else if (parser.getName().equals("DevInfoResponse")) {

						}
						// ThumbnailServer
						else if (parser.getName().equals("ThumbnailServer")) {
							thumbServer = new ThumbnailServer();
							thumbServer.host = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "host");
							thumbServer.port = Integer
									.parseInt(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE, "port"));

							ProtocalData.mThumbnailServer = thumbServer;

						} else if (parser.getName().equals("DevList")) {
							deviceList = new ArrayList<Device>();
						} else if (parser.getName().equals("dev")) {
							device = new Device();
							device.DeviceId = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "ID");
							device.Type = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Type");
							device.RunStatus = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Runstatus");
							device.P2PId = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "P2PId");

						} else if (parser.getName().equals("CameraList")) {
							cameraList = new ArrayList<MyCamera>();
						} else if (parser.getName().equals("StreamList")) {
							camera.Stream_List = new ArrayList<Stream>();
						} else if (parser.getName().equals("Stream")) {
							stream = new Stream();
							stream.ID = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "ID");
							stream.Name = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Name");
							stream.Protocal = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Protocol");
							camera.Stream_List.add(stream);
						} else if (parser.getName().equals("Camera")) {
							camera = new MyCamera();
							camera.CameraId = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "ID");
							camera.CameraName = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Name");
							camera.PTZType = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"PTZType"));
							camera.Type = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Type");
							String Runstatus = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Runstatus");
							// if (null == Runstatus) {
							//
							// } else {
							// camera.Runstatus = Integer.parseInt(parser
							// .getAttributeValue(
							// XmlPullParser.NO_NAMESPACE,
							// "Runstatus"));
							// }
							// String PTZType = parser.getAttributeValue(
							// XmlPullParser.NO_NAMESPACE, "PTZType");
							// if (null == PTZType) {
							//
							// } else {
							// camera.PTZType = Integer.parseInt(parser
							// .getAttributeValue(
							// XmlPullParser.NO_NAMESPACE,
							// "PTZType"));
							// }

						}
						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equals("StreamList")) {
							// camera.Stream_List.add(stream);
						} else if (parser.getName().equals("Camera")) {
							cameraList.add(camera);
						} else if (parser.getName().equals("CameraList")) {
							device.cameraList = cameraList;
						} else if (parser.getName().equals("dev")) {
							deviceList.add(device);
						} else if (parser.getName().equals("DevList")) {
							ProtocalData.mDeviceList = deviceList;

						}
						break;
					}
					eventType = parser.next();
				}

				// ProtocalData.mMyCameraList = cameraList;
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/*
	 * 请求设备参数响应报文解析
	 */
	public int parseGetCameraParamResponse(Boolean result, byte[] responseData) {
		int retCode = -1;
		if (result) {

			CameraParam cameraParam = new CameraParam();

			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");

				LoggerUtil.d("parseGetCameraParamResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseGetCameraParamResponse errcode="
									+ retCode);

						} else if (parser.getName().equals("NetInfo")) {
							CameraNetInfo netInfo = new CameraNetInfo();
							netInfo.NetType = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "NetType");
							netInfo.SignalStrength = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"SignalStrength"));
							cameraParam.cameraNetInfo = netInfo;

						} else if (parser.getName().equals("SwitchInfo")) {

							CameraSwitchInfo switchInfo = new CameraSwitchInfo();
							switchInfo.Guard = Integer
									.parseInt(parser
											.getAttributeValue(
													XmlPullParser.NO_NAMESPACE,
													"Guard"));
							switchInfo.Detection = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"Detection"));
							switchInfo.Sensitivity = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"Sensitivity"));
							switchInfo.SenAlarm = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"SenAlarm"));
							switchInfo.AlarmRec = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"AlarmRec"));
							switchInfo.SMS = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE, "SMS"));
							switchInfo.MMS = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE, "MMS"));
							switchInfo.SMSRestart = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"SMSRestart"));
							cameraParam.cameraSwitchInfo = switchInfo;

						} else if (parser.getName().equals("DiskInfo")) {
							CameraDiskInfo diskInfo = new CameraDiskInfo();
							diskInfo.Total = Integer
									.parseInt(parser
											.getAttributeValue(
													XmlPullParser.NO_NAMESPACE,
													"Total"));
							diskInfo.Used = Integer
									.parseInt(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE, "Used"));
							diskInfo.Status = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"Status"));
							cameraParam.cameraDiskInfo = diskInfo;

						} else if (parser.getName().equals("OSDInfo")) {

							CameraOSDInfo osdInfo = new CameraOSDInfo();
							osdInfo.Flag = Integer
									.parseInt(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE, "Flag"));
							osdInfo.Name = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Name");

							LoggerUtil.d("OSDInfo=" + "Flag:" + osdInfo.Flag
									+ "\n" + "Name:" + osdInfo.Name);

							cameraParam.cameraOsdInfo = osdInfo;
						} else if (parser.getName().equals("BasicInfo")) {

							MyCamera baseInfo = new MyCamera();

							baseInfo.UpStreamNum = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"UpStreamNum"));
							baseInfo.Msisdn = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Msisdn");
							baseInfo.Ver = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Ver");
							baseInfo.BuildTime = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "BuildTime");
							baseInfo.RunDuration = Long.parseLong(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RunDuration"));
							baseInfo.Company = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Company");
							cameraParam.cameraBaseInfo = baseInfo;

						}
						break;
					case XmlPullParser.END_TAG:

						if (parser.getName().equals("Message")) {

							if (cameraParam != null) {

								ProtocalData.mCameraParam = cameraParam;
							}
						}
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception ex) {

				Log.d(TAG,
						"parseGetCameraParamResponse-->exception:"
								+ ex.getMessage());
			}

		} else {
			retCode = -1;
		}
		return retCode;
	}

	public int parseDeviceParamSetPasswordResponse(Boolean result,
			byte[] responseData) {
		Log.i(TAG, "parseDeviceParamSetResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseDeviceParamSetResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseDeviceParamSetResponse RetCode="
									+ retCode);
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	public int parseDeviceParamSetOSDResponse(Boolean result,
			byte[] responseData) {
		Log.i(TAG, "parseDeviceParamSetResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseDeviceParamSetResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseDeviceParamSetResponse RetCode="
									+ retCode);
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	int parseMediaPlayResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseMediaPlayResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseMediaPlayResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				Stream stream = null;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						System.out.println("name " + parser.getName());
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseMediaPlayResponse errcode="
									+ retCode);
						} else if (parser.getName().equals("StreamIP")) {

							eventType = parser.next();

							// parser.require(XmlPullParser.START_TAG, null,
							// "StreamIP");
							String itemText = parser.getText();
							ProtocalData.mCurStream.ip = itemText/*
																 * parser.getText
																 * (
																 * )parser.nextText
																 * ()
																 */;
							// parser.nextTag(); // this call shouldn��t be
							// necessary!
							//
							// parser.require(XmlPullParser.END_TAG, null,
							// "StreamIP");
						} else if (parser.getName().equals("StreamPort")) {
							eventType = parser.next();
							String itemText = parser.getText();
							ProtocalData.mCurStream.port = Integer
									.parseInt(itemText);
						} else if (parser.getName().equals("MediaPlayResponse")) {
							// eventType = parser.next();
							if (stream == null) {
								stream = new Stream();
								ProtocalData.mCurStream = stream;
							}
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	int parseRecordPlayResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseRecordPlayResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseRecordPlayResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				Stream stream = null;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:

						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseRecordPlayResponse errcode="
									+ retCode);
						} else if (parser.getName().equals("StreamIP")) {

							eventType = parser.next();
							String itemText = parser.getText();
							ProtocalData.mCurStream.ip = itemText;

						} else if (parser.getName().equals("StreamPort")) {
							eventType = parser.next();
							String itemText = parser.getText();
							ProtocalData.mCurStream.port = Integer
									.parseInt(itemText);
						} else if (parser.getName().equals("DevSessionId")) {
							eventType = parser.next();
							String itemText = parser.getText();
							ProtocalData.mDevSessionId = itemText;
						}

						else if (parser.getName().equals("Transport")) {
							eventType = parser.next();
							String itemText = parser.getText();
							ProtocalData.mCurStream.Transport = itemText;
						} else if (parser.getName()
								.equals("RecordPlayResponse")) {
							// eventType = parser.next();
							if (stream == null) {
								stream = new Stream();
								ProtocalData.mCurStream = stream;
							}
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/*
	 * 解析云录像预览请求后 报文
	 */
	public int parseCloudRecordPlayResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseCloudRecordPlayResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseCloudRecordPlayResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:

						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseCloudRecordPlayResponse errcode="
									+ retCode);
						} else if (parser.getName().equals("StreamIP")) {

							ProtocalData.mCloudRecordPlay.ServerIP = parser
									.nextText();

						} else if (parser.getName().equals("StreamPort")) {
							ProtocalData.mCloudRecordPlay.ServerPort = parser
									.nextText();
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	int parseMediaQuitResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseMediaQuitResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseMediaQuitResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseMediaQuitResponse errcode="
									+ retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "parseMediaQuitResponse retDesc="
									+ retDesc);
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {

			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	int parseBindIdResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseBindIdResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "BindIdResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				MyCamera camera = null;
				List<MyCamera> cameraList = null;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						cameraList = new ArrayList<MyCamera>();
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "BindIdResponse errcode=" + retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "BindIdResponse retDesc=" + retDesc);
						} else if (parser.getName().equals("BindIdResponse")) {

						} else if (parser.getName().equals("CameraList")) {
							cameraList = new ArrayList<MyCamera>();
						} else if (parser.getName().equals("Camera")) {
							camera = new MyCamera();
							camera.CameraId = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Id");
							camera.CameraName = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Name");
							camera.Type = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Type");
							String Runstatus = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Runstatus");
							if (null == Runstatus) {

							} else {
								camera.Runstatus = Integer.parseInt(parser
										.getAttributeValue(
												XmlPullParser.NO_NAMESPACE,
												"Runstatus"));
							}
							String PTZType = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "PTZType");
							if (null == PTZType) {

							} else {
								camera.PTZType = Integer.parseInt(parser
										.getAttributeValue(
												XmlPullParser.NO_NAMESPACE,
												"PTZType"));
							}

						}
						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equals("Camera")) {
							cameraList.add(camera);
						}
						break;
					}
					eventType = parser.next();
				}
				ProtocalData.mMyCameraList = cameraList;
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	int parseMediaAuthResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseMediaAuthResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "MediaAuthResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						Log.d(TAG, "parseMediaAuthResponse xml tagname:"
								+ parser.getName());
						if (parser.getName().equalsIgnoreCase("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "MediaAuthResponse errcode=" + retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "MediaAuthResponse retDesc=" + retDesc);
						} else if (parser.getName().equalsIgnoreCase(
								"MediaAuthResponse")) {
							// ProtocalData.mMediaAuth = new MediaAuth();

						}
						// else
						// if(parser.getName().equalsIgnoreCase("MediaUserNum")){
						// eventType = parser.next();
						// String UserNum = parser.nextText();
						// ProtocalData.mMediaAuth.MediaUserNum =
						// Integer.valueOf(UserNum);
						// }
						else if (parser.getName().equalsIgnoreCase(
								"Haspivilege")) {
							// eventType = parser.next();
							int haspivilege = Integer
									.valueOf(parser.nextText());
							if (haspivilege == 0) {
								ProtocalData.Haspivilege = true;
							} else {
								ProtocalData.Haspivilege = false;
							}
						}

						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	int parseQueryRecordResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseQueryRecordResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "QueryRecordResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				RecordInfo record = null;
				List<RecordInfo> recordList = null;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "QueryRecordResponse errcode=" + retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "QueryRecordResponse retDesc=" + retDesc);
						} else if (parser.getName().equals(
								"GET_DEV_RECORDS_ACK")) {

						} else if (parser.getName().equals("PageInfo")) {
							ProtocalData.mQueryRecord.BeginInex = Integer
									.valueOf(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"BeginIndex"));
							ProtocalData.mQueryRecord.RecordNum = Integer
									.valueOf(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RecordNum"));
						} else if (parser.getName().equals("CameraId")) {
							ProtocalData.mQueryRecord.CAMERA_ID = parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"CameraId");
						} else if (parser.getName().equals("RecordInfoList")) {
							recordList = new ArrayList<RecordInfo>();
						} else if (parser.getName().equals("RecordInfo")) {
							record = new RecordInfo();
							record.Location = 1;// 1表示设备录像
							record.BeginTime = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "BeginTime");
							record.ContentId = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "ContentId");
							record.EndTime = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "EndTime");
							record.recordType = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RecordType");
							record.ContentSize = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "ContentSize");
							recordList.add(record);
						}
						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equals("RecordInfo")) {
							// recordList.add(record);
						}
						break;
					}
					eventType = parser.next();
				}
				ProtocalData.mQueryRecord.RecordInfos = recordList;
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 解析获取云录像列表 返回报文
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseQueryCloudRecordResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseQueryRecordResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseQueryCloudRecordResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				RecordInfo record = null;
				List<RecordInfo> recordList = null;
				List<AlarmGuardEventInfo> alarmFileList = null;
				AlarmGuardEventInfo alarmFile = null;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseQueryCloudRecordResponse errcode="
									+ retCode);
						} else if (parser.getName().equals("Records")) {
							recordList = new ArrayList<RecordInfo>();
						} else if ((parser.getName().equals("Record"))) {
							record = new RecordInfo();
							record.Location = Integer
									.parseInt(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE, "Type"));
							record.ContentSize = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "ContentSize");
							record.ContentId = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RecordId");
							record.ServerId = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "ServerId");
							record.EndTime = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "EndTime");
							record.BeginTime = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "BeginTime");
							record.recordType = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RecordType");

						} else if (parser.getName().equals("AttchList")) {
							// 告警记录集合
							alarmFileList = new ArrayList<AlarmGuardEventInfo>();
						} else if (parser.getName().equals("AlarmAttch")) {
							alarmFile = new AlarmGuardEventInfo();
							alarmFile.Type = Integer
									.parseInt(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE, "Type"));
							alarmFile.AttchURL = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "AttchURL");
							alarmFile.BeginTime = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "BeginTime");

							if (alarmFileList != null) {
								alarmFileList.add(alarmFile);
							}
						}
						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equals("Records")) {
							ProtocalData.mQueryRecord.RecordInfos = recordList;
						} else if (parser.getName().equals("Record")) {
							record.alarmAttachList = alarmFileList;
							recordList.add(record);
						}
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 解析获取录像计划，报警计划请求后报文
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseQueryDevPlanResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseQueryDevPlanResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseQueryDevPlanResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				DevPlan devPlan = null;
				PlanList devPlanList = null;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("PlanList")) {
							devPlanList = new PlanList();
							devPlanList.planList = new ArrayList<DevPlan>();
							devPlanList.type = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Type");

						} else if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
						}

						else if ((parser.getName().equals("Plan"))) {
							devPlan = new DevPlan();

							devPlan.pid = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE, "PID"));
							devPlan.enable = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"Enable"));
							devPlan.cycle = Integer
									.parseInt(parser
											.getAttributeValue(
													XmlPullParser.NO_NAMESPACE,
													"Cycle"));
							devPlan.period = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Period");

							// devPlanList.planList.add(devPlan);
						} else if ((parser.getName().equals("Week1"))) {
							devPlan.week1 = parser.nextText();

						} else if ((parser.getName().equals("Week2"))) {
							devPlan.week2 = parser.nextText();

						} else if ((parser.getName().equals("Week3"))) {
							devPlan.week3 = parser.nextText();

						} else if ((parser.getName().equals("Week4"))) {
							devPlan.week4 = parser.nextText();

						} else if ((parser.getName().equals("Week5"))) {
							devPlan.week5 = parser.nextText();

						} else if ((parser.getName().equals("Week6"))) {
							devPlan.week6 = parser.nextText();

						} else if ((parser.getName().equals("Week7"))) {
							devPlan.week7 = parser.nextText();

						}
						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equals("Plan")) {
							devPlanList.planList.add(devPlan);
						} else if (parser.getName().equals("PlanList")) {
							ProtocalData.mDevPlanList = devPlanList;
						}
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 解析设置设备计划请求后返回的报文
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	int parseSetDevPlanResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseSetDevPlanResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseSetDevPlanResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseCameraCtrlResponse errcode="
									+ retCode);

						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {

			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	int parseTerminalDeviceResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseTerminalDeviceResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "TerminalDevice xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "TerminalDevice errcode=" + retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "TerminalDevice retDesc=" + retDesc);
						} else if (parser.getName().equals(
								"TerminalDeviceResponse")) {

						} else if (parser.getName().equals("Type")) {
							eventType = parser.next();
							ProtocalData.mTerminalDevice.Type = parser
									.getText();
						} else if (parser.getName().equals("TermVersion")) {
							eventType = parser.next();
							ProtocalData.mTerminalDevice.TermVersion = parser
									.getText();
						} else if (parser.getName().equals("TermType")) {
							eventType = parser.next();
							ProtocalData.mTerminalDevice.TermType = parser
									.getText();
						} else if (parser.getName().equals("TermName")) {
							eventType = parser.next();
							ProtocalData.mTerminalDevice.TermName = parser
									.getText();
						} else if (parser.getName().equals("IMEI")) {
							eventType = parser.next();
							ProtocalData.mTerminalDevice.IMEI = parser
									.getText();
						} else if (parser.getName().equals("TermStatus")) {
							eventType = parser.next();
							ProtocalData.mTerminalDevice.TermStatus = parser
									.getText();
						} else if (parser.getName().equals("ArmStatus")) {
							eventType = parser.next();
							ProtocalData.mTerminalDevice.ArmStatus = parser
									.getText();
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	int parseCameraCtrlResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseCameraCtrlResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseCameraCtrlResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseCameraCtrlResponse errcode="
									+ retCode);
							// String retDesc =
							// parser.getAttributeValue(XmlPullParser.NO_NAMESPACE,
							// "RetDesc");
							// Log.i(TAG,
							// "parseSimpleResponse retDesc="+retDesc);
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {

			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	int parseSimpleResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseSimpleResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseSimpleResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseSimpleResponse errcode=" + retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "parseSimpleResponse retDesc=" + retDesc);
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {

			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 解析获取语音对讲服务器后的报文
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseSoundServerResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseSoundServerResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseSoundServerResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				SoundTalkServer server = null;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseSoundServerResponse errcode="
									+ retCode);
						} else if (parser.getName().equals("StreamIP")) {

							ProtocalData.mSoundTalkServer.StreamIP = parser
									.nextText();
						} else if (parser.getName().equals("StreamPort")) {

							ProtocalData.mSoundTalkServer.StreamPort = parser
									.nextText();
						} else if (parser.getName().equals(
								"SoundTalkServerResponse")) {
							if (server == null) {
								server = new SoundTalkServer();
								ProtocalData.mSoundTalkServer = server;
							}
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 解析获取下载所需要的服务器信息后的报文
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseRecordDownLoadServerResponse(Boolean result,
			byte[] responseData) {
		Log.i(TAG, "parseRecordDownLoadServerResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseRecordDownLoadServerResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				SoundTalkServer server = null;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG,
									"parseRecordDownLoadServerResponse errcode="
											+ retCode);
						} else if (parser.getName().equals("StreamIP")) {

							ProtocalData.mRecordDownLoad.StreamIP = parser
									.nextText();
						} else if (parser.getName().equals("StreamPort")) {

							ProtocalData.mRecordDownLoad.StreamPort = Integer
									.parseInt(parser.nextText());
						} else if (parser.getName().equals("DevSessionId")) {
							ProtocalData.mRecordDownLoad.DevSessionId = parser
									.nextText();
						} else if (parser.getName().equals("Transport")) {
							ProtocalData.mRecordDownLoad.Transport = parser
									.nextText();
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 删除云录像报文解析
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseDelCloudRecordResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseDelRecordResponse....");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseDelRecordResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						Log.d(TAG, "parseDelRecordResponse xml tagname:"
								+ parser.getName());
						if (parser.getName().equalsIgnoreCase("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseDelRecordResponse errcode="
									+ retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "parseDelRecordResponse retDesc="
									+ retDesc);
						} else if (parser.getName().equalsIgnoreCase(
								"parseDelRecordResponse")) {
						}

						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 删除设备录像报文解析
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseDelDeviceRecordResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseDelDeviceRecordResponse....");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseDelDeviceRecordResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						Log.d(TAG, "parseDelDeviceRecordResponse xml tagname:"
								+ parser.getName());
						if (parser.getName().equalsIgnoreCase("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseDelDeviceRecordResponse errcode="
									+ retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "parseDelDeviceRecordResponse retDesc="
									+ retDesc);
						} else if (parser.getName().equalsIgnoreCase(
								"parseDelDeviceRecordResponse")) {
						}

						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 解析获取设备能力信息报文
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseDeviceAblityResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseDeviceAblityResponse");
		int retCode = -1;
		DeviceAbilityInfo deviceAbility = null;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseDeviceAblityResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseDeviceAblityResponse errcode="
									+ retCode);
							if (retCode == 200) {
								deviceAbility = new DeviceAbilityInfo();
							}
						} else if (parser.getName().equals(
								"GetDeviceFunResponse")) {

							deviceAbility.nRealPlay = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RealPlayFlag"));
							deviceAbility.nAudioTalk = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"AudioTalkFlag"));

							deviceAbility.nAlarmReport = Integer
									.parseInt(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"AlarmReportFlag"));

							deviceAbility.nAlarmRecord = Integer
									.parseInt(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"AlarmRecordFlag"));

							deviceAbility.nAlarmPicture = Integer
									.parseInt(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"AlarmPictureFlag"));

							deviceAbility.nParamConfig = Integer
									.parseInt(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"ParamConfigFlag"));
							deviceAbility.nFileQuery = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"FileQueryFlag"));

							deviceAbility.nFilePlay = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"FilePlayFlag"));

							deviceAbility.nFileDownload = Integer
									.parseInt(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"FileDownloadFlag"));

							deviceAbility.nPtz = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"PTZFlags"));

							ProtocalData.mDeviceAbility = deviceAbility;

						}
						break;
					case XmlPullParser.END_TAG:

						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 中心录像开关设置报文解析
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseCloudRecordFlagResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseCloudRecordFlagResponse....");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseCloudRecordFlagResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						Log.d(TAG, "parseCloudRecordFlagResponse xml tagname:"
								+ parser.getName());
						if (parser.getName().equalsIgnoreCase("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseCloudRecordFlagResponse errcode="
									+ retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "parseCloudRecordFlagResponse retDesc="
									+ retDesc);
						} else if (parser.getName().equalsIgnoreCase(
								"parseCloudRecordFlagResponse")) {
						}

						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 中心录像开关获取报文解析
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseGetCloudRecordFlagResponse(Boolean result,
			byte[] responseData) {
		Log.i(TAG, "parseGetCloudRecordFlagResponse....");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			DeviceSwitchInfoCfg switchInfo = null;
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseGetCloudRecordFlagResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						Log.d(TAG,
								"parseGetCloudRecordFlagResponse xml tagname:"
										+ parser.getName());
						if (parser.getName().equalsIgnoreCase("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG,
									"parseGetCloudRecordFlagResponse errcode="
											+ retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG,
									"parseGetCloudRecordFlagResponse retDesc="
											+ retDesc);
						} else if (parser.getName().equalsIgnoreCase(
								"GetCloudRecordInfoResponse")) {

							switchInfo = new DeviceSwitchInfoCfg();
						} else if (parser.getName().equalsIgnoreCase("DevSN")) {

							switchInfo.devID = parser.nextText();
							switchInfo.nSwitchType = 2;

						} else if (parser.getName().equalsIgnoreCase("Flag")) {

							switchInfo.nSwitchFlag = Integer.parseInt(parser
									.nextText());

						}

						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equalsIgnoreCase(
								"GetCloudRecordInfoResponse")) {
							ProtocalData.mDeviceSwitchCfg = switchInfo;
						}

						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 获取告警图片报文解析
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseAlarmAttchResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseAlarmAttchResponse....");
		int retCode = -1;
		if (result) {
			List<AlarmGuardEventInfo> alarmAttachList = null;
			AlarmGuardEventInfo attachInfo = null;
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseAlarmAttchResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						Log.d(TAG, "parseAlarmAttchResponse xml tagname:"
								+ parser.getName());
						if (parser.getName().equalsIgnoreCase("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseAlarmAttchResponse errcode="
									+ retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "parseAlarmAttchResponse retDesc="
									+ retDesc);
						} else if (parser.getName().equalsIgnoreCase(
								"AttchList")) {
							alarmAttachList = new ArrayList<AlarmGuardEventInfo>();

						} else if (parser.getName().equalsIgnoreCase(
								"TotalCount")) {
							Log.i(TAG, "parseAlarmAttchResponse TotalCount="
									+ Integer.parseInt(parser.nextText()));

						} else if (parser.getName().equalsIgnoreCase(
								"AlarmAttch")) {

							attachInfo = new AlarmGuardEventInfo();
							attachInfo.BeginTime = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "BeginTime");
							attachInfo.Type = Integer
									.parseInt(parser.getAttributeValue(
											XmlPullParser.NO_NAMESPACE, "Type"));
							attachInfo.AttchURL = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "AttchURL");

							alarmAttachList.add(attachInfo);
						}

						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equalsIgnoreCase("AttchList")) {
							ProtocalData.mAlarmAttachList = alarmAttachList;

						}
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 报警开关设置报文解析
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseAlarmSwitchSetResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseAlarmSwitchSetResponse....");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseAlarmSwitchSetResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						Log.d(TAG, "parseAlarmSwitchSetResponse xml tagname:"
								+ parser.getName());
						if (parser.getName().equalsIgnoreCase("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseAlarmSwitchSetResponse errcode="
									+ retCode);
							String retDesc = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "RetDesc");
							Log.i(TAG, "parseAlarmSwitchSetResponse retDesc="
									+ retDesc);
						} else if (parser.getName().equalsIgnoreCase(
								"parseCloudRecordFlagResponse")) {
						}

						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 解析获取服务器信息
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseGetServerInfoResponse(Boolean result, byte[] responseData) {
		Log.i(TAG, "parseGetServerInfoResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseGetServerInfoResponse获取服务器信息的 xml:" + xml);
				parser.setInput(is, "UTF-8");

				ServerInfo serverInfo = null;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseGetServerInfoResponse errcode="
									+ retCode);
						} else if (parser.getName()
								.equals("ServerInfoResponse")) {

						} else if ((parser.getName().equals("ServerInfo"))) {
							serverInfo = new ServerInfo();
							serverInfo.Name = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Name");
							serverInfo.LocalPort = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "LocalPort");
							serverInfo.LocalIp = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "LocalIp");
							serverInfo.PublicPort = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "PublicPort");
							serverInfo.PublicIp = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "PublicIp");
							serverInfo.Type = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "Type");
							serverInfo.ID = parser.getAttributeValue(
									XmlPullParser.NO_NAMESPACE, "ID");

						}
						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equals("ServerInfoResponse")) {
							ProtocalData.serverInfo = serverInfo;
						}
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		} else {
			retCode = -1;
		}
		return retCode;
	}

	/**
	 * 解析重启设备信息
	 * 
	 * @param result
	 * @param responseData
	 * @return
	 */
	public int parseReStartDeviceResponse(Boolean result, byte[] responseData) {

		Log.i(TAG, "parseReStartDeviceResponse");
		int retCode = -1;
		if (result) {
			XmlPullParser parser = Xml.newPullParser();
			InputStream is = new ByteArrayInputStream(responseData);
			try {
				String xml = new String(responseData, "UTF-8");
				Log.i(TAG, "parseReStartDeviceResponse xml:" + xml);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// parse result
						if (parser.getName().equals("Result")) {
							retCode = Integer.parseInt(parser
									.getAttributeValue(
											XmlPullParser.NO_NAMESPACE,
											"RetCode"));
							Log.i(TAG, "parseReStartDeviceResponse errcode="
									+ retCode);

						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {

			}
		} else {
			retCode = -1;
		}
		return retCode;

	}
}
