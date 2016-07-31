package com.temobi.vcp.sdk.data;



/*
 * 命令信息
 */
public class CommInfo {
	public CommHeader commHeader;//命令头(包含了命令类型)
	public Object pData;//命令数据
	public CommInfo(CommHeader cmdHeader,Object data){
		this.commHeader = cmdHeader;
		this.pData = data;
	}
	public CommInfo(){
		
	}
}
