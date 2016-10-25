package com.xue.liang.app.group;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.adapter.GroupAdapter;
import com.xue.liang.app.data.bean.MemberInfo;
import com.xue.liang.app.data.reponse.YidongGroupMemberResp;
import com.xue.liang.app.dialog.AddGroupDialogFragment;



import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/25.
 */

public class GroupActivity extends FragmentActivity implements GroupContract.View<YidongGroupMemberResp> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private GroupAdapter groupAdapter;

    private List<MemberInfo> memberList;

    private GroupPresenter groupPresenter;

    private String mphoneNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        initView();
    }


    public void initView() {
        if (getIntent() != null) {
            mphoneNum = getIntent().getStringExtra("phonenum");
        }

        groupPresenter = new GroupPresenter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        groupAdapter = new GroupAdapter(this, memberList);
        recyclerView.setAdapter(groupAdapter);
        groupPresenter.getGroupMemberList(mphoneNum);
    }


    @OnClick(R.id.bt_add_player)
    public void addPlayer() {
        new AddGroupDialogFragment.Builder().setOnAddGroupListener(onAddGroupListener).show(getSupportFragmentManager(), "Dialg");
    }

    @OnClick(R.id.bt_back)
    public void close() {
        finish();
    }


    public AddGroupDialogFragment.OnAddGroupListener onAddGroupListener = new AddGroupDialogFragment.OnAddGroupListener() {
        @Override
        public void onAdd(String name, String addphoneNum) {
            groupPresenter.addGroupMemberList(mphoneNum, addphoneNum, name);
        }

        @Override
        public void onCancle() {

        }
    };


    @Override
    public void onSuccess(YidongGroupMemberResp yidongGroupMemberResp) {
        Toast.makeText(getApplicationContext(), yidongGroupMemberResp.getYiDongAlarmResp().getResultMsg(), Toast.LENGTH_SHORT).show();
        groupAdapter.reshListData(yidongGroupMemberResp.getMemberList());
    }

    @Override
    public void onFail(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddSuccess() {
        

    }
}
