package com.xue.liang.app.v3.fragment.alarmprocesse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.activity.alarmprocess.AlarmProcessDeatialActivity;
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

/**
 * Created by jikun on 2016/11/18.
 * 待处理报警Fragment
 */

public class StayPendingAlarmFragment extends BaseFragment implements PendAlarmContract.View {


    @BindView(R.id.swipeRefreshLayout)
    SHSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private PendPresenter pendPresenter;

    private LoginRespBean mloginRespBean;

    private final int count = 10;

    private int startindex = 0;

    private List<AlarmRespBean.ResponseBean> responseBeanList;


    private int[] type = {AlarmReqBean.TYPE_TO_BE_PROCESSED, AlarmReqBean.TYPE_HAS_BEEN_REPORTED, AlarmReqBean.TYPE_HAS_BEEN_AUTOMATICALLY_REPORTED};


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

    public static StayPendingAlarmFragment newInstance(LoginRespBean loginRespBean) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(BundleConstant.BUNDLE_STAY_PENDING_ALARM, loginRespBean);
        StayPendingAlarmFragment stayPendingAlarmFragment = new StayPendingAlarmFragment();
        stayPendingAlarmFragment.setArguments(arguments);
        return stayPendingAlarmFragment;
    }


    @Override
    protected void initViews() {


        responseBeanList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mloginRespBean = bundle.getParcelable(BundleConstant.BUNDLE_STAY_PENDING_ALARM);
        }


        setupRecycler();


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
                readyGoForResult(AlarmProcessDeatialActivity.class, 0, bundle);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);


        gridLayoutManager.setSmoothScrollbarEnabled(true);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(lookup);
        recyclerView.setLayoutManager(gridLayoutManager);


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

        swipeRefreshLayout.finishRefresh();

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
        swipeRefreshLayout.finishRefresh();
    }

    @Override
    public void onSuccessMore(AlarmRespBean bean) {

        swipeRefreshLayout.finishLoadmore();

        if (bean != null & bean.getResponse() != null && !bean.getResponse().isEmpty()) {
            responseBeanList.addAll(bean.getResponse());
            adapter.notifyDataSetChanged();
        } else {
            showToast("暂无数据");
        }


    }

    @Override
    public void onFailMore() {

        swipeRefreshLayout.finishLoadmore();
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
        if (requestCode == 0) {
            reshData();
        }
    }
}
