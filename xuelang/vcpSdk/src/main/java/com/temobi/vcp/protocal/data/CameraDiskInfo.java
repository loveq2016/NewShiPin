package com.temobi.vcp.protocal.data;

/*
 * 摄像头存储卡信息
 */
public class CameraDiskInfo {

	public int Total;//总存储空间，单位：M
	public int Used;//已使用的存储空间，单位：M
	public int Status;//1：存储卡损坏，2：存储卡获取不到,3：存储已满
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Total="+Total+";Used="+Used+";Status="+Status;
	}
}
