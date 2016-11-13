package com.xue.liang.app.v3.fragment.easypeopleinfo;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.constant.BundleConstant;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/11/6.
 */

public class EasyPeopleInfoFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.webview)
    WebView webview;

    private String mPhone;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    public  static  EasyPeopleInfoFragment newInstance(String phone) {
        Bundle arguments = new Bundle();
        arguments.putString(BundleConstant.EASY_PEOPLE_INFO_PHONE, phone);
        EasyPeopleInfoFragment easyPeopleInfoFragment = new EasyPeopleInfoFragment();
        easyPeopleInfoFragment.setArguments(arguments);
        return easyPeopleInfoFragment;
    }

    @Override
    protected void initViews() {
        tv_title.setText("便民信息");

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPhone = bundle.getString(BundleConstant.EASY_PEOPLE_INFO_PHONE);
        }

        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        String url = "http://www.uphsh.com/wap/38fceb89d44a4778bb2459556820c69a?userToken=" + mPhone;
        webview.loadUrl(url);
        //设置Web视图
        webview.setWebViewClient(new webViewClient());


    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_easy_people_info;
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
