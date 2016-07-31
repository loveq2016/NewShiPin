package com.temobi.vcp.protocal.data;



import java.util.List;

public class LoginInfo {
	
	// request data
	public String LoginId;
	public List<String> Dev_List;
	
	
	
	public String LoginMode;
	public String EncMode;
	public String Password;
	// response data
	public List<MyCamera> Camera_List;
	
	
	public String SessionId;
	public int HeartbeatTime;
	public int UserNum;
	public String Nonce;
	public String ServerTS;







}