package com.temobi.vcp.protocal;

/**
 * 图片下载回调接口
 */
public interface OnDownLoadProcessListener{
	
	static final int DOWNLOAD_SCCUESSED=7001;
	static final int DOWNLOAD_FAILED=7002;
	static final int DOWNLOADING=7003;
	/**
	 * 下载响应
	 * 
	 * @param responseCode
	 * @param message   消息内容，比如下载进度
	 */
	void onDownLoadResponse(int responseCode, Object obj);
	
	

}