package com.xue.liang.app.v3.fragment.help;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jph.takephoto.model.TResult;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.AlarmAdapter;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.base.BaseTakePhotoFragment;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpReq;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpResp;
import com.xue.liang.app.v3.constant.LoginInfoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/2.
 */
public class HelpFragment extends BaseFragment {



    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.rl_content_pic)
    RelativeLayout rl_content_pic;

    @BindView(R.id.rl_content_video)
    RelativeLayout rl_content_video;


    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViews() {

        tv_title.setText("报警求助");

        getChildFragmentManager().beginTransaction().replace(R.id.rl_content_pic,new HelpPictureFragment()).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.rl_content_video,new HelpCamraVideoFragment()).commit();

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_help;
    }
}
