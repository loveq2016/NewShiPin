package com.xue.liang.app.v3.fragment.device;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.PlayerAdapter;
import com.xue.liang.app.v3.adapter.RegionAdapter;
import com.xue.liang.app.v3.adapter.viewholder.RegionViewHolder;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.bean.device.DeviceReqBean;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.bean.postalarm.PostAlermReq;
import com.xue.liang.app.v3.bean.postalarm.PostAlermResp;
import com.xue.liang.app.v3.bean.region.RegionRespBean;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpReq;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpResp;
import com.xue.liang.app.v3.constant.CarmIdConstant;
import com.xue.liang.app.v3.constant.LoginInfoUtils;
import com.xue.liang.app.v3.event.RegionAreasEvent;
import com.xue.liang.app.v3.event.RegionCamraEvent;
import com.xue.liang.app.v3.event.UrlEvent;
import com.xue.liang.app.v3.fragment.help.HelpContract;
import com.xue.liang.app.v3.fragment.help.HelpPresenter;
import com.xue.liang.app.v3.fragment.player.PlayerFragment;
import com.xue.liang.app.v3.utils.AlarmTypeConstant;
import com.xue.liang.app.v3.utils.Constant;
import com.xue.liang.app.v3.utils.DateUtil;
import com.xue.liang.app.v3.utils.DeviceUtil;
import com.xue.liang.app.v3.utils.XPermissionUtils;
import com.xue.liang.app.v3.widget.SettingFragmentDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Administrator on 2016/11/2.
 */
public class DeviceFragment extends BaseFragment implements DeviceContract.View, HelpContract.View {


    public static final String TAG = DeviceFragment.class.getSimpleName();

    public static final int CARMERA = 1;//拍照

    public static final String Bundle_Data = "LoginData";
    @BindView(R.id.listview)
    ListView listview;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private PlayerAdapter playerAdapter;
    private DevicePresenter devicePresenter;

    private HelpPresenter helpPresenter;

    private MaterialDialog updateFileMaterialDialog;

    private List<DeviceRespBean.ResponseBean> dataList;

    private DeviceRespBean.ResponseBean deviceInfo;

    private LoginRespBean mloginRespBean;

    private String mCurrentCamerId = "";

    private String mac = "";

    private RegionAreasEvent regionAreasEvent;

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

    private RegionAdapter regionAdapter;

    public void setUpRecyclerView() {
        regionAdapter = new RegionAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(regionAdapter);
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        if (null != bundle) {
            mloginRespBean = bundle.getParcelable(Bundle_Data);
        }

        devicePresenter = new DevicePresenter(this);

        helpPresenter = new HelpPresenter(this);

        setupListView();
        initPlayerFragment();
        mac = DeviceUtil.getMacAddress(getActivity().getApplicationContext());


        devicePresenter.getRegion(mloginRespBean.getUser_id());
        setUpRecyclerView();


    }

    private void onRefreshData(String region_id) {
        String type = DeviceUtil.getWhickPhoneType(getContext());
        DeviceReqBean deviceReqBean = new DeviceReqBean();
        deviceReqBean.setReg_tel(LoginInfoUtils.getInstance().getPhoneNum());
        deviceReqBean.setTermi_type(type);
        deviceReqBean.setRegion_id(region_id);
        deviceReqBean.setTermi_unique_code(LoginInfoUtils.getInstance().getMacAdrress());

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
                EventBus.getDefault().post(new UrlEvent(TAG, url));

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
            regionAreasEvent.getViewHolder().reshCamreData(dataList);

        } else {
            showToast("视频列表为空");
        }


    }

    @Override
    public void onFail(DeviceRespBean userInfo) {
        String info = "";
        if (null != userInfo && !TextUtils.isEmpty(userInfo.getRet_string())) {
            info = userInfo.getRet_string();
        } else {
            info = "获取视频列表失败";
        }
        showToast(info);

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
    public void ongetRegionSuccess(RegionRespBean bean) {
        regionAdapter.reshData(bean.getChildList());

    }

    @Override
    public void ongetRegionFail(RegionRespBean bean) {
        String msg = "";
        if (bean != null && !TextUtils.isEmpty(bean.getRet_string())) {
            msg = bean.getRet_string();
        }
        showToast(msg);
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

    private String imageFilePath;

    @OnClick(R.id.bt_camera)
    public void openCamre() {
        XPermissionUtils.requestPermissions(getActivity(), 1, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory() + "/Images");
                if (!file.exists()) {
                    file.mkdirs();
                }
                imageFilePath = Environment.getExternalStorageDirectory() + "/Images/" +
                        "cameraImg" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                Uri mUri = Uri.fromFile(
                        new File(imageFilePath));
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);
                cameraIntent.putExtra("return-data", true);
                startActivityForResult(cameraIntent, CARMERA);

            }

            @Override
            public void onPermissionDenied() {
                showToast("相机权限被禁止");

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CARMERA && resultCode == Activity.RESULT_OK) {
            List<String> fileList = new ArrayList<>();
            //Uri uri = data.getParcelableExtra(android.provider.MediaStore.EXTRA_OUTPUT);

            fileList.add(imageFilePath);

            AlarmForHelpReq bean = new AlarmForHelpReq();
            bean.setAlarm_text("");
            bean.setTermi_type("2");
            bean.setUser_id(LoginInfoUtils.getInstance().getLoginRespBean().getUser_id());
            helpPresenter.updateFileAndAlarm(fileList, bean);

        }
//        else if (requestCode == VIDEO) {
//            setdataFromVideo(requestCode, resultCode, data);
//        }
    }

    @OnClick(R.id.bt_sos)
    public void sendAlarm() {
        AlarmForHelpReq bean = new AlarmForHelpReq();
        bean.setAlarm_text("");
        bean.setTermi_type("2");
        bean.setUser_id(LoginInfoUtils.getInstance().getLoginRespBean().getUser_id());
        helpPresenter.doAlarmAfterUpdataFile(bean);
    }

    @OnClick(R.id.bt_setting)
    public void showSettingDialog() {

        SettingFragmentDialog msettingFragmentDialog = new SettingFragmentDialog();
        msettingFragmentDialog.setOnCofimLister(new SettingFragmentDialog.onCofimLister() {
            @Override
            public void onSuccess() {
                getActivity().finish();
            }
        });
        msettingFragmentDialog.show(getFragmentManager(),
                "dialog");


    }


    @Override
    public void onUpdateStartFile() {
        showUpdateProgressDialog();

    }

    @Override
    public void onUpdateProgressUpFile(float progress, long total, int id) {
        if (updateFileMaterialDialog != null) {
            int p = (int) (progress * 100);
            updateFileMaterialDialog.setProgress(p);
        }

    }

    @Override
    public void onUpdateFileFail(String errorinfo) {
        dimissUpdateProgressDialog();
        Toast.makeText(getActivity(), "上传图片失败" + errorinfo, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpdateFileSuccess(List<String> fileList) {
        dimissUpdateProgressDialog();
        Toast.makeText(getActivity(), "上传图片成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAlarmSuccess(AlarmForHelpResp resp) {

        Toast.makeText(getActivity(), "报警成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAlarmFail() {
        Toast.makeText(getActivity(), "报警失败", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected void showUpdateProgressDialog() {
        boolean showMinMax = true;
        updateFileMaterialDialog = new MaterialDialog.Builder(getActivity())
                .title("上传中")
                .content("请等待")
                .progress(false, 100, showMinMax)
                .show();
        updateFileMaterialDialog.setCancelable(false);
    }

    protected void dimissUpdateProgressDialog() {
        if (updateFileMaterialDialog != null & updateFileMaterialDialog.isShowing()) {
            updateFileMaterialDialog.dismiss();
        }
    }

    @Subscribe
    public void onEventMainThread(RegionAreasEvent event) {
        regionAreasEvent = event;
        onRefreshData(event.getRegionAreas().getRegion_id());

    }

    @Subscribe
    public void onEventMainThread(RegionCamraEvent event) {
        deviceInfo = event.getResponseBean();

        mCurrentCamerId = deviceInfo.getDev_id();
        String devicename = deviceInfo.getDev_name();
        String url = deviceInfo.getDev_url();
        EventBus.getDefault().post(new UrlEvent(TAG, url));

    }


}
