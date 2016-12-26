package com.xue.liang.app.v3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.viewholder.CameraViewHolder;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/18.
 */

public class CameraAdapter extends RecyclerView.Adapter<CameraViewHolder> implements CameraViewHolder.OnCameraclickListener {

    private LayoutInflater layoutInflater;

    private Context mcontext;

    private List<DeviceRespBean.ResponseBean> data;


    public CameraAdapter(Context context) {
        mcontext = context;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public CameraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.player_item, parent, false);
        return new CameraViewHolder(view, mcontext);
    }

    @Override
    public void onBindViewHolder(CameraViewHolder holder, int position) {
        holder.bindView(data.get(position), position);
        holder.setOnCameraclickListener(this);

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }


    public void reshData(List<DeviceRespBean.ResponseBean> data) {
        this.data = data;
        notifyDataSetChanged();

    }

    public List<DeviceRespBean.ResponseBean> getData() {
        return data;
    }

    @Override
    public void onItem(int index) {
        if (null != data && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (index == i) {
                    data.get(i).setIschoose(true);
                } else {
                    data.get(i).setIschoose(false);
                }
            }
            reshData(data);
        }
    }



}
