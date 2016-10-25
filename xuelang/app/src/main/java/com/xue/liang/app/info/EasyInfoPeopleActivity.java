package com.xue.liang.app.info;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xue.liang.app.R;

import com.xue.liang.app.alarm.AlarmActivity2;
import com.xue.liang.app.common.Config;

import com.xue.liang.app.utils.DeviceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EasyInfoPeopleActivity extends FragmentActivity {

    @BindView(R.id.webview)
    protected WebView webview;

    @BindView(R.id.btn_alarmwarning)
    ImageButton btn_alarmwarning;

    @BindView(R.id.tv_title)
    TextView tv_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_info_people);
        ButterKnife.bind(this);
        initView();
    }


    public void initView() {

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
        String url = "http://www.uphsh.com/wap/38fceb89d44a4778bb2459556820c69a?userToken=" + Config.TEST_PHONE_NUMBER;
        webview.loadUrl(url);
        //设置Web视图
        webview.setWebViewClient(new webViewClient());


        if (DeviceUtil.isPhone(getApplicationContext())) {
            //2为手机

        } else {
            //1为机顶盒

            btn_alarmwarning.setVisibility(View.GONE);
        }
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

    @OnClick(R.id.btn_alarmwarning)
    public void toAlarmActivity() {
        Intent intent = new Intent();
        intent.setClass(this, AlarmActivity2.class);
        startActivity(intent);
    }
}
