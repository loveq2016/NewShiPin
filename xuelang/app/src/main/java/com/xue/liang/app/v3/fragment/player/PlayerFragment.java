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
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.activity.alarmprocess.AlarmProcessDeatialActivity;

import com.xue.liang.app.v3.base.BaseFragment;

import com.xue.liang.app.v3.event.UrlEvent;

import com.xue.liang.app.v3.fragment.device.DeviceFragment;

import java.io.IOException;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;




public class PlayerFragment extends BaseFragment {


    public static final String TAG=PlayerFragment.class.getSimpleName();


    private String path="rtsp://119.164.59.39:1554/iptv/Tvod/iptv/001/001/ch15050914035980594154.rsc/27191_Uni.sdp";

    private Uri mUri;


    private IjkMediaPlayer mMediaPlayer ;

    private Context mAppContext;

    private Surface surface;


    @BindView(R.id.tv_info)
    protected TextView tv_info;


    @BindView(R.id.playerView)
    TextureView playerView;

    private StringBuilder infoBuilder=new StringBuilder();
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
        mAppContext=getContext();

        playerView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                surface=new Surface(surfaceTexture);
                infoBuilder.append("onSurfaceTextureAvailable");
                Log.e(TAG,TAG+"--onSurfaceTextureAvailable");
//                mUri = Uri.parse(path);
//                openVideo();

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
                Log.e(TAG,TAG+"--onSurfaceTextureSizeChanged");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                Log.e(TAG,TAG+"--onSurfaceTextureDestroyed");
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
        if (mUri == null ) {
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

            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration", 3000);
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 1);  //unlimited
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");

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

    IMediaPlayer.OnPreparedListener onPreparedListener=new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            Log.e(TAG,TAG+"--onPrepared");
        }
    };

    IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener=  new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
            Log.e(TAG,TAG+"--onVideoSizeChanged");
        }
    };

    IMediaPlayer.OnCompletionListener onCompletionListener=new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {

            Log.e(TAG,TAG+"--onCompletion");
            tv_info.setText("onCompletion");

        }
    };


    IMediaPlayer.OnErrorListener onErrorListener= new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            Log.e(TAG,TAG+"--onError:"+what);
            tv_info.setText("onError:"+what);
            return false;
        }
    };

    IMediaPlayer.OnInfoListener onInfoListener= new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            Log.e(TAG,TAG+"--onInfo:"+what);
            tv_info.setText("onInfo:"+what);
            return false;
        }
    };

    IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener= new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            Log.e(TAG,TAG+"--onBufferingUpdate:"+percent);
            Log.e(TAG,"测试代码-onBufferingUpdate:"+percent);
            tv_info.setText("onBufferingUpdate:"+percent);
        }
    };

    IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener= new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            Log.e(TAG,TAG+"--onSeekComplete:");
            tv_info.setText("onSeekComplete");
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
        if(event.getTag().equals(DeviceFragment.TAG)){
            mUri=Uri.parse(event.getUrl());
            openVideo();
        }else if(event.getTag().equals(AlarmProcessDeatialActivity.TAG)){
            mUri=Uri.parse(event.getUrl());
            openVideo();
        }

    }

}
