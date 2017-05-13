package com.xue.liang.app.v2.main;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.alarm.AlarmActivity2_;
import com.xue.liang.app.v2.common.Config;
import com.xue.liang.app.v2.data.reponse.DeviceListResp;
import com.xue.liang.app.v2.data.reponse.DeviceListResp.DeviceItem;
import com.xue.liang.app.v2.data.reponse.SendAlarmResp;
import com.xue.liang.app.v2.data.reponse.UpdateAlarmResp;
import com.xue.liang.app.v2.data.reponse.YiDongAlarmResp;
import com.xue.liang.app.v2.data.request.DeviceListReq;
import com.xue.liang.app.v2.data.request.SendAlarmReq;
import com.xue.liang.app.v2.data.request.UpdateAlarmReq;
import com.xue.liang.app.v2.dialog.SettingFragmentDialog;
import com.xue.liang.app.v2.event.UrlEvent;
import com.xue.liang.app.v2.group.GroupActivity_;
import com.xue.liang.app.v2.http.manager.HttpManager;
import com.xue.liang.app.v2.http.manager.data.HttpReponse;
import com.xue.liang.app.v2.http.manager.listenter.HttpListenter;
import com.xue.liang.app.v2.http.manager.listenter.LoadingHttpListener;
import com.xue.liang.app.v2.info.EasyInfoPeopleActivity_;
import com.xue.liang.app.v2.info.InfoListActivity_;
import com.xue.liang.app.v2.main.adapter.PlayerAdapter;
import com.xue.liang.app.v2.player.PlayerFragment;
import com.xue.liang.app.v2.type.HttpType;
import com.xue.liang.app.v2.utils.BusinessCodeUtils;
import com.xue.liang.app.v2.utils.DefaultData;
import com.xue.liang.app.v2.utils.DeviceUtil;
import com.xue.liang.app.v2.utils.MacUtil;
import com.xue.liang.app.v2.utils.ShareKey;
import com.xue.liang.app.v2.utils.SharedDB;
import com.xue.liang.app.v2.utils.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


@EActivity(R.layout.player_activity)
public class MainActivity extends FragmentActivity implements MainContract.View<YiDongAlarmResp> {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private boolean is6995 = false;

    @ViewById(R.id.title_main)
    protected View title_main;


    @ViewById(R.id.listview)
    ListView listview;


    @ViewById(R.id.ll_btn)
    LinearLayout ll_btn;

    @ViewById(R.id.btn_full_sceen_other)
    Button btn_full_sceen_other;


    @ViewById(R.id.little_title_tv)
    TextView little_title_tv;


    @ViewById(R.id.tv_gundong_info)
    TextView tv_gundong_info;

    @ViewById(R.id.btn_people_info)
    Button btn_people_info;


    @ViewById(R.id.btn_alarmwarning)
    ImageButton btn_alarmwarning;

    @ViewById(R.id.btn_setting)
    Button btn_setting;

    @ViewById(R.id.bottom_rl_gundong_info)
    RelativeLayout bottom_rl_gundong_info;

    @ViewById(R.id.bottom_rl_all)
    RelativeLayout bottom_rl_all;

    @ViewById(R.id.btn_sos)
    Button btn_sos;

    private PlayerAdapter playerAdapter;

    private String mdevicieId;

    private String mphoneNum;

    private List<DeviceItem> deviceItemList = new ArrayList<DeviceItem>();

    private MainPresenter mainPresenter;

    private Map<Integer, Boolean> deviceHttpMAP = new HashMap<>();

    @AfterViews
    public void initView() {
        btn_sos.setVisibility(View.VISIBLE);

        is6995 = SharedDB.getBooleanValue(getApplicationContext(), ShareKey.IS_6995_KEY, DefaultData.Default_IS_6995_FALSE);
        mainPresenter = new MainPresenter(this);
        initFragment();
        initAdapter();
        startGetDeviceList();

        btn_people_info.setVisibility(View.VISIBLE);
        btn_alarmwarning.setVisibility(View.VISIBLE);


        startPaomaDENG();
    }

    @Click(R.id.btn_group)
    public void toGroupActivity() {
        Intent intent = new Intent();
        intent.putExtra("phonenum", mphoneNum);
        intent.setClass(this, GroupActivity_.class);
        startActivity(intent);
    }

    @Click({R.id.btn_full_sceen, R.id.btn_full_sceen_other})
    public void setFullScreen() {
        boolean isfull = title_main.getVisibility() == View.VISIBLE ? true : false;
        if (isfull) {
            btn_full_sceen_other.setVisibility(View.VISIBLE);
            title_main.setVisibility(View.GONE);
            listview.setVisibility(View.GONE);

            bottom_rl_gundong_info.setVisibility(View.GONE);
            bottom_rl_all.setVisibility(View.GONE);

            ll_btn.setVisibility(View.GONE);


        } else {
            btn_full_sceen_other.setVisibility(View.GONE);
            title_main.setVisibility(View.VISIBLE);
            listview.setVisibility(View.VISIBLE);
            bottom_rl_gundong_info.setVisibility(View.VISIBLE);
            bottom_rl_all.setVisibility(View.VISIBLE);
            ll_btn.setVisibility(View.VISIBLE);
        }

    }

    private void initFragment() {
        PlayerFragment playerFragment = new PlayerFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.playerFrament, playerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initAdapter() {
        playerAdapter = new PlayerAdapter(getApplicationContext(),
                deviceItemList);
        listview.setAdapter(playerAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                listview.setItemChecked(position, true);
                String deviceid = deviceItemList.get(position).getDev_id();
                mdevicieId = deviceItemList.get(position).getDev_id();
                String devicename = deviceItemList.get(position).getDev_name();
                String url = deviceItemList.get(position).getDev_url();
                //String url1 = "rtsp://218.200.202.144:556/HongTranSvr?DevId=fdc382e7-bb5b-4e36-bbd1-c28060c7fdb4&Session=fdc382e7-bb5b-4e36-bbd1-c28060c7fdb4&Url=\"rtsp://admin:admin12345@117.139.27.44:554/h264/ch1/main/av_stream\"";
                EventBus.getDefault().post(new UrlEvent(url));

            }
        });


    }

    private class TreeTimeHttpListener implements HttpListenter<DeviceListResp> {
        private static final int TYPE_BUSINESS_CODE = 0;//业务账号模式
        private static final int TYPE_WIRED_MAC_ADDR = 1;//有线MAC模式
        private static final int TYPE_WIFI_MAC_ADDRESS = 2;//无线MAC地址
        private int type = TYPE_BUSINESS_CODE;

        TreeTimeHttpListener(int code) {
            type = code;
        }


        @Override
        public void onFailed(String msg) {
            String info = "";
            switch (type) {
                case TYPE_BUSINESS_CODE:
                    info = "业务账号模式";
                    break;
                case TYPE_WIRED_MAC_ADDR:
                    info = "有线MAC模式";
                    break;
                case TYPE_WIFI_MAC_ADDRESS:
                    info = "无线MAC模式";
                    break;

            }
            Toast.makeText(getApplicationContext(), info + "请求服务器失败:" + msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(HttpReponse<DeviceListResp> httpReponse) {


            if (httpReponse != null && httpReponse.getData() != null && httpReponse.getData().getResponse() != null && httpReponse.getData().getResponse().getArList() != null) {
                deviceItemList = httpReponse.getData().getResponse().getArList();
                playerAdapter.reshData(deviceItemList);
                mphoneNum = httpReponse.getData().getUser_tel();


                String groupname = httpReponse.getData().getResponse().getGroupname();//村名
                if (!TextUtils.isEmpty(groupname))
                    little_title_tv.setText(groupname);
            } else {
                String wiredMac = MacUtil.getWiredMacAddr();//有线MAC地址
                String wifiMac = MacUtil.getWifiMacAddress(getApplicationContext());//无线MAC地址
                switch (type) {
                    case TYPE_BUSINESS_CODE:
                        //当请求模式为TYPE_BUSINESS_CODE
                        if (deviceHttpMAP.get(TreeTimeHttpListener.TYPE_WIRED_MAC_ADDR)) {

                            getDeviceList(getSupportFragmentManager(), new TreeTimeHttpListener(TreeTimeHttpListener.TYPE_WIRED_MAC_ADDR), wiredMac);
                        } else {

                            getDeviceList(getSupportFragmentManager(), new TreeTimeHttpListener(TreeTimeHttpListener.TYPE_WIFI_MAC_ADDRESS), wifiMac);
                        }


                        break;
                    case TYPE_WIRED_MAC_ADDR:
                        getDeviceList(getSupportFragmentManager(), new TreeTimeHttpListener(TreeTimeHttpListener.TYPE_WIFI_MAC_ADDRESS), wifiMac);

                        break;
                    case TYPE_WIFI_MAC_ADDRESS:
                        Toast.makeText(getApplicationContext(), "未能获取列表", Toast.LENGTH_SHORT).show();
                        break;

                }
            }


        }
    }

    public void startGetDeviceList() {
        deviceHttpMAP.clear();

        String bussinessCode = BusinessCodeUtils.getValue(getApplicationContext(), BusinessCodeUtils.USER_ID);//业务ID
        //String bussinessCode="138909919795";
        String wiredMac = MacUtil.getWiredMacAddr();//有线MAC地址
        String wifiMac = MacUtil.getWifiMacAddress(getApplicationContext());//无线MAC地址

        deviceHttpMAP.put(TreeTimeHttpListener.TYPE_BUSINESS_CODE, !TextUtils.isEmpty(bussinessCode));

        deviceHttpMAP.put(TreeTimeHttpListener.TYPE_WIRED_MAC_ADDR, !TextUtils.isEmpty(wiredMac) && wiredMac.equals("02:00:00:00:00:00"));


        deviceHttpMAP.put(TreeTimeHttpListener.TYPE_WIFI_MAC_ADDRESS, !TextUtils.isEmpty(wifiMac) && wifiMac.equals("02:00:00:00:00:00"));

        if (deviceHttpMAP.get(TreeTimeHttpListener.TYPE_BUSINESS_CODE)) {
            getDeviceList(getSupportFragmentManager(), new TreeTimeHttpListener(TreeTimeHttpListener.TYPE_BUSINESS_CODE), bussinessCode);
        } else {

            if (deviceHttpMAP.get(TreeTimeHttpListener.TYPE_WIRED_MAC_ADDR)) {

                getDeviceList(getSupportFragmentManager(), new TreeTimeHttpListener(TreeTimeHttpListener.TYPE_WIRED_MAC_ADDR), wiredMac);
            } else {

                getDeviceList(getSupportFragmentManager(), new TreeTimeHttpListener(TreeTimeHttpListener.TYPE_WIFI_MAC_ADDRESS), wifiMac);
            }

        }


    }


    public void getDeviceList(FragmentManager fragmentManager, HttpListenter listenter, String mac) {


        String url = Config.getDeviceListUrl();
        DeviceListReq deviceListReq = new DeviceListReq(Config.TEST_TYPE, "", mac, null, null);
        //NoticeDetailReq noticeDetailReq = new NoticeDetailReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, id);
        HttpManager.HttpBuilder<DeviceListReq, DeviceListResp> httpBuilder = new HttpManager.HttpBuilder<DeviceListReq, DeviceListResp>();
        httpBuilder.buildRequestValue(deviceListReq)
                .buildResponseClass(DeviceListResp.class)
                .buildUrl(url)
                .buildHttpListenter(listenter)
                .build()
                .dopost("DeviceList");
    }

    @Click(R.id.btn_info_notice)
    public void toInfoListActivity() {
        Intent intent = new Intent();
        intent.setClass(this, InfoListActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.btn_people_info)
    public void toEasyInfoPeopleActivity() {

        Bundle bundle = new Bundle();
        bundle.putString("phone", Config.TEST_PHONE_NUMBER);
        Intent intent = new Intent();
        intent.setClass(this, EasyInfoPeopleActivity_.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Click({R.id.btn_fire, R.id.btn_theft, R.id.btn_hurt, R.id.btn_other})
    public void Call110(View view) {
        switch (view.getId()) {
            case R.id.btn_fresh:
                startGetDeviceList();
                break;
            case R.id.btn_fire:
                alarmDialog(HttpType.FireAlarm);
                break;
            case R.id.btn_theft:
                alarmDialog(HttpType.TheftAlarm);
                break;
            case R.id.btn_hurt:
                alarmDialog(HttpType.HurtAlarm);
                break;
            case R.id.btn_other:
                alarmDialog(HttpType.OtherAlarm);
                break;
        }

    }


    private void alarmDialog(final HttpType type) {
        if (is6995) {
            showAlermDialog(type);
        } else {
            if (DeviceUtil.isPhone(getApplicationContext())) {
                //2为手机
                checkCallPermissions();//拨打电话
            } else {
                //1为机顶盒

            }
            sendAlarm(type.value(), getSupportFragmentManager());
        }

    }


    /**
     * @param alermType       报警类型：101 医疗救助 102火灾 103盗抢 104 伤害
     * @param fragmentManager
     */
    private void sendAlarm(int alermType, FragmentManager fragmentManager) {

        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<SendAlarmResp>() {
            @Override
            public void onFailed(String msg) {
                if (TextUtils.isEmpty(msg)) {
                    msg = "null";
                }

                Toast.makeText(getApplicationContext(), "报警失败--" + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(HttpReponse<SendAlarmResp> httpReponse) {
                Toast.makeText(getApplicationContext(), "报警成功", Toast.LENGTH_SHORT).show();

            }
        }, fragmentManager);

        String url = Config.getSendAlarmUrl();
        //DeviceListReq deviceListReq=new DeviceListReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC,null,null);
        SendAlarmReq sendAlarmReq = new SendAlarmReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, String.valueOf(alermType), mdevicieId);
        //NoticeDetailReq noticeDetailReq = new NoticeDetailReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, id);
        HttpManager.HttpBuilder<SendAlarmReq, SendAlarmResp> httpBuilder = new HttpManager.HttpBuilder<SendAlarmReq, SendAlarmResp>();
        httpBuilder.buildRequestValue(sendAlarmReq)
                .buildResponseClass(SendAlarmResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("SendAlarm");

    }

    @Click(R.id.btn_alarmwarning)
    public void toAlarmActivity() {
        Intent intent = new Intent();
        intent.setClass(this, AlarmActivity2_.class);
        startActivity(intent);
    }

    @Click(R.id.btn_setting)
    public void toSettingDialog() {
        SettingFragmentDialog msettingFragmentDialog = new SettingFragmentDialog();
        msettingFragmentDialog.setOnCofimLister(new SettingFragmentDialog.onCofimLister() {
            @Override
            public void onSuccess() {
                finish();
            }
        });
        msettingFragmentDialog.show(getSupportFragmentManager(),
                "dialog");
    }

    @Click(R.id.btn_sos)
    public void doSendSos() {
        //创建一个AlertDialog对话框
        //Author : 博客园-依旧淡然
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("登录提示")
                .setIcon(R.mipmap.ic_launcher).setMessage("是否SOS求助？").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateAlermHelp(getSupportFragmentManager(), "", new ArrayList<String>());
                            }
                        })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();

        dialog.show();

    }

    /**
     * 开始跑马灯
     */
    public void startPaomaDENG() {
        AnimationSet animationSet = new AnimationSet(true);
        //参数1～2：x轴的开始位置
        //参数3～4：y轴的开始位置
        //参数5～6：x轴的结束位置
        //参数7～8：x轴的结束位置
        TranslateAnimation translateAnimation =
                new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0f,
                        Animation.RELATIVE_TO_PARENT, -1f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f);
        translateAnimation.setDuration(12000);
        translateAnimation.setRepeatMode(TranslateAnimation.RESTART);
        translateAnimation.setRepeatCount(-1);
        animationSet.addAnimation(translateAnimation);


        tv_gundong_info.startAnimation(animationSet);

    }

    private void doCall6995() {
        String number = "6995";
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);

    }

    /**
     * API23 6.0需要检测权限
     */
    private void checkCallPermissions() {
        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CALL_PHONE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            doCall6995();
//
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_CONTACTS},
//                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//
//        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }


        } else {

            doCall6995();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    doCall6995();

                } else {

                    ToastUtil.showToast(getApplicationContext(), "未能获得拨打电话的权限", Toast.LENGTH_SHORT);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onSuccess(YiDongAlarmResp yiDongAlarmResp) {
        Toast.makeText(getApplicationContext(), yiDongAlarmResp.getResultMsg(), Toast.LENGTH_SHORT).show();
        // ToastUtil.showToast(getApplicationContext(), yiDongAlarmResp.getResultMsg(), Toast.LENGTH_SHORT);
    }

    @Override
    public void onFail(String info) {
        //ToastUtil.showToast(getApplicationContext(), info, Toast.LENGTH_LONG);
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();


    }


    private class ChoiceOnClickListener implements DialogInterface.OnMultiChoiceClickListener {


        private int which = 0;
        private boolean isChecked = true;

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            this.which = which;
            this.isChecked = isChecked;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public int getWhich() {
            return which;
        }

        public void setWhich(int which) {
            this.which = which;
        }
    }

    int chooseInt = 0;

    private void showAlermDialog(final HttpType type) {
        chooseInt = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("是否通过6995");
        final String[] item = {"语音求助", "短信求助", "电话求助"};
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        builder.setSingleChoiceItems(item, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chooseInt = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                switch (chooseInt) {
                    case 0:
                        mainPresenter.sendCall(mphoneNum, 1);//语音报警
                        break;
                    case 1:
                        mainPresenter.sendCall(mphoneNum, 2);//短信报警
                        break;
                    case 2:
                        if (DeviceUtil.isPhone(getApplicationContext())) {
                            //2为手机
                            checkCallPermissions();//拨打电话
                        } else {
                            //1为机顶盒
                            mainPresenter.sendCall(mphoneNum, 3);//语音报警
                        }

                        break;
                }
                sendAlarm(type.value(), getSupportFragmentManager());
                //Toast.makeText(MainActivity.this, "为：" + item[chooseInt], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    private void updateAlermHelp(FragmentManager fragmentManager, String content, List<String> fileids) {


        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<UpdateAlarmResp>() {
            @Override
            public void onFailed(String msg) {
                ToastUtil.showToast(getApplicationContext(), "报警失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void onSuccess(HttpReponse<UpdateAlarmResp> httpReponse) {
                if (null != httpReponse && null != httpReponse.getData()) {
                    if (httpReponse.getData().getRet_code() == 0) {
                        ToastUtil.showToast(getApplicationContext(), "报警成功", Toast.LENGTH_SHORT);

                    } else {
                        ToastUtil.showToast(getApplicationContext(), "报警失败ret_code=" + httpReponse.getData().getRet_code(), Toast.LENGTH_SHORT);
                    }
                } else {
                    ToastUtil.showToast(getApplicationContext(), "报警失败", Toast.LENGTH_SHORT);
                }


            }
        }, fragmentManager);

        String url = Config.getUpdateAlarmUrl();


        UpdateAlarmReq updateAlarmReq = new UpdateAlarmReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, content, fileids);
        //NoticeDetailReq noticeDetailReq = new NoticeDetailReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, id);
        HttpManager.HttpBuilder<UpdateAlarmReq, UpdateAlarmResp> httpBuilder = new HttpManager.HttpBuilder<UpdateAlarmReq, UpdateAlarmResp>();
        httpBuilder.buildRequestValue(updateAlarmReq)
                .buildResponseClass(UpdateAlarmResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("UpdateAlarm");
    }

}
