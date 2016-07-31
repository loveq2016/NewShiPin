package com.temobi.vcp.protocal.data;



public class TerminalDevice {
	public String Type;
	public String TermVersion;
	public String TermType;
	public String TermName;
	public String IMEI;
	public String TermStatus;
	public String ArmStatus;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Type=" + Type + ";TermVersion=" + TermVersion + ";TermType="
				+ TermType + ";TermName=" + TermName + ";IMEI=" + IMEI
				+ ";TermStatus=" + TermStatus + ";ArmStatus=" + ArmStatus;
	}
}
