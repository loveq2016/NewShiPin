package com.xue.liang.app.v3.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.AlarmAdapter;
import com.xue.liang.app.v3.adapter.CameraAdapter;
import com.xue.liang.app.v3.adapter.RegionAdapter;
import com.xue.liang.app.v3.bean.region.RegionRespBean;
import com.xue.liang.app.v3.event.RegionCamraEvent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/17.
 */

public class RegionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    @BindView(R.id.iv_bt_region_more)
    ImageView iv_bt_region_more;
    @BindView(R.id.tv_textview)
    protected TextView tv_textview;
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    private RegionAdapter regionAdapter;

    private CameraAdapter cameraAdapter;


    private Type current_type = Type.TYPE_REGION;

    public enum Type {
        TYPE_REGION, TYPE_CARMERA


    }


    private RegionRespBean.RegionAreas mRegionAreas;

    private Context mcontext;


    public RegionViewHolder(Context context, View itemView) {
        super(itemView);
        mcontext = context;
        this.itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
        setUpRecyclerView();

        regionAdapter = new RegionAdapter(mcontext);
        cameraAdapter = new CameraAdapter(mcontext);

    }

    public void bindData(RegionRespBean.RegionAreas regionAreas) {

        mRegionAreas = regionAreas;
        tv_textview.setText(regionAreas.getRegion_name());


    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(mRegionAreas.getGroup_id())) {
            current_type=Type.TYPE_REGION;
            recyclerView.setAdapter(regionAdapter);
            if (null != regionAdapter.getData() && regionAdapter.getData().size() > 0) {
                regionAdapter.reshData(new ArrayList<RegionRespBean.RegionAreas>());
            } else {
                regionAdapter.reshData(mRegionAreas.getChildList());
            }
            showMoreImage();
        } else {
            current_type=Type.TYPE_CARMERA;
            recyclerView.setAdapter(regionAdapter);
            if (null != regionAdapter.getData() && regionAdapter.getData().size() > 0) {
                regionAdapter.reshData(new ArrayList<RegionRespBean.RegionAreas>());
            } else {
                regionAdapter.reshData(mRegionAreas.getChildList());
            }
            showMoreImage();
        }


    }

    public void showMoreImage() {
        if (null != regionAdapter.getData() && regionAdapter.getData().size() > 0) {
            iv_bt_region_more.setSelected(true);
        } else {
            iv_bt_region_more.setSelected(false);
        }

    }

    public void setUpRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void onEventMainThread(RegionCamraEvent event) {
        if(current_type==Type.TYPE_CARMERA){
            cameraAdapter.reshData(event.getResponseBeanList());
        }

    }


}
