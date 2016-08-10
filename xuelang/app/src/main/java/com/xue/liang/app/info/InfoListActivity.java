package com.xue.liang.app.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xue.liang.app.R;
import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.NoticeResp;
import com.xue.liang.app.data.request.NoticeReq;
import com.xue.liang.app.http.manager.HttpManager;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.listenter.HttpListenter;
import com.xue.liang.app.http.manager.listenter.LoadingHttpListener;
import com.xue.liang.app.info.adapter.InfoAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_info_list)
public class InfoListActivity extends FragmentActivity {


    @ViewById(R.id.listview)
    protected ListView listView;

    private InfoAdapter infoAdapter;


    private List<NoticeResp.NoticeItem> noticeItemList = new ArrayList<>();


    @Click(R.id.bt_back)
    public void closeActivity() {
        finish();

    }

    @Click(R.id.btn_alarmwarning)
    public void toAlermActivity() {

    }

    @AfterViews
    public void initView() {
        initaAdapter();
        getNoticeList(getSupportFragmentManager());
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
        intent.setClass(this, InfoDetailActivity_.class);
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
        HttpManager.HttpBuilder<NoticeReq, NoticeResp> httpBuilder = new HttpManager.HttpBuilder<>();
        httpBuilder.buildRequestValue(noticeReq)
                .buildResponseClass(NoticeResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("Notice");
    }


}