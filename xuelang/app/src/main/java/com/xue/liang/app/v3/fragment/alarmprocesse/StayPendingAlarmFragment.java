package com.xue.liang.app.v3.fragment.alarmprocesse;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.bean.StayPendBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.constant.BundleConstant;
import com.xue.liang.app.v3.utils.Constant;
import com.xue.liang.app.v3.utils.EncodeUtils;

import butterknife.BindView;

/**
 * Created by jikun on 2016/11/18.
 * 待处理报警Fragment
 */

public class StayPendingAlarmFragment extends BaseFragment {


    private String url;

    private LoginRespBean mloginRespBean;

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

    public static StayPendingAlarmFragment newInstance(LoginRespBean loginRespBean) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(BundleConstant.BUNDLE_STAY_PENDING_ALARM, loginRespBean);
        StayPendingAlarmFragment stayPendingAlarmFragment = new StayPendingAlarmFragment();
        stayPendingAlarmFragment.setArguments(arguments);
        return stayPendingAlarmFragment;
    }


    private String getUrl() {
        StringBuilder stringBuilder = new StringBuilder();

        StayPendBean stayPendBean = new StayPendBean();
        stayPendBean.setUser_id(mloginRespBean.getUser_id());
        stayPendBean.setTermi_type(Constant.PHONE);
        stringBuilder.append(UriHelper.getStayPendingAlarmUrl());
        String json = new Gson().toJson(stayPendBean);

        String base64String = new String(EncodeUtils.base64Encode(json));
        stringBuilder.append(base64String);
        return stringBuilder.toString();

    }

    @Override
    protected void initViews() {


        Bundle bundle = getArguments();
        if (bundle != null) {
            mloginRespBean = bundle.getParcelable(BundleConstant.BUNDLE_STAY_PENDING_ALARM);
        }

        url = getUrl();

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
