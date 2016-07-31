package com.temobi.ttalk;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NativeSupport {
	private static final String TAG = "NativeSupport";
	//native basic functions
	public native void native_init();
	public native void native_destroy();	
	public native void audioRecordCallback(byte[] frame);
	public native String getDevAudioInfo();
    
	public static final int TM_AUDIOTALK_TRANSFER_CONNECT_ERROR=1000;
	public static final int TM_AUIOTALK_TRANSFER_CONNECT_OK=1001;
	public static final int TM_AUDIOTALK_TRANSFER_RECONNECT=1002;
	public static final int TM_AUIOTALK_TRANSFER_TIMEOUT=1006;
	public static final int TM_AUDIOTALK_TRANSFER_GETDEVAUDIOINFO=1007;

	
	private Handler mEventHandler;
	private int mNativeContext=0; // accessed by native methods
	private String cur_dir_for_linux="/data/data/com.temobi.ttalk/lib";
	private String phoneparam="devid=9999&cameraid=100&server_ip=192.168.3.250&server_port=14444&"; 	
	
	static {
		String[] so_names = new String[3];
		/*so_names[0] = "rmtAMRNBCodec";*/	so_names[0] = "rmAACEnc"; //added it by ihappy for change audio encoder from amr to aac
		so_names[1] = "audiotalktransfer";	
		so_names[2] = "ttalk";		
		sos_load( so_names );		
	}	
	public void setHandler(Handler hander) {
		mEventHandler = hander;		
		//native_init();
	}
	public void initTalkVoice(){
		native_init();
	}
	public void setCurDirForLinux(String path){
		cur_dir_for_linux=path;
	}
	/* �ͻ��˵�ID,��������IP,port*/
	public void setPhoneParam(String param){
		phoneparam=param;
	}
	//public void postEventFromNative(int what, int arg1, int arg2)
	//{
	//		Log.v(TAG, "postEventFromNative what:"+what+" arg:"+arg1+" "+arg2);		
	//		if (mEventHandler != null) {
	//			Message m = mEventHandler.obtainMessage(what, arg1, arg2);
	//			mEventHandler.sendMessage(m);
	//		}
	//}	
	
	public void postEventFromNative(int notify_id, int param)
	{
			Log.v(TAG, "postEventFromNative notify_id:"+notify_id+" param:"+param);		
			Log.d(TAG, "postEventFromNative mEventHandler is null=?"+String.valueOf(mEventHandler==null));
			if (mEventHandler != null) {
				/*Message m = mEventHandler.obtainMessage(what, arg1, arg2);*/
				Message m = mEventHandler.obtainMessage(notify_id, param);
				mEventHandler.sendMessage(m);
			}
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
}
















