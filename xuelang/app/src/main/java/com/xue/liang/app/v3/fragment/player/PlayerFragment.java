package com.xue.liang.app.v3.fragment.player;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.temobi.android.player.TMPCPlayer;
import com.temobi.android.player.TMPCPlayer.OnVideoSizeChangedListener;
import com.temobi.vcp.protocal.McpClientProtocalData;
import com.temobi.vcp.protocal.McpClientProtocalData.IPlayerStateCallback;
import com.temobi.vcp.protocal.TmPlayerStatus;
import com.temobi.vcp.sdk.data.CommInfo;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.constant.BundleConstant;
import com.xue.liang.app.v3.event.UrlEvent;
import com.xue.liang.app.v3.fragment.device.DeviceFragment;


import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class PlayerFragment extends Fragment {

    private String msign = "";//用来判断是否播放视频
    private SurfaceView surfacev_player;

    private TextView text_promptmsg;

    private RelativeLayout rl_player;

    private SurfaceHolder surfaceHolder;

    private McpClientProtocalData mcpClientProtocalData;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mcpClientProtocalData = McpClientProtocalData.getInstance();
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        if (bundle != null) {

            msign = bundle.getString(BundleConstant.BUNDLE_PLAYER_SIGN);

        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.frament_player, container, false);
        initView(view);
        initHolder();

        return view;
    }

    public static PlayerFragment getInstance(String sign) {
        PlayerFragment playerFragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BundleConstant.BUNDLE_PLAYER_SIGN, sign);
        playerFragment.setArguments(bundle);
        return playerFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        mcpClientProtocalData.releasePlayer();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initView(View view) {
        surfacev_player = (SurfaceView) view.findViewById(R.id.surfacev_player);
        text_promptmsg = (TextView) view.findViewById(R.id.text_promptmsg);
        rl_player = (RelativeLayout) view.findViewById(R.id.rl_player);

    }

    private void initHolder() {
        surfaceHolder = surfacev_player.getHolder();
        surfaceHolder.addCallback(surfaceCallback);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Surface回调函数
     */
    Callback surfaceCallback = new Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub

            // TODO Auto-generated method stub

            holder.setKeepScreenOn(true);

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub

        }
    };

    private void play(String url) {
        // 初始化播放器
        text_promptmsg.setText("加载中");
        text_promptmsg.setVisibility(View.VISIBLE);
        try {
            mcpClientProtocalData.releasePlayer();
            mcpClientProtocalData.initPlayer(getActivity()
                    .getApplicationContext(), surfaceHolder);

            mcpClientProtocalData.getPlayer().setOnVideoSizeChangedListener(
                    changedListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mcpClientProtocalData.doPlay(url, playerStateCallback);
    }

    IPlayerStateCallback playerStateCallback = new IPlayerStateCallback() {

        @Override
        public void ResultCallbackFun(int nCommnd, int nResult, CommInfo pInfo,
                                      int nContext) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPlayerStateCallback(int code, int param, Object info) {
            // TODO Auto-generated method stub
            Message msg = null;
            switch (code) {
                case TmPlayerStatus.TmPlayerMediaInfo:

                    // 正在获取视频

                    text_promptmsg.setText("准备播放" + "...");

                    break;
                case TmPlayerStatus.TmPlayerBuffering:

                    text_promptmsg.setText("缓冲中" + param + "%");

                    break;
                case TmPlayerStatus.TmPlyaerPlayStart:
                    surfacev_player.setBackgroundColor(getResources().getColor(
                            android.R.color.transparent));
                    text_promptmsg.setText("准备播放" + "...");
                    text_promptmsg.setVisibility(View.GONE);

                    break;
                case TmPlayerStatus.TmPlayerError:

                    if (param == TMPCPlayer.TMPC_NO_HTTP_URL_FAILED) {
                        text_promptmsg.setText("播放出错," + "错误码为:" + "未能请求到播放地址");
                    } else {
                        text_promptmsg.setText("播放出错," + ",错误码为:" + param);
                    }
                    break;
                case TmPlayerStatus.TmPlayerPlayFinish:
                    text_promptmsg.setText("播放完成" + "TmPlayerPlayFinish");
                    break;

                default:
                    break;
            }

        }
    };

    OnVideoSizeChangedListener changedListener = new OnVideoSizeChangedListener() {

        @Override
        public void onVideoSizeChanged(TMPCPlayer player, int width, int height) {
            // TODO Auto-generated method stub
            WindowManager windowManager = getActivity().getWindowManager();
            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            int screenHeight = windowManager.getDefaultDisplay().getHeight();
            mcpClientProtocalData.setFullScreen(surfacev_player, rl_player,
                    width, height, screenWidth, screenHeight);

        }
    };

    @Subscribe
    public void onEventMainThread(UrlEvent event) {
        if(event.getTag().equals(DeviceFragment.TAG)){
            surfacev_player.setBackgroundColor(getResources().getColor(
                    android.R.color.black));
            play(event.getUrl());
        }else if(event.getTag().equals(DeviceFragment.TAG)){
            surfacev_player.setBackgroundColor(getResources().getColor(
                    android.R.color.black));
            play(event.getUrl());
        }

    }

}
