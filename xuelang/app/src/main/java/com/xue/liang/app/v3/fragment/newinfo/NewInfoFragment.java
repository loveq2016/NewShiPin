package com.xue.liang.app.v3.fragment.newinfo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.activity.newinfo.NewInfoDetailActivity;
import com.xue.liang.app.v3.adapter.NewInfoAdapter;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.bean.alarm.AlarmReqBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.bean.notice.NoticeReqBean;
import com.xue.liang.app.v3.bean.notice.NoticeRespBean;
import com.xue.liang.app.v3.config.TestData;
import com.xue.liang.app.v3.constant.BundleConstant;
import com.xue.liang.app.v3.fragment.alarmprocesse.PendPresenter;
import com.xue.liang.app.v3.fragment.alarmprocesse.StayPendingAlarmFragment;
import com.xue.liang.app.v3.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/11/2.
 */
public class NewInfoFragment extends BaseFragment implements NewInfoContract.View {


    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    public SHSwipeRefreshLayout swipeRefreshLayout;

    private NewInfoAdapter adapter;

    private List<NoticeRespBean.ResponseBean> mListData = new ArrayList<>();

    private NewInfoPresenter newInfoPresenter;


    private LoginRespBean mloginRespBean;

    private final int count = 10;

    private int startindex = 0;


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
        tv_title.setText("新闻公告");
        newInfoPresenter = new NewInfoPresenter(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mloginRespBean = bundle.getParcelable(BundleConstant.BUNDLE_NEW_INFO);
        }

        setupRecyclerView();

        reshData();

    }

    private void reshData() {
        NoticeReqBean noticeReqBean = new NoticeReqBean();
        noticeReqBean.setTermi_type(Constant.PHONE);
        noticeReqBean.setTermi_unique_code(TestData.termi_unique_code);
        noticeReqBean.setCount(count);
        noticeReqBean.setStrat_index(startindex);
        noticeReqBean.setUser_id(mloginRespBean.getUser_id());
        newInfoPresenter.loadData(noticeReqBean);
    }


    private void loadMoreData() {
        startindex = startindex + mListData.size();
        NoticeReqBean noticeReqBean = new NoticeReqBean();
        noticeReqBean.setTermi_type(Constant.PHONE);
        noticeReqBean.setTermi_unique_code(TestData.termi_unique_code);
        noticeReqBean.setCount(count);
        noticeReqBean.setStrat_index(startindex);
        noticeReqBean.setUser_id(mloginRespBean.getUser_id());
        newInfoPresenter.loadMoreData(noticeReqBean);

    }


    public static NewInfoFragment newInstance(LoginRespBean loginRespBean) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(BundleConstant.BUNDLE_NEW_INFO, loginRespBean);
        NewInfoFragment newInfoFragment = new NewInfoFragment();
        newInfoFragment.setArguments(arguments);
        return newInfoFragment;
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


        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                reshData();

            }

            @Override
            public void onLoading() {
                loadMoreData();

            }

            @Override
            public void onRefreshPulStateChange(float v, int i) {

            }

            @Override
            public void onLoadmorePullStateChange(float v, int i) {

            }
        });


    }

    @Override
    public void onSuccess(NoticeRespBean noticeRespBean) {
        swipeRefreshLayout.finishRefresh();

        if (noticeRespBean != null & noticeRespBean.getResponse() != null&&!noticeRespBean.getResponse().isEmpty()) {
            mListData.clear();
            mListData.addAll(noticeRespBean.getResponse());
            adapter.notifyDataSetChanged();
        }else{
            showToast("暂无数据");
        }

    }

    @Override
    public void onFail() {
        swipeRefreshLayout.finishRefresh();

    }

    @Override
    public void onSuccessMore(NoticeRespBean noticeRespBean) {
        swipeRefreshLayout.finishLoadmore();

        if (noticeRespBean != null & noticeRespBean.getResponse() != null&&!noticeRespBean.getResponse().isEmpty()) {
            mListData.addAll(noticeRespBean.getResponse());
            adapter.notifyDataSetChanged();
        }else{
            showToast("暂无数据");
        }

    }

    @Override
    public void onFailMore() {
        swipeRefreshLayout.finishLoadmore();

    }


    @Override
    public void showLoadingView(String msg) {
        // showProgressDialog();


    }

    @Override
    public void hideLoadingView() {
        //dimissProgressDialog();
    }

    @Override
    public void onError(String info) {

    }
}
