package com.xue.liang.app.v3.activity.alarmprocess;

import android.os.Bundle;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.alarm.AlarmDetailReqBean;
import com.xue.liang.app.v3.bean.alarm.AlarmDetailRespBean;
import com.xue.liang.app.v3.bean.alarm.AlarmRespBean;
import com.xue.liang.app.v3.constant.LoginInfoUtils;
import com.xue.liang.app.v3.utils.Constant;

/**
 * Created by jikun on 2016/12/22.
 */

public class AlreadyProcessingAlarmDetialActivity extends BaseActivity implements AlredyProcessingContract.View {
    private AlredyProcessingPresenter processingPresenter;

    private AlarmRespBean.ResponseBean bean;

    @Override
    protected void getBundleExtras(Bundle extras) {
        bean = extras.getParcelable("process");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_alread_alarm_detail;
    }

    @Override
    protected void initViews() {

        reshData();

    }

    private void reshData() {
        processingPresenter = new AlredyProcessingPresenter(this);
        AlarmDetailReqBean alarmDetailReqBean = new AlarmDetailReqBean();
        alarmDetailReqBean.setTermi_type(Constant.PHONE);
        alarmDetailReqBean.setAlarm_id(bean.getAlarm_id());
        alarmDetailReqBean.setUser_id(LoginInfoUtils.getInstance().getLoginRespBean().getUser_id());

        processingPresenter.getAlreadyAlarmDetail(alarmDetailReqBean);
    }

    @Override
    public void onSuccess(AlarmDetailRespBean bean) {

    }

    @Override
    public void onFail(AlarmDetailRespBean bean) {

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

    }
}
