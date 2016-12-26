package com.xue.liang.app.v3.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.RegionAdapter;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;
import com.xue.liang.app.v3.bean.postalarm.PostAlermResp;
import com.xue.liang.app.v3.bean.region.RegionRespBean;
import com.xue.liang.app.v3.constant.LoginInfoUtils;
import com.xue.liang.app.v3.fragment.device.DeviceContract;
import com.xue.liang.app.v3.fragment.device.DevicePresenter;

import butterknife.BindView;

public class TestActivity extends BaseActivity implements DeviceContract.View {

    DevicePresenter devicePresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private RegionAdapter regionAdapter;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_test2;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        devicePresenter = new DevicePresenter(this);
        setUpRecyclerView();
    }

    public void setUpRecyclerView() {
         regionAdapter = new RegionAdapter(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(regionAdapter);
    }


    public void test(View veiw){
        devicePresenter.getRegion(LoginInfoUtils.getInstance().getLoginRespBean().getUser_id());
    }

    @Override
    public void onSuccess(DeviceRespBean userInfo) {

    }

    @Override
    public void onFail(DeviceRespBean userInfo) {

    }

    @Override
    public void onPostAlermSuccess(PostAlermResp postAlermResp) {

    }

    @Override
    public void onPostAlermFail(String msg) {

    }

    @Override
    public void onPtzCmdSuccess(String msg) {

    }

    @Override
    public void onPtzCmdFail(String msg) {

    }

    @Override
    public void ongetRegionSuccess(RegionRespBean bean) {
        regionAdapter.reshData(bean.getChildList());
    }

    @Override
    public void ongetRegionFail(RegionRespBean bean) {

    }

    @Override
    public void showLoadingView(String msg) {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void onError(String info) {

    }
}
