package com.xue.liang.app.v3.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.utils.IpAndPortUtils;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;


public class SettingFragmentDialog extends DialogFragment {
    private EditText et_ip;
    private EditText et_port;
    private EditText et_mac;
    private Button bt_mac;
    private Button bt_rest;
    private Button bt_confim;
    private Button bt_cancle;

    private String ip;
    private int port;
    private String mac;

    private Context mContext;

    private onCofimLister onCofimLister;

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

        bt_mac.setOnClickListener(clickListener);
        bt_rest.setOnClickListener(clickListener);
        bt_confim.setOnClickListener(clickListener);
        bt_cancle.setOnClickListener(clickListener);

    }

    private void initViewData() {
        ip = UriHelper.IP;

        port = UriHelper.PORT;

        et_ip.setText(ip);
        et_port.setText(port + "");
    }

    /**
     * 重置数据
     */
    private void restData() {

        IpAndPortUtils.recoveryIpAndPort(mContext);
        et_ip.setText(UriHelper.IP);
        et_port.setText(UriHelper.PORT + "");


    }

    private void getLocalMac() {
        String mac = getMac();
        if (TextUtils.isEmpty(mac)) {
            Toast.makeText(getActivity(), "获取本机MAC地址失败", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        et_mac.setText(mac);
    }

    private void confim() {
        ip = et_ip.getText().toString().trim();
        port = Integer.valueOf(et_port.getText().toString().trim());
        Toast.makeText(getActivity(), "修改了设置参数", Toast.LENGTH_SHORT).show();
        IpAndPortUtils.saveIpAndPort(mContext, ip, port);
        if (onCofimLister != null) {
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
