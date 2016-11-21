package com.xue.liang.app.v3.activity.alarmprocess;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.alarm.AlarmRespBean;

import butterknife.BindView;

public class AlarmProcessDeatialActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_image)
    ImageView iv_image;


    @BindView(R.id.iv_map)
    ImageView iv_map;

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

    private AlarmRespBean.ResponseBean bean;

    @Override
    protected void getBundleExtras(Bundle extras) {
        bean = extras.getParcelable("process");

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_alarm_process_deatial;
    }

    @Override
    protected void initViews() {
        tv_title.setText(getResources().getString(R.string.alarm_process_deatial));
        setViewByData();

    }

    private void setViewByData() {
        if(bean==null){
            return;
        }
        tv_alarm_name.setText("报警人:" + bean.getUser_name());
        tv_alarm_type.setText("报警类型:" + bean.getAlarm_type());
        tv_alarm_time.setText("报警时间:" + bean.getAlarm_time());
        tv_alarm_phone.setText("联系电话:" + bean.getUser_tel());
        tv_alarm_address.setText("报警地址:" + bean.getUser_address());
    }
}
