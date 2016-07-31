package com.temobi.vcp.protocal.data;



import java.util.List;

public class MediaAuth {
	public String devId="";
	public String CameraId="";
	public int MediaUserNum;
	public boolean Haspivilege;
	public String StreamIP="";
	public String StreamPort="";
	public List<Stream> StreamList;

	public String toString() {
		return "CameraId=" + CameraId + ";MediaUserNum=" + MediaUserNum
				+ ";Haspivilege=" + Haspivilege + ";StreamIP=" + StreamIP
				+ ";StreamPort=" + StreamPort + "";
	}
}
