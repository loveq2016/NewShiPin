package com.xue.liang.app.v3.fragment.newinfo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.NewInfoAdapter;
import com.xue.liang.app.v3.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/11/2.
 */
public class NewInfoFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    private NewInfoAdapter adapter;

    private List<String> mListData;

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

        setupRecyclerView();

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_new_info;
    }

    protected void setupRecyclerView() {
        mListData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mListData.add("");
        }
        if (adapter == null) {
            adapter = new NewInfoAdapter(getContext(),mListData);
        }
        mRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);

        layoutManager.setOrientation(GridLayoutManager.VERTICAL);


        layoutManager.setSmoothScrollbarEnabled(true);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);


    }
}
