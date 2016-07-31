package com.temobi.vcp.protocal.data;

public class ServerInfo {

	/** *Request请求消息-----设备标识 */
	public String DevSN = "";// 设备标识

	/**
	 * Request请求消息----服务器类型 1：业务管理系统 2：接入服务器； 3：录像服务器； 4：流媒体服务器； 5：客户端服务器
	 * 6：业务管理服务器 7：P2P服务器
	 */
	public String ServerType = "7";

	// 应答消息Response
	/*** 应答消息Response----服务器Id */
	public String ID;
	/*** 应答消息Response----服务器的名称 */
	public String Name;
	/*** 应答消息Response----服务器类型 */
	public String Type;
	/*** 应答消息Response----服务器的内网IP */
	public String LocalIp;
	/*** 应答消息Response----服务器内网端口 */
	public String LocalPort;
	/*** 应答消息Response----服务器的公网IP */
	public String PublicIp;
	/*** 应答消息Response----服务器公网端口 */
	public String PublicPort;

	@Override
	public String toString() {
		return "ServerInfo [DevSN=" + DevSN + ", ServerType=" + ServerType
				+ ", ID=" + ID + ", Name=" + Name + ", Type=" + Type
				+ ", LocalIp=" + LocalIp + ", LocalPort=" + LocalPort
				+ ", PublicIp=" + PublicIp + ", PublicPort=" + PublicPort + "]";
	}

}
