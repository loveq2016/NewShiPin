package com.xue.liang.app.v2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.base.BaseActivity;
import com.xue.liang.app.v2.entity.InfoBean;
import com.xue.liang.app.v2.http.HttpResult;
import com.xue.liang.app.v2.presenter.IinfoList;
import com.xue.liang.app.v2.presenter.impl.InfoListPresenter;
import com.xue.liang.app.v2.sadapter.RecyclerAdapter;
import com.xue.liang.app.v2.sadapter.viewholder.RecyclerViewHolder;
import com.xue.liang.app.v2.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InfoListActivity extends BaseActivity<InfoListPresenter> implements IinfoList.IInfoListView {


    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;


    @BindView(R.id.btn_alarmwarning)
    ImageButton btn_alarmwarning;


    @BindView(R.id.tv_title)
    TextView tv_title;

    private RecyclerAdapter<InfoBean> adapter;


    private List<InfoBean> dataList = new ArrayList<>();

    private String userid;

    private String mac;

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
        initRecyclerView();
        btn_alarmwarning.setVisibility(View.GONE);

        if (null != getIntent()) {

            userid = getIntent().getStringExtra("userid");
            mac = getIntent().getStringExtra("phoneOrMac");


        }
        mPresenter.getInfoList(userid, mac);
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }

    @Override
    protected InfoListPresenter createPresenter() {
        return new InfoListPresenter(this);
    }


    public void toDeatilActivity(String guid) {
        Bundle bundle = new Bundle();
        bundle.putString("guid", guid);
        Intent intent = new Intent();
        intent.setClass(this, InfoDetailActivity.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    public void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new RecyclerAdapter<InfoBean>(dataList, R.layout.adatper_info_item) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, InfoBean item) {
                holder.setText(R.id.title_info, item.getTitle());
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((position, item) -> {

            InfoBean bean = dataList.get(position);

            toDeatilActivity(bean.getGuid());


        });
    }

    @Override
    public void getInfoDetailSuccess(HttpResult<List<InfoBean>> result) {

        dataList.clear();
        dataList = result.getResponse();
        if(dataList.isEmpty()){
            ToastUtils.show("服务器返回的列表为空");
        }
        adapter.refreshWithNewData(dataList);

    }
}