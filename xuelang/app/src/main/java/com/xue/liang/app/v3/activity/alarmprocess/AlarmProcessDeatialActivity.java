package com.xue.liang.app.v3.activity.alarmprocess;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.alarm.AlarmRespBean;
import com.xue.liang.app.v3.bean.alarmhandle.AlarmHandleReqBean;
import com.xue.liang.app.v3.bean.alarmhandle.AlarmHandleRespBean;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.constant.LoginInfoUtils;
import com.xue.liang.app.v3.utils.Constant;


import butterknife.BindView;
import butterknife.OnClick;

public class AlarmProcessDeatialActivity extends BaseActivity implements AlarmProcessDeatailContract.View {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_image)
    ImageView iv_image;


    @BindView(R.id.webview)
    WebView webview;

    @BindView(R.id.tv_alarm_name)
    TextView tv_alarm_name;
    @BindView(R.id.tv_alarm_type)
    TextView tv_alarm_type;
    @BindView(R.id.tv_alarm_time)
    TextView tv_alarm_time;
    @BindView(R.id.tv_alarm_phone)
    TextView tv_alarm_phone;
    @BindView(R.id.tv_alarm_address)
    TextView tv_alarm_address;

    private AlarmRespBean.ResponseBean bean;


    private AlarmProcessDeatailPresenter alarmProcessDeatailPresenter;

    @Override
    protected void getBundleExtras(Bundle extras) {
        bean = extras.getParcelable("process");

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_alarm_process_deatial;
    }

    @Override
    protected void initViews() {
        tv_title.setText(getResources().getString(R.string.alarm_process_deatial));
        setViewByData();
        String url = UriHelper.IP + ":" + UriHelper.PORT + bean.getMap_url();
        initWebView(url);
        alarmProcessDeatailPresenter = new AlarmProcessDeatailPresenter(this);

    }

    /**
     * 上报报警  	0 完成  1 上报
     *
     * @param state
     */
    public void alarmHandler(int state) {
        AlarmHandleReqBean alarmHandleReqBean = new AlarmHandleReqBean();
        alarmHandleReqBean.setAlarm_id(bean.getAlarm_id());
        alarmHandleReqBean.setAlarm_state(0);  // //是	0 完成  1 上报
        String userid = "";
        if (LoginInfoUtils.getInstance().getLoginRespBean() != null) {
            userid = LoginInfoUtils.getInstance().getLoginRespBean().getUser_id();
        }
        alarmHandleReqBean.setUser_id(userid);
        alarmHandleReqBean.setAlarm_text("");
        alarmHandleReqBean.setAlarm_type(bean.getAlarm_type() + "");
        alarmHandleReqBean.setTermi_type(Constant.PHONE);
        alarmProcessDeatailPresenter.loadData(alarmHandleReqBean);
    }

    private void setViewByData() {
        if (bean == null) {
            return;
        }
        tv_alarm_name.setText("报警人:" + bean.getUser_name());
        tv_alarm_type.setText("报警类型:" + bean.getAlarm_type());
        tv_alarm_time.setText("报警时间:" + bean.getAlarm_time());
        tv_alarm_phone.setText("联系电话:" + bean.getUser_tel());
        tv_alarm_address.setText("报警地址:" + bean.getUser_address());
    }


    @OnClick(R.id.bt_alarm_progress)
    public void handlerAlarm() {
        alarmHandler(0);
    }

    @OnClick(R.id.bt_back)
    public void back() {
        finish();
    }

    private void initWebView(String url) {
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
        webview.setWebViewClient(new AlarmWebViewClient());
    }

    @Override
    public void onSuccess(AlarmHandleRespBean alarmHandleRespBean) {
        Toast.makeText(getApplicationContext(), "处理报警成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFail(AlarmHandleRespBean alarmHandleRespBean) {
        Toast.makeText(getApplicationContext(), "处理报警失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingView(String msg) {
        showProgressDialog("处理报警中", "请稍等");

    }

    @Override
    public void hideLoadingView() {
        dimissProgressDialog();

    }

    @Override
    public void onError(String info) {

        if (info == null) {
            info = "";
        }
        Toast.makeText(getApplicationContext(), "处理报警失败" + info, Toast.LENGTH_SHORT).show();

    }

    //Web视图
    public static class AlarmWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
