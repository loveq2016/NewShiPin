package com.temobi.ttalk;

import java.lang.Thread.State;

import android.util.Log;

public abstract class CustomThread {
	private static final String TAG = "WorkerThread";
	protected boolean isRunning;
	protected WorkerThread mThread;	
	protected String threadName="unknow";
    public void resume()
    {
    	if( isRunning )
    		return;
    	log("start ... ");    	
    	isRunning = true;
    	mThread = new WorkerThread();
    	mThread.start();
    }
    public void  pause()
    {
    	if( !isRunning )
    		return;
    	log("pause ... ");
    	isRunning = false;
		try {		
			if( mThread != null )
			{
				log("interrupt ... ");
				mThread.interrupt();
				log("join ... ");
				mThread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}    	
		log("pause over ");
    }
    public boolean GetRunningState()
    {
    	return isRunning;
    }
    public State getState()
    {
    	return mThread.getState();
    }
    protected void log(String str)
    {
    	Log.v(TAG, "["+threadName+"]"+str);
    }
	protected abstract void thread_enty();	
    class WorkerThread extends Thread {  
        public void run() {
        	//Thread.interrupted()
        	long id = Thread.currentThread().getId();
            try {            	
        		log("thread started id="+id);            	
               	thread_enty();
                log("thread stoped id="+id);
            } catch (Throwable e) { 		//InterruptedException e
            	log("thread aborted id="+id+" "+e);
            }
            if( isRunning )
            	isRunning = false;
        }  
    }; 
}
