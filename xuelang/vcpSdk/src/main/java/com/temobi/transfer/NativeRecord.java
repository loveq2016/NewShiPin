package com.temobi.transfer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NativeRecord {
	private static final String TAG = "NativeRecord";
	private int mNativeContext; 
	private Handler mEventHandler;
	
	private String cur_dir_for_linux="/data/data/com.temobi.transfer/lib";
	private String phoneparam="Recordpath=/sdcard/record.mp4&DevSessionID=00008888&server_ip=192.168.3.250&server_port=18681&";
	
	//native basic functions
	public native void nativeInit();
	public native void nativePause();
	public native void nativeExit();
	
	//public native int nativeGetDownloadFileLength();
	public native int nativeGetDownloadPercent();
	
	public static final int TM_RECORD_DOWNLOAD_CONNECT_ERROR=1060;
	public static final int TM_RECORD_DOWNLOAD_CONNECT_OK=1061;
	public static final int TM_RECORD_DOWNLOAD_BEGIN_ERROR=1062 ;
	public static final int TM_RECORD_DOWNLOAD_RECONNECT=1066;
	public static final int TM_RECORD_DOWNLOAD_TIMEOUT=1067; 
	public static final int TM_RECOR_DOWNLOAD_RESET=1068;	
	public static final int TM_RECORD_DOWNLOAD_OK=1069;
	public static final int TM_RECORD_DOWNLOAD_RESERVED=1070;
	public static final int TM_RECORD_DOWNLOAD_GETTOTALLENGTH=TM_RECORD_DOWNLOAD_RESERVED+1;
	
	
	
	static {
		String[] so_names = new String[2];
		so_names[0] = "RecordDownLoad";	
		so_names[1] = "record";		
		sos_load( so_names );		
	}
	
	////////////////////////////so loads
	static private void sos_load(String[] so_names)
	{
		int i;
		for(i=0;i<so_names.length;++i)
		{
			try {
				System.loadLibrary(so_names[i]);
		    } catch (UnsatisfiedLinkError ex) {
		    	Log.v(TAG,"load library: "+so_names[i]+"failed");
		    }		
		}
	}
	public void setHandler(Handler hander) {
		mEventHandler = hander;				
	}
	public void setCurDirForLinux(String path){
		cur_dir_for_linux=path;
	}
	/* �ͻ��˵�ID,��������IP,port*/
	public void setPhoneParam(String param){
		phoneparam=param;
	}
	public void startDownload()
	{
		nativeInit();
	}
	public void stopDownload()
	{
		nativeExit();
	}
	public void postEventFromNative(int notify_id, int param)
	{
			Log.v(TAG, "postEventFromNative which about record notify_id:"+notify_id+" param:"+param);		
			if (mEventHandler != null) {
				Message m = mEventHandler.obtainMessage(notify_id, param);
				mEventHandler.sendMessage(m);
			}
	}
}

































