package com.xue.liang.app.v3.fragment.newinfo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.activity.newinfo.NewInfoDetailActivity;
import com.xue.liang.app.v3.adapter.NewInfoAdapter;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.bean.notice.NoticeReqBean;
import com.xue.liang.app.v3.bean.notice.NoticeRespBean;
import com.xue.liang.app.v3.config.TestData;
import com.xue.liang.app.v3.constant.BundleConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/11/2.
 */
public class NewInfoFragment extends BaseFragment implements NewInfoContract.View {


    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    private NewInfoAdapter adapter;

    private List<NoticeRespBean.ResponseBean> mListData;

    private NewInfoPresenter newInfoPresenter;


    @Override
    protected void onFirstUserVisible() {

        //reshData();

    }

    @Override
    protected void onUserVisible() {
        Log.d("c", "c");

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViews() {
        newInfoPresenter = new NewInfoPresenter(this);

        setupRecyclerView();

        reshData();

    }

    private void reshData() {
        NoticeReqBean noticeReqBean = new NoticeReqBean();
        noticeReqBean.setTermi_type("2");
        noticeReqBean.setTermi_unique_code(TestData.termi_unique_code);
        noticeReqBean.setCount(20);
        noticeReqBean.setStrat_index(0);
        noticeReqBean.setUser_id("123");
        newInfoPresenter.loadData(noticeReqBean);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_new_info;
    }

    protected void setupRecyclerView() {
        mListData = new ArrayList<>();

        if (adapter == null) {
            adapter = new NewInfoAdapter(getContext(), mListData);
        }

        adapter.setmItemClickLister(new NewInfoAdapter.RecyclerItemClickListener<NoticeRespBean.ResponseBean>() {
            @Override
            public void onItemClick(View view, int section, int postion, NoticeRespBean.ResponseBean value) {

                Bundle bundle = new Bundle();
                bundle.putString(BundleConstant.GUID, value.getGuid());
                readyGo(NewInfoDetailActivity.class, bundle);

            }
        });
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

    @Override
    public void onSuccess(NoticeRespBean noticeRespBean) {
        if (noticeRespBean != null & noticeRespBean.getResponse() != null) {
            adapter.reshData(noticeRespBean.getResponse());
        }

    }

    @Override
    public void onFail(NoticeRespBean noticeRespBean) {

    }

    @Override
    public void showLoadingView(String msg) {
        showProgressDialog();


    }

    @Override
    public void hideLoadingView() {
        dimissProgressDialog();
    }

    @Override
    public void onError(String info) {

    }
}
