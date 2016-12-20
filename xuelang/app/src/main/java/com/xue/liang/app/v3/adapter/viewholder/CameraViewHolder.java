package com.xue.liang.app.v3.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.CameraAdapter;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;
import com.xue.liang.app.v3.event.RegionAreasEvent;
import com.xue.liang.app.v3.event.RegionCamraEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/12/18.
 */

public class CameraViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context mcontext;
    private DeviceRespBean.ResponseBean responseBean;

    @BindView(R.id.play_item_url_tv)
    protected TextView play_item_url_tv;

    private OnCameraclickListener onCameraclickListener;

    private int mposition;

    public CameraViewHolder(View itemView, Context context) {
        super(itemView);
        mcontext = context;
        this.itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onClick(View view) {
        EventBus.getDefault().post(new RegionCamraEvent(responseBean));
        if(onCameraclickListener!=null){
            onCameraclickListener.onItem(mposition);
        }

    }

    public void bindView(DeviceRespBean.ResponseBean responseBean,int position) {
        mposition=position;
        this.responseBean = responseBean;
        if(responseBean.ischoose()){
            this.itemView.setBackgroundColor(mcontext.getResources().getColor(R.color.blue));
        }else{
            this.itemView.setBackgroundColor(mcontext.getResources().getColor(R.color.md_divider_white));
        }

        play_item_url_tv.setText(responseBean.getDev_name());
    }



    public void setOnCameraclickListener(OnCameraclickListener onCameraclickListener) {
        this.onCameraclickListener = onCameraclickListener;
    }

    public interface OnCameraclickListener {
        void onItem(int index);
    }
}

