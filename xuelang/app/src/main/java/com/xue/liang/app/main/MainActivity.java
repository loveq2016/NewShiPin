package com.xue.liang.app.main;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.alarm.AlarmActivity_;
import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.DeviceListResp;
import com.xue.liang.app.data.reponse.DeviceListResp.DeviceItem;
import com.xue.liang.app.data.reponse.SendAlarmResp;
import com.xue.liang.app.data.request.DeviceListReq;
import com.xue.liang.app.data.request.SendAlarmReq;
import com.xue.liang.app.event.UrlEvent;
import com.xue.liang.app.http.manager.HttpManager;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.listenter.HttpListenter;
import com.xue.liang.app.http.manager.listenter.LoadingHttpListener;
import com.xue.liang.app.info.EasyInfoPeopleActivity;
import com.xue.liang.app.info.EasyInfoPeopleActivity_;
import com.xue.liang.app.info.InfoListActivity_;
import com.xue.liang.app.main.adapter.PlayerAdapter;
import com.xue.liang.app.player.PlayerFragment;
import com.xue.liang.app.type.HttpType;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


@EActivity(R.layout.player_activity)
public class MainActivity extends FragmentActivity {

    @ViewById(R.id.title_main)
    protected  View title_main;


    @ViewById(R.id.listview)
    ListView listview;


    @ViewById(R.id.bottom_rl)
    RelativeLayout bottom_rl;

    @ViewById(R.id.ll_btn)
    LinearLayout ll_btn;

    @ViewById(R.id.btn_full_sceen_other)
    Button btn_full_sceen_other;

    private PlayerAdapter playerAdapter;

    private List<DeviceItem> deviceItemList = new ArrayList<>();

    @AfterViews
    public void initView() {
        initFragment();
        initAdapter();
    }

    @Click({R.id.btn_full_sceen,R.id.btn_full_sceen_other})
    public void setFullScreen(){
        boolean isfull=title_main.getVisibility()==View.VISIBLE?true:false;
        if(isfull){
            btn_full_sceen_other.setVisibility(View.VISIBLE);
            title_main.setVisibility(View.GONE);
            listview.setVisibility(View.GONE);
            bottom_rl.setVisibility(View.GONE);
            ll_btn.setVisibility(View.GONE);
        }else{
            btn_full_sceen_other.setVisibility(View.GONE);
            title_main.setVisibility(View.VISIBLE);
            listview.setVisibility(View.VISIBLE);
            bottom_rl.setVisibility(View.VISIBLE);
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
                String devicename = deviceItemList.get(position).getDev_name();
                String url = deviceItemList.get(position).getDev_url();
                //String url1 = "rtsp://218.200.202.144:556/HongTranSvr?DevId=fdc382e7-bb5b-4e36-bbd1-c28060c7fdb4&Session=fdc382e7-bb5b-4e36-bbd1-c28060c7fdb4&Url=\"rtsp://admin:admin12345@117.139.27.44:554/h264/ch1/main/av_stream\"";
                EventBus.getDefault().post(new UrlEvent(url));

            }
        });
        getDeviceList(getSupportFragmentManager());

    }

    public void getDeviceList(FragmentManager fragmentManager) {
        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<DeviceListResp>() {
            @Override
            public void onFailed(String msg) {
                Toast.makeText(getApplicationContext(), "请求服务器失败:" + msg, Toast.LENGTH_SHORT).show();
                ;
            }

            @Override
            public void onSuccess(HttpReponse<DeviceListResp> httpReponse) {

                deviceItemList = httpReponse.getData().getResponse();
                playerAdapter.reshData(deviceItemList);
            }
        }, fragmentManager);


        String url = Config.getDeviceListUrl();
        DeviceListReq deviceListReq = new DeviceListReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, null, null);
        //NoticeDetailReq noticeDetailReq = new NoticeDetailReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, id);
        HttpManager.HttpBuilder<DeviceListReq, DeviceListResp> httpBuilder = new HttpManager.HttpBuilder<>();
        httpBuilder.buildRequestValue(deviceListReq)
                .buildResponseClass(DeviceListResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
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
    public void toEasyInfoPeopleActivity(){

        Bundle bundle=new Bundle();
        bundle.putString("url","http://baidu.com");
        Intent intent = new Intent();
        intent.setClass(this, EasyInfoPeopleActivity_.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Click({R.id.btn_fire, R.id.btn_theft, R.id.btn_hurt, R.id.btn_other})
    public void Call110(View view) {
        switch (view.getId()) {
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
        Dialog alertDialog = new AlertDialog.Builder(this).setTitle("确定报警？")
                .setMessage("您确定要报警？").setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        sendAlarm(type.value(), getSupportFragmentManager());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).create();
        alertDialog.show();
    }


    /**
     * @param alermType       报警类型：101 医疗救助 102火灾 103盗抢 104 伤害
     * @param fragmentManager
     */
    private void sendAlarm(int alermType, FragmentManager fragmentManager) {

        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<SendAlarmResp>() {
            @Override
            public void onFailed(String msg) {
                if(TextUtils.isEmpty(msg)){
                    msg="null";
                }

                Toast.makeText(getApplicationContext(),"报警失败--"+msg,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(HttpReponse<SendAlarmResp> httpReponse) {
                 Toast.makeText(getApplicationContext(),"报警成功",Toast.LENGTH_SHORT).show();

            }
        }, fragmentManager);

        String url = Config.getSendAlarmUrl();
        //DeviceListReq deviceListReq=new DeviceListReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC,null,null);
        SendAlarmReq sendAlarmReq = new SendAlarmReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, String.valueOf(alermType), null);
        //NoticeDetailReq noticeDetailReq = new NoticeDetailReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, id);
        HttpManager.HttpBuilder<SendAlarmReq, SendAlarmResp> httpBuilder = new HttpManager.HttpBuilder<>();
        httpBuilder.buildRequestValue(sendAlarmReq)
                .buildResponseClass(SendAlarmResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("SendAlarm");

    }
    @Click(R.id.btn_alarmwarning)
    public void toAlarmActivity(){
        Intent intent = new Intent();
        intent.setClass(this, AlarmActivity_.class);


        startActivity(intent);
    }
}
