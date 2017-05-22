package com.xue.liang.app.v2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.base.BaseActivity;
import com.xue.liang.app.v2.base.BasePresenter;


import butterknife.BindView;
import butterknife.OnClick;

public class EasyInfoPeopleActivity extends BaseActivity {

    @BindView(R.id.webview)
    protected WebView webview;

    @BindView(R.id.btn_alarmwarning)
    ImageButton btn_alarmwarning;

    @BindView(R.id.tv_title)
    TextView tv_title;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_easy_info_people;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        tv_title.setText("便民信息");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String phone = bundle.getString("phone");
        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        String url = "http://www.uphsh.com/wap/38fceb89d44a4778bb2459556820c69a?userToken=" + phone;
        webview.loadUrl(url);
        //设置Web视图
        webview.setWebViewClient(new webViewClient());

        btn_alarmwarning.setVisibility(View.GONE);

    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @OnClick(R.id.bt_back)
    public void close() {
        finish();
    }


}
