package com.xue.liang.app.v3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.viewholder.EmptyViewHolder;
import com.xue.liang.app.v3.adapter.viewholder.FooterAlarmViewHolder;
import com.xue.liang.app.v3.adapter.viewholder.PendAlarmViewHolder;
import com.xue.liang.app.v3.base.baseadapter.SectionedRecyclerViewAdapter;
import com.xue.liang.app.v3.bean.alarm.AlarmRespBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/20.
 */

public class StayPendingAlarmAdapter extends SectionedRecyclerViewAdapter<EmptyViewHolder, PendAlarmViewHolder, FooterAlarmViewHolder> {


    private List<AlarmRespBean.ResponseBean> mResponseBeanList;

    protected Context context = null;

    public StayPendingAlarmAdapter(Context context, List<AlarmRespBean.ResponseBean> responseBeanList) {
        this.context = context;
        mResponseBeanList = responseBeanList;

    }

    @Override
    protected int getSectionCount() {
        return 1;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return mResponseBeanList == null ? 0 : mResponseBeanList.size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    protected EmptyViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {

        View view = getLayoutInflater().inflate(R.layout.adapter_empty, parent, false);
        return new EmptyViewHolder(view);
    }

    @Override
    protected FooterAlarmViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.footer_alarm_view_holder, parent, false);
        return new FooterAlarmViewHolder(view);
    }

    @Override
    protected PendAlarmViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.adapter_pending_alarm, parent, false);
        return new PendAlarmViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(EmptyViewHolder holder, int section) {

    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterAlarmViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(PendAlarmViewHolder holder, int section, int position) {
        holder.render(mResponseBeanList.get(position), section, position);
        if (mItemClickLister != null) {
            holder.setRecyclerItemClickListener(mItemClickLister);
        }
    }

    public List<AlarmRespBean.ResponseBean> getmResponseBeanList() {
        return mResponseBeanList;
    }
}
