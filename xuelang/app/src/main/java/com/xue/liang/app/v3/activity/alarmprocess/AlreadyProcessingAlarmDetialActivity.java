package com.xue.liang.app.v3.activity.alarmprocess;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.AlarmDetailAdapter;
import com.xue.liang.app.v3.adapter.ReceivAlarmDetailAdapter;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.base.baseadapter.SectionedSpanSizeLookup;
import com.xue.liang.app.v3.bean.alarm.AlarmDetailReqBean;
import com.xue.liang.app.v3.bean.alarm.AlarmDetailRespBean;
import com.xue.liang.app.v3.bean.alarm.AlarmRespBean;
import com.xue.liang.app.v3.constant.LoginInfoUtils;
import com.xue.liang.app.v3.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jikun on 2016/12/22.
 */

public class AlreadyProcessingAlarmDetialActivity extends BaseActivity implements AlredyProcessingContract.View {


    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.alarm_people_recyclerView)
    RecyclerView alarm_people_recyclerView;

    @BindView(R.id.revice_alarm_recyclerView)
    RecyclerView revice_alarm_recyclerView;

    @BindView(R.id.tv_exera)
    TextView tv_exera;


    private AlredyProcessingPresenter processingPresenter;

    private AlarmRespBean.ResponseBean bean;

    private AlarmDetailAdapter alarmDetailAdapter;

    private ReceivAlarmDetailAdapter receivAlarmDetailAdapter;

    @Override
    protected void getBundleExtras(Bundle extras) {
        bean = extras.getParcelable("process");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_alread_alarm_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tv_title.setText(getResources().getString(R.string.already_alarm_process_deatial));

        setupAlarm_people_recyclerView();
        setupRevice_Alarm_people_recyclerView();
        reshData();

    }


    private void setupAlarm_people_recyclerView() {
        alarmDetailAdapter = new AlarmDetailAdapter(getApplicationContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);

        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(alarmDetailAdapter, gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(lookup);

        alarm_people_recyclerView.setHasFixedSize(true);
        alarm_people_recyclerView.setNestedScrollingEnabled(false);
        alarm_people_recyclerView.setLayoutManager(gridLayoutManager);
        alarm_people_recyclerView.setAdapter(alarmDetailAdapter);

    }

    private void setupRevice_Alarm_people_recyclerView() {
        receivAlarmDetailAdapter = new ReceivAlarmDetailAdapter(getApplicationContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);

        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(receivAlarmDetailAdapter, gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(lookup);

        revice_alarm_recyclerView.setHasFixedSize(true);
        revice_alarm_recyclerView.setNestedScrollingEnabled(false);
        revice_alarm_recyclerView.setLayoutManager(gridLayoutManager);
        revice_alarm_recyclerView.setAdapter(receivAlarmDetailAdapter);

    }

    private void reshData() {
        processingPresenter = new AlredyProcessingPresenter(this);
        AlarmDetailReqBean alarmDetailReqBean = new AlarmDetailReqBean();
        alarmDetailReqBean.setTermi_type(Constant.PHONE);
        alarmDetailReqBean.setAlarm_id(bean.getAlarm_id());
        alarmDetailReqBean.setUser_id(LoginInfoUtils.getInstance().getLoginRespBean().getUser_id());

        processingPresenter.getAlreadyAlarmDetail(alarmDetailReqBean);
    }

    @OnClick(R.id.bt_back)
    public void back() {
        finish();
    }

    @Override
    public void onSuccess(AlarmDetailRespBean bean) {


        alarmDetailAdapter.reshData(getCallInfoList(bean));

        receivAlarmDetailAdapter.resh(bean.getReceiv_list());

        tv_exera.setText(bean.getAlarm_text());

    }


    public List<String> getCallInfoList(AlarmDetailRespBean bean) {
        List<String> data = new ArrayList<>();
        AlarmDetailRespBean.CallInfoBean callInfoBean = bean.getCall_info();
        if (callInfoBean != null) {

            data.add("报警人姓名:" + callInfoBean.getCall_user_name());
            data.add("电话:" + callInfoBean.getCall_user_tel());
            data.add("报警类型:" + callInfoBean.getCall_alarm_type());
            data.add("报警人所属:" + callInfoBean.getCall_group_name());
            data.add("地址:" + callInfoBean.getCall_user_detail());
            data.add("报警时间:" + callInfoBean.getCall_alarm_time());
        }
        return data;

    }

    @Override
    public void onFail(AlarmDetailRespBean bean) {
        showToast(bean.getRet_string());
    }

    @Override
    public void showLoadingView(String msg) {
        showProgressDialog();

    }

    @Override
    public void hideLoadingView() {
        dimissProgressDialog();

    }

    @Override
    public void onError(String info) {
        showToast(info);

    }
}
