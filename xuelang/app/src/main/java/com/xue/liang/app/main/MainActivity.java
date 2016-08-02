package com.xue.liang.app.main;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.DeviceListResp;
import com.xue.liang.app.data.reponse.DeviceListResp.DeviceItem;
import com.xue.liang.app.data.request.DeviceListReq;
import com.xue.liang.app.event.UrlEvent;
import com.xue.liang.app.http.manager.HttpManager;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.listenter.HttpListenter;
import com.xue.liang.app.http.manager.listenter.LoadingHttpListener;
import com.xue.liang.app.main.adapter.PlayerAdapter;
import com.xue.liang.app.player.PlayerFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


@EActivity(R.layout.player_activity)
public class MainActivity extends FragmentActivity {


    @ViewById(R.id.listview)
    ListView listview;

    private PlayerAdapter playerAdapter;

    private List<DeviceItem> deviceItemList = new ArrayList<>();

    @AfterViews
    public void initView() {
        initFragment();
        initAdapter();
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
                String url1 = "rtsp://218.200.202.144:556/HongTranSvr?DevId=fdc382e7-bb5b-4e36-bbd1-c28060c7fdb4&Session=fdc382e7-bb5b-4e36-bbd1-c28060c7fdb4&Url=\"rtsp://admin:admin12345@117.139.27.44:554/h264/ch1/main/av_stream\"";
                EventBus.getDefault().post(new UrlEvent(url1));

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
}
