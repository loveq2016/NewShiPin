package com.xue.liang.app.v3.fragment.alarmprocesse;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.constant.BundleConstant;

import butterknife.BindView;

/**
 * Created by jikun on 2016/11/18.
 * 待处理报警Fragment
 */

public class StayPendingAlarmFragment extends BaseFragment {


    private String url;

    @BindView(R.id.webview)
    WebView webview;


    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    public static StayPendingAlarmFragment newInstance(String phone) {
        Bundle arguments = new Bundle();
        arguments.putString(BundleConstant.EASY_PEOPLE_INFO_PHONE, phone);
        StayPendingAlarmFragment stayPendingAlarmFragment = new StayPendingAlarmFragment();
        stayPendingAlarmFragment.setArguments(arguments);
        return stayPendingAlarmFragment;
    }

    @Override
    protected void initViews() {
        url= UriHelper.getStayPendingAlarmUrl();

        Bundle bundle = getArguments();
        if (bundle != null) {
            //mPhone = bundle.getString(BundleConstant.EASY_PEOPLE_INFO_PHONE);
        }


        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页

        webview.loadUrl(url);
        //设置Web视图
        webview.setWebViewClient(new webViewClient());


    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_stay_pending;
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
