package com.temobi.vcp.sdk.data;

/*
 * 视频媒体信息
 */
public class MediaInfo {

	public int dwDuration;
	public int videoWidth;
	public int videoHeight;
	public int video_available;
	public int	audio_available;
	 
	public int already_buffer_time;/*unit is millisecond*/
	public int cur_play_pos;/*unit is millisecond*/
	
	public int delivered;
	public int skipped;
	public int	fps;
	public int live;
	public int bitrate;

	/*audio*/
	public int	freq;		/* DSP frequency -- samples per second */
	public int	format;		/* Audio data format,if every sample is 16bits, the value is 16. */
	public int channels;	/* Number of channels: 1 mono, 2 stereo */
}
