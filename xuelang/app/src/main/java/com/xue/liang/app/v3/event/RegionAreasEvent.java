package com.xue.liang.app.v3.event;

import com.xue.liang.app.v3.adapter.viewholder.RegionViewHolder;
import com.xue.liang.app.v3.bean.region.RegionRespBean;

/**
 * Created by jikun on 2016/12/19.
 */

public class RegionAreasEvent {
    private RegionRespBean.RegionAreas regionAreas;

    private RegionViewHolder viewHolder;

    public RegionAreasEvent(RegionRespBean.RegionAreas regionAreas,RegionViewHolder viewHolder) {
        this.regionAreas = regionAreas;
        this.viewHolder=viewHolder;
    }

    public RegionRespBean.RegionAreas getRegionAreas() {
        return regionAreas;
    }

    public void setRegionAreas(RegionRespBean.RegionAreas regionAreas) {
        this.regionAreas = regionAreas;
    }

    public RegionViewHolder getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(RegionViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }
}
