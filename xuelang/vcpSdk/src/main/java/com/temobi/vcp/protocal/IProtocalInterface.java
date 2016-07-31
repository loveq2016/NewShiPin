package com.temobi.vcp.protocal;



public interface IProtocalInterface {
	//响应回调
	public void onProtocalNotifycation(int cmd, int errcode);
	
	//是否取消响应回调
	public void isCancel(boolean isCancel);
	
}
