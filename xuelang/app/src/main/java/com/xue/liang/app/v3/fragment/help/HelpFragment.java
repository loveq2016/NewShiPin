package com.xue.liang.app.v3.fragment.help;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.AlarmAdapter;
import com.xue.liang.app.v3.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/11/2.
 */
public class HelpFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<String> mdata;

    private AlarmAdapter alarmAdapter;

    private String emptyString = "";
    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViews() {
        initData();
        setUpRecyclerView();

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_help;
    }

    private AlarmAdapter.OnItemClickListener<String> onItemClickListener=new AlarmAdapter.OnItemClickListener<String>() {
        @Override
        public void onitemClick(int position, String data) {

        }
    };

    private void initData() {
        //默认初始化1个数据
        mdata = new ArrayList<>();
        mdata.add(emptyString);
    }

    private void setUpRecyclerView(){
        alarmAdapter = new AlarmAdapter(getContext(), mdata);
        alarmAdapter.setOnitemClickListener(onItemClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(alarmAdapter);
    }
}
