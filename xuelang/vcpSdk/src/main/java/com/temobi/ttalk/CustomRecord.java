package com.temobi.ttalk; 

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.util.Log;

public class CustomRecord extends CustomThread{
	private static final String TAG = "CustomRecord";
    /*public static  int frequency = 8000;*/  private int frequency=44100;  //added it by ihappy for change audio encoder from amr to aac 
    public static int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;  
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;  
    private int recBufSize;  
    private AudioRecord audioRecord;
    private final static Object mLock = new Object();

    
    private AudioWriteFrameCallback mWriteFrameCallback;
    public interface AudioWriteFrameCallback
    {
        void Write(byte[] data);        
    };    
   
    public CustomRecord()
    {
    	threadName = "CustomRecord";
    	OnCreate(); 	
    }
    public CustomRecord(int SampleRate,int channels)
    {
    	frequency = SampleRate;
    	if( 1 == channels )
    		channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    	else
    		channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
    	OnCreate();
    }
    public final void setCallback(AudioWriteFrameCallback cb) {
        synchronized(mLock) {
        	mWriteFrameCallback = cb;
        }    	    
    }     
    public int GetSampleRate()
    {
    	return frequency;
    }
    public int GetChannels()
    {
    	if( channelConfiguration == AudioFormat.CHANNEL_CONFIGURATION_MONO )
    		return 1;
    	else
    		return 2;
    }
    public void SetBufferSize(int size)
    {
    	recBufSize = size;
    }    
    private void OnCreate()
    {    	
    	mWriteFrameCallback = null;
 	   recBufSize = AudioRecord.getMinBufferSize(frequency,  
               channelConfiguration, audioEncoding);
 	  Log.v(TAG, "trace 3");
	   audioRecord = new AudioRecord(AudioSource.MIC, frequency,  
               channelConfiguration, audioEncoding, recBufSize);   
	   Log.v(TAG, "CustomRecord "+recBufSize+" "+audioRecord);   	
    	
    }
    public void pause1()
    {
        audioRecord.stop();
        audioRecord.release();
        pause(); /*CustomThread.pause();*/
		log("pause over2 ");
    }

	/* ���ڲɼ�����Ƶ������ */
    public void adjustVolume(boolean val,double vol)
    {
    	;
    }
    
    /*��������ƽ��ֵ*/
	private double calculateVolume(byte[] buffer){
		
		double sumVolume = 0.0;
		double avgVolume = 0.0;
		double volume = 0.0;
		for(int i = 0; i < buffer.length; i+=2){
			int v1 = buffer[i] & 0xFF;
			int v2 = buffer[i + 1] & 0xFF;
			int temp = v1 + (v2 << 8);
			if (temp >= 0x8000) {
				temp = 0xffff - temp;
			}
			sumVolume += Math.abs(temp);
		}
		avgVolume = sumVolume / buffer.length / 2;	
		volume = Math.log10(1 + avgVolume) * 10;
		
		return volume;
	}
    /* ���ڲɼ�����Ƶ����*/
    private void RaiseVolume(byte buf[], int size,int uRepeat,double vol)
    {
     if (0>=size )
     {
      return;
     }
     for (int i = 0; i < size;)
     {
      long minData = -0x8000; //�����8bit����������-0x80
      long maxData = 0x7FFF;//�����8bit����������0xFF
      
      short wData = buf[i+1];
      /*wData = MAKEWORD(buf[i],buf[i+1]);*/ /*wData=(short) ( ( (byte)(buf[i])| ((short)((byte)(buf[i+1]) ))<<8)  );*/ wData=(short)((buf[i+1]<<8)|buf[i]);
      long dwData = wData;
      for (int j = 0; j < uRepeat; j++)
      {
       dwData = (long)(dwData * 1.25);//1.25;
       if (dwData < -0x8000)
       {
        dwData = -0x8000;
       }
       else if (dwData > 0x7FFF)
       {
        dwData = 0x7FFF;
       }
      }
      //wData = LOWORD(dwData);
      //buf[i] = LOBYTE(wData);
      //buf[i+1] = HIBYTE(wData);
      wData=(short)(dwData&0x0000ffff);
      buf[i]=(byte)(wData&0x00ff);
      buf[i+1]=(byte)(wData&0xff00);
      i += 2;
     }
    }
    
    protected void thread_enty()
    {
    	int totbytes=0;
    	int times = 0;
        byte[] buffer = new byte[recBufSize];
        audioRecord.startRecording();
        Log.v(TAG, "audioRecord thread_enty");
        while (isRunning) {  
            int bufferReadResult = audioRecord.read(buffer, 0,  
                    recBufSize);  
            if (AudioRecord.ERROR_INVALID_OPERATION == bufferReadResult )
            {
            	Log.e(TAG, "err,read- "+bufferReadResult);
            	break;
            }
            if( bufferReadResult <= 0 )
            {
            	Log.e(TAG, "err,read "+bufferReadResult);
            	break;
            }
            // byte[] tmpBuf = new byte[bufferReadResult];  
            //System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);   
               /* ����������ֵ*/
              //byte[]tmpBuf = new byte[bufferReadResult];  
             //System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);
             /*֮��ָ����*/  
             //double volume=calculateVolume(tmpBuf);
              // Log.v(TAG,"volume=" + volume);
             /* ���ڲɼ�����Ƶ����*/
              //RaiseVolume(buffer,bufferReadResult,1,1.25);
            synchronized(mLock) {
                if( mWriteFrameCallback != null )                    
                	mWriteFrameCallback.Write(buffer);
            }
            {
        		totbytes += bufferReadResult;
        		++times;
            }
            
     
           /* ����������С*/
            
        }  
        

		
        audioRecord.stop();
        buffer = null;
        Log.v(TAG, "record thread stoped "+totbytes+" "+times);
    }    

}






