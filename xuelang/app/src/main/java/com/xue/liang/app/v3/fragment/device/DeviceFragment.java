package com.xue.liang.app.v3.fragment.device;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.PlayerAdapter;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.bean.device.DeviceReqBean;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;
import com.xue.liang.app.v3.bean.postalarm.PostAlermReq;
import com.xue.liang.app.v3.bean.postalarm.PostAlermResp;
import com.xue.liang.app.v3.config.TestData;
import com.xue.liang.app.v3.event.UrlEvent;
import com.xue.liang.app.v3.fragment.player.PlayerFragment;
import com.xue.liang.app.v3.utils.AlarmTypeConstant;
import com.xue.liang.app.v3.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/11/2.
 */
public class DeviceFragment extends BaseFragment implements DeviceContract.View {
    @BindView(R.id.listview)
    ListView listview;


    private PlayerAdapter playerAdapter;
    private DevicePresenter devicePresenter;

    private List<DeviceRespBean.ResponseBean> dataList;

    @Override
    protected void onFirstUserVisible() {


    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViews() {
        devicePresenter = new DevicePresenter(this);

        setupListView();
        initPlayerFragment();

        onRefreshData();


    }

    private void onRefreshData() {
        String type = DeviceUtil.getWhickPhoneType(getContext());
        DeviceReqBean deviceReqBean = new DeviceReqBean();
        deviceReqBean.setReg_tel(TestData.phoneNum);
        deviceReqBean.setTermi_type(type);
        deviceReqBean.setTermi_unique_code(TestData.termi_unique_code);

        devicePresenter.loadData(deviceReqBean);

    }

    private void setupListView() {
        dataList = new ArrayList<>();

        playerAdapter = new PlayerAdapter(getContext(), dataList);
        listview.setAdapter(playerAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                listview.setItemChecked(position, true);
                String deviceid = dataList.get(position).getDev_id();
                String devicename = dataList.get(position).getDev_name();
                String url = dataList.get(position).getDev_url();
                EventBus.getDefault().post(new UrlEvent(url));

            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_player;
    }

    @Override
    public void onSuccess(DeviceRespBean deviceRespBean) {

        if (deviceRespBean != null) {
            dataList = deviceRespBean.getResponse();
            playerAdapter.reshData(dataList);
        }


    }

    @Override
    public void onFail(DeviceRespBean userInfo) {

    }

    @Override
    public void onPostAlermSuccess(PostAlermResp postAlermResp) {
        Toast.makeText(getActivity(), "报警成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostAlermFail(String msg) {
        Toast.makeText(getActivity(), "报警失败", Toast.LENGTH_SHORT).show();
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

    private void initPlayerFragment() {
        PlayerFragment playerFragment = new PlayerFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.playerFrament, playerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.bt_other_alarm, R.id.bt_hurt_alarm, R.id.bt_theft_alarm, R.id.bt_fire_alarm})
    public void alarm(View view) {

        switch (view.getId()) {
            case R.id.bt_other_alarm:
                sendAlarm(AlarmTypeConstant.OTHER);
                break;
            case R.id.bt_hurt_alarm:
                sendAlarm(AlarmTypeConstant.DANGEROUS_DAMAGE);
                break;
            case R.id.bt_theft_alarm:
                sendAlarm(AlarmTypeConstant.STOLEN_ROB);
                break;
            case R.id.bt_fire_alarm:
                sendAlarm(AlarmTypeConstant.FIFE);
                break;
        }

    }

    private void sendAlarm(int type) {
        PostAlermReq postAlermReq = new PostAlermReq();
        postAlermReq.setTermi_type("2");
        postAlermReq.setAlerm_level(0);
        postAlermReq.setAlerm_type(type);
        postAlermReq.setCam_dev_name("");
        postAlermReq.setCam_dev_uid("");
        postAlermReq.setCam_url("");
        postAlermReq.setStb_car_id("");
        postAlermReq.setStb_id("");
        postAlermReq.setStb_info("");
        postAlermReq.setStb_type(2);
        postAlermReq.setUpdate_time("");
        postAlermReq.setUser_id("");
        devicePresenter.postalarmType(postAlermReq);
    }


}
