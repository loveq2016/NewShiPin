package com.temobi.vcp.protocal.data;

/*
 * 摄像头各类开关信息
 */
public class CameraSwitchInfo {

	public int Guard;// 一健布防/撤防开关,1：布防,0：撤防
	public int Detection;// 运动侦测报警开关，1：开，0：关
	public int Sensitivity;// 运动侦测灵敏度:1－高灵敏度，2－中灵敏度，3－低灵敏度
	public int SenAlarm;// 外接传感器报警开关，1：开，0：关
	public int AlarmRec;// 报警录像开关，1：开，0：关
	public int SMS;// 报警短信通知开关，1：开，0：关
	public int MMS;// 报警彩信通知开关，1：开，0：关
	public int SMSRestart;// 短信唤醒/重启开关，1：开，0：关

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Guard=" + Guard + ";Detection=" + Detection + ";Sensitivity="
				+ Sensitivity + ";SenAlarm=" + SenAlarm + ";AlarmRec="
				+ AlarmRec + ";SMS=" + SMS + ";MMS=" + MMS + ";SMSRestart="
				+ SMSRestart;
	}
}
