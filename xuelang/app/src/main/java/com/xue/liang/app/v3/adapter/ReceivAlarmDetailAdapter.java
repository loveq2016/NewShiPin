package com.xue.liang.app.v3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.viewholder.EmptyViewHolder;
import com.xue.liang.app.v3.adapter.viewholder.HeaderAlarmDetailViewHolder;
import com.xue.liang.app.v3.adapter.viewholder.ReceiveAlarmDetailViewHolder;
import com.xue.liang.app.v3.base.baseadapter.SectionedRecyclerViewAdapter;
import com.xue.liang.app.v3.bean.alarm.AlarmDetailRespBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class ReceivAlarmDetailAdapter extends SectionedRecyclerViewAdapter<HeaderAlarmDetailViewHolder, ReceiveAlarmDetailViewHolder, EmptyViewHolder> {


    protected Context context = null;

    private List<AlarmDetailRespBean.ReceivBean> data;


    public ReceivAlarmDetailAdapter(Context context) {
        this.context = context;

    }

    @Override
    protected int getSectionCount() {
        return 1;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return data != null ? data.size() : 0;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderAlarmDetailViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.viewholder_header_alarm_detail, parent, false);
        return new HeaderAlarmDetailViewHolder(view);
    }

    @Override
    protected EmptyViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected ReceiveAlarmDetailViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.viewholder_receivealarmdetail, parent, false);
        return new ReceiveAlarmDetailViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderAlarmDetailViewHolder holder, int section) {
        holder.bindView("接警人信息");
    }

    @Override
    protected void onBindSectionFooterViewHolder(EmptyViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(ReceiveAlarmDetailViewHolder holder, int section, int position) {
        holder.bindView(data.get(position),position);

    }

    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    public void resh(List<AlarmDetailRespBean.ReceivBean> data) {
        this.data = data;
        notifyDataSetChanged();

    }
}
