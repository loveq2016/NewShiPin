package com.temobi.android.player;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.temobi.cache.memory.MD5;
import com.temobi.cache.memory.MemoryCache;
import com.temobi.cache.memory.MemoryUtil;
import com.temobi.vcp.protocal.LoggerUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.net.Proxy;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import android.view.Surface;
import android.view.SurfaceHolder;

public class TMPCPlayer implements SurfaceHolder.Callback {

	static {
		System.loadLibrary("TMPC_jni");
	}

	static final String DEBUG_url = "playurl";
	static final String DEBUG_display = "jni_java";
	static final String DEBUG_tmpcState = "tmpcState";
	static final String JNI_JAVA_VERSION = "01.09.01";

	/**
	 * default false, if true TMPCPlayer will print debug logs.
	 */
	public static boolean IS_DEBUG_MODE = true;

	/**
	 * delay times for print media information in debug mode, default 1 (about
	 * 500 ms print once) this value must bigger than 0
	 *
	 * @since TMPCPlayer 1.1
	 */
	public static int DEBUG_PRINT_INFO_DELAY = 1;

	/**
	 * indicate that use network with no agent
	 */
	public static final byte APN_CMNET = 0;
	/**
	 * indicate that use network with agent 10.0.0.172:80
	 */
	public static final byte APN_CMWAP = 1;

	public static final byte TMPC_LINK_TYPE_UDP = 0;
	public static final byte TMPC_LINK_TYPE_TCP = 1;
	public static final byte TMPC_LINK_TYPE_TCP_USEPROXY = 2;
	public static final byte RTSP_RTP_WATCH_UDP = 4;
	public static final byte RTSP_RTP_WATCH_TCP = 5;

	static final long DEF_WAIT_TIME = 15;

	private long WAIT_TIME = DEF_WAIT_TIME;
	private static int buuferSize = 320 * 240;

	private static final Object surfaceLock = new Object();
	private static final Object eventLock = new Object();

	protected static TMPCPlayer player;
	public PlayerParamater param;
	private SurfaceHolder holder;
	private Surface surface;
	/* display outside */
	private Canvas canvas;
	private Bitmap bitmap;
	private Rect rectDest;
	private Matrix matrix;
	private Paint paint;
	/* display outside */

	private String m_sPlayUrl;
	private int m_SeekPos;
	// ============================
	private int m_BufferTime = 3000;
	private int m_LinkType = 0;
	private int m_Moniter = 0;
	private int m_BufferMode = 0;// 0,memory buffer; 1,sdcard buffer
	private int m_NetworkTimeOut = 0;
	// ============================
	private Surface m_Surface;
	private int m_Audio_BufSize;
	private AudioTrack audiotrack;
	String cmmmName = "";
	private AudioTrack m_Audiotrack;
	private String cur_dir_for_linux;
	private int isDisableHardwareCodec = 1;
	private int isAccelerateVideoRender = 0;// default to close
	private int seek_mode = 1;// default acurrate mode

	// CDR
	private int CHANNEL_ID = 0; //
	private int PROG_ID = 0; //
	private String BILL_ID = "0";// max 12
	private String Served_msisdn = "0";// max 32 //唯一MSISDN
	private String Reserved = "0";// max 12 // 64 Bytes

	int hasBeenDes; // flag for surface whether destroyed
	int req_startPos;
	// multi programs info
	private int nb_programs; // number of programs
	private int nb_index; // selected program index
	private String tag; // e.g Width1xHeight1 Bitrate1$Width2xHeight2 Bitrate2

	protected OnBufferingUpdateListener mOnBufferingUpdateListener;
	protected OnCompletionListener mOnCompletionListener;
	protected OnErrorListener mOnErrorListener;
	protected OnPreparedListener mOnPreparedListener;
	protected OnSeekCompleteListener mOnSeekCompleteListener;
	protected OnVideoSizeChangedListener mOnVideoSizeChangedListener;
	protected OnCurrentPositionListener mOnCurrentPositionListener;
	protected OnRecodeListener mOnRecodeListener;

	private int[] rawPicture;

	private int[] rawPituresCopy;

	private int rawWidth;
	private int rawHeight;
	private int pictureX;
	private int pictureY;
	private int pictureW;
	private int pictureH;
	// private int rotation;

	private boolean screenOnWhilePlaying;
	private boolean isThreadStated;
	private boolean isSeekable;
	private static boolean isInitiated;
	private boolean isRtsp;
	private boolean isSeeking;
	public boolean isStarted;

	protected MediaInfo mediaState;
	private int tmpcState;
	protected int state;
	private int percent;
	public static final int PLAYER_CLOSE = 0;
	public static final int PLAYER_STOPED = 1;
	public static final int PLAYER_STARTED = 2;
	public static final int PLAYER_GOTINFO = 3;
	public static final int PLAYER_PAUSED = 4;
	public static final int PLAYER_BUFFERING = 5;
	public static final int PLAYER_PLAYING = 6;

	private boolean isSufaceCallbackOpen = true;
	private float m_Scale = 1;
	private int m_Translate = 1;

	public float scale_centerX = 0;// 放大缩小依赖的中心坐标 x
	public float scale_centerY = 0;// 放大缩小依赖的中心坐标 y

	public float translate_dx = 0;// 移动到的位置 x
	public float translate_dy = 0;// 移动到的位置 y

	public Matrix mMatrix = new Matrix();
	public Matrix matrixTemp = new Matrix();
	public boolean isTranslate = false;// 是否平移
	public boolean isScale = false;// 是否缩放
	public boolean isTranslated = false;// 是否已经平移
	public boolean isScaled = false;// 是否已经缩放
	float[] values = new float[9];
	private float currentScale = 1.0f;// 当前缩放比例(相对于原图像)
	private float scaleTemp;

	private static String currentPlayUrl = "";// 当前正在使用的URL

	private MemoryCache memoryCache;

	/**
	 * 获取前/后n天日期(M月d日)
	 *
	 * @author 龚嘉
	 * @return
	 */
	private static String getMonthDay(int diff) {
		DateFormat df = new SimpleDateFormat("y年M月d日");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, diff);
		return df.format(c.getTime());
	}

	private static boolean checkExpDate(int diff) {
		boolean flag = false;
		String dateTag = getMonthDay(diff);
		if (dateTag.startsWith("2014年8月") || dateTag.startsWith("2014年9月")
				|| dateTag.startsWith("2014年10月")) {
			flag = true;
		}
		return flag;
	}

	private TMPCPlayer(int a) {
		// if(checkExpDate(0)){
		initPlayer();
		// }
		// + android.os.Build.VERSION.SDK_INT + " DEVICE="
		// + android.os.Build.DEVICE + " ID=" + android.os.Build.ID);

	}

	public TMPCPlayer() {

	}

	protected void initPlayer() {
		mediaState = null;
		mediaState = new MediaInfo();
	}

	public static TMPCPlayer createPlayer(String packageName, String url,
										  byte apn, SurfaceHolder holder) throws ParamaterFormatException {
		return createPlayer(packageName, url, apn, null, 0, null, holder);
	}

	public static TMPCPlayer createPlayer(Context context, Uri url,
										  SurfaceHolder holder) throws ParamaterFormatException {
		return createPlayer(context.getApplicationContext().getPackageName(),
				url == null ? null : url.toString(),
				Proxy.getDefaultHost() == null ? APN_CMNET : APN_CMWAP, null,
				0, null, holder);
	}

	public static TMPCPlayer createPlayer(Context context, Uri url,
										  SurfaceHolder holder, String apnName)
			throws ParamaterFormatException {
		if (apnName == null || apnName.length() < 1) {
			throw new ParamaterFormatException("apnName can not be null");
		}
		return createPlayer(context.getApplicationContext().getPackageName(),
				url == null ? null : url.toString(), APN_CMWAP, null, 0,
				apnName, holder);
	}

	public static TMPCPlayer createPlayer(String packageName, String url,
										  byte apn, String proxyHost, int proxyPort, SurfaceHolder holder)
			throws ParamaterFormatException {
		return createPlayer(packageName, url, apn, proxyHost, proxyPort, null,
				holder);
	}

	public static TMPCPlayer createPlayer(String packageName, String url,
										  byte apn, String cmmmName, SurfaceHolder holder)
			throws ParamaterFormatException {
		return createPlayer(packageName, url, apn, null, 0, cmmmName, holder);
	}

	public static TMPCPlayer createPlayer(String packageName, String url,
										  byte apn, String proxyHost, int proxyPort, String cmmmName,
										  SurfaceHolder holder) throws ParamaterFormatException {

		isInitiated = false;
		Log.i(DEBUG_display, "Jni_java_version : " + JNI_JAVA_VERSION);

		if (packageName == null || packageName.length() < 1) {
			throw new ParamaterFormatException("player paramater error");
		}
		if (holder == null) {
			throw new ParamaterFormatException("holder is null");
		}
		if (apn != APN_CMNET && apn != APN_CMWAP) {
			throw new ParamaterFormatException(
					"apn type error, must be 0(for cmnet) or 1(for cmwap)");
		}

		if (player == null) {
			player = new TMPCPlayer(0);
		} else {
			try {
				player.stop();
			} catch (OperationException e) {

			}
			player.initPlayer();
		}
		player.bitmap = null;
		player.param = null;
		player.cmmmName = cmmmName;
		Log.w("demo", "cmmmName2 = " + cmmmName);
		player.param = new PlayerParamater(packageName, url, apn, proxyHost,
				proxyPort, cmmmName);

		player.rawWidth = 0;
		player.rawHeight = 0;
		player.state = PLAYER_STOPED;

		player.holder = holder;
		/*
		 * if (holder != null) { holder.addCallback(player); }
		 */
		if (url != null) {
			player.isRtsp = false;
			player.isSeekable = player.checeSeekable(url);
			currentPlayUrl = url;
		}

		player.hasBeenDes = 0;

		String[] so_names = new String[9];
		so_names[0] = "rmh265dec";
		so_names[1] = "tmpc_source_neon";
		so_names[2] = "tmpc_source";
		so_names[3] = "tmpc_color_neon";
		so_names[4] = "tmpc_color";
		so_names[5] = "TMPC_jni";
		so_names[6] = "openglesRender";
		so_names[7] = "tmpc_surface";
		so_names[8] = get_hd_so_name();

		//
		if (!isInitiated) {
			String bk_dir = player.param.cur_dir_for_linux;
			int endIndex = bk_dir.length() - 4; // - "/lib"
			if (endIndex > 0) {
				player.param.cur_dir_for_linux = bk_dir.substring(0, endIndex)
						+ "/app_lib";
			}
			Log.i(DEBUG_display, "cur_dir_for_linux ="
					+ player.param.cur_dir_for_linux);
			// step 1: try "/app_lib" path
			if (!sos_load(so_names)) {
				boolean isRetry = false;
				if (player.param.cur_dir_for_linux != bk_dir) {
					player.param.cur_dir_for_linux = bk_dir;
					isRetry = sos_load(so_names);
				}
				if (!isRetry) {
					player.param.cur_dir_for_linux = "/system/lib";
					// step 3: try system lib
					sos_load2(so_names);
				}
			}

		}

		return player;
	}

	public void set_BufferTime(int ms) {
		m_BufferTime = ms;
	}

	public void set_LinkType(int type) {
		m_LinkType = type;
	}

	public void set_moniter(int moniter) {
		m_Moniter = moniter;
	}

	public void set_BufferMode(int mode) {
		m_BufferMode = mode;
	}

	public void disableHardwareCodec(int isDisabled) {
		isDisableHardwareCodec = isDisabled;
	}

	public void EnableAccelerateVideoRender(int isEnabled) {
		isAccelerateVideoRender = isEnabled;
	}

	public void setSeekMode(int mode) {
		seek_mode |= mode;
	}

	public void DisableHttpRangeField(boolean isDisable) {
		if (isDisable)
			seek_mode |= 0x80;
		else
			seek_mode &= 0x7f;
	}

	public void SetInstantMode(boolean mode, int link_type) {
		if (null != player.param) {
			player.param.SetInstantMode(mode);
			player.param.SetLinkType(link_type);
		} else {
			Loger.d(DEBUG_display, "param is null, setinstantmode failed");
		}

		player.set_LinkType(link_type);
		player.set_moniter(mode ? 1 : 0);
		Loger.d(DEBUG_display, "set link_type=" + link_type + ", monitor="
				+ m_Moniter);
	}

	public void SetShouldBufferTime(int ms) {
		m_BufferTime = ms;
		if (null != player.param) {
			player.param.SetShouldBufferTime(ms);
		}
	}

	public String getProgramsTag() {
		return tag;
	}

	public int getProgramNum() {
		return nb_programs;
	}

	public int getCurProgramNum() {
		return nb_index;
	}

	public void setCurProgramNum(int programNum) {
		nb_index = programNum;
		nativeSetProgram(nb_index);
	}

	public int getNetworkTraffic() {
		return nativeGetNetworkTraffic();
	}

	public int getNetworkSpeed() {
		return nativeGetNetworkSpeed();
	}

	public void setFullScreen(boolean isFullScreen) {// ljg
		if (isFullScreen)
			nativeClearVideoSurface(1);
		else
			nativeClearVideoSurface(0);
	}

	public void setScaleT(float scale) {

		m_Scale = scale;

	}

	// public void ClearVideoSurface() {
	// nativeClearVideoSurface(0);
	// }

	private String addPortForRtsp(String url) {
		String out_url = null;
		int bIndex = 0;
		int eIndex = 0;
		int colonIndex = 0;
		eIndex = url.indexOf("://") + 3;
		String header = url.substring(bIndex, eIndex);
		bIndex = eIndex;
		eIndex = url.indexOf('/', bIndex);
		colonIndex = url.substring(bIndex, eIndex).indexOf(':', 0);
		if (colonIndex == -1 && header.equalsIgnoreCase("rtsp://")) {
			out_url = url.substring(0, eIndex) + ":554" + url.substring(eIndex);
		} else {
			out_url = url;
		}
		return out_url;
	}

	public void setPlayUrl(String url) throws OperationException,
			ParamaterFormatException {
		Log.d(DEBUG_display, "setPlayUrl-->state=" + state);
		if (param == null) {
			throw new OperationException(
					"the player's main parameter is null. Please check if the player is released!");
		}

		// state > PLAYER_STOPED
		if (state > PLAYER_STARTED) {
			throw new OperationException(
					"can't change the play url bofore the player stoped or releazed");
		}

		if (url.length() < 10) {
			throw new ParamaterFormatException(
					"TMPCPlayer::checeSeekable, the paramater \"url\" is too short");
		}

		String reurl = url.substring(0, 4).toLowerCase() + url.substring(4);

		reurl = addPortForRtsp(reurl);

		param.setUrl(reurl);

		isSeekable = checeSeekable(reurl);

		m_sPlayUrl = reurl;
		Log.d("TMPCPlayer", "setPlayUrl-->m_sPlayUrl=" + m_sPlayUrl);

		if (!m_sPlayUrl.equals(currentPlayUrl)) {

			// 更换播放地址
			isStarted = false;
			currentPlayUrl = m_sPlayUrl;
		}
	}

	public void setApn(byte apn) throws OperationException,
			ParamaterFormatException {
		if (param == null) {
			throw new OperationException(
					"the player's main parameter is null. Please check if the player is released!");
		}

		if (state > PLAYER_STOPED) {
			throw new OperationException(
					"can't change the network apn bofore the player stoped or releazed");
		}

		param.setApn(apn);
	}

	public boolean isSeekable() {
		return isSeekable;
	}

	public short[] getPsdInfo() {
		short[] psd = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		return psd;
	}

	public MediaInfo getMediaInfo() {
		return mediaState;
	}

	public int getTPlayerState() {
		return state;
	}

	public boolean isPausable() {
		return isSeekable;
	}

	public int getCurrentPosition() {
		if (state > PLAYER_STOPED) {
			mediaState.cur_play_pos = nativeGetCurrentTime();
			return mediaState.cur_play_pos;
		}
		return 0;
	}

	public int getAlreadyBufferTime() {
		// int percent=nativeGetBufferProgress();
		// int set_buffer=param.getBufferTime();
		//
		// if (state > PLAYER_STOPED) {
		// mediaState.already_buffer_time=percent*set_buffer/100;
		// return mediaState.already_buffer_time;
		// }
		// return 0;
		// changed by 9song 20130513
		int alreadybuffertime = nativeGetAlreadyBufferTime();
		if (state > PLAYER_STOPED)
			return alreadybuffertime;
		else
			return 0;
	}

	public int getDuration() {
		if (state > PLAYER_STOPED) {
			return mediaState.dwDuration;
		}
		return 0;
	}

	public int getVideoHeight() {
		if (state > PLAYER_GOTINFO) {
			return rawHeight;
		}
		return 0;
	}

	public int getVideoWidth() {
		if (state > PLAYER_GOTINFO) {
			return rawWidth;
		}
		return 0;
	}

	public boolean isLooping() {
		return false;
	}

	public boolean isPlaying() {
		return state == PLAYER_PLAYING;
	}

	public int State() {
		return state;
	}

	public void prepare() {

		mPrepareHandler.sleep(500);
	}

	public void SetDisplayOutside(boolean javaDis) {
		if (null == this.param)
			return;

		if (javaDis) {
			// 1外显
			this.param.setOutDisplay(1);
		} else {
			// 0内显
			this.param.setOutDisplay(0);
		}
	}

	public void SetPsdNum(int band_num) {
		param.SetPsdNum(band_num);
	}

	private PrepareHandler mPrepareHandler = new PrepareHandler(
			Looper.getMainLooper());

	class PrepareHandler extends Handler {

		// since 1.3.5
		public PrepareHandler(Looper l) {
			super(l);
		}

		public void handleMessage(Message msg) {
			if (mOnPreparedListener != null) {
				mOnPreparedListener.onPrepared(TMPCPlayer.this);
			}
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	public void prepareAsync() {
		prepare();
	}

	public void start() throws OperationException {
		req_startPos = 0;
		start(-1);
	}

	public void StartRecord(String s, int val) {
		tmpcStartRecord(s, val);
	}

	public void StopRecord() {
		tmpcStopRecord();
	}

	private static boolean so_load(String dir, String soname) {
		String libFileName = dir + "/lib" + soname + ".so";
		if ((new File(libFileName)).exists()) {
			try {
				Log.i(DEBUG_display, "try to load " + soname + " from " + dir);
				System.load(libFileName);
			} catch (UnsatisfiedLinkError ULe) {
				Log.e(DEBUG_display, "there is no " + soname + " in " + dir);
			}
			return true;
		}
		return false;
	}

	private static void so_load2(String soname) {
		try {
			Log.i(DEBUG_display, "try to load " + soname + " from cust");
			System.loadLibrary(soname);
		} catch (UnsatisfiedLinkError e) {
			Log.e(DEBUG_display, "lib" + soname + ".so not found");
		}
	}

	private static void sos_load2(String[] so_names) {
		int i;
		for (i = 0; i < so_names.length; ++i) {
			so_load2(so_names[i]);
		}
	}

	private static boolean sos_load(String[] so_names) {
		int i;
		boolean result = false;
		for (i = 0; i < so_names.length; ++i) {
			if (so_load(player.param.cur_dir_for_linux, so_names[i]))
				result = true;
		}
		return result;
	}

	private static String get_hd_so_name() {
		if (android.os.Build.VERSION.SDK_INT <= 8) {
			// 2.1
			if (android.os.Build.VERSION.SDK_INT <= 7) {
				return "tmpc_hd_21";
			} else {
				return "tmpc_hd_22";
			}
		} else // > 4.2
			if (android.os.Build.VERSION.SDK_INT > 16) {
				return "tmpc_hd_42";
			} else // > 4.1
				if (android.os.Build.VERSION.SDK_INT > 15) {
					return "tmpc_hd_41";
				} else // 4.0
					if (android.os.Build.VERSION.SDK_INT >= 14) {
						return "tmpc_hd_40";
					} else {
						return "tmpc_hd";
					}
	}

	/**
	 * Starts or resumes playback at startPos.
	 *
	 * @param msec
	 *            the offset in milliseconds from the start to play to. -1 play
	 *            at beginning.
	 *
	 * @throws OperationException
	 *             if called in a illegal state
	 */
	public void start(int startPos) throws OperationException {
		if (state == PLAYER_CLOSE) {
			throw new OperationException("the player is released");
		}
		// String[] so_names = new String[9];
		// so_names[0] = "rmh265dec";
		// so_names[1] = "tmpc_source_neon";
		// so_names[2] = "tmpc_source";
		// so_names[3] = "tmpc_color_neon";
		// so_names[4] = "tmpc_color";
		// so_names[5] = "TMPC_jni";
		// so_names[6] = "openglesRender";
		// so_names[7] = "tmpc_surface";
		// so_names[8] = get_hd_so_name();
		//
		// if (!isInitiated) {
		// String bk_dir = player.param.cur_dir_for_linux;
		// int endIndex = bk_dir.length() - 4; // - "/lib"
		// if (endIndex > 0) {
		// player.param.cur_dir_for_linux = bk_dir.substring(0, endIndex)
		// + "/app_lib";
		// }
		// Log.i(DEBUG_display, "cur_dir_for_linux ="
		// + player.param.cur_dir_for_linux);
		// // step 1: try "/app_lib" path
		// if (!sos_load(so_names)) {
		// boolean isRetry = false;
		// if (player.param.cur_dir_for_linux != bk_dir) {
		// player.param.cur_dir_for_linux = bk_dir;
		// isRetry = sos_load(so_names);
		// }
		// if (!isRetry) {
		// player.param.cur_dir_for_linux = "/system/lib";
		// // step 3: try system lib
		// sos_load2(so_names);
		// }
		// }
		// }

		if (!isInitiated) {
			// tmpcInit(player.param.cur_dir_for_linux);
			cur_dir_for_linux = player.param.cur_dir_for_linux;
			nativeInit();
			isInitiated = true;
			tmSetDisplayMode(param.display_outside);
		}

		if (null != player.holder) {
			if (0 == param.display_outside) {
				// 内显
				player.param.mSurface = player.holder.getSurface();
				m_Surface = player.param.mSurface;
				Log.i("surface",
						"-------------------> TMPCPlayer.createPlayer :: mSurface is valid = "
								+ player.param.mSurface.isValid() + ", id = "
								+ player.param.mSurface);
				if (player.param.mSurface.isValid() == false) {
					// 如果是一个无效的surface
					throw new OperationException(
							"player.param.mSurface.isValid()=false");
				}
			} else {
				// 外显
				player.param.mSurface = player.holder.getSurface();
				m_Surface = player.param.mSurface;
				if (m_Surface.isValid()) {
					player.holder.addCallback(this);
				} else {
					throw new OperationException(
							"player.param.mSurface.isValid()=false");
				}

			}
		}

		if (startPos < 0) {
			param.start_pos = 0;
			req_startPos = -1;
		} else {
			Log.e(tag, "ts_seek: startPos=" + startPos);
			if (PLAYER_PAUSED == state)
				req_startPos = startPos;
			else
				req_startPos = -1;
			param.start_pos = startPos;
		}
		m_SeekPos = param.start_pos;
		Log.e(tag, "ts_seek: m_SeekPos=" + m_SeekPos);
		param.hasBeenDes = hasBeenDes;
		// tmpcStart(param);
		if (!isStarted) {

			Log.i("surface", "-=-=-=-=-=nativeStart=-=-=-=-=,param.APN_Type="
					+ param.APN_Type + "param.proxyaddr=" + param.proxyaddr
					+ "param.proxyport=" + param.proxyport);
			nativeStart(param.APN_Type, param.proxyaddr, param.proxyport);
			isStarted = true;
		} else {
			Log.d("TMPCPlayer", "start()-->m_sPlayUrl=" + m_sPlayUrl);
			nativePlay();
			Log.d("TMPCPlayer", "start()-->m_sPlayUrl=" + m_sPlayUrl);
		}
		hasBeenDes = 0;
		if (state <= PLAYER_STOPED) {
			update();
			changeState(PLAYER_STARTED);
		}
		if (screenOnWhilePlaying && holder != null) {
			holder.setKeepScreenOn(true);
		}
	}

	/**
	 * Pauses playback.
	 * <p>
	 * call {@link #isPausable()} to check if the playback is pausable
	 * </p>
	 *
	 * @throws OperationException
	 *             if called in a illegal state or the playback isn't pausable
	 */
	public void pause() throws OperationException {
		Log.e("JAR", "-=-=-=-=-=pause-=-=-=-=-=-=-");
		if (state < PLAYER_PAUSED) {
			throw new OperationException("illegal state");
		}
		if (!isPausable()) {
			throw new OperationException("can not pause on live mode");
		}
		nativePause();
		Log.e("JAR", "-=-=-=-=-=123456-=-=-=-=-=-=-");
		changeState(PLAYER_PAUSED);
		Log.e("JAR", "-=-=-=-=-=654321-=-=-=-=-=-=-");
		if (screenOnWhilePlaying && holder != null) {
			holder.setKeepScreenOn(false);
		}
	}

	/**
	 * Seeks to specified time.
	 * <p>
	 * call {@link #isSeekable()} to check if the playback is seekable
	 * </p>
	 *
	 * @param msec
	 *            the offset in milliseconds from the start to seek to.
	 * @throws OperationException
	 *             if called in a illegal state or the playback isn't seekable
	 */
	public void seekTo(int msec, int seekToKeyFrame) throws OperationException {
		if (!isPlaying()) {
			throw new OperationException("playback is not in playing state");
		}
		if (!isSeekable()) {
			throw new OperationException("can not seek on live mode");
		}
		Log.d("jni_java", "seekTo : " + msec);
		isSeeking = true;
		nativeSeek(msec, seekToKeyFrame);
		req_startPos = -1;
	}

	public void stop() throws OperationException {
		/*
		 * if (state == PLAYER_CLOSE) { throw new
		 * OperationException("the player is released"); }
		 */
		if (state > PLAYER_STOPED) {
			changeState(PLAYER_STOPED);
			nativeStop();
			if (null != audiotrack) {
				audiotrack.release();
				audiotrack = null;
			}
			if (screenOnWhilePlaying && holder != null) {
				holder.setKeepScreenOn(false);
			}
		}
	}

	public void reset() {
		try {
			Log.d("jni", "======================reset");
			stop();
		} catch (OperationException e) {

		}
	}

	public void release() {
		Log.d("jni", "======================release");
		if (player != null) {
			try {
				stop();
			} catch (OperationException e) {

			}
		}

		changeState(PLAYER_CLOSE);

		// nativeQuit(); //2009-12-10 since 1.3.1

		if (null != bitmap) {
			if (!bitmap.isRecycled()) {
				bitmap.recycle(); // 回收图片所占的内存
				System.gc(); // 提醒系统及时回收
			}
		}

		rawPicture = null;
		param = null;
		holder = null;
		surface = null;
		canvas = null;
		bitmap = null;
		rectDest = null;
		matrix = null;
		paint = null;

		mOnBufferingUpdateListener = null;
		mOnCompletionListener = null;
		mOnErrorListener = null;
		mOnPreparedListener = null;
		mOnSeekCompleteListener = null;
		mOnVideoSizeChangedListener = null;
		mOnCurrentPositionListener = null;
		mOnRecodeListener = null;
		player = null;

	}

	public void setDisplay(SurfaceHolder holder) {
		this.holder = holder;
		if (holder != null) {
			// holder.addCallback(this);
			if (screenOnWhilePlaying && state > PLAYER_STOPED) {
				holder.setKeepScreenOn(true);
			}
		}
	}

	/* add by gongjia, must invoke by demo for Android4.4 */
	public void setSurfaceLock(SurfaceHolder holder) {
		this.holder = holder;
		if (holder != null) {
			try {
				canvas = holder.lockCanvas(null);// 获取画布
			} catch (Exception e) {
				Log.e("onError", "clear err = " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	public void setLooping(boolean looping) {

	}

	public void setScreenOnWhilePlaying(boolean screenOn) {
		screenOnWhilePlaying = screenOn;
		if (holder != null) {
			holder.setKeepScreenOn(screenOn && (state > PLAYER_STOPED));
		}
	}

	public void setOnBufferingUpdateListener(
			TMPCPlayer.OnBufferingUpdateListener listener) {
		mOnBufferingUpdateListener = listener;
	}

	public void setOnCurrentPositionListener(
			TMPCPlayer.OnCurrentPositionListener listener) {
		mOnCurrentPositionListener = listener;
	}

	public void setOnCompletionListener(TMPCPlayer.OnCompletionListener listener) {
		mOnCompletionListener = listener;
	}

	public void setOnErrorListener(TMPCPlayer.OnErrorListener listener) {
		mOnErrorListener = listener;
	}

	public void setOnRecodeListener(TMPCPlayer.OnRecodeListener listener) {
		mOnRecodeListener = listener;
	}

	public void setOnVideoSizeChangedListener(
			TMPCPlayer.OnVideoSizeChangedListener listener) {
		mOnVideoSizeChangedListener = listener;
	}

	public boolean getRawPicture888(int[] buffer) {
		boolean isOk = false;
		int bufferSize = 0;

		bufferSize = tmpcGetRawPicture32(buffer);
		Log.d("TMPCPlayer", "buffer length=" + buffer.length + ";buffer[0]="
				+ buffer[0] + ";buffer[1]=" + buffer[1]);
		if (buffer[0] == 0 || buffer[1] == 0) {
			bufferSize = 0;
		}

		if (bufferSize > 0) {
			isOk = true;
		} else {
			isOk = false;
		}
		return isOk;

	}

	public int[] getRawPicture999() {

		int[] buffer = null;

		// if (rawPituresCopy != null && rawPituresCopy.length > 0) {
		//
		// buffer = rawPituresCopy;
		// }
		if (rawPicture != null && rawPicture.length > 0) {

			buffer = rawPicture;
		}

		return buffer;
	}

	/*
	 * private native boolean tmpcInit(String s);///////////// private native
	 * void tmpcUnInit();////////////// private native boolean
	 * tmpcStart(PlayerParamater param);////////////// private native int
	 * tmpcGetPlayerState();//////////// private native boolean tmpcBeginShow();
	 * ////////// private native int tmpcGetRawPicture(byte[] buffer);//////////
	 * private native int tmpcGetRawPicture32(int[] buffer);//////////// private
	 * native boolean tmpcEndShow();/////////// private native void
	 * tmpcPause();/////////// private native int tmpcSeek(int ms);////////////
	 * private native void tmpcStop();/////////////// private native int
	 * tmpcSetAudioVolume(int val );//////////// //private native int
	 * tmpcGetAudioVolume();///////// private native int
	 * tmpcSetAudioTrack(PlayerParamater param);/////////// private native void
	 * tmpcRotate(int rotate);/////////// private native void
	 * tmpcNotifySurfaceDestroyed();//////////// private native int
	 * tmpcGetAudioFreq();/////////// private native void
	 * tmpcGetMediaInfo(MediaInfo mediaInfo);/////////////// private native
	 * short[]tmpcGetPsdInfo();
	 */

	public native void nativeInit();

	public native void nativeSetup(Object PlayerEngineImpl);

	public native void nativeStart(int apnType, String proxy, int port);

	public native void nativePlay();

	public native void nativeStop();

	public native void nativeQuit();

	public native void nativePause();

	public native void nativeSetAudioTrack();

	public native int nativeGetPlayStatus();

	public native void nativeSeek(int pos, int bkey);

	public native int nativeGetBufferProgress();

	public native int nativeGetAlreadyBufferTime();

	public native int nativeGetCurrentTime();

	public native void tmSetDisplayMode(int s);

	/*
	 * s 录制的路径 val 录制的时间，单位为秒
	 */
	private native int tmpcStartRecord(String s, int val);

	private native int tmpcStopRecord();

	private native int nativeGetNetworkTraffic();

	private native int nativeGetNetworkSpeed();

	// mute 1：静音 0 非静音
	public native int nativeSetAudioMute(int mute);

	private native void nativeClearVideoSurface(int reserve);

	// ================= push buffer

	private native boolean tmpcBeginShow(); // ////////

	private native int tmpcGetRawPicture32(int[] buffer);// //////////

	private native boolean tmpcEndShow();// /////////

	// ================= push buffer

	public native boolean tmpcStartGetPlayUrl(String str);

	public native String tmpcGetPlayurl();

	public native void nativeSetProgram(int progarm);

	/**
	 * 加速播放
	 *
	 * @param num
	 *            分子
	 * @param den
	 *            分母
	 * @return
	 */
	private native void nativeSetPlaySpeed(int num, int den);

	/* get player library version */
	private native static String tmpcGetPlayerVersion();

	public static String getPlayerVersion() {
		return tmpcGetPlayerVersion();
	}

	/**
	 * 加速播放
	 *
	 * @param num
	 *            分子
	 * @param den
	 *            分母
	 * @return
	 */
	public void setPlaySpeed(int num, int den) {
		nativeSetPlaySpeed(num, den);

	}

	protected void changeState(int state) {
		// Loger.d("state", ".............................. change state = " +
		// state);
		this.state = state;

		switch (state) {
			case PLAYER_CLOSE:
				break;

			case PLAYER_STOPED:
				break;

			case PLAYER_STARTED:
				break;

			case PLAYER_GOTINFO:
				break;

			case PLAYER_BUFFERING:
				break;

			case PLAYER_PAUSED:
				break;

			case PLAYER_PLAYING:
				break;
		}
	}

	private RefreshHandler mRedrawHandler = new RefreshHandler(
			Looper.getMainLooper());

	class RefreshHandler extends Handler {

		public RefreshHandler(Looper l) {
			super(l);
		}

		public void handleMessage(Message msg) {
			if (state >= PLAYER_STOPED) {
				if (PLAYER_STOPED == state)
					WAIT_TIME = 100;
				else
					WAIT_TIME = DEF_WAIT_TIME;

				TMPCPlayer.this.update();
			} else {
				Log.w(DEBUG_display, "handleMessage state =" + state);
			}
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	public void CreatAudioTrackFromNative(int freq, int format, int channels)
			throws IOException {
		Log.w("demo", " CreatAudioTrackFromNative ");

		// FileOutputStream out = null;
		// out = new FileOutputStream(new File("/sdcard/11.log"));
		// String str = null;
		// str = String.format("freq %d, format %d  channel %d\n",
		// freq,format,channels);
		// out.write(str.getBytes());
		// out.close();

		int fmt = 0;
		switch (format) {
			case 16:
				fmt = AudioFormat.ENCODING_PCM_16BIT;
				break;
			case 8:
				fmt = AudioFormat.ENCODING_PCM_8BIT;
				break;
			default:
				Log.e(DEBUG_display, "format Error");
				return;
		}

		int chnls = 0;
		switch (channels) {
			case 1:
				chnls = AudioFormat.CHANNEL_CONFIGURATION_MONO;
				break;
			case 2:
				chnls = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
				break;
			default:
				Log.e(DEBUG_display, "channels Error");
				return;
		}

		param.audiotrack_buffersize = AudioTrack.getMinBufferSize(freq, chnls,
				fmt) * 4;
		if (param.audiotrack_buffersize < 1024) {
			Loger.d(DEBUG_display, "buffsize is too small");

			// param.audiotrack_buffersize=12;
		} else {
			// param.audiotrack_buffersize*=12;
		}
		m_Audio_BufSize = param.audiotrack_buffersize;
		Loger.i(DEBUG_display, "audiotrack_buffersize = "
				+ param.audiotrack_buffersize);

		audiotrack = new AudioTrack(AudioManager.STREAM_MUSIC, freq, chnls,
				fmt, param.audiotrack_buffersize, AudioTrack.MODE_STREAM);

		Log.i(DEBUG_display, "buffsersize = " + param.audiotrack_buffersize);
		if (audiotrack == null) {
			Log.d("demo", "CreatAudioTrackFromNative failed!");
			return;
		}
		player.param.audiotrack = audiotrack;
		m_Audiotrack = player.param.audiotrack;
		nativeSetAudioTrack();
	}

	public void setDataSource(String path) throws IllegalStateException,
			IOException, IllegalArgumentException {
		try {
			setPlayUrl(path);
		} catch (OperationException e) {
			throw new IllegalStateException(e.getMessage());
		} catch (ParamaterFormatException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	// 9song 20120417
	public void setCDR(int channelID, int progID, String billID,
					   String servedmsisdn, String reserved) {
		param.setGDCDR(channelID, progID, billID, servedmsisdn, reserved);
		player.CHANNEL_ID = channelID; //
		player.PROG_ID = progID; //
		player.BILL_ID = billID;// max 12
		player.Served_msisdn = servedmsisdn;// max 32 //唯一MSISDN
		player.Reserved = reserved;// max 12 // 64 Bytes
	}

	public void setAudioStreamType(int streamtype) {

	}

	private void onSizeChanged(int width, int height) {
		if (mOnVideoSizeChangedListener != null) {
			mOnVideoSizeChangedListener.onVideoSizeChanged(this, width, height);
		}
	}

	private void onComplet(boolean finish) {
		try { // modif::2009-07-01 14:47
			Log.d("jni", "============onComplet()==========onComplet");
			stop();
			screenOnWhilePlaying = false;
			isThreadStated = false;
			isSeekable = false;
			isInitiated = false;
			isStarted = false;
			isRtsp = false;
			isSeeking = false;
		} catch (OperationException e) {
			Loger.e("onError", "trying to stop the play error");
		}
		if (finish && mOnCompletionListener != null) {
			mOnCompletionListener.onCompletion(this);
		}
	}

	private void onBufferingBegin() {
		if (mOnBufferingUpdateListener != null) {
			mOnBufferingUpdateListener.onBufferingBegin(this);
		}
	}

	private void onBufferingUpdate(int percent) {
		if (mOnBufferingUpdateListener != null) {
			mOnBufferingUpdateListener.onBufferingUpdate(this, percent);
		}
	}

	private void onCurrentPosition(int currentPosition) {
		if (mOnCurrentPositionListener != null) {
			mOnCurrentPositionListener.onCurrentPosition(this, currentPosition);
		}
	}

	private void onBufferingComplete() {
		if (mOnBufferingUpdateListener != null) {
			mOnBufferingUpdateListener.onBufferingComplete(this);
		}
		if (mOnSeekCompleteListener != null && isSeeking) {
			mOnSeekCompleteListener.onSeekComplete(this);
		}
		isSeeking = false;
	}

	private void onError(int what) {
		Log.e("onError", "======================onError" + what);
		if (mOnErrorListener != null) {
			if (mOnErrorListener.onError(this, what, 0)) {
				return;
			}
		}
		onComplet(false);
	}

	private void onError(int what, int param) {
		Log.e("onError", "======================onError" + what + " param="
				+ param);
		if (mOnErrorListener != null) {
			if (mOnErrorListener.onError(this, what, param)) {
				return;
			}
		}
		if (what != TMPC_HLS_SUGGEST_CHANGECHANEL)
			onComplet(false);
		else
			setCurProgramNum(param);
	}

	private boolean checeSeekable(String url) throws ParamaterFormatException {
		if (url.length() < 10) {
			throw new ParamaterFormatException(
					"TMPCPlayer::checeSeekable, the paramater \"url\" is too short");
		}

		String reurl = url.substring(0, 4).toLowerCase() + url.substring(4);
		if (reurl.startsWith(PlayerParamater.TMSS)) {
			return false;
		}
		if (reurl.startsWith(PlayerParamater.RTSP))
			isRtsp = true;
		return true;
	}

	private void rawPictureSizeChanged(int width, int height) {
		rawWidth = mediaState.videoWidth;
		rawHeight = mediaState.videoHeight;
		Log.d(DEBUG_display, "rawPictureSizeChanged");
		Log.d(DEBUG_display, "rawW =" + rawWidth + "  rawH = " + rawHeight);
		Log.e(DEBUG_display,
				"-=-=-=-=-=-=-=-=-param.display_outside-=-=-=-=-=-=-=-="
						+ param.display_outside);

		synchronized (surfaceLock) {
			Log.d(DEBUG_display, "new rawPicture");
			rawPicture = null;
			if (1 == param.display_outside) {
				if (rawPicture == null) {
					LoggerUtil.e("显示内存大小:" + "rawWidth:" + rawWidth
							+ "-------------" + "rawHeight:" + rawHeight);
					// System.gc();
					// rawPicture = new int[rawWidth * rawHeight];
					rawPicture = tmpcGetRawBuffer(rawWidth * rawHeight);
				}
				// rawPicture = new int[rawWidth * rawHeight];
			}

		}
		if (1 == param.display_outside)
			setPictureFixToHolder(rawWidth, rawHeight, holder);

		onSizeChanged(rawWidth, rawHeight);
	}

	private void setPictureFixToHolder(int width, int height,
									   SurfaceHolder holder) {
		if (holder == null) {
			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: holder = null");
			return;
		}
		if (width <= 0 || height <= 0) {
			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: width = " + width
							+ "; height = " + height);
			return;
		}
		Log.w(DEBUG_display, "setPictureFixToHolder0"
				+ "TMPCPlayer::setPictureFixToHolder: width = " + width
				+ "; height = " + height);
		synchronized (surfaceLock) {
			Log.w(DEBUG_display, "setPictureFixToHolder1");
			surface = null;
			surface = holder.getSurface();

			rectDest = null;
			rectDest = holder.getSurfaceFrame();
			matrix = null;
			matrix = new Matrix();
			int rectw = width;// width;//rectDest.right - rectDest.left;
			int recth = height;// height;//rectDest.bottom - rectDest.top;
			Log.i(DEBUG_display, "rect = " + rectDest.right + " "
					+ rectDest.left + " " + rectDest.bottom + " "
					+ rectDest.top);

			rectDest.right = width;
			rectDest.left = 0;
			rectDest.bottom = height;
			rectDest.top = 0;

			Log.e(DEBUG_display, "rect = " + rectDest.right + " "
					+ rectDest.left + " " + rectDest.bottom + " "
					+ rectDest.top);

			int rotation = 0;

			Log.w(DEBUG_display, "setPictureFixToHolder2 :" + rectw + "*"
					+ recth + " => " + width + "*" + height);
			if (((width > height) && (rectw < recth))
					|| ((width < height) && (rectw > recth))) {
				rotation = 90;
				Log.w(DEBUG_display, "rotation = 90");
			}

			float scale = 1;
			if (rotation == 0) {
				float h_scale = (float) rectw / width;
				float v_scale = (float) recth / height;
				scale = h_scale < v_scale ? h_scale : v_scale;

				pictureW = new Float(width * scale).intValue();
				pictureH = new Float(height * scale).intValue();
				if (pictureW < rectw) {
					pictureX = (rectw - pictureW) / 2;
				} else {
					pictureX = 0;
				}
				if (pictureH < recth) {
					pictureY = (recth - pictureH) / 2;
				} else {
					pictureY = 0;
				}
			} else if (rotation == 90) {
				float h_scale = (float) recth / width;
				float v_scale = (float) rectw / height;
				scale = h_scale < v_scale ? h_scale : v_scale;

				pictureW = new Float(width * scale).intValue();
				pictureH = new Float(height * scale).intValue();
				if (pictureW < recth) {
					pictureX = (recth - pictureW) / 2;
				} else {
					pictureX = 0;
				}
				if (pictureH < rectw) {
					pictureY = (rectw - pictureH) / 2 - rectw;
				} else {
					pictureY = -rectw;
				}
			}

			pictureX = new Float(pictureX / scale).intValue();
			pictureY = new Float(pictureY / scale).intValue();

			paint = null;
			if (scale != 1) {
				paint = new Paint();
				paint.setFlags(Paint.FILTER_BITMAP_FLAG);
			}

			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: width = " + width);
			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: height = " + height);
			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: rectw = " + rectw);
			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: recth = " + recth);

			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: scale = " + scale);
			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: paint = " + paint);
			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: rotation = " + rotation);
			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: pictureX = " + pictureX);
			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: pictureY = " + pictureY);
			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: pictureW = " + pictureW);
			Loger.d(DEBUG_display,
					"TMPCPlayer::setPictureFixToHolder: pictureH = " + pictureH);

			matrix.postScale(scale, scale);
			matrix.postRotate(rotation);
		}

	}

	/*
	 * private void setPictureFixToHolder(int width, int height, SurfaceHolder
	 * holder) { if (holder == null) { Loger.d(DEBUG_display,
	 * "TMPCPlayer::setPictureFixToHolder: holder = null"); return; } if (width
	 * <= 0 || height <= 0) { Loger.d(DEBUG_display,
	 * "TMPCPlayer::setPictureFixToHolder: width = " + width + "; height = " +
	 * height); return; } Log.w(DEBUG_display, "setPictureFixToHolder0");
	 * synchronized (surfaceLock) { Log.w(DEBUG_display,
	 * "setPictureFixToHolder1"); surface = null; surface = holder.getSurface();
	 * rectDest = null; rectDest = holder.getSurfaceFrame(); matrix = null;
	 * matrix = new Matrix(); int rectw = rectDest.right - rectDest.left; int
	 * recth = rectDest.bottom - rectDest.top; Log.i(DEBUG_display,
	 * "rect = "+rectDest
	 * .right+" "+rectDest.left+" "+rectDest.bottom+" "+rectDest.top);
	 *
	 * int rotation = 0;
	 *
	 * Log.w(DEBUG_display,
	 * "setPictureFixToHolder2 :"+rectw+"*"+recth+" => "+width+"*"+height); if
	 * (((width > height) && (rectw < recth)) || ((width < height) && (rectw >
	 * recth))) { rotation = 90; Log.w(DEBUG_display, "rotation = 90"); }
	 *
	 * float scale = 1; if (rotation == 0) { float h_scale = (float)rectw/width;
	 * float v_scale = (float)recth/height; scale = h_scale < v_scale ? h_scale
	 * : v_scale;
	 *
	 * pictureW = new Float(width*scale).intValue(); pictureH = new
	 * Float(height*scale).intValue(); if (pictureW < rectw) { pictureX = (rectw
	 * - pictureW)/2; } else { pictureX = 0; } if (pictureH < recth) { pictureY
	 * = (recth - pictureH)/2; } else { pictureY = 0; } } else if (rotation ==
	 * 90) { float h_scale = (float)recth/width; float v_scale =
	 * (float)rectw/height; scale = h_scale < v_scale ? h_scale : v_scale;
	 *
	 * pictureW = new Float(width*scale).intValue(); pictureH = new
	 * Float(height*scale).intValue(); if (pictureW < recth) { pictureX = (recth
	 * - pictureW)/2; } else { pictureX = 0; } if (pictureH < rectw) { pictureY
	 * = (rectw - pictureH)/2 - rectw; } else { pictureY = - rectw; } }
	 *
	 * pictureX = new Float(pictureX/scale).intValue(); pictureY = new
	 * Float(pictureY/scale).intValue();
	 *
	 * paint = null; if (scale != 1) { paint = new Paint();
	 * paint.setFlags(Paint.FILTER_BITMAP_FLAG); }
	 *
	 * Loger.d(DEBUG_display, "TMPCPlayer::setPictureFixToHolder: width = " +
	 * width); Loger.d(DEBUG_display,
	 * "TMPCPlayer::setPictureFixToHolder: height = " + height);
	 * Loger.d(DEBUG_display, "TMPCPlayer::setPictureFixToHolder: rectw = " +
	 * rectw); Loger.d(DEBUG_display,
	 * "TMPCPlayer::setPictureFixToHolder: recth = " + recth);
	 *
	 * Loger.d(DEBUG_display, "TMPCPlayer::setPictureFixToHolder: scale = " +
	 * scale); Loger.d(DEBUG_display,
	 * "TMPCPlayer::setPictureFixToHolder: paint = " + paint);
	 * Loger.d(DEBUG_display, "TMPCPlayer::setPictureFixToHolder: rotation = " +
	 * rotation); Loger.d(DEBUG_display,
	 * "TMPCPlayer::setPictureFixToHolder: pictureX = " + pictureX);
	 * Loger.d(DEBUG_display, "TMPCPlayer::setPictureFixToHolder: pictureY = " +
	 * pictureY); Loger.d(DEBUG_display,
	 * "TMPCPlayer::setPictureFixToHolder: pictureW = " + pictureW);
	 * Loger.d(DEBUG_display, "TMPCPlayer::setPictureFixToHolder: pictureH = " +
	 * pictureH);
	 *
	 *
	 * matrix.postScale(scale, scale); matrix.postRotate(rotation); } }
	 */

	private void setPictureFixToHolder2(int width, int height,
										SurfaceHolder holder) {
	}

	/**
	 * indicate paramater formate error
	 *
	 */
	public static class ParamaterFormatException extends Exception {
		private static final long serialVersionUID = 1L;

		public ParamaterFormatException(String msg) {
			super(msg);
		}
	}

	/**
	 * indicate operation erorr
	 *
	 */
	public static class OperationException extends Exception {
		private static final long serialVersionUID = 1L;

		public OperationException(String msg) {
			super(msg);
		}
	}

	public interface OnBufferingUpdateListener {

		void onBufferingUpdate(TMPCPlayer player, int percent);

		void onBufferingBegin(TMPCPlayer player);

		void onBufferingComplete(TMPCPlayer player);
	}

	public interface OnCompletionListener {
		void onCompletion(TMPCPlayer player);
	}

	public interface OnCurrentPositionListener {
		void onCurrentPosition(TMPCPlayer player, int currentPosition);
	}

	public interface OnRecodeListener {
		void onTMPCRecodeStarted();

		void onTMPCRecodeStoped();
	}

	public interface OnVideoSizeChangedListener {

		public void onVideoSizeChanged(TMPCPlayer player, int width, int height);
	}

	public interface OnPreparedListener {

		public void onPrepared(TMPCPlayer player);
	}

	public interface OnSeekCompleteListener {
		public void onSeekComplete(TMPCPlayer player);
	}

	public void setOnSeekCompleteListener(
			TMPCPlayer.OnSeekCompleteListener listener) {
		mOnSeekCompleteListener = listener;
	}

	public void setOnPreparedListener(TMPCPlayer.OnPreparedListener listener) {
		mOnPreparedListener = listener;
	}

	public interface OnErrorListener {
		/**
		 * Called to indicate an error.
		 *
		 * @param player
		 *            the TMPCPlayer the error pertains to
		 * @param what
		 *            the type of error that has occurred:
		 *            <ul>
		 *            <li>{@link #TMPC_ERROR_OUT_OF_MEMORY}
		 *            <li>{@link #TMPC_ERROR_INIT_DEVICE_FAIL}
		 *            <li>{@link #TMPC_ERROR_OPEN_LIB_FAIL}
		 *            <li>{@link #TMPC_ERROR_INIT_STREAM_FAIL}
		 *            <li>{@link #TMPC_ERROR_NETWORK_FAIL}
		 *            <li>{@link #TMPC_ERROR_NETWORK_REFUSE}
		 *            <li>{@link #TMPC_ERROR_NETWORK_TIMEOUT}
		 *            <li>{@link #TMPC_ERROR_MEDIA_SPEC}
		 *            <li>{@link #TMPC_ERROR_FILE_UNSUPPORT}
		 *            <li>{@link #TMPC_ERROR_UNKNOWN}
		 *            </ul>
		 * @param extra
		 *            an extra code, specific to the error. Typically
		 *            implementation dependant.
		 * @return True if the method handled the error, false if it didn't.
		 *         Returning false, or not having an OnErrorListener at all,
		 *         will cause the OnCompletionListener to be called.
		 */
		boolean onError(TMPCPlayer player, int what, int extra);
	}

	/**
	 * out of memory error
	 */
	public static final int TMPC_ERROR_OUT_OF_MEMORY = 0;

	/**
	 * initiate audio/video device erro
	 */
	public static final int TMPC_ERROR_INIT_DEVICE_FAIL = 1;

	/**
	 * open lib error
	 */
	public static final int TMPC_ERROR_OPEN_LIB_FAIL = 2;

	/**
	 * initiate steam error
	 */
	public static final int TMPC_ERROR_INIT_STREAM_FAIL = 3;

	/**
	 * network error
	 */
	public static final int TMPC_ERROR_NETWORK_FAIL = 4;

	/**
	 * network refuse
	 *
	 * @since TMPCPlayer 1.1
	 */
	public static final int TMPC_ERROR_NETWORK_REFUSE = 5;

	/**
	 * network time out
	 *
	 * @since TMPCPlayer 1.1
	 */
	public static final int TMPC_ERROR_NETWORK_TIMEOUT = 6;

	/**
	 * media file error
	 */
	public static final int TMPC_ERROR_MEDIA_SPEC = 7;

	/**
	 * file format unsupport
	 */
	public static final int TMPC_ERROR_FILE_UNSUPPORT = 8;

	/**
	 * unknown error
	 */
	public static final int TMPC_ERROR_UNKNOWN = 9;
	/**
	 * no object
	 */
	public static final int TMPC_ERROR_NO_PLAY_OBJECT = 10;
	/**
	 * invalid url
	 */
	public static final int TMPC_ERROR_INVALID_URL = 11;
	/**
	 * start plugin failed
	 */
	// public static final int TMPC_ERROR_START_PLUGIN_FAILED = 11;

	public static final int TMPC_CODEC_ERROR_FAILED = 12;
	public static final int TMPC_DEMOPLAY_TIMEOUT_FAILED = 13;
	public static final int TMPC_HLS_SUGGEST_CHANGECHANEL = 14; // hls流建议，切换频道消息（网络状况太差），可以不处理
	/**
	 * TP2P Function ERROR failed
	 */
	public static final int TMPC_TP2P_SERVER_NOT_FOUND = 80; // TP2P服务器不在线
	public static final int TMPC_TP2P_NO_P2P_CHANNEL = 81; // 无法建立TP2P服务
	public static final int TMPC_TP2P_SERVER_COMMON_FAIL = 82; // TP2P服务所有其他错误

	static class Loger {
		static void d(String tag, String msg) {
			if (IS_DEBUG_MODE) {
				Log.d(tag, msg);
			}
		}

		static void e(String tag, String msg) {
			if (IS_DEBUG_MODE) {
				Log.e(tag, msg);
			}
		}

		static void w(String tag, String msg) {
			if (IS_DEBUG_MODE) {
				Log.w(tag, msg);
			}
		}

		static void i(String tag, String msg) {
			if (IS_DEBUG_MODE) {
				Log.i(tag, msg);
			}
		}
	}

	// ============================================surface

	// ==============================================================================
	private void handlePosition() {
		if (null != mOnCurrentPositionListener && state > PLAYER_STOPED) {
			onCurrentPosition(mediaState.cur_play_pos);
			positionHandler.sleep(1000);
		}
	}

	private PositionHandler positionHandler = new PositionHandler(
			Looper.getMainLooper());

	class PositionHandler extends Handler {
		public PositionHandler(Looper mainLooper) {
			super(mainLooper);
		}

		public void handleMessage(Message msg) {
			handlePosition();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	private void PostEventFromNative(int notify_id, int param) {
		if (event_id != -1) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		synchronized (eventLock) {
			Log.i("TMPCPlayer", "PostEventFromNative(" + event_id + ","
					+ notify_id + "," + param + ")");
			switch (notify_id) {
				case TMPC_START_RECORD:
				case TMPC_STOP_RECORD:
					recode_event_id = notify_id;
					break;
				default:
					event_id = notify_id;
					event_param = param;
					break;
			}
		}
		// mRedrawHandler.sleep(0);
	}

	private void Get_mediaInfo_from_native(int dwDuration, int videoWidth,
										   int videoHeight, int live, int bitrate) {

		Log.w("Get_mediaInfo_from_native",
				"Get_mediaInfo_from_native dwDuration=" + dwDuration
						+ ";videoWidth=" + videoWidth + ";videoHeight="
						+ videoHeight + ";live=" + live);

		mediaState.dwDuration = dwDuration;
		mediaState.videoWidth = videoWidth;
		mediaState.videoHeight = videoHeight;
		mediaState.live = live;
		mediaState.bitrate = bitrate;

		if (mediaState.dwDuration <= 0)
			isSeekable = false;
		else if (mediaState.dwDuration > 0)
			isSeekable = true;
	}

	private void Get_ProgramsInfo_From_Native(int programsNum,
											  int curProgramNum, String videoInfo) {
		this.nb_programs = programsNum;
		this.nb_index = curProgramNum;
		this.tag = videoInfo;
		Log.i(DEBUG_display, "programsinfo-->nb_programs:" + nb_programs);
		Log.i(DEBUG_display, "programsinfo-->nb_index:" + nb_index);
		Log.i(DEBUG_display, "programsinfo-->tag:" + tag);
	}

	private void Get_Cur_Status_From_Native(int pos) {
		Log.w("CB", "Get_Cur_Pos_From_Native pos=" + pos);
		mediaState.cur_play_pos = pos;
	}

	int event_id = 0xffff;
	int recode_event_id = -1;
	int loop;
	int isValideAudio;
	long sleepTime;
	int event_param;

	private void update() {
		int param = 0;
		long updatetime = System.currentTimeMillis();
		loop++;

		if (state > PLAYER_STOPED) {
			// tmpcState = nativeGetPlayStatus();
			synchronized (eventLock) {
				tmpcState = event_id;
				param = event_param;
				event_id = -1;
			}
			if ((tmpcState > 0) && (tmpcState != 52)) {
				Loger.d(DEBUG_tmpcState, "============== tmpcState > 0 : "
						+ tmpcState);
			}

			switch (recode_event_id) {
				case TMPC_START_RECORD:
					if (null != mOnRecodeListener)
						mOnRecodeListener.onTMPCRecodeStarted();
					recode_event_id = -1;
					break;
				case TMPC_STOP_RECORD:
					if (null != mOnRecodeListener)
						mOnRecodeListener.onTMPCRecodeStoped();
					recode_event_id = -1;
					break;
				default:
					break;
			}

			switch (tmpcState) {
				case TMPC_NO_SOURCE_DEMUX: // 播放地址错误
				case TMPC_OUT_OF_MEMORY: // 没有足够内存
				case TMPC_PARSER_ERROR:
				case TMPC_EXCEPTION_ERROR:
				case RTSP_DISCONNECT_FROM_SERVER:
				case RTSP_TIME_OUT:
				case RTSP_NETWORK_ERROR:
					try { // modif::2009-07-01 14:47
						Log.d("jni", "======================onComplet  tmpcState="
								+ tmpcState);
						stop();
					} catch (OperationException e) {
						Loger.e("onError", "trying to stop the play error");
					}
					onError(TMPC_ERROR_NETWORK_FAIL);
					// event_id=-1;
					return;

				// add by chodison
				case TMPC_SOURCE_UNKNOWN_ERROR:
				case TMPC_SOURCE_INVALID:
					try {
						Log.d("jni", "======================onComplet  tmpcState="
								+ tmpcState);
						stop();
					} catch (OperationException e) {
						Loger.e("onError", "trying to stop the play error");
					}
					if (tmpcState == TMPC_SOURCE_INVALID)
						onError(TMPC_ERROR_INVALID_URL);
					else
						onError(TMPC_ERROR_UNKNOWN);
					return;

				case TMPC_START_BUFFER_DATA:
					changeState(PLAYER_BUFFERING);
					onBufferingBegin();
					break;

				case TMPC_DISCONNECT_FROM_SERVER:
					Loger.d(DEBUG_tmpcState,
							"========================== tmpcState > 0 : TMPC_DISCONNECT_FROM_SERVER : "
									+ tmpcState);
					try { // modif::2009-07-01 14:47
						Log.d("jni",
								"==========TMPC_DISCONNECT_FROM_SERVER============onComplet");
						stop();
					} catch (OperationException e) {
						Loger.e("onError", "trying to stop the play error");
					}
					onError(TMPC_ERROR_NETWORK_FAIL);
					break;

				case TMPC_START_PLAY:
					// TmpcGetPlayUrl urlhelper = new TmpcGetPlayUrl();
					// urlhelper.tmpcNotifyPlayinfo();
					Log.d("JAR", "======================TMPC_START_PLAY param="
							+ param);
					if (player.req_startPos > -1 && player.isSeekable()) {
						isSeeking = true;
						nativeSeek(req_startPos, 0);
						req_startPos = -1;
					} else {
						changeState(PLAYER_PLAYING);
						handlePosition();
						onBufferingComplete();
						if (param > 0)
							onError(TMPC_HLS_SUGGEST_CHANGECHANEL, param - 1);
						// event_id=-1;
					}
					break;

				case TMPC_PLAY_FINISH:
					Log.d("jni", "===========TMPC_PLAY_FINISH===========onComplet");
					onComplet(true);
					// TmpcGetPlayUrl urlhelper = new TmpcGetPlayUrl();
					// urlhelper.tmpcNotifyPlayinfo();
					// event_id=-1;
					break;

				case TMPC_NETWORK_ERROR:
					try { // modif::2009-07-01 14:47
						Log.d("jni",
								"===========TMPC_NETWORK_ERROR===========onComplet");
						stop();
					} catch (OperationException e) {
						Loger.e("onError", "trying to stop the play error");
					}
					onError(TMPC_ERROR_NETWORK_FAIL);
					// event_id=-1;
					return;

				case TMPC_CODEC_ERROR:
					Log.d("jni", "======================TMPC_CODEC_ERROR");
					try {
						stop();
					} catch (OperationException e) {
						Loger.e("onError", "trying to stop the play error");
					}
					onError(TMPC_CODEC_ERROR_FAILED);
					// event_id=-1;
					return;
				case TMPC_DEMOPLAY_TIMEOUT:
					Log.d("jni", "======================TMPC_DEMOPLAY_TIMEOUT");
					try {
						stop();
					} catch (OperationException e) {
						Loger.e("onError", "trying to stop the play error");
					}
					onError(TMPC_DEMOPLAY_TIMEOUT_FAILED);
					// event_id=-1;
					return;
				case TMPC_MEDIA_SPEC_ERROR:
					try {
						Log.d("jni",
								"==============TMPC_MEDIA_SPEC_ERROR========onComplet");
						stop();
					} catch (OperationException e) {
						Loger.e("onError", "trying to stop the play error");
					}
					onError(TMPC_ERROR_MEDIA_SPEC);
					// event_id=-1;
					return;
				// break;

				case TMPC_NO_AUDIO_CODEC:
					onError(TMPC_ERROR_OPEN_LIB_FAIL);
					// event_id=-1;
					break;

				case TMPC_NO_VIDEO_CODEC:
					onError(TMPC_ERROR_OPEN_LIB_FAIL);
					// event_id=-1;
					break;

				case TMPC_NO_PLAY_OBJECT:
					try { // modif::2009-07-01 14:47
						Log.d("jni",
								"==========TMPC_NO_PLAY_OBJECT============onComplet");
						stop();
					} catch (OperationException e) {
						Loger.e("onError", "trying to stop the play error");
					}
					onError(TMPC_ERROR_NO_PLAY_OBJECT);
					return;

				case TMPC_CHARGE_ACCESS_REFUSAL:
				case TMPC_TEMOBI_ACCESS_REFUSAL:
					onError(TMPC_ERROR_NETWORK_REFUSE);
					return;

				case TMPC_TEMOBI_TIME_OUT:
					try {
						Log.d("jni",
								"========TMPC_TEMOBI_TIME_OUT==============onComplet");
						stop();
					} catch (OperationException e) {
						Loger.e("onError", "trying to stop the play error");
					}
					onError(TMPC_ERROR_NETWORK_TIMEOUT);
					return;
				case TMPC_TP2P_CONNECT_ERROR:
					onError(param);
					return;
				default:
					if (tmpcState != -1)
						Log.d("jni", "======================tmpcState:" + tmpcState);
					// event_id=-1;
					break;
			}
		}

		if (state > PLAYER_STOPED) {
			// Get_mediaInfo_from_native(mediaState.dwDuration,mediaState.videoWidth,mediaState.videoHeight,mediaState.live);
			if (rawWidth != mediaState.videoWidth
					|| rawHeight != mediaState.videoHeight) {
				if (mediaState.videoWidth > 0 && mediaState.videoHeight > 0) { // modify::2009-07-01
					// 09:28
					rawPictureSizeChanged(mediaState.videoWidth,
							mediaState.videoHeight);
				} else {
					Log.e(DEBUG_display, "meidaState w/h="
							+ mediaState.videoWidth + "/"
							+ mediaState.videoHeight);
				}
			}
			if (state == PLAYER_BUFFERING) {
				// int cur_percent =
				// mediaState.already_buffer_time*100/param.getBufferTime();
				int cur_percent = nativeGetBufferProgress();
				if (percent != cur_percent) {
					percent = cur_percent;
					// Loger.d("buffer",
					// "===================== buffer percent = "
					// + percent);
				}

				onBufferingUpdate(percent > 100 ? 100 : percent); // modify::2009-07-01
				// 09:28
			}
			/*
			 * if(mediaState.dwDuration <= 0) isSeekable = false; else
			 * if(mediaState.dwDuration > 0) isSeekable = true;
			 */

			if (IS_DEBUG_MODE) {
				/*
				 * if (loop%(50*DEBUG_PRINT_INFO_DELAY)==49) {
				 * Log.d("mediaState",
				 * "===============================================");
				 * Log.d("mediaState", ".........already_buffer_time = " +
				 * mediaState.already_buffer_time); Log.d("mediaState",
				 * ".........mediaState.audio_available = " +
				 * mediaState.audio_available); Log.d("mediaState",
				 * ".........mediaState.cur_play_pos = " +
				 * mediaState.cur_play_pos); Log.d("mediaState",
				 * ".........mediaState.dwDuration = " + mediaState.dwDuration);
				 * Log.d("mediaState", ".........mediaState.video_available = "
				 * + mediaState.video_available); Log.d("mediaState",
				 * ".........mediaState.videoHeight = " +
				 * mediaState.videoHeight); Log.d("mediaState",
				 * ".........mediaState.videoWidth = " + mediaState.videoWidth);
				 * Log.d("mediaState", ".........mediaState.delivered = " +
				 * mediaState.delivered); Log.d("mediaState",
				 * ".........mediaState.skipped = " + mediaState.skipped);
				 * Log.d("mediaState", ".........mediaState.fps = " +
				 * mediaState.fps); Log.d("mediaState",
				 * ".........mediaState.freq = " + mediaState.freq);
				 * Log.d("mediaState", ".........mediaState.format = " +
				 * mediaState.format); Log.d("mediaState",
				 * ".........mediaState.channels = " + mediaState.channels); }
				 */

			}

			if (1 == this.param.display_outside) {
				if (tmpcBeginShow()) {
					if (rawPicture == null || rawPicture.length == 0) {
						Loger.e(DEBUG_display,
								"...................... rawPicture == null!!!");
						tmpcEndShow();
						onError(TMPC_ERROR_UNKNOWN);
						return;
					}
					int length = tmpcGetRawPicture32(rawPicture);

					// test video picture
					// Loger.e(DEBUG_display,
					// "......................,rawPicture len=" + length
					// + ",rawPicture[0]=" + rawPicture[0]
					// + ",rawPicture[1]=" + rawPicture[1]);
					if ((length > 0) || (null != rawPicture)) {

						if (bitmap == null) {

							try {
								// 实例化Bitmap
								bitmap = Bitmap.createBitmap(rawWidth,
										rawHeight, Bitmap.Config.RGB_565);
							} catch (OutOfMemoryError e) {
								//
								LoggerUtil.d("内存溢出发生了内存溢出");
							}

							// memoryCache.put(MD5.toMD5(m_sPlayUrl), bitmap);

						}

						bitmap.setPixels(rawPicture, 0, rawWidth, 0, 0,
								rawWidth, rawHeight);

						synchronized (surfaceLock) {
							if (surface != null && rectDest != null) {

								try {
									canvas = surface.lockCanvas(rectDest);

								} catch (Exception e) {
									Log.d(DEBUG_display,
											"update()-->surface.lockCanvas() exception="
													+ e.getMessage());
									e.printStackTrace();
								}

								if (canvas != null) {

									float scale = (float) (1.0 * m_Scale);
									// Log.d("TMPCPlayer",
									// "update postScale-->m_Scale="
									// + scale);

									if (isScale) {

										mMatrix.getValues(values);

										// if(values[0]<1){
										// values[0]=1.0f;
										// }
										// mMatrix.setValues(values);
										// currentScale = values[0];
										//
										// Log.d("TMPCPlayer",
										// "update postScale-->m_Scale currentScale="
										// + currentScale);
										// if(scale<1.0f){
										// Log.d("TMPCPlayer",
										// "update postScale-->...........");
										// //缩小
										// if(currentScale<=1.0f){
										// mMatrix.postScale(1.0f, 1.0f,
										// scale_centerX, scale_centerY);
										// }else{
										// mMatrix.postScale(1.0f-scaleTemp,
										// 1.0f-scaleTemp,
										// scale_centerX, scale_centerY);
										// }
										// }else{
										// mMatrix.postScale(scale, scale,
										// scale_centerX, scale_centerY);
										// scaleTemp = scale;
										// scaleTemp = scaleTemp/10;
										// }

										if (!isScaled) {
											if (scale > 1 && scale <= 2) {
												scale = (float) 1.5;
											} else if (scale > 2 && scale <= 3) {
												scale = (float) 2.5;
											} else if (scale > 3 && scale < 4) {
												scale = (float) 3.5;
											} else if (scale >= 4) {
												scale = (float) 4;
											}
											float nowscale = (float) 1.0;
											currentScale = currentScale * scale;
											Log.d("当前放大倍数", "当前放大倍数"
													+ currentScale);

											if (currentScale > 4) {
												float tscale = currentScale
														/ scale;

												currentScale = 4;
												scale = 4 / tscale;

											} else if (currentScale <= 1) {

												float tscale = currentScale
														/ scale;
												currentScale = 1;
												scale = 1 / tscale;

											} else if (currentScale > 1
													&& currentScale < 4) {

											} else if (currentScale == 4
													|| currentScale == 1) {
												scale = 1;

											}
											Log.d("当前放大倍数", "当前放大倍数"
													+ currentScale);
											Log.d("当前放大倍数", "scale====" + scale);
											mMatrix.postScale(scale, scale,
													scale_centerX,
													scale_centerY);

											test();

											matrixTemp.set(mMatrix);
											isScaled = true;
										}

									}
									if (isTranslate) {

										if (!isTranslated) {
											mMatrix.postTranslate(translate_dx,
													translate_dy);
											matrixTemp.set(mMatrix);
											isTranslated = true;
										}
										mMatrix.getValues(values);

										float bitmapWidth = rawWidth
												* values[0];
										float bitmapHeight = rawHeight
												* values[0];
										Log.d("TMPCPlayer",
												"update translate-->values[2]="
														+ values[2]
														+ " values[5]="
														+ values[5]
														+ ";bitmapWidth="
														+ bitmapWidth
														+ ",bitmapHeight="
														+ bitmapHeight
														+ ";rectDest.width()="
														+ rectDest.width()
														+ ";rectDest.height()="
														+ rectDest.height()
														+ ";rawWidth="
														+ rawWidth
														+ ";rawHeight="
														+ rawHeight);

										float left = values[2];
										float top = values[5];

										if (bitmapWidth >= rectDest.width()) {
											if (left >= 0) {
												values[2] = 0;
											}
											if (top >= 0) {
												values[5] = 0;
											}

											float right = values[2]
													+ bitmapWidth;
											float bottom = values[5]
													+ bitmapHeight;
											Log.d("TMPCPlayer",
													"update translate-->before right="
															+ right);
											if (right <= rectDest.width()) {

												values[2] = -(bitmapWidth - rectDest
														.width());

											}
											if (bottom <= rectDest.height()) {
												values[5] = -(bitmapHeight - rectDest
														.height());
											}

											//
											mMatrix.setValues(values);
											matrixTemp.set(mMatrix);
										} else {

											if (left < 0) {
												values[2] = 0;
											}
											if (top < 0) {
												values[5] = 0;
											}
											float right = values[2]
													+ bitmapWidth;
											float bottom = values[5]
													+ bitmapHeight;
											if (right >= rectDest.width()) {
												values[2] = rectDest.width()
														- bitmapWidth;
											}
											if (bottom >= rectDest.height()) {
												values[5] = rectDest.height()
														- bitmapHeight;
											}
											mMatrix.setValues(values);
											matrixTemp.set(mMatrix);
										}

									}

									canvas.drawColor(Color.BLACK);
									// canvas.concat(mMatrix);

									canvas.drawBitmap(bitmap, mMatrix, paint);

									surface.unlockCanvasAndPost(canvas);
									mMatrix = canvas.getMatrix();
									mMatrix.set(matrixTemp);

								} else {
									if (IS_DEBUG_MODE) {
										Loger.e(DEBUG_display,
												"...................... canvas = null");
									}
								}
							} else {
								if (IS_DEBUG_MODE) {
									Loger.e(DEBUG_display,
											"...................... surface = null");
								}
							}
						}
					} else {
						Loger.e(DEBUG_display,
								"...................... NO pictureBuffer");
					}
					tmpcEndShow();
				}
			} // 1 == this.param.display_outside

		}

		updatetime = System.currentTimeMillis() - updatetime;
		updatetime = WAIT_TIME - updatetime;
		if (updatetime < 5) {
			updatetime = 5;
		}
		sleepTime = updatetime;
		mRedrawHandler.sleep(updatetime);
	}

	private void test() {

		if (!isTranslated) {
			mMatrix.postTranslate(translate_dx, translate_dy);
			matrixTemp.set(mMatrix);
			isTranslated = true;
		}
		mMatrix.getValues(values);

		float bitmapWidth = rawWidth * values[0];
		float bitmapHeight = rawHeight * values[0];
		Log.d("TMPCPlayer", "update translate-->values[2]=" + values[2]
				+ " values[5]=" + values[5] + ";bitmapWidth=" + bitmapWidth
				+ ",bitmapHeight=" + bitmapHeight + ";rectDest.width()="
				+ rectDest.width() + ";rectDest.height()=" + rectDest.height()
				+ ";rawWidth=" + rawWidth + ";rawHeight=" + rawHeight);

		float left = values[2];
		float top = values[5];

		if (bitmapWidth >= rectDest.width()) {
			if (left >= 0) {
				values[2] = 0;
			}
			if (top >= 0) {
				values[5] = 0;
			}

			float right = values[2] + bitmapWidth;
			float bottom = values[5] + bitmapHeight;
			Log.d("TMPCPlayer", "update translate-->before right=" + right);
			if (right <= rectDest.width()) {

				values[2] = -(bitmapWidth - rectDest.width());

			}
			if (bottom <= rectDest.height()) {
				values[5] = -(bitmapHeight - rectDest.height());
			}

			//
			mMatrix.setValues(values);
			matrixTemp.set(mMatrix);
		} else {

			if (left < 0) {
				values[2] = 0;
			}
			if (top < 0) {
				values[5] = 0;
			}
			float right = values[2] + bitmapWidth;
			float bottom = values[5] + bitmapHeight;
			if (right >= rectDest.width()) {
				values[2] = rectDest.width() - bitmapWidth;
			}
			if (bottom >= rectDest.height()) {
				values[5] = rectDest.height() - bitmapHeight;
			}
			mMatrix.setValues(values);
			matrixTemp.set(mMatrix);
		}

	};

	/** * 请求播放地址错误（服务器中转模式） */
	public static final int TMPC_NO_HTTP_URL_FAILED = 20141204;// 请求播放地址错误（服务器中转模式）
	/** * 请求播放地址错误（P2P模式） */
	public static final int TMPC_NO_P2P_HTTP_URL_FAILED = 20141211;// 请求播放地址错误（P2P模式）

	public static final int TMPC_NO_AUDIO_CODEC = 4;// 媒体格式不支持
	public static final int TMPC_NO_VIDEO_CODEC = 5;// 媒体格式不支持
	public static final int TMPC_NETWORK_ERROR = 22;// 网络错误
	public static final int TMPC_MEDIA_SPEC_ERROR = 23;// 媒体信息错误
	public static final int TMPC_CHARGE_ACCESS_REFUSAL = 24;// 鉴权未通过
	public static final int TMPC_TEMOBI_ACCESS_REFUSAL = 25;// 鉴权未通过
	public static final int TMPC_NO_PLAY_OBJECT = 26;// 视频源不存在
	public static final int TMPC_DISCONNECT_FROM_SERVER = 27;// 与服务器连接中断
	public static final int TMPC_TEMOBI_TIME_OUT = 28;// 超时
	public static final int TMPC_START_RECORD = 29;
	public static final int TMPC_STOP_RECORD = 30;
	public static final int TMPC_CODEC_ERROR = 40; // 解码器错误（硬解码）
	public static final int TMPC_DEMOPLAY_TIMEOUT = 41; // 5min播放限时通知
	public static final int TMPC_NO_SOURCE_DEMUX = 1; // 播放地址错误
	public static final int TMPC_OUT_OF_MEMORY = 0; // 没有足够内存
	public static final int TMPC_SOURCE_UNKNOWN_ERROR = 60; // 打开播放地址错误
	public static final int TMPC_SOURCE_INVALID = 601; // 无效播放地址

	public final static int SCREEN_FULL = 0;
	public final static int SCREEN_FIT_OVERLAP = 1;
	public final static int SCREEN_FIT = 2;
	public final static int SCREEN_DEFAULT = 3;

	public final static int SEEK_MIN = 2000; // seek unit is 30s . pre/next
	public final static int SEEK_UNIT = 30000; // seek unit is 30s . pre/next
	public final static int MEDIA_CONTROL_TIME_INTERVAL = 5000;
	public final static int NORMAL_WIDTH = 400;
	public final static int NORMAL_HEIGHT = 240;
	public final static int MSG_GET_ALREADY_BUF = 99;
	public final static int MSG_INIT_PLAY = 98;
	public final static int MSG_NET_SWITCHED = 97;
	public final static int MSG_NET_SWITCHED_VALUE = 96;
	public final static int MSG_DELAY_INIT_PLAY = 1000;
	public final static int STATUS_BUFFER_ING = 0;
	public final static int STATUS_PLAY_ING = 1;
	public final static int STATUS_COMPLETE = 2;
	public final static int STATUS_SWITCH_BACK = 3;
	public final static int STATUS_ERROR = 4;
	public final static int STATUS_INIT = 5;
	public final static int STATUS_INIT_FROM_LAUNCHER = 6;

	public static final int MESSAGE_POST_TIMEINFO = 40;
	public static final int MESSAGE_POST_CURENTTIME = 41;
	public static final int MESSAGE_POST_ORG_SIZE = 42;
	public static final int MESSAGE_POST_PLAY_STATUS = 43;
	public static final int MESSAGE_POST_INIT_FULL_SCREEN = 44;
	public static final int MESSAGE_POST_INIT_DEF_SCREEN = 45;

	// Message of Rtsp
	public static final int RTSP_DISCONNECT_FROM_SERVER = 258; // 与服务器连接中断
	public static final int RTSP_TIME_OUT = 269; // 重连3次没有连接成功，报超时
	public static final int RTSP_NETWORK_ERROR = 265; // 网络错误
	public static final int TMPC_PARSER_ERROR = 31; // URL格式有误
	public static final int TMPC_EXCEPTION_ERROR = 32; // 播放器异常

	// Message Of APN,Net etc.
	public static final int LOAD_ACTIVATE_APN_ERR = 11;// 连接网络失败，请检查网络设置
	public static final int LOAD_TIMEOUT_ERR = 12;// 设置50秒启动超时
	public static final int LOAD_CHECK_SIM_ERR = 13;// 请检查是否有SIM卡，或者是否打开WIFI.

	public static final int TMPC_START_BUFFER_DATA = 52;
	public static final int TMPC_PRE_PLAY = 53;
	public static final int TMPC_START_PLAY = 54;
	public static final int TMPC_PLAY_FINISH = 55;

	public static final int TMPC_PS_UNKNOWN = 0;
	public static final int TMPC_PS_CONNECT = 1;
	public static final int TMPC_PS_BUFFER = 2;
	public static final int TMPC_PS_PLAY = 3;
	public static final int TMPC_PS_PAUSE = 4;
	public static final int TMPC_PS_SEEKING = 5;
	public static final int TMPC_PS_STOP = 6;

	public static final int RAW_DATA_BLOCK_SIZE = 16384; // Set the block size
	// used to write a
	// ByteArrayOutputStream
	// to byte[]
	public static final int ERROR_IO_EXCEPTION = 1;
	public static final int ERROR_FILE_NOT_FOUND = 2;

	public static final int TMPC_TP2P_CONNECT_ERROR = 141217; // TP2P连接错误

	/*
	 *
	 * private static final int _BASE = 0x0000; private static final int _PLAY =
	 * 0x00FF; private static final int _PLUGIN = 0xFF00;
	 *
	 * //private static final int TMPC_OK = _BASE + 1; // private static final
	 * int TMPC_OUT_OF_MEMORY = 0; // 02 private static final int
	 * TMPC_INIT_AUDIO_ERROR = _BASE + 3; // 03 private static final int
	 * TMPC_OPEN_AUDIO_ERROR = _BASE + 8888; // 04 private static final int
	 * TMPC_INIT_VIDEO_ERROR = _BASE + 9999; // 05 private static final int
	 * TMPC_OPEN_VIDEO_ERROR = _BASE + 6; // 06 private static final int
	 * TMPC_CREATE_264_DECODE_ERROR = _BASE + 7; // 07
	 *
	 * private static final int TMPC_NOTIFY_MEDIA_INFO = _PLAY + 0; // 255
	 * private static final int TMPC_START_BUFFER_DATA = _PLAY + 1; // 256
	 * private static final int TMPC_CONNECT_SERVER_OK = 52;//_PLAY + 2; // 257
	 * private static final int TMPC_DISCONNECT_FROM_SERVER = 27;//_PLAY + 3; //
	 * 258 private static final int TMPC_DATA_DOWNLOAD_FINISH = _PLAY + 4; //
	 * 259 private static final int TMPC_START_PLAY =54;//_PLAY + 5; // 260
	 * private static final int TMPC_INITIAL_STREAM_ERROR = _PLAY + 6; // 261
	 * private static final int TMPC_WAIT_AUDIO_DATA = _PLAY + 7; // 262 private
	 * static final int TMPC_WAIT_VIDEO_DATA = _PLAY + 8; // 263 private static
	 * final int TMPC_PLAY_FINISH = 55;//_PLAY + 9; // 264 private static final
	 * int TMPC_NETWORK_ERROR = 22;//_PLAY + 10; // 265 private static final int
	 * TMPC_MEDIA_SPEC_ERROR = 23;//_PLAY + 11; // 266 private static final int
	 * TMPC_PRE_PLAY = 53;//_PLAY + 12; // 267 private static final int
	 * TMPC_NET_RECONNECT = _PLAY + 13; // 268
	 *
	 * private static final int TMPC_NO_AUDIO_CODEC = 4;//_PLUGIN + 0; // 65280
	 * private static final int TMPC_NO_VIDEO_CODEC = 5;//_PLUGIN + 1; // 65281
	 * private static final int TMPC_NO_PLAY_OBJECT = 26;//_PLUGIN + 2; // 65282
	 * private static final int TMPC_LOAD_PLUGIN_FAILED = _PLUGIN + 3; // 65283
	 * private static final int TMPC_NOT_IMPLEMENT_PLUGIN = _PLUGIN + 4; //
	 * 65284 private static final int TMPC_OPEN_PLUGIN_FAILED = _PLUGIN + 5; //
	 * 65285 private static final int TMPC_START_PLUGIN_FAILED = _PLUGIN + 6; //
	 * 65286 private static final int TMPC_RECONNECT_TO_SERVER = _PLUGIN + 7; //
	 * 65287 private static final int TMPC_CHARGE_ACCESS_REFUSAL = 24;//_PLUGIN
	 * + 9; // 65289 private static final int TMPC_TEMOBI_ACCESS_REFUSAL =
	 * 25;//_PLUGIN + 10; // 65290 private static final int TMPC_TEMOBI_TIME_OUT
	 * = 28;//_PLUGIN + 11; // 65291 private static final int TMPC_PARSER_ERROR
	 * = 30; private static final int TMPC_EXCEPTION_ERROR = 31; private static
	 * final int RTSP_DISCONNECT_FROM_SERVER = 258; private static final int
	 * RTSP_TIME_OUT = 269; private static final int RTSP_NETWORK_ERROR = 265;
	 */

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
		if (player != null) {
			// Log.w(DEBUG_display,
			// "surfaceChanged() w:"+width+"  h:"+height+"  format:"+format+"  displayoutside:"+param.display_outside);
			if (!isSufaceCallbackOpen) {
				Log.w(DEBUG_display, "surfacecallback closed!");
				return;
			}

			if (null != param && 1 == param.display_outside && tmpcBeginShow()) {
				surfaceDestroyed(null);
				setPictureFixToHolder(rawWidth, rawHeight, holder);
				if ((1 == this.param.display_outside) && (null != rawPicture)
						&& (tmpcBeginShow())) {
					int length = tmpcGetRawPicture32(rawPicture);
					if ((length > 0) || (null != rawPicture)) {
						if (bitmap == null) {
							Log.d(DEBUG_display, "createbitmap2 " + rawWidth
									+ "/" + rawHeight);
							LoggerUtil.d("打印建立图片");
							bitmap = Bitmap.createBitmap(rawWidth, rawHeight,
									Bitmap.Config.RGB_565);
						}
						bitmap.setPixels(rawPicture, 0, rawWidth, 0, 0,
								rawWidth, rawHeight);
						synchronized (surfaceLock) {
							if (surface != null && rectDest != null) {
								try {
									canvas = surface.lockCanvas(rectDest);
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (canvas != null) {
									canvas.concat(matrix);
									canvas.drawBitmap(bitmap, pictureX,
											pictureY, paint);
									surface.unlockCanvasAndPost(canvas);
								} else {
									if (IS_DEBUG_MODE) {
										Loger.e(DEBUG_display,
												"...................... canvas = null");
									}
								}
							} else {
								if (IS_DEBUG_MODE) {
									Loger.e(DEBUG_display,
											"...................... surface = null");
								}
							}
						}
					} else {
						Loger.e(DEBUG_display,
								"...................... NO pictureBuffer");
					}
				} // 1 == this.param.display_outside
				tmpcEndShow();
			}
		}

	}

	public void setSurfaceCallback(boolean aOpenClose) {
		Log.w(DEBUG_display, "surfacecallback closed!");
		isSufaceCallbackOpen = aOpenClose;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.w(DEBUG_display, "===================== surfaceCreated()");
		if (!isSufaceCallbackOpen) {
			Log.w(DEBUG_display, "surfacecallback closed!");
			return;
		}

		if (null != param && 1 == param.display_outside) {
			surfaceDestroyed(null);
			setPictureFixToHolder(rawWidth, rawHeight, holder);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.w(DEBUG_display, "===================== surfaceDestroyed() "
				+ tmpcState);

		if (!isSufaceCallbackOpen) {
			return;
		}

		hasBeenDes = 1;
		if (null != param && 1 == param.display_outside) {
			rectDest = null;
			surface = null;
			matrix = null;
		}

	}

	public void setBitmapCacheOn(Context context) {
		memoryCache = MemoryUtil.createMemoryCache(context, 0);
	}

	/* get raw buffer */
	private native static int[] tmpcGetRawBuffer(int len);

	// private Bitmap getbitmap(){
	// BitmapFactory.Options opts = new BitmapFactory.Options();
	// opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
	//
	// opts.inSampleSize = 1;
	// opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
	// opts.inPurgeable = true;
	// opts.inInputShareable = true;
	// opts.inDither = false;
	// opts.inPurgeable = true;
	//
	// opts.inTempStorage = new byte[16 * 1024];
	//
	//
	//
	// Bitmap bmp = BitmapFactory.decodeByteArray(data, offset, length)(, id,
	// opts);
	// }
	//
	// public static Bitmap readBitMap(Context context, int resId) {
	//
	// BitmapFactory.Options opt = new BitmapFactory.Options();
	//
	// opt.inPreferredConfig = Bitmap.Config.RGB_565;
	//
	// opt.inPurgeable = true;
	//
	// opt.inInputShareable = true;
	//
	// // 获取资源图片
	//
	// InputStream is = context.getResources().openRawResource(resId);
	//
	// return BitmapFactory.decodeStream(is, null, opt);
	//
	// }
}
