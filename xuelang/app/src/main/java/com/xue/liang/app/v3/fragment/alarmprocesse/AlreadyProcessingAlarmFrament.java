package com.xue.liang.app.v3.fragment.alarmprocesse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import com.xue.liang.app.R;

import com.xue.liang.app.v3.activity.alarmprocess.AlreadyProcessingAlarmDetialActivity;
import com.xue.liang.app.v3.adapter.StayPendingAlarmAdapter;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.base.baseadapter.SectionedRecyclerViewAdapter;
import com.xue.liang.app.v3.base.baseadapter.SectionedSpanSizeLookup;
import com.xue.liang.app.v3.bean.alarm.AlarmReqBean;
import com.xue.liang.app.v3.bean.alarm.AlarmRespBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.constant.BundleConstant;
import com.xue.liang.app.v3.utils.Constant;
import com.xue.liang.app.v3.utils.SharedDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * Created by jikun on 2016/11/18.
 * 已处理
 */

public class AlreadyProcessingAlarmFrament extends BaseFragment implements PendAlarmContract.View {


    @BindView(R.id.mRefreshLayout)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private PendPresenter pendPresenter;

    private LoginRespBean mloginRespBean;

    private final int count = 10;

    private int startindex = 0;

    private List<AlarmRespBean.ResponseBean> responseBeanList;


    private int[] type = {AlarmReqBean.TYPE_COMPLETED};


    private StayPendingAlarmAdapter adapter;

    @Override
    protected void onFirstUserVisible() {
        reshData();
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    public static AlreadyProcessingAlarmFrament newInstance(LoginRespBean loginRespBean) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(BundleConstant.BUNDLE_STAY_PENDING_ALARM, loginRespBean);
        AlreadyProcessingAlarmFrament alreadyProcessingAlarmFrament = new AlreadyProcessingAlarmFrament();
        alreadyProcessingAlarmFrament.setArguments(arguments);
        return alreadyProcessingAlarmFrament;
    }


    @Override
    protected void initViews() {


        responseBeanList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mloginRespBean = bundle.getParcelable(BundleConstant.BUNDLE_STAY_PENDING_ALARM);
        }


        setupRecycler();
        setupRefreshLayoutView();


    }

    private void setupRefreshLayoutView(){
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate(){

            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                reshData();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                loadMoreData();
                return false;
            }
        });
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder =  new BGANormalRefreshViewHolder(getContext(), true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

    }


    protected void setupRecycler() {
        if (adapter == null) {
            adapter = new StayPendingAlarmAdapter(getActivity().getApplicationContext(), responseBeanList);
        }
        recyclerView.setAdapter(adapter);
        adapter.setItemClickLister(new SectionedRecyclerViewAdapter.RecyclerItemClickListener<AlarmRespBean.ResponseBean>() {
            @Override
            public void onItemClick(View view, int section, int postion, AlarmRespBean.ResponseBean value) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("process", value);
                String key = value.generateDistinguishId();
                SharedDB.putBooleanValue(getContext(), key, true);
                //readyGo(AlarmProcessDeatialActivity.class, bundle);
                readyGoForResult(AlreadyProcessingAlarmDetialActivity.class, 0, bundle);

            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);


        gridLayoutManager.setSmoothScrollbarEnabled(true);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(lookup);
        recyclerView.setLayoutManager(gridLayoutManager);

//        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//                reshData();
//            }
//
//            @Override
//            public void onLoadMore() {
//                loadMoreData();
//            }
//        });


    }

    private void reshData() {
        startindex = 0;
        responseBeanList.clear();
        AlarmReqBean alarmReqBean = new AlarmReqBean(Constant.PHONE, mloginRespBean.getUser_id(), mloginRespBean.getGroup_value(), type, startindex, count);
        pendPresenter = new PendPresenter(this);
        pendPresenter.loadData(alarmReqBean);
    }

    private void loadMoreData() {
        startindex = startindex + responseBeanList.size();
        AlarmReqBean alarmReqBean = new AlarmReqBean(Constant.PHONE, mloginRespBean.getUser_id(), mloginRespBean.getGroup_value(), type, startindex, count);
        pendPresenter = new PendPresenter(this);
        pendPresenter.loadMoreData(alarmReqBean);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_stay_pending;
    }


    @Override
    public void onSuccess(AlarmRespBean bean) {

        mRefreshLayout.endRefreshing();

        if (bean != null & bean.getResponse() != null && !bean.getResponse().isEmpty()) {
            responseBeanList.clear();
            responseBeanList.addAll(bean.getResponse());
            adapter.notifyDataSetChanged();
        } else {
            responseBeanList.clear();
            adapter.notifyDataSetChanged();
            showToast("暂无数据");
        }


    }

    @Override
    public void onFail() {
        mRefreshLayout.endRefreshing();
    }

    @Override
    public void onSuccessMore(AlarmRespBean bean) {

        mRefreshLayout.endLoadingMore();

        if (bean != null & bean.getResponse() != null && !bean.getResponse().isEmpty()) {
            responseBeanList.addAll(bean.getResponse());
            adapter.notifyDataSetChanged();
        } else {
            showToast("暂无数据");
        }


    }

    @Override
    public void onFailMore() {

        mRefreshLayout.endLoadingMore();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            Log.e("已处理报警","已处理报警");
            reshData();
        }
    }
}
