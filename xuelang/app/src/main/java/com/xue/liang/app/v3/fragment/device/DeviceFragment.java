package com.xue.liang.app.v3.fragment.device;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.bean.postalarm.PostAlermReq;
import com.xue.liang.app.v3.bean.postalarm.PostAlermResp;
import com.xue.liang.app.v3.config.TestData;
import com.xue.liang.app.v3.constant.CarmIdConstant;
import com.xue.liang.app.v3.event.UrlEvent;
import com.xue.liang.app.v3.fragment.player.PlayerFragment;
import com.xue.liang.app.v3.utils.AlarmTypeConstant;
import com.xue.liang.app.v3.utils.Constant;
import com.xue.liang.app.v3.utils.DateUtil;
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


    public static final String TAG = DeviceFragment.class.getSimpleName();

    public static final String Bundle_Data = "LoginData";
    @BindView(R.id.listview)
    ListView listview;


    private PlayerAdapter playerAdapter;
    private DevicePresenter devicePresenter;

    private List<DeviceRespBean.ResponseBean> dataList;

    private DeviceRespBean.ResponseBean deviceInfo;

    private LoginRespBean mloginRespBean;

    private String mCurrentCamerId = "";

    private String mac = "";

    @Override
    protected void onFirstUserVisible() {


    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    public static DeviceFragment newInstance(LoginRespBean loginRespBean) {
        DeviceFragment deviceFragment = new DeviceFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Bundle_Data, loginRespBean);
        deviceFragment.setArguments(bundle);
        return deviceFragment;

    }

    @Override
    protected void initViews() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            mloginRespBean = bundle.getParcelable(Bundle_Data);
        }

        devicePresenter = new DevicePresenter(this);

        setupListView();
        initPlayerFragment();
        mac = DeviceUtil.getMacAddress(getActivity().getApplicationContext());

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

                deviceInfo = dataList.get(position);
                listview.setItemChecked(position, true);
                String deviceid = dataList.get(position).getDev_id();
                mCurrentCamerId = deviceid;
                String devicename = dataList.get(position).getDev_name();
                String url = dataList.get(position).getDev_url();
                EventBus.getDefault().post(new UrlEvent(TAG,url));

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
        showToast(postAlermResp.getRet_string());
    }

    @Override
    public void onPostAlermFail(String msg) {
        showToast(msg);

    }

    @Override
    public void onPtzCmdSuccess(String msg) {
        showToast("云台控制成功");

    }

    @Override
    public void onPtzCmdFail(String msg) {
        showToast("云台控制失败");
    }

    @Override
    public void showLoadingView(String msg) {

        showProgressDialog("请求中", "请等待");
    }

    @Override
    public void hideLoadingView() {
        dimissProgressDialog();
    }

    @Override
    public void onError(String info) {

    }

    private void initPlayerFragment() {
        PlayerFragment playerFragment = PlayerFragment.getInstance(TAG);
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
        if (deviceInfo != null) {
            PostAlermReq postAlermReq = new PostAlermReq();
            postAlermReq.setTermi_type(Constant.PHONE);
            postAlermReq.setAlerm_level(0);//是 告警级别 0~5
            postAlermReq.setAlerm_type(type);
            postAlermReq.setCam_dev_name(deviceInfo.getDev_name());
            postAlermReq.setCam_dev_uid(deviceInfo.getDev_id());
            postAlermReq.setCam_url(deviceInfo.getDev_url());
            postAlermReq.setStb_id(mac);//设置MAC地址
            postAlermReq.setStb_info("");
            postAlermReq.setStb_type(Integer.valueOf(Constant.PHONE));
            postAlermReq.setUpdate_time(DateUtil.getCurrentTime());
            postAlermReq.setUser_id(mloginRespBean.getUser_id());
            devicePresenter.postalarmType(postAlermReq);
        } else {
            showToast("请选择一个摄像头");
        }

    }

    @OnClick({R.id.btn_ptz_left, R.id.btn_ptz_up, R.id.btn_ptz_right, R.id.btn_ptz_down})
    public void ptz(View view) {
        switch (view.getId()) {
            case R.id.btn_ptz_left:
                sendPtz(CarmIdConstant.CARM_ID_LEFT);
                break;
            case R.id.btn_ptz_up:
                sendPtz(CarmIdConstant.CARM_ID_UP);
                break;
            case R.id.btn_ptz_right:
                sendPtz(CarmIdConstant.CARM_ID_RIGHT);
                break;
            case R.id.btn_ptz_down:
                sendPtz(CarmIdConstant.CARM_ID_DOWN);
                break;
            default:
                break;
        }

    }

    private void sendPtz(int cmdzl) {


        if (mloginRespBean != null && !TextUtils.isEmpty(mCurrentCamerId)) {

            String sessionID = mloginRespBean.getAlias_id();
            String cameraID = mCurrentCamerId;
            int cmdID = cmdzl;
            int param1 = 1;//速度

            devicePresenter.startPtzCmd(sessionID, cameraID, cmdID, param1, 0);

        } else {
            showToast("请选择一个摄像头");
        }


    }


}
