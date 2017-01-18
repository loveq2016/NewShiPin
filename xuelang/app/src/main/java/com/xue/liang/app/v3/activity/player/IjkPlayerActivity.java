package com.xue.liang.app.v3.activity.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.FrameLayout;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.fragment.player.PlayerFragment;


import java.io.IOException;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by jikun on 17/1/17.
 */

public class IjkPlayerActivity extends BaseActivity {
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_ijk_player;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initPlayerFragment();

    }

    private void initPlayerFragment() {
        PlayerFragment playerFragment = PlayerFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, playerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
