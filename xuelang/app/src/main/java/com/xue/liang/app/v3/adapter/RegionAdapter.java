package com.xue.liang.app.v3.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.viewholder.RegionViewHolder;
import com.xue.liang.app.v3.bean.region.RegionRespBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */

public class RegionAdapter extends RecyclerView.Adapter<RegionViewHolder> {

    private LayoutInflater layoutInflater;

    private Context mcontext;

    private List<RegionRespBean.RegionAreas> data;

    public RegionAdapter(Context context) {
        mcontext = context;
        // this.data = data;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public RegionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.adapter_region_item, parent, false);
        return new RegionViewHolder(mcontext, view);
    }

    @Override
    public void onBindViewHolder(RegionViewHolder holder, int position) {
        holder.bindData(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }



    public void reshData(List<RegionRespBean.RegionAreas> data) {
        this.data=data;
        notifyDataSetChanged();
    }

    public List<RegionRespBean.RegionAreas> getData() {
        return data;
    }

}
