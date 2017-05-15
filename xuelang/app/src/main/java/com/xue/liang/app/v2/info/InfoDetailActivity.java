package com.xue.liang.app.v2.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.alarm.AlarmActivity2;
import com.xue.liang.app.v2.base.BaseActivity;
import com.xue.liang.app.v2.common.Config;
import com.xue.liang.app.v2.data.reponse.NoticeDetailResp;
import com.xue.liang.app.v2.data.request.NoticeDetailReq;
import com.xue.liang.app.v2.http.manager.HttpManager;
import com.xue.liang.app.v2.http.manager.data.HttpReponse;
import com.xue.liang.app.v2.http.manager.listenter.HttpListenter;
import com.xue.liang.app.v2.http.manager.listenter.LoadingHttpListener;
import com.xue.liang.app.v2.utils.DeviceUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class InfoDetailActivity extends BaseActivity {

    @BindView(R.id.text_content)
    TextView text_content;


    @BindView(R.id.btn_alarmwarning)
    ImageButton btn_alarmwarning;


    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_info_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        tv_title.setText("公告通知");
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String guid = bundle.getString("guid");
        getNoticeDetail(guid, getSupportFragmentManager());


        if (DeviceUtil.isPhone(getApplicationContext())) {
            //2为手机

        } else {
            //1为机顶盒

            btn_alarmwarning.setVisibility(View.GONE);
        }
    }


    public void getNoticeDetail(String id, FragmentManager fragmentManager) {


        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<NoticeDetailResp>() {
            @Override
            public void onFailed(String msg) {
                if (TextUtils.isEmpty(msg)) {
                    msg = "null";
                }
                Toast.makeText(getApplicationContext(), "请求失败:" + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(HttpReponse<NoticeDetailResp> httpReponse) {
                if (httpReponse != null && httpReponse.getData() != null && !TextUtils.isEmpty(httpReponse.getData().getContent())) {
                    text_content.setText(httpReponse.getData().getContent());
                } else {
                    Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                }

            }
        }, fragmentManager);

        String url = Config.getNoticeDetailUrl();
        NoticeDetailReq noticeDetailReq = new NoticeDetailReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, id);
        HttpManager.HttpBuilder<NoticeDetailReq, NoticeDetailResp> httpBuilder = new HttpManager.HttpBuilder<NoticeDetailReq, NoticeDetailResp>();
        httpBuilder.buildRequestValue(noticeDetailReq)
                .buildResponseClass(NoticeDetailResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("NoticeDetail");
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
