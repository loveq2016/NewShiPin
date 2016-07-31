package com.temobi.vcp.protocal;

public class MVPCommand {
		public static final int VCP_GET_DEVINFO = 0x0001; //获取设备信息
		public static final int VCP_HEARTBEAT = 0x0002; //心跳
       	public static final int VCP_MODIFY_PWD = 0x0003; //修改设备访问的密码
		public static final int VCP_RTSP_URL = 0x0004; //get camera's play url
		public static final int VCP_CAMERA_LIST = 0x0005; //获取宜居通终端手机号绑定的摄像头列表
		public static final int VCP_MEDIA_AUTH = 0x0006; //宜居通摄像头访问鉴权请求
		public static final int VCP_CAMERA_CTRL = 0x0007; //宜居通摄像头云台控制消息
		public static final int VCP_QUERY_RECORD = 0x0008; //宜居通摄像头录像查询消息
		public static final int VCP_MEDIA_PLAY = 0x0009; //设备实时视频访问
		public static final int VCP_MEDIA_QUIT = 0x000A; //设备实时视频访问退出
		public static final int VCP_TERMINAL_STATUS = 0x000B; //宜居通终端设备信息查询
	  	public static final int VCP_RESERVEDA = 0x000C; //宜居通终端设备信息查询
	  	public static final int VCP_QUERY_CAMERA_PARAM=0x000D;//查询摄像头参数信息
	  	public static final int VCP_SET_SERVERINFO=0x000F;//设置客户端服务器信息
	  	public static final int VCP_PRE_SET=0x001A;//预设点操作
	  	public static final int VCP_MEDIA_PLAY_RECORD = 0x001B; //开始播放设备录像
	  	public static final int VCP_SET_CAMERA_PARAM=0x001C;//设置设备参数
	  	public static final int VCP_GET_CLOUDRECORD=0x001D;//获取云录像
	  	public static final int VCP_GET_DEV_PLAN=0x001E;//获取设备计划
	  	public static final int VCP_SET_DEV_PLAN=0x001F;//设置设备计划
	  	public static final int VCP_GET_DEV_VERSOION=0X002F;//获取设备软件版本
	  	public static final int VCP_GET_SOUNDTALK_SERVER=0X003F;//获取语音对讲服务器信息
	  	public static final int VCP_GET_RECORD_DOWNLOAD_SERVER=0X004F;//获取录像下载服务器信息
	  	public static final int VCP_MEDIA_PLAY_CLOUD_RECORD=0X005F;//开始播放云录像
	  	public static final int VCP_RECORD_DEL=0X006A;//删除设备录像文件
	  	public static final int VCP_DEVICEABILITY_QUERY=0X006B;//设备 能力查询
	  	public static final int VCP_CLOUD_RECORDSET_FLAG=0X006C;//中心录像开关
	  	public static final int VCP_QUERY_ALARM_ATTACH=0X006D;//告警附件查询
	  	public static final int VCP_CLOUD_RECORDGET_FLAG=0X006E;//中心录像开关查询
	  	public static final int VCP_CLOUD_RECORD_DEL=0X006F;//删除云录像文件
	  	public static final int VCP_ALARM_SWITCH_SET=0X007A;//设置报警开关设置
	  	public static final int VCP_ALARM_SWITCH_GET=0X007B;//报警开关查询
	  	public static final int VCP_QUERY_ALARM_PICTURE=0X007C;//报警图片查询
	  	public static final int VCP_QUERY_DEVICE_ALARM_FILE=0X007D;//设备上告警文件查询
	  	public static final int VCP_QUERY_CLOUD_ALARM_FILE=0X007E;//中心服务器上告警文件查询
	  	
	 	public static final int VCP_QUERY_ALARM_FILE_BY_ID=0X007F;//按报警ID文件查询
	  	
	 	public static final int VCP_GET_SERVER_INFO=0x008A;//获取服务器信息（P2P）
	  	public static final int VCP_SET_CAMERA_PARAM_PASSWORD=0x008E;//设置设备参数-密码
		public static final int VCP_SET_CAMERA_PARAM_OSD=0x008F;//设置设备参数-OSD
		
		public static final int VCP_RESTART_DEVICE=0x009A;//重启设备
		
		
	
	  	
		

		
		//云台控制方位
		public static final String PTZ_UP="PTZ_UP";
		public static final String PTZ_DOWN="PTZ_DOWN";
		public static final String PTZ_LEFT="PTZ_LEFT";
		public static final String PTZ_RIGHT="PTZ_RIGHT";
		public static final String PTZ_STOP="PTZ_STOP";
		
		
		
		
		//云台控制 命令
		
		
		
		//错误码(服务器返回)
		public static final int REQUEST_FAIL = 1;//请求/操作失败[通用]
		public static final int REQUEST_TIMEOUT = 7;//请求超时
		public static final int PWD_WRONG = 8;//密码错误，登陆失败
		public static final int MODIFY_PWD = 9;//默认密码首次登陆，修改密码
		public static final int SESSION_TIMEOUT = 99;//会话超时，重新登陆
		public static final int OPERATION_NOT_SUPPORT = 405;//不支持该操作
		public static final int INVALID_ECODE_MODE = 1001;//加密模式无效
		public static final int SERVER_PWD_WRONG = 1103;//错误的旧服务密码
		public static final int PARSE_ERR = 9998;//报文解析异常
		
		//120.197.93.92:2080 公网   账号:123456789   密码1111
		//测试 120.197.110.182:2080
		public static String INPUT_IP = "";
		public static String DEFAULT_IP_PORT="120.197.110.182";
		public static String SERVER_IP ="192.168.3.177"; //218.17.55.73//183.230.40.71//"192.168.3.18";//192.168.3.177,192.168.3.172，120.197.110.182
//		public static final String YJT_ADDRESS = "120.197.93.92:2080";
		public static String SERVER_PORT = ":2800";//800//2800
		public static final String HTTP = "http://";
		public static final String SESSIONID_FIELD = "_x_sessionid_";
		public static final String IPCSN_FIELD ="_x_ipcsn_";
		public static final String CAMERA_ID ="_x_cameraId_";
		public static final String OPERID ="_x_optId_";
		public static final String ENCODE_KEY = "temobi2012-yjt!@$%";//加密密钥
		
	
		
		
		
		
		public static final String GET_DEVINFO_URL = "/spi/getdevinfo";
		public static final String HEAETBEAT_URL = "/yjt/heartbeat.do";
		
     		
		public static final String MEDIA_PLAY_URL = "/spi/mediaplay";
		public static final String MEDIA_QUIT_URL = "/spi/mediaquit";
		
		public static final String MODIFY_PWD_URL = "/yjt/modifypass";
		public static final String GET_RTSP_URL = "/yjt/GetRtspPlayUrl";
		public static final String CAMERA_LIST_URL = "/yjt/getcameralist";
		public static final String MEDIA_AUTH_URL = "/spi/mediaauth";
	
		public static final String CAMERA_CTRL_URL = "/spi/cameractrl";
		public static final String QUERY_RECORD_URL = "/spi/recordquest";
		public static final String QUERY_CLOUD_RECORD="/spi/cloud/queryrecord";
		public static final String PLAY_CLOUD_RECORD="/spi/cloud/recordplay";

		public static final String TERMINAL_STATUS_URL = "/yjt/getterminalstatus";
		public static final String SET_TERMINAL_URL = "/yjt/setterminalstatus";
		
		public static final String GET_CAMERA_PARAM_URL="/spi/getdevparam";
		public static final String SET_CAMERA_PARAM_URL="/spi/changedevparam";
		public static final String RECORD_PLAY_URL = "/spi/recordplay";
		public static final String DEV_PLAN_QUERY="/spi/queryrecordplan";
		public static final String DEV_PLAN_SET="/spi/setrecordplan";
		public static final String GET_SOUNDTALK_SERVER="/spi/soundtalk";
		public static final String GET_RECORDDOWNLOAD_SERVER="/spi/recorddownload";
		public static final String DEL_CLOUD_RECORD_URL="/spi/delrecordfileinfo";
		public static final String GET_DEVICE_ABILITY="/spi/getdevfuninfo";
		public static final String CLOUD_RECORDSET_FLAG_URL="/spi/setcloudrecordinfo";
		public static final String ALARM_PICTURE_QUERY_URL="/spi/querypicturelist";
		public static final String ALARM_PICTURE_DOWNLOAD_URL="/spi/image";
		public static final String CLOUD_RECORDGET_FLAG_URL="/spi/getcloudrecordinfo";
		public static final String DEL_DEVICERECORD_URL="/spi/delrecord";
		public static final String ALARM_ATTACH_QUERY_URL="/spi/queryalarmattchlist";
		
		public static final String GET_SERVER_INFO_URL="/spi/getserverinfobyidtype";
		
		public static final String RESTART_DEVICE_URL="/spi/devicerestart";
		
	
}


















