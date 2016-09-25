package com.xue.liang.app.group;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xue.liang.app.R;
import com.xue.liang.app.adapter.GroupAdapter;
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
public class GroupActivity extends FragmentActivity {

    @ViewById(R.id.recyclerView)
    RecyclerView recyclerView;
    private GroupAdapter groupAdapter;

    private List<String> data;

    @AfterViews
    public void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        data = new ArrayList<>();
        data.add("13");
        data.add("13");
        data.add("13");
        data.add("13");
        groupAdapter = new GroupAdapter(this, data);
        recyclerView.setAdapter(groupAdapter);


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
        public void onAdd(String name, String phoneNum) {

        }

        @Override
        public void onCancle() {

        }
    };
}
