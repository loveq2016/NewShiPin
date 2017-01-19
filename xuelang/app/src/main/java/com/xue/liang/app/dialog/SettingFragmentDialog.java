package com.xue.liang.app.dialog;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;


import android.content.Context;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.common.Config;
import com.xue.liang.app.utils.DefaultData;
import com.xue.liang.app.utils.ShareKey;
import com.xue.liang.app.utils.SharedDB;

public class SettingFragmentDialog extends DialogFragment {
    private EditText et_ip;
    private EditText et_port;
    private EditText et_mac;
    private Button bt_mac;
    private Button bt_rest;
    private Button bt_confim;
    private Button bt_cancle;
    private CheckBox cb_6995;

    private String ip;
    private String port;
    private String mac;

    private Context mContext;

    private onCofimLister onCofimLister;

    private boolean is6995Open=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.setting_dialog_fragment,
                container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = getActivity().getApplicationContext();

        getDialog().getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView(view);
        initViewData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {

        et_ip = (EditText) view.findViewById(R.id.et_ip);
        et_port = (EditText) view.findViewById(R.id.et_port);
        et_mac = (EditText) view.findViewById(R.id.et_mac);
        bt_mac = (Button) view.findViewById(R.id.bt_mac);
        bt_rest = (Button) view.findViewById(R.id.bt_rest);
        bt_confim = (Button) view.findViewById(R.id.bt_confim);
        bt_cancle = (Button) view.findViewById(R.id.bt_cancle);
        cb_6995= (CheckBox) view.findViewById(R.id.cb_6995);

        bt_mac.setOnClickListener(clickListener);
        bt_rest.setOnClickListener(clickListener);
        bt_confim.setOnClickListener(clickListener);
        bt_cancle.setOnClickListener(clickListener);
        cb_6995.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is6995Open=b;
            }
        });

    }

    private void initViewData() {
        ip = SharedDB.getStringValue(mContext, ShareKey.IP_KEY,
                DefaultData.Default_IP);
        port = SharedDB.getStringValue(mContext, ShareKey.PORT_KEY,
                DefaultData.Default_Port);
        mac = SharedDB.getStringValue(mContext, ShareKey.MAC_KEY,
                DefaultData.Default_Mac);

        is6995Open=SharedDB.getBooleanValue(mContext, ShareKey.IS_6995_KEY,
                false);
        Log.d("测试代码", "测试代码initViewData()===" + ip);
        et_ip.setText(ip);
        et_port.setText(port);
        et_mac.setText(mac);
        cb_6995.setChecked(is6995Open);
    }

    /**
     * 重置数据
     */
    private void restData() {
        SharedDB.putStringValue(mContext, ShareKey.IP_KEY,
                DefaultData.Default_IP);
        SharedDB.putStringValue(mContext, ShareKey.PORT_KEY,
                DefaultData.Default_Port);
        SharedDB.putStringValue(mContext, ShareKey.MAC_KEY,
                DefaultData.Default_Mac);
        SharedDB.putBooleanValue(mContext, ShareKey.IS_6995_KEY,
                DefaultData.Default_IS_6995_FALSE);
        Log.d("测试代码", "测试代码restData()===" + ip);
        et_ip.setText(DefaultData.Default_IP);
        et_port.setText(DefaultData.Default_Port);
        et_mac.setText(DefaultData.Default_Mac);
        cb_6995.setChecked(DefaultData.Default_IS_6995_FALSE);

    }

    private void getLocalMac() {
        String mac = getMac();
        if (TextUtils.isEmpty(mac)) {
            Toast.makeText(getActivity(), "获取本机MAC地址失败", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        // SharedDB.putStringValue(mContext, ShareKey.MAC_KEY, mac);
        et_mac.setText(mac);
    }

    private void confim() {
        ip = et_ip.getText().toString().trim();
        port = et_port.getText().toString().trim();
        mac = et_mac.getText().toString().trim();
        Toast.makeText(getActivity(), "修改了设置参数", Toast.LENGTH_SHORT).show();
        Log.d("测试代码", "测试代码--confim=" + ip);
        SharedDB.putStringValue(mContext, ShareKey.IP_KEY, ip);
        SharedDB.putStringValue(mContext, ShareKey.PORT_KEY, port);
        SharedDB.putStringValue(mContext, ShareKey.MAC_KEY, mac);
        SharedDB.putBooleanValue(mContext, ShareKey.IS_6995_KEY, is6995Open);
        Config.IP = ip;
        Config.PORT = port;
        //Config.Mac = mac;
        if(onCofimLister!=null){
            onCofimLister.onSuccess();
        }
        dismissAllowingStateLoss();

    }

    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.bt_mac:
                    getLocalMac();
                    break;
                case R.id.bt_rest:
                    restData();
                    break;
                case R.id.bt_confim:
                    confim();
                    break;
                case R.id.bt_cancle:
                    dismissAllowingStateLoss();

                    break;

                default:
                    break;
            }
        }
    };

    private String getMac() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    public interface onCofimLister {
        void onSuccess();
    }

    public void setOnCofimLister(SettingFragmentDialog.onCofimLister onCofimLister) {
        this.onCofimLister = onCofimLister;
    }
}
