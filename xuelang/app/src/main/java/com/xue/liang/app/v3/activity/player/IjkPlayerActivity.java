package com.xue.liang.app.v3.activity.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseActivity;


import java.io.IOException;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by jikun on 17/1/17.
 */

public class IjkPlayerActivity extends BaseActivity {
    //  mAdapter.addItem("rtsp://119.164.59.39:1554/iptv/Tvod/iptv/001/001/ch15050914035980594154.rsc/27191_Uni.sdp", "rtsp IPTV-CCTV");
    private String path="rtsp://119.164.59.39:1554/iptv/Tvod/iptv/001/001/ch15050914035980594154.rsc/27191_Uni.sdp";

    private Uri mUri;
    private int mCurrentBufferPercentage;

    private long mPrepareStartTime = 0;

    private IjkMediaPlayer mMediaPlayer ;

    private Context mAppContext;

    private Surface surface;


    public static final String TAG=IjkPlayerActivity.class.getSimpleName();





    @BindView(R.id.playerView)
    TextureView playerView;
    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_ijk_player;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mAppContext=getApplicationContext();

        playerView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                surface=new Surface(surfaceTexture);
                mUri = Uri.parse(path);
                openVideo();

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

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
            final Context context = getApplicationContext();
            // REMOVED: SubtitleController

            // REMOVED: mAudioSession
            mMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {

                }
            });
            mMediaPlayer.setOnPreparedListener(onPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setOnErrorListener(onErrorListener);
            mMediaPlayer.setOnInfoListener(onInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
            mMediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
            mCurrentBufferPercentage = 0;
            String scheme = mUri.getScheme();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mMediaPlayer.setDataSource(mAppContext, mUri, null);
            } else {
                mMediaPlayer.setDataSource(mUri.toString());
            }
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mPrepareStartTime = System.currentTimeMillis();
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

        }
    };

    IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener=  new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
            Log.e(TAG,TAG+"----onVideoSizeChanged=");
        }
    };

    IMediaPlayer.OnCompletionListener onCompletionListener=new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {

            Log.e(TAG,TAG+"----onCompletion=");

        }
    };


    IMediaPlayer.OnErrorListener onErrorListener= new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            Log.e(TAG,TAG+"----onError="+what);
            return false;
        }
    };

    IMediaPlayer.OnInfoListener onInfoListener= new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            Log.e(TAG,TAG+"----onInfo="+what);
            return false;
        }
    };

    IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener= new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
          Log.e(TAG,TAG+"----onBufferingUpdate="+percent);
        }
    };

    IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener= new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            Log.e(TAG,TAG+"----onSeekComplete=");
        }
    };
}
