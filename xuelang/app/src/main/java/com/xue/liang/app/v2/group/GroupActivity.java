package com.xue.liang.app.v2.group;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.adapter.GroupAdapter;
import com.xue.liang.app.v2.base.BaseActivity;
import com.xue.liang.app.v2.data.bean.MemberInfo;
import com.xue.liang.app.v2.data.reponse.YidongGroupMemberResp;
import com.xue.liang.app.v2.dialog.AddGroupDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/25.
 */

public class GroupActivity extends BaseActivity implements GroupContract.View<YidongGroupMemberResp> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private GroupAdapter groupAdapter;

    private List<MemberInfo> memberList;

    private GroupPresenter groupPresenter;

    private String mphoneNum;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_group;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
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
        Toast.makeText(getApplicationContext(), "添加成员成功", Toast.LENGTH_SHORT).show();
        groupPresenter.getGroupMemberList(mphoneNum);

    }


}
