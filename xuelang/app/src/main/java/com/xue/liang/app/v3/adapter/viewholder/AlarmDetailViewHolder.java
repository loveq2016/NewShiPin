package com.xue.liang.app.v3.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xue.liang.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/22.
 */

public class AlarmDetailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_info)
    TextView tv_info;

    public AlarmDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(String info) {
        tv_info.setText(info);
    }
}
