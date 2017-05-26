package com.xue.liang.app.v2.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.base.BaseActivity;
import com.xue.liang.app.v2.base.Constant;
import com.xue.liang.app.v2.dialog.SettingFragmentDialog;
import com.xue.liang.app.v2.entity.DeviceEntity;
import com.xue.liang.app.v2.entity.GroupMember6995Entity;
import com.xue.liang.app.v2.entity.SendCall6995Entity;
import com.xue.liang.app.v2.event.UrlEvent;
import com.xue.liang.app.v2.fragment.PlayerFragment;
import com.xue.liang.app.v2.http.HttpResult;
import com.xue.liang.app.v2.http.RetrofitManager;
import com.xue.liang.app.v2.presenter.IMain;
import com.xue.liang.app.v2.presenter.impl.MainPresenterImpl;
import com.xue.liang.app.v2.sadapter.RecyclerAdapter;
import com.xue.liang.app.v2.sadapter.viewholder.RecyclerViewHolder;
import com.xue.liang.app.v2.utils.BusinessCodeUtils;
import com.xue.liang.app.v2.utils.MacUtil;
import com.xue.liang.app.v2.utils.ShareprefUtils;
import com.xue.liang.app.v2.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by jikun on 17/5/19.
 */

public class MainActivity extends BaseActivity<MainPresenterImpl> implements IMain.MianView {

    @BindView(R.id.title_main)
    protected View title_main;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;

    @BindView(R.id.btn_full_sceen_other)
    Button btn_full_sceen_other;


    @BindView(R.id.little_title_tv)
    TextView little_title_tv;


    @BindView(R.id.tv_gundong_info)
    TextView tv_gundong_info;

    @BindView(R.id.btn_people_info)
    Button btn_people_info;


    @BindView(R.id.btn_alarmwarning)
    ImageButton btn_alarmwarning;


    @BindView(R.id.bottom_rl_gundong_info)
    RelativeLayout bottom_rl_gundong_info;

    @BindView(R.id.bottom_rl_all)
    RelativeLayout bottom_rl_all;

    @BindView(R.id.btn_sos)
    Button btn_sos;


    private String phoneormac = "";
    private String useid = "";
    private DeviceEntity deviceEntity;
    private RecyclerAdapter<DeviceEntity> adapter;


    private List<DeviceEntity> deviceEntityList;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.player_activity;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {


        initIpPortData();
        deviceEntityList = new ArrayList<>();
        initRecyclerView();
        startTranslateAnimation();

        getCamerListData();
        initPlayerFragment();

        btn_people_info.setVisibility(View.VISIBLE);
        btn_alarmwarning.setVisibility(View.INVISIBLE);


    }

    public void initIpPortData() {
        Constant.IP = ShareprefUtils.get("key_ip",
                Constant.DEFAULT_IP);
        Constant.PORT = ShareprefUtils.get("key_port",
                Constant.DEFAULT_PORT);
        Constant.is6995Open = ShareprefUtils.get("key_is_6995_open",
                Constant.Default_IS_6995);
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }

    @Override
    protected MainPresenterImpl createPresenter() {
        return new MainPresenterImpl(this);
    }


    private void initPlayerFragment() {

        loadRootFragment(R.id.playerFrament, PlayerFragment.newInstance());

    }


    @Override
    public void getDeviceListSuccess(HttpResult<List<DeviceEntity>> result) {
        phoneormac = ShareprefUtils.get(Constant.Key_Termi_Unique_Code, "");
        useid = result.getUser_tel();
        deviceEntityList.clear();
        deviceEntityList = result.getResponse();
        if (deviceEntityList.isEmpty()) {
            ToastUtils.show("服务器返回的列表为空");
        }
        adapter.refreshWithNewData(deviceEntityList);
    }

    @Override
    public void postAlermSuccess() {

    }

    @Override
    public void post6995AlarmSuccess(SendCall6995Entity entity) {
        if (null != entity) {
            toast("6995" + entity.getResultMsg());
        } else {
            toast("6995报警失败");
        }


    }

    @Override
    public void get6995GroupMemberSuccess(GroupMember6995Entity entity) {
        if (null != entity && null != entity.getSendCall6995Entity()) {
            toast(entity.getSendCall6995Entity().getResultMsg());
        } else {
            toast("获取6995成员失败");
        }

    }

    @Override
    public void add6995GroupMemberSuccess(SendCall6995Entity entity) {
        if (null != entity) {
            toast(entity.getResultMsg());
        } else {
            toast("添加6995成员失败");
        }
    }

    @OnClick({R.id.btn_full_sceen, R.id.btn_full_sceen_other})
    public void fullScreen() {
        boolean isfull = title_main.getVisibility() == View.VISIBLE ? true : false;
        btn_full_sceen_other.setVisibility(isfull ? View.VISIBLE : View.GONE);
        title_main.setVisibility(isfull ? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(isfull ? View.GONE : View.VISIBLE);
        bottom_rl_gundong_info.setVisibility(isfull ? View.GONE : View.VISIBLE);
        bottom_rl_all.setVisibility(isfull ? View.GONE : View.VISIBLE);
        ll_btn.setVisibility(isfull ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.btn_people_info)
    public void toEasyInfoPeopleActivity() {

        String phone = ShareprefUtils.get(Constant.Key_Termi_Unique_Code, "");
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        Intent intent = new Intent();
        intent.setClass(this, EasyInfoPeopleActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }


    @OnClick(R.id.btn_info_notice)
    public void toInfoList() {
        Intent intent = new Intent();
        intent.setClass(this, InfoListActivity.class);
        intent.putExtra("userid", useid);
        intent.putExtra("phoneOrMac", phoneormac);

        startActivity(intent);
    }

    @OnClick(R.id.btn_setting)
    public void showSettingDialog() {
        SettingFragmentDialog settingFragmentDialog = new SettingFragmentDialog();
        settingFragmentDialog.setOnCofimLister(() -> {
            finish();
        });
        settingFragmentDialog.show(getSupportFragmentManager(), SettingFragmentDialog.class.getSimpleName());
    }

    /**
     * 开始跑马灯
     */
    public void startTranslateAnimation() {
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

    public void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new RecyclerAdapter<DeviceEntity>(deviceEntityList, R.layout.player_item) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, DeviceEntity item) {
                holder.setText(R.id.play_item_url_tv, item.getDev_name());

                holder.setSelected(R.id.rl_item_group, item.ischoosed());
                if(item.ischoosed()){
                    holder.requestFocus(R.id.rl_item_group);
                }
               // holder.setFocusable(R.id.rl_item_group, item.ischoosed());
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((position, item) -> {

            deviceEntity = deviceEntityList.get(position);
            for (int i = 0; i < deviceEntityList.size(); i++) {
                if (i == position) {
                    deviceEntityList.get(i).setIschoosed(true);
                } else {
                    deviceEntityList.get(i).setIschoosed(false);
                }

            }
            DeviceEntity entity = deviceEntityList.get(position);
            String url = entity.getDev_url();
            //url = PlayerFragment.path;测试用的URl
            EventBus.getDefault().post(new UrlEvent(url));
            adapter.refreshWithNewData(deviceEntityList);


        });
    }

    @OnClick({R.id.btn_fresh, R.id.btn_fire, R.id.btn_theft, R.id.btn_hurt, R.id.btn_other})
    public void Call110(View view) {


        int type = 0;
        switch (view.getId()) {
            case R.id.btn_fresh:
                getCamerListData();
                break;
            case R.id.btn_fire:
                type = 102;//火警
                do6995AlarmOrNormalAlarm(type);
                break;
            case R.id.btn_theft:
                type = 103;//盗抢
                do6995AlarmOrNormalAlarm(type);
                break;
            case R.id.btn_hurt:
                type = 104;//伤害
                do6995AlarmOrNormalAlarm(type);
                break;
            case R.id.btn_other:

                type = 9;//伤害
                do6995AlarmOrNormalAlarm(type);
                break;
        }

    }


    private void do6995AlarmOrNormalAlarm(int type) {
        if (null == deviceEntity) {
            ToastUtils.show("请选择一个摄像头");
            return;
        }
        boolean is6995 = ShareprefUtils.get("key_is_6995_open", false);
        if (is6995) {
            show6995Dialog();
        } else {
            showAlarmDialog(type);
        }
    }

    private void showAlarmDialog(int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("是否发起报警");
        builder.setPositiveButton("确定", (dialog, which) -> postAlarm(type));
        builder.setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }


    int chooseInt = 0;

    private void show6995Dialog() {
        String[] item = {"语音求助", "短信求助", "电话求助"};
        chooseInt = 1;
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("是否通过6995")
                //* 第一个参数指定我们要显示的一组下拉单选框的数据集合
                //* 第二个参数代表索引，指定默认哪一个单选框被勾选上.
                //* 第三个参数给每一个单选项绑定一个监听器
                .setSingleChoiceItems(item, 1, (dialog, which) -> chooseInt = which)
                .setPositiveButton("确定", (dialog, which) -> {
                    //1 语音报警
                    //2 短信报警
                    //3语音报警
                    mPresenter.get6995sendCall(phoneormac, chooseInt);//语音报警
                    postAlarm(9);
                    //火灾


                }).setNegativeButton("取消", (dialog, which) -> {

        }).show();


    }

    public void postAlarm(int type) {

        mPresenter.getPostalerm(phoneormac, type + "", useid, deviceEntity);

    }

    private void getCamerListData() {
        deviceEntity = null;
        String bussinessCode = BusinessCodeUtils.getValue(getApplicationContext(), BusinessCodeUtils.USER_ID);//业务ID
        String lineMac = MacUtil.getWiredMacAddr();//有线MAC地址
        String wifiMac = MacUtil.getWifiMacAddress(getApplicationContext());//无线MAC地址
        mPresenter.getDeviceCameraList(bussinessCode, lineMac, wifiMac);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RetrofitManager.getInstance().destory();
    }
}
