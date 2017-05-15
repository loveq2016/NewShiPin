package com.xue.liang.app.v2.info;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xue.liang.app.v2.R;

import com.xue.liang.app.v2.alarm.AlarmActivity2;
import com.xue.liang.app.v2.base.BaseActivity;
import com.xue.liang.app.v2.common.Config;
import com.xue.liang.app.v2.data.reponse.NoticeResp;
import com.xue.liang.app.v2.data.request.NoticeReq;
import com.xue.liang.app.v2.http.manager.HttpManager;
import com.xue.liang.app.v2.http.manager.data.HttpReponse;
import com.xue.liang.app.v2.http.manager.listenter.HttpListenter;
import com.xue.liang.app.v2.http.manager.listenter.LoadingHttpListener;

import com.xue.liang.app.v2.info.adapter.InfoAdapter;
import com.xue.liang.app.v2.utils.DeviceUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InfoListActivity extends BaseActivity {


    @BindView(R.id.listview)
    protected ListView listView;

    private InfoAdapter infoAdapter;


    private List<NoticeResp.NoticeItem> noticeItemList = new ArrayList<NoticeResp.NoticeItem>();


    @BindView(R.id.btn_alarmwarning)
    ImageButton btn_alarmwarning;


    @BindView(R.id.tv_title)
    TextView tv_title;

    @OnClick(R.id.bt_back)
    public void closeActivity() {
        finish();

    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_info_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tv_title.setText("公告通知");
        initaAdapter();
        getNoticeList(getSupportFragmentManager());


        if (DeviceUtil.isPhone(getApplicationContext())) {
            //2为手机

        } else {
            //1为机顶盒

            btn_alarmwarning.setVisibility(View.GONE);
        }
    }


    private void initaAdapter() {
        infoAdapter = new InfoAdapter(this, noticeItemList);
        listView.setAdapter(infoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String guid = noticeItemList.get(position).getGuid();
                toDeatilActivity(guid);
            }
        });
    }

    public void toDeatilActivity(String guid) {
        Bundle bundle = new Bundle();
        bundle.putString("guid", guid);
        Intent intent = new Intent();
        intent.setClass(this, InfoDetailActivity.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }


    public void getNoticeList(FragmentManager fragmentManager) {
        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<NoticeResp>() {
            @Override
            public void onFailed(String msg) {

            }

            @Override
            public void onSuccess(HttpReponse<NoticeResp> httpReponse) {
                noticeItemList = httpReponse.getData().getResponse();
                infoAdapter.reshData(noticeItemList);

            }
        }, fragmentManager);

        String url = Config.getNoticeUrl();
        NoticeReq noticeReq = new NoticeReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC);
        HttpManager.HttpBuilder<NoticeReq, NoticeResp> httpBuilder = new HttpManager.HttpBuilder<NoticeReq, NoticeResp>();
        httpBuilder.buildRequestValue(noticeReq)
                .buildResponseClass(NoticeResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("Notice");
    }


    @OnClick(R.id.btn_alarmwarning)
    public void toAlarmActivity() {
        Intent intent = new Intent();
        intent.setClass(this, AlarmActivity2.class);


        startActivity(intent);
    }


}
