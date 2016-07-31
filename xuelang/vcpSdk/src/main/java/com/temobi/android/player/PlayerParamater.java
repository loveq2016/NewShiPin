package com.temobi.android.player;







import com.temobi.android.player.TMPCPlayer.ParamaterFormatException;

import android.media.AudioTrack;
import android.os.Debug;
import android.util.Log;
import android.view.Surface;

public class PlayerParamater {
 
	static final String FILE = "file://";
	static final String TMSS = "tmss://";
	static final String RTSP = "rtsp://";
	
	static final int BUFFERTIME_FILE = 4000;
	static final int BUFFERTIME_NET = 6000;
	
	static final int DEFAULT_DISPLAY_MODE = 0;
	
	byte apn;
	String url;
	
	String cmmmName = "";
	
	String play_url; // max 256
	String host_addr; // max 32
	int hostport;
	int guestport; /* optional */

	int av_flag; /* Audio,Video receive flag */
	int start_pos;

	int should_buffer_time; /* unit is millisecond */
	int posx;
	int posy;
	String cur_dir_for_linux; // max 256
	
	int hasBeenDes;

	// guangdong
	int CHANNEL_ID; // 
	int PROG_ID; // 
	String BILL_ID;// max 12 
	String Served_msisdn;// max 32 //唯一MSISDN
	String Reserved;// max 12 // 64 Bytes

	/* new item about network */
	int APN_Type; /* TMPC_NETWORK_CMNET,TMPC_NETWORK_CMWAP */
	int APN_ID; /* 0,1,2... */
	int Link_Type = 0;/* TMPC_LINK_TYPE_UDP,TMPC_LINK_TYPE_TCP,TMPC_LINK_TYPE_TCP_USEPROXY */

	/* only for TMPC_LINK_TYPE_TCP_USEPROXY */
	String proxyaddr;// max 32 /*proxy server's address*/
	int proxyport; /* proxy server's port */

	int use_env;/* must be TMPC_ENV_SH_VERSION OR TMPC_ENV_GD_VERSION */
	int display_outside;/* 0:1 */
	

//	 String v_rendertype;  		//postbuffer/overlay......
//	 String v_format;        	//yuv422/rgb......
	 String a_param1;       
	 String a_param2;       
	 String c_param1;       
	 String c_param2;       
	
     
     int SDK_Version;
	Surface mSurface;
	AudioTrack audiotrack;
	int audiotrack_buffersize;
	
	int psd_band_num;
	
	boolean isInstantMode;

	PlayerParamater(String packageName, String url, byte apn) throws TMPCPlayer.ParamaterFormatException {
		this(packageName, url, apn, null, 0, null);
	}
	
	PlayerParamater(String packageName, String url, byte apn, String proxyHost, int proxyPort, String cmmmName) throws TMPCPlayer.ParamaterFormatException {
		Log.d(TMPCPlayer.DEBUG_url, "packageName = " + packageName + " ; url = " + url + " ; apn = " + (apn==TMPCPlayer.APN_CMNET ? "cmnet" : "cmwap"));
		 
		should_buffer_time = 0;
		
		isInstantMode = false;
		//  
		if(packageName.endsWith("@wd"))
		{
			cur_dir_for_linux = "/data/data/" 
				+ packageName.substring(0, packageName.length() - 3) + "/files"; // max 256	
		}
		else
		{
			cur_dir_for_linux = "/data/data/" + packageName + "/lib"; // max 256
		}
		
		
//		if (url.startsWith(TMSS) || url.startsWith(FILE)) {
//			parseUrl(url, apn);
//		} else {
//			file(url);
//		}
//		SDK_Version = android.os.Build.VERSION.SDK_INT;
		String SDK = android.os.Build.VERSION.SDK;
		Log.i("jni_java","SDK : " + SDK);
		
		SDK_Version = Integer.parseInt(SDK);
		Log.i("jni_java","SDK_Version : " + SDK_Version);
		
		if (url != null) {
			parseUrl(url, apn);
		} else {
			this.apn = apn;
		}
		
		if (proxyHost != null) {
			this.proxyaddr = proxyHost;
			this.proxyport = proxyPort;
		}
		
		Log.d("jni_java", "proxyaddr = "+this.proxyaddr);
		Log.d("jni_java", "proxyport = "+this.proxyport);
		
//		if (cmmmName != null) {
			this.cmmmName = cmmmName;
//		}
		
		psd_band_num =0;
		display_outside = DEFAULT_DISPLAY_MODE;
//		file(url);
//		cmnet(url);
//		cmwap(url);
	}
	
	// true : instantmode
	// false: normal
	public void SetInstantMode(boolean mode)
	{
		isInstantMode = mode;
	}
	
	public void SetLinkType(int link_type)
	{
		Link_Type = link_type;
	}
	
	public void SetShouldBufferTime(int ms)
	{
		if(ms > 0)
		{
			should_buffer_time = ms;	
		}
	}

	public boolean IsDisplayOutside()
	{
		return (1==display_outside);
	}
	void setUrl(String url) throws ParamaterFormatException {
		parseUrl(url, apn);
	}
	
	void setGDCDR(int channelID, int progID, String billID, String servedmsisdn, String reserved){
		this.CHANNEL_ID = channelID;
		this.PROG_ID = progID;
		this.BILL_ID = billID;
		this.Served_msisdn = servedmsisdn;
		this.Reserved = reserved;
	}
	
	void setApn(byte apn) throws ParamaterFormatException {
		this.apn = apn;
		parseUrl(url, apn);
	}
	
	void setOutDisplay(int outSideDis)
	{
		if(0 == outSideDis)
			display_outside = 0;
		else
			display_outside = 1;
	}
	
	void SetPsdNum(int band_num)
	{
		psd_band_num = band_num;
	}
	
	private void parseUrl(String url, byte apn) throws TMPCPlayer.ParamaterFormatException{
		TMPCPlayer.Loger.d(TMPCPlayer.DEBUG_url, "url = " + url + " ; apn = " + (apn==TMPCPlayer.APN_CMNET ? "cmnet" : "cmwap"));
		if (url == null ||url.length() < 1 || apn < 0 || apn > 1) {
			throw new ParamaterFormatException("player paramater error");
		}
		
		this.apn = apn;
		this.url = url;
		
		Log.d("PlayerParamater", "parseUrl-->should_buffer_time="+should_buffer_time);
		if(!(should_buffer_time > 0))
		{
			if (url.startsWith(TMSS) || url.startsWith(FILE) || url.startsWith(RTSP)) {
				should_buffer_time = BUFFERTIME_NET;
			} else {
				file(url);
				should_buffer_time = BUFFERTIME_FILE;
				return;
			}	
		}
		
		try {
			String header = null;
			if (url.startsWith(TMSS) || url.startsWith(FILE) || url.startsWith(RTSP)) {
				int bIndex = 0;
				int eIndex = 0;
				
				int ipv6Index = 0;
				eIndex = url.indexOf("://") + 3;
				header = url.substring(bIndex, eIndex);
				
				ipv6Index = url.indexOf("://[");
				if(ipv6Index != -1){//IPV6 address
					//ip
					bIndex = eIndex;
					eIndex = url.indexOf(']');
					host_addr = url.substring(bIndex+1, eIndex);
					//port
					bIndex = eIndex;
					eIndex = url.indexOf('/', bIndex);
					hostport = Integer.parseInt(url.substring(bIndex + 2, eIndex));
					//play_url
					play_url = header + url.substring(eIndex + 1);
					
				}else{				//IPV4 address
					
					bIndex = eIndex;
					eIndex = url.indexOf(':', bIndex);
					
					host_addr = url.substring(bIndex, eIndex);
					Log.d("PlayerParamater", "parseUrl-->host_addr="+host_addr);
					bIndex = eIndex;
					eIndex = url.indexOf('/', bIndex);
					
					hostport = Integer.parseInt(url.substring(bIndex + 1, eIndex));
					Log.d("PlayerParamater", "parseUrl-->hostport="+hostport);
					play_url = header + url.substring(eIndex + 1);
					Log.d("PlayerParamater", "parseUrl-->play_url,should_buffer_time="+should_buffer_time);
					
				}
			}else{
				file(url);
				should_buffer_time = BUFFERTIME_FILE;
			}
			
			
			if (apn == TMPCPlayer.APN_CMWAP) {
				//hostport = 443;
				cmwap();
			} else {
				cmnet();
			}
			
			TMPCPlayer.Loger.d(TMPCPlayer.DEBUG_url, "url = " + url);
			TMPCPlayer.Loger.d(TMPCPlayer.DEBUG_url, "header = " + header);
			TMPCPlayer.Loger.d(TMPCPlayer.DEBUG_url, "host_addr = " + host_addr);
			TMPCPlayer.Loger.d(TMPCPlayer.DEBUG_url, "hostport = " + hostport);
			TMPCPlayer.Loger.d(TMPCPlayer.DEBUG_url, "play_url = " + play_url);
		} catch (Exception e) {
			Log.e(TMPCPlayer.DEBUG_url, "url formate error, url must like this: tmss://host:port/path");
			throw new TMPCPlayer.ParamaterFormatException("url formate error, url must like this: tmss://host:port/path");
		}
		
		//@modify since 1.2	2009-11-12	url
		play_url = url;
	}
	private void parseIPV6(){
		
	}
	void setBufferTime(int time) {
		should_buffer_time = time;
	}
	
	int getBufferTime() {
		return should_buffer_time;
	}

	private void file(String url) {
		play_url = url;
//		play_url = "/sdcard/love.mp4";

		host_addr = "218.204.255.133"; // max 32
		hostport = 9901;
		guestport = 11; /* optional */

		av_flag = 3; /* Audio,Video receive flag */
		start_pos = 0;

//		should_buffer_time = TMPCPlayer.bufferTime; /* unit is millisecond */
		posx = 15;
		posy = 16;
//		cur_dir_for_linux = "/data/data/com.temobi.android.player/lib"; // max 256

		// guangdong
//		CHANNEL_ID = 17; // ID
//		PROG_ID = 18; // ID
//		BILL_ID = "BILL_ID";// max 12 
//		Served_msisdn = "Served_msisdn";// max 32 唯一MSISDN
//		Reserved = "Reserved";// max 12 // 64 Bytes

		/* new item about network */
		APN_Type = 0; /* TMPC_NETWORK_CMNET,TMPC_NETWORK_CMWAP */
		APN_ID = 5; /* 0,1,2... */
		// Link_Type = 0;/* TMPC_LINK_TYPE_UDP,TMPC_LINK_TYPE_TCP,TMPC_LINK_TYPE_TCP_USEPROXY */

		/* only for TMPC_LINK_TYPE_TCP_USEPROXY */
		if(null == proxyaddr)
		{
			proxyaddr = "proxyaddr";// max 32 /*proxy server's address*/
			proxyport = 22; /* proxy server's port */	
		}

		use_env = 1;/* must be TMPC_ENV_SH_VERSION 0 OR TMPC_ENV_GD_VERSION 1 */
		// display_outside = DEFAULT_DISPLAY_MODE;
	}

	private void cmnet() {
//		play_url = url;
//		play_url = "tmss://tmes_123";
//
//		host_addr = "218.204.255.133"; // max 32
//		hostport = 9901;
		guestport = 11; /* optional */

		av_flag = 3; /* Audio,Video receive flag */
		start_pos = 0;

//		should_buffer_time = TMPCPlayer.bufferTime; /* unit is millisecond */
		posx = 15;
		posy = 16;
//		cur_dir_for_linux = "/data/data/com.temobi.android.player/lib"; // max 256

		// guangdong
//		CHANNEL_ID = 17; // ID
//		PROG_ID = 18; // ID
//		BILL_ID = "BILL_ID";// max 12 
//		Served_msisdn = "Served_msisdn";// max 32 //唯一MSISDN
//		Reserved = "Reserved";// max 12 // 64 Bytes

		/* new item about network */
		APN_Type = 0; /* TMPC_NETWORK_CMNET,TMPC_NETWORK_CMWAP */
		APN_ID = 5; /* 0,1,2... */
		// Link_Type = 0;/* TMPC_LINK_TYPE_UDP,TMPC_LINK_TYPE_TCP,TMPC_LINK_TYPE_TCP_USEPROXY */

		/* only for TMPC_LINK_TYPE_TCP_USEPROXY */
		if(null == proxyaddr)
		{
			proxyaddr = "proxyaddr";// max 32 /*proxy server's address*/
			proxyport = 22; /* proxy server's port */	
		}
		

		//use_env = 1;/* must be TMPC_ENV_SH_VERSION 0 OR TMPC_ENV_GD_VERSION 1 */
		// display_outside = DEFAULT_DISPLAY_MODE;
	}

	private void cmwap() {
//		play_url = url;
//		play_url = "tmss://tmes_123";
//		
//		host_addr = "218.204.255.133"; // max 32
//		hostport = 443; // 9901
		guestport = 11; /* optional */

		av_flag = 3; /* Audio,Video receive flag */
		start_pos = 0;

//		should_buffer_time = TMPCPlayer.bufferTime; /* unit is millisecond */
		posx = 15;
		posy = 16;
//		cur_dir_for_linux = "/data/data/com.temobi.android.player/lib"; // max 256

		// guangdong
//		CHANNEL_ID = 17; //ID
//		PROG_ID = 18; // ID
//		BILL_ID = "BILL_ID";// max 12 //
//		Served_msisdn = "Served_msisdn";// max 32 // 唯一MSISDN
//		Reserved = "Reserved";// max 12 // 64 Bytes

		/* new item about network */
		APN_Type = 1; /* TMPC_NETWORK_CMNET,TMPC_NETWORK_CMWAP */
		APN_ID = -1; /* 0,1,2... */
		// Link_Type = 0;/* TMPC_LINK_TYPE_UDP,TMPC_LINK_TYPE_TCP,TMPC_LINK_TYPE_TCP_USEPROXY */

		if(null == proxyaddr)
		{
			/* only for TMPC_LINK_TYPE_TCP_USEPROXY */
			proxyaddr = "10.0.0.172";// max 32 /*proxy server's address*/
			proxyport = 80; /* proxy server's port */	
		}
		

		use_env = 1;// must be TMPC_ENV_SH_VERSION 0 OR TMPC_ENV_GD_VERSION 1
		// display_outside = DEFAULT_DISPLAY_MODE;
	}
}
