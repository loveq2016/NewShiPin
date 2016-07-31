package com.temobi.vcp.protocal.data;

public class ModifyPwd {
	public String OldPassword;
	public String NewPassword;
	public int Enc_Mode;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "oldpwd="+OldPassword+";new pwd="+NewPassword+";enc_mode="+Enc_Mode;
	}
}
