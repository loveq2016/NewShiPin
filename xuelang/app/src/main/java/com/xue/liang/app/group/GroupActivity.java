package com.xue.liang.app.group;

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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/25.
 */
@EActivity(R.layout.activity_group)
public class GroupActivity extends FragmentActivity implements GroupContract.View<YidongGroupMemberResp> {

    @ViewById(R.id.recyclerView)
    RecyclerView recyclerView;
    private GroupAdapter groupAdapter;

    private List<MemberInfo> memberList;

    private GroupPresenter groupPresenter;

    private String mphoneNum;

    @AfterViews
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


    @Click(R.id.bt_add_player)
    public void addPlayer() {
        new AddGroupDialogFragment.Builder().setOnAddGroupListener(onAddGroupListener).show(getSupportFragmentManager(), "Dialg");
    }

    @Click(R.id.bt_back)
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
