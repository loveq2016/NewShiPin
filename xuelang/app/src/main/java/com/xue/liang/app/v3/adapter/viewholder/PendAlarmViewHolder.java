package com.xue.liang.app.v3.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.application.MainApplication;
import com.xue.liang.app.v3.base.baseadapter.SectionedRecyclerViewAdapter;
import com.xue.liang.app.v3.bean.alarm.AlarmRespBean;
import com.xue.liang.app.v3.utils.Constant;
import com.xue.liang.app.v3.utils.SharedDB;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/20.
 */

public class PendAlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.tv_alarm_name)
    TextView tv_alarm_name;
    @BindView(R.id.tv_alarm_type)
    TextView tv_alarm_type;
    @BindView(R.id.tv_alarm_time)
    TextView tv_alarm_time;
    @BindView(R.id.tv_alarm_phone)
    TextView tv_alarm_phone;
    @BindView(R.id.tv_alarm_address)
    TextView tv_alarm_address;
    @BindView(R.id.tv_alarm_staus_name)
    TextView tv_alarm_staus_name;


    private AlarmRespBean.ResponseBean mBean;

    private int mSection;
    private int mPosition;
    private Context context;

    SectionedRecyclerViewAdapter.RecyclerItemClickListener recyclerItemClickListener;

    public PendAlarmViewHolder(View itemView) {
        super(itemView);
        this.itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
        context = MainApplication.getInstance().getApplicationContext();
    }

    public void render(AlarmRespBean.ResponseBean bean, int section, int position) {
        mSection = section;
        mPosition = position;
        mBean = bean;
        if (SharedDB.getBooleanValue(context, bean.generateDistinguishId(), false)) {
            setAllTextViewColor(context.getResources().getColor(R.color.grey));
        } else {
            setAllTextViewColor(context.getResources().getColor(R.color.black));
        }
        tv_alarm_name.setText("报警人:" + bean.getUser_name());
        tv_alarm_type.setText("报警类型:" + bean.getAlarm_type_name());
        tv_alarm_time.setText("报警时间:" + bean.getAlarm_time());
        tv_alarm_phone.setText("联系电话:" + bean.getUser_tel());
        tv_alarm_address.setText("报警地址:" + bean.getUser_address());
        tv_alarm_staus_name.setText("报警状态:"+bean.getAlarm_state_name());

    }

    public void setAllTextViewColor(int color) {
        tv_alarm_name.setTextColor(color);
        tv_alarm_type.setTextColor(color);
        tv_alarm_time.setTextColor(color);
        tv_alarm_phone.setTextColor(color);
        tv_alarm_address.setTextColor(color);
        tv_alarm_staus_name.setTextColor(color);

    }

    @Override
    public void onClick(View view) {
        if (recyclerItemClickListener != null) {
            recyclerItemClickListener.onItemClick(view, mSection, mPosition, mBean);
        }

    }

    public SectionedRecyclerViewAdapter.RecyclerItemClickListener getRecyclerItemClickListener() {
        return recyclerItemClickListener;
    }

    public void setRecyclerItemClickListener(SectionedRecyclerViewAdapter.RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }
}
