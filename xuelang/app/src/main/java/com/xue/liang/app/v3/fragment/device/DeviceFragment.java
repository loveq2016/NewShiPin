package com.xue.liang.app.v3.fragment.device;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.RegionAdapter;
import com.xue.liang.app.v3.application.MainApplication;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.bean.ServerInfoBean;
import com.xue.liang.app.v3.bean.device.DeviceReqBean;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.bean.postalarm.PostAlermReq;
import com.xue.liang.app.v3.bean.postalarm.PostAlermResp;
import com.xue.liang.app.v3.bean.region.RegionRespBean;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpReq;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpResp;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.constant.CarmIdConstant;
import com.xue.liang.app.v3.constant.LoginInfoUtils;
import com.xue.liang.app.v3.constant.UserType;
import com.xue.liang.app.v3.event.RegionAreasEvent;
import com.xue.liang.app.v3.event.RegionCamraEvent;
import com.xue.liang.app.v3.event.UrlEvent;
import com.xue.liang.app.v3.fragment.help.HelpContract;
import com.xue.liang.app.v3.fragment.help.HelpPresenter;
import com.xue.liang.app.v3.fragment.player.PlayerFragment;
import com.xue.liang.app.v3.location.AMapLocationHelper;
import com.xue.liang.app.v3.utils.AlarmTypeConstant;
import com.xue.liang.app.v3.utils.Constant;
import com.xue.liang.app.v3.utils.DateUtil;
import com.xue.liang.app.v3.utils.DeviceUtil;
import com.xue.liang.app.v3.utils.XPermissionUtils;
import com.xue.liang.app.v3.widget.SettingFragmentDialog;
import com.xue.liang.app.v3.widget.ShowGirdFragmentDialog;
import com.xue.liang.app.v3.widget.UpdateFileFragmentDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Administrator on 2016/11/2.
 */
public class DeviceFragment extends BaseFragment implements DeviceContract.View, HelpContract.View, AMapLocationHelper.OnLocationGetListener {


    public static final String TAG = DeviceFragment.class.getSimpleName();

    public static final int CARMERA = 1;//拍照

    public static final int VIDEO = 2;//录像

    public static final String Bundle_Data = "LoginData";

    @BindView(R.id.rl_ptz)
    RelativeLayout rl_ptz;


    @BindView(R.id.little_title_tv)
    TextView little_title_tv;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private DevicePresenter devicePresenter;

    private HelpPresenter helpPresenter;

    private MaterialDialog updateFileMaterialDialog;

    private List<DeviceRespBean.ResponseBean> dataList;

    private DeviceRespBean.ResponseBean deviceInfo;

    private LoginRespBean mloginRespBean;

    private String mCurrentCamerId = "";

    private String mac = "";

    private RegionAreasEvent regionAreasEvent;

    private AMapLocationHelper aMapLocationHelper;

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

    /**
     * 初始化PTZ控制UI 只有管理用户才能显示云台控制按钮
     */
    private void setUpPztUi() {
        if (mloginRespBean.getUser_type() == UserType.USER_TYPE_NOMAL) {
            rl_ptz.setVisibility(View.GONE);
        } else {
            rl_ptz.setVisibility(View.VISIBLE);
        }

    }


    public void setUpRecyclerView() {
        regionAdapter = new RegionAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(regionAdapter);
    }

    @Override
    protected void initViews() {

        setUpLocation();
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        if (null != bundle) {
            mloginRespBean = bundle.getParcelable(Bundle_Data);
        }

        devicePresenter = new DevicePresenter(this);

        helpPresenter = new HelpPresenter(this);


        initPlayerFragment();
        mac = DeviceUtil.getMacAddress(getActivity().getApplicationContext());


        devicePresenter.getRegion(mloginRespBean.getUser_id());
        setUpPztUi();
        setUpRecyclerView();

        little_title_tv.setText(mloginRespBean.getGroup_name());


    }


    private void setUpLocation() {
        int time = 60 * 1000;
        aMapLocationHelper = new AMapLocationHelper(getContext());
        aMapLocationHelper.setOnLocationGetListener(this);
        aMapLocationHelper.startLocation(time);
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


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_device;
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
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String cameraID = mCurrentCamerId;
                if (mloginRespBean != null) {
                    devicePresenter.stopPtzCmd(cameraID, mloginRespBean);
                }
            }
        }, 1000);


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
        PlayerFragment playerFragment = PlayerFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.playerFrament, playerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.bt_other_alarm, R.id.bt_hurt_alarm, R.id.bt_theft_alarm, R.id.bt_fire_alarm})
    public void alarm(View view) {

        switch (view.getId()) {
            case R.id.bt_other_alarm:
                showAlermDialog(AlarmTypeConstant.OTHER);
                break;
            case R.id.bt_hurt_alarm:
                showAlermDialog(AlarmTypeConstant.DANGEROUS_DAMAGE);

                break;
            case R.id.bt_theft_alarm:
                showAlermDialog(AlarmTypeConstant.STOLEN_ROB);

                break;
            case R.id.bt_fire_alarm:
                showAlermDialog(AlarmTypeConstant.FIFE);
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
            if (aMapLocationHelper.getLastKnownLocation() != null) {
                postAlermReq.setLatitude(aMapLocationHelper.getLastKnownLocation().getLatitude());
                postAlermReq.setLongitude(aMapLocationHelper.getLastKnownLocation().getLongitude());
            }
            devicePresenter.postalarmType(postAlermReq);
        } else {
            showToast("请选择一个摄像头");
        }

    }


    @OnTouch({R.id.btn_ptz_left, R.id.btn_ptz_up, R.id.btn_ptz_right, R.id.btn_ptz_down})
    public boolean ptz(View view, MotionEvent event) {


        switch (view.getId()) {
            case R.id.btn_ptz_left:
                sendPtz(CarmIdConstant.CARM_ID_LEFT, event);
                break;
            case R.id.btn_ptz_up:
                sendPtz(CarmIdConstant.CARM_ID_UP, event);
                break;
            case R.id.btn_ptz_right:
                sendPtz(CarmIdConstant.CARM_ID_RIGHT, event);
                break;
            case R.id.btn_ptz_down:
                sendPtz(CarmIdConstant.CARM_ID_DOWN, event);
                break;
            default:
                break;
        }
        return true;

    }

    private void sendPtz(int cmdzl, MotionEvent event) {


        if (mloginRespBean != null && !TextUtils.isEmpty(mCurrentCamerId)) {


            String cameraID = mCurrentCamerId;

            switch (event.getAction()) {


                case MotionEvent.ACTION_DOWN:
                    devicePresenter.startPtzCmd(cmdzl, cameraID, mloginRespBean);
                    break;

                case MotionEvent.ACTION_UP:
                    // devicePresenter.stopPtzCmd(cameraID,mloginRespBean);

                    break;

            }


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


    private String videoFilePath;

    @OnClick(R.id.bt_video)
    public void openVideo() {
        XPermissionUtils.requestPermissions(getActivity(), 1, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory() + "/Video");
                if (!file.exists()) {
                    file.mkdirs();
                }
                videoFilePath = Environment.getExternalStorageDirectory() + "/Video/" +
                        "VideoImg" + String.valueOf(System.currentTimeMillis()) + ".mp4";
                Uri mUri = Uri.fromFile(
                        new File(videoFilePath));
                videoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);
                videoIntent.putExtra("return-data", true);
                startActivityForResult(videoIntent, VIDEO);

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
            bean.setResource_type("1");
            bean.setAlarm_text("");
            bean.setTermi_type("2");
            bean.setUser_id(LoginInfoUtils.getInstance().getLoginRespBean().getUser_id());
            helpPresenter.updateFileAndAlarm(fileList, bean);

        } else if (requestCode == VIDEO && resultCode == Activity.RESULT_OK) {
            List<String> fileList = new ArrayList<>();
            //Uri uri = data.getParcelableExtra(android.provider.MediaStore.EXTRA_OUTPUT);

            fileList.add(videoFilePath);

            AlarmForHelpReq bean = new AlarmForHelpReq();
            bean.setResource_type("2");
            bean.setAlarm_text("");
            bean.setTermi_type("2");
            bean.setUser_id(LoginInfoUtils.getInstance().getLoginRespBean().getUser_id());
            helpPresenter.updateFileAndAlarm(fileList, bean);
        }
    }

    @OnClick(R.id.bt_sos)
    public void sendSosAlarm() {

        UpdateFileFragmentDialog updateFileFragmentDialog = new UpdateFileFragmentDialog();
        updateFileFragmentDialog.show(getFragmentManager(), UpdateFileFragmentDialog.class.getSimpleName());
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

    @OnClick(R.id.bt_show_gird)
    public void showgird() {
        ShowGirdFragmentDialog showGirdFragmentDialog = new ShowGirdFragmentDialog();
        showGirdFragmentDialog.show(getFragmentManager(), "dev");

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
        Toast.makeText(getActivity(), "上传文件失败" + errorinfo, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpdateFileSuccess(List<String> fileList) {
        dimissUpdateProgressDialog();
        Toast.makeText(getActivity(), "上传文件成功", Toast.LENGTH_SHORT).show();

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
        aMapLocationHelper.destroyLocation();
        String cameraID = mCurrentCamerId;
        if (mloginRespBean != null) {
            devicePresenter.stopPtzCmd(cameraID, mloginRespBean);
        }
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

        mCurrentCamerId = deviceInfo.getCamera_info().getCamera_id();
        String devicename = deviceInfo.getDev_name();
        String url = deviceInfo.getDev_url();
        EventBus.getDefault().post(new UrlEvent(TAG, url));

    }


    private Map<String, Boolean> map;

    boolean[] testboolean = {true, true, true};
    String[] teststring = {"电话求助", "短信求助", "语音求助"};

    boolean ischose = true;

    private void showAlermDialog(final int type) {
        if (null == deviceInfo) {
            showToast("请选择一个摄像头");
            return;
        }

        String[] titles = {"电话求助"};
        boolean[] chooses = {true};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("是否通过6995");


        builder.setMultiChoiceItems(titles, chooses, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                ischose = isChecked;
            }

        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("测试代码", "测试代码确定---which=" + which);


                if (ischose) {
                    checkCallPermissions();//拨打电话
                }
                sendAlarm(type);
//                if (map.get("电话求助")) {
//                    checkCallPermissions();//拨打电话
//                }
//                if (map.get("短信求助")) {
//                    //mainPresenter.sendCall(mphoneNum,2);//短信报警
//                }
//                if (map.get("语音求助")) {
//                    //mainPresenter.sendCall(mphoneNum,1);//语音报警
//                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    /**
     * API23 6.0需要检测权限
     */
    private void checkCallPermissions() {

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }


        } else {

            doCall6995();

        }

    }

    private void doCall6995() {
        String number = "6995";
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);

    }

    @Override
    public void onLocationGetSuccess(AMapLocation loc) {
        Log.e("测试代码", "测试代码onLocationGetSuccess" + "Latitude=" + loc.getLatitude() + "Longitude=" + loc.getLongitude());
    }

    @Override
    public void onLocationGetFail(AMapLocation loc) {
        Log.e("测试代码", "测试代码onLocationGetFail" + "Latitude=");
    }

    ServInfo servInfo;

    private boolean startPtz(String cameraID) {


        final int[] cmdIDs = {1, 2, 3, 4, 11, 12, 13, 14, 7, 8, 9, 10};
        String[] datas =
                {"云台转上", "云台转下", "云台转左", "云台转右", "云台左上", "云台右上", "云台左下", "云台右下", "镜头拉近", "镜头拉远", "镜头近焦", "镜头远焦"};

        ServerInfoBean serverInfoBean = mloginRespBean.getServer_info();
        String loginIP = mloginRespBean.getServer_info().getServer_ip(); //云台控制的服务器IP
        int loginPort = mloginRespBean.getServer_info().getServer_port(); //云台控制的服务器port
        String userName = serverInfoBean.getLogin_name();//云台控制的用户名
        String password = serverInfoBean.getLogin_pass();//云台控制的密码

        servInfo = new ServInfo();
        String macAddress = DeviceUtil.getMacAddress(MainApplication.getInstance().getApplicationContext());
        boolean issuccess = VMSNetSDK.getInstance().login("https://" + loginIP, userName, password, macAddress, servInfo);

        int errorCode = VMSNetSDK.getInstance().getLastErrorCode();
        String errorDesc = VMSNetSDK.getInstance().getLastErrorDesc();


        Log.e("测试代码", "测试代码issuccess:" + issuccess + ",errorCode:" + errorCode + ",errorDesc:" + errorDesc);

        String sessionId = servInfo.getSessionID();


        boolean testissuccess = VMSNetSDK.getInstance().sendStartPTZCmd(loginIP, loginPort, sessionId, cameraID, 3, 3, 600, 0 + "");
        Log.e("测试代码", "测试代码testissuccess:" + testissuccess + ",errorCode:" + errorCode + ",errorDesc:" + errorDesc);
        return issuccess;
    }
}
