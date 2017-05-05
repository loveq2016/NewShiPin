package com.xue.liang.app.v3.fragment.player;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.activity.alarmprocess.AlarmProcessDeatialActivity;

import com.xue.liang.app.v3.base.BaseFragment;

import com.xue.liang.app.v3.event.UrlEvent;

import com.xue.liang.app.v3.fragment.device.DeviceFragment;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class PlayerFragment extends BaseFragment {


    public static final String TAG = PlayerFragment.class.getSimpleName();

    //private String path="rtsp://119.164.59.39:1554/iptv/Tvod/iptv/001/001/ch15050914035980594154.rsc/27191_Uni.sdp";


    private String path = "http://vod1.fangyan.tv/baf9a54d78113b54.mp4";

    private Uri mUri;


    private IjkMediaPlayer mMediaPlayer;

    private Context mAppContext;

    private Surface surface;


    @BindView(R.id.progress_bar)
    protected ProgressBar progress_bar;


    @BindView(R.id.tv_info)
    protected TextView tv_info;


    @BindView(R.id.playerView)
    TextureView playerView;

    private StringBuilder infoBuilder = new StringBuilder();

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {
//        if(mMediaPlayer!=null&&mMediaPlayer.isPlaying()){
//            mMediaPlayer.start();
//        }
    }

    @Override
    protected void onUserInvisible() {
//        if(mMediaPlayer!=null&&mMediaPlayer.isPlaying()){
//            mMediaPlayer.pause();
//        }

    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_player;
    }


//
//    @OnClick(R.id.play)
//    public void playUrl(){
//        mUri=Uri.parse(path);
//        openVideo();
//    }


    public static PlayerFragment newInstance() {
        PlayerFragment playerFragment = new PlayerFragment();

        return playerFragment;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        infoBuilder.append("initViews");
        mAppContext = getContext();

        playerView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                surface = new Surface(surfaceTexture);
                infoBuilder.append("onSurfaceTextureAvailable");
                Log.e(TAG, TAG + "--onSurfaceTextureAvailable");
//                mUri = Uri.parse(path);
//                openVideo();

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
                Log.e(TAG, TAG + "--onSurfaceTextureSizeChanged");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                Log.e(TAG, TAG + "--onSurfaceTextureDestroyed");
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                //Log.e(TAG,TAG+"--onSurfaceTextureUpdated");
            }
        });


    }


    @TargetApi(Build.VERSION_CODES.M)
    private void openVideo() {
        showLoadingView();
        if (mUri == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release();

        AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        try {
            mMediaPlayer = new IjkMediaPlayer();


            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 0);//48 to 0
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "probesize", "524288");  //for first display fast, but maybe can not play succ
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");


//            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration", 3000);
//            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 1);  //unlimited
//            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
//            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");

            // TODO: create SubtitleController in MediaPlayer, but we need
            // a context for the subtitle renderers
            final Context context = getContext();
            // REMOVED: SubtitleController

            // REMOVED: mAudioSession
            mMediaPlayer.setOnPreparedListener(onPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setOnErrorListener(onErrorListener);
            mMediaPlayer.setOnInfoListener(onInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
            mMediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
            String scheme = mUri.getScheme();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mMediaPlayer.setDataSource(mAppContext, mUri, null);
            } else {
                mMediaPlayer.setDataSource(mUri.toString());
            }
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();


            // REMOVED: mPendingSubtitleTracks

            // we don't set the target state here either, but preserve the
            // target state that was there before.

        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // REMOVED: mPendingSubtitleTracks.clear();
        }
    }


    /*
 * release the media player in any state
 */
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            Log.e(TAG, TAG + "--onPrepared");
        }
    };

    IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
            Log.e(TAG, TAG + "--onVideoSizeChanged");
        }
    };

    IMediaPlayer.OnCompletionListener onCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {

            Log.e(TAG, TAG + "--onCompletion");
            //showCompetionView("播放完成");


        }
    };

    /**
     * -1004    MEDIA_ERROR_IO
     * <p>
     * -1007    MEDIA_ERROR_MALFORMED
     * <p>
     * 700     MEDIA_INFO_VIDEO_TRACK_LAGGING
     * <p>
     * 3        MEDIA_INFO_VIDEO_RENDERING_START
     * <p>
     * 1	  MEDIA_INFO_UNKNOWN
     * <p>
     * 801        MEDIA_INFO_NOT_SEEKABLE
     * <p>
     * 802     MEDIA_INFO_METADATA_UPDATE
     * <p>
     * 702     MEDIA_INFO_BUFFERING_END
     * <p>
     * 800    MEDIA_INFO_BAD_INTERLEAVING
     * <p>
     * -1010   MEDIA_ERROR_UNSUPPORTED
     * <p>
     * -110    MEDIA_ERROR_TIMED_OUT
     * <p>
     * 100     MEDIA_ERROR_SERVER_DIED
     * <p>
     * 200    MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK
     * <p>
     * -1007   MEDIA_ERROR_MALFORMED
     * <p>
     * -1004   MEDIA_ERROR_IO
     * <p>
     * -1000   MEDIA_UNKONOW_ERROR
     */

    IMediaPlayer.OnErrorListener onErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            Log.e(TAG, TAG + "--onError:" + what);
            showErrorView("错误代码:" + what + "");

            return false;
        }
    };

    IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {

            switch (what) {
                case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                    break;
                case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    Log.e(TAG, "XUELIANG____视频开始渲染:");
                    showSuccessView();
                    break;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    //开始接收流
                    Log.d(TAG, "MEDIA_INFO_BUFFERING_START:");
                    Log.e(TAG, "XUELIANG____开始接收流:");
                    showLoadingView();


                    break;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    //接受流完成
                    Log.e(TAG, "XUELIANG____接收流完成:");
                    showSuccessView();
                    //开始出画面

                    break;
                case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                    Log.d(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + extra);
                    break;
                case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                    Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                    break;
                case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                    break;
                case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                    Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:");
                    break;
                case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                    Log.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    break;
                case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                    Log.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    break;
                case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + extra);
                    break;
                case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    Log.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                    break;
            }
            return false;
        }
    };

    IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {


        }
    };

    IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            Log.e(TAG, TAG + "--onSeekComplete:");

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    @Subscribe
    public void onEventMainThread(UrlEvent event) {
        if (event.getTag().equals(DeviceFragment.TAG)) {
            mUri = Uri.parse(event.getUrl());
            openVideo();
        } else if (event.getTag().equals(AlarmProcessDeatialActivity.TAG)) {
            mUri = Uri.parse(event.getUrl());
            openVideo();
        }

    }

    private void showLoadingView() {
        progress_bar.setVisibility(View.VISIBLE);

        tv_info.setVisibility(View.GONE);

    }

    private void showSuccessView() {
        progress_bar.setVisibility(View.GONE);

        tv_info.setVisibility(View.GONE);
    }

    private void showErrorView(String info) {
        progress_bar.setVisibility(View.GONE);
        tv_info.setVisibility(View.VISIBLE);
        tv_info.setText(info);
    }

    private void showCompetionView(String info) {
        progress_bar.setVisibility(View.GONE);
        tv_info.setVisibility(View.VISIBLE);
        tv_info.setText(info);
    }

}
