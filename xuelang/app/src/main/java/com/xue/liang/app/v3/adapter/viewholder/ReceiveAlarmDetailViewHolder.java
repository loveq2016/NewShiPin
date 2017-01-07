package com.xue.liang.app.v3.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.bean.alarm.AlarmDetailRespBean;
import com.xue.liang.app.v3.utils.NumberChangeToChinese;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/22.
 */

public class ReceiveAlarmDetailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_receiv_name)
    TextView tv_receiv_name;


    @BindView(R.id.tv_receiv_tel)
    TextView tv_receiv_tel;

    @BindView(R.id.tv_receiv_type)
    TextView tv_receiv_type;

    @BindView(R.id.tv_receiv_group_name)
    TextView tv_receiv_group_name;

    @BindView(R.id.tv_receiv_detail)
    TextView tv_receiv_detail;

    @BindView(R.id.tv_receiv_time)
    TextView tv_receiv_time;
    public ReceiveAlarmDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(AlarmDetailRespBean.ReceivBean bean,int position){
        NumberChangeToChinese numToChinese = new NumberChangeToChinese();
        String index=numToChinese.numberToChinese(position+1);
        tv_receiv_name.setText("第"+index+"接警人姓名:"+bean.getReceiv_name());
        tv_receiv_tel.setText("电话:"+bean.getReceiv_tel());
        tv_receiv_type.setText("");
        tv_receiv_group_name.setText("接警人所属"+bean.getReceiv_group_name());
        tv_receiv_detail.setText("地址:"+bean.getReceiv_detail());
        tv_receiv_time.setText("接警时间"+bean.getReceiv_time());





;

    }
}
