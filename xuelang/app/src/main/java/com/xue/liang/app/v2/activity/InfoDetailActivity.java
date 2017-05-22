package com.xue.liang.app.v2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.base.BaseActivity;
import com.xue.liang.app.v2.http.HttpResult;
import com.xue.liang.app.v2.presenter.IinfoDetail;
import com.xue.liang.app.v2.presenter.impl.InfoDetailPresenter;

import butterknife.BindView;
import butterknife.OnClick;


public class InfoDetailActivity extends BaseActivity<InfoDetailPresenter> implements IinfoDetail.IInfoDetailView {

    @BindView(R.id.text_content)
    TextView text_content;


    @BindView(R.id.btn_alarmwarning)
    ImageButton btn_alarmwarning;


    @BindView(R.id.tv_title)
    TextView tv_title;

    private String guid;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_info_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        tv_title.setText("公告通知");
        btn_alarmwarning.setVisibility(View.GONE);
        if (null != getIntent()) {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            guid = bundle.getString("guid");
        }


        mPresenter.getInfoDetail(guid);

    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }

    @Override
    protected InfoDetailPresenter createPresenter() {
        return new InfoDetailPresenter(this);
    }


    @OnClick(R.id.bt_back)
    public void close() {
        finish();
    }


    @Override
    public void getInfoDetailSuccess(HttpResult<String> result) {

        tv_title.setText(result.getTitle());
        text_content.setText(result.getContent());


    }
}