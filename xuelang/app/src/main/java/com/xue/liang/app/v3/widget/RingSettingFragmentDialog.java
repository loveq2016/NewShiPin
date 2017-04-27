package com.xue.liang.app.v3.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.xue.liang.app.v3.utils.Constant;
import com.xue.liang.app.v3.utils.SharedDB;


public class RingSettingFragmentDialog extends DialogFragment {
    private EditText et_ip;
    private EditText et_port;
    private EditText et_username;
    private EditText et_password;

    private Button bt_rest;
    private Button bt_confim;
    private Button bt_cancle;

    private String userName;
    private String password;
    private String ip;
    private int port;


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
        View view = inflater.inflate(R.layout.ring_setting_dialog_fragment,
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

        et_username = (EditText) view.findViewById(R.id.et_username);
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_ip = (EditText) view.findViewById(R.id.et_ip);
        et_port = (EditText) view.findViewById(R.id.et_port);

        bt_rest = (Button) view.findViewById(R.id.bt_rest);
        bt_confim = (Button) view.findViewById(R.id.bt_confim);
        bt_cancle = (Button) view.findViewById(R.id.bt_cancle);

        bt_rest.setOnClickListener(clickListener);
        bt_confim.setOnClickListener(clickListener);
        bt_cancle.setOnClickListener(clickListener);

    }

    private void initViewData() {


        userName = SharedDB.getStringValue(getContext(), Constant.RING_USERNAME, "admin");
        password = SharedDB.getStringValue(getContext(), Constant.RING_PASSWORD, "admin");
        ip = SharedDB.getStringValue(getContext(), Constant.RING_IP, "171.216.84.160");
        port = SharedDB.getIntValue(getContext(), Constant.RING_PORT, 8000);

        et_username.setText(userName);

        et_password.setText(password);

        et_ip.setText(ip);

        et_port.setText(port + "");

    }

    /**
     * 重置数据
     */
    private void restData() {

        userName = "admin";
        password = "admin";
        ip = "171.216.84.160";
        port = 8000;
        saveShareDb(userName, password, ip, port);
        et_username.setText(userName);

        et_password.setText(password);

        et_ip.setText(ip);

        et_port.setText(port + "");


    }


    private void confim() {
        userName = et_username.getText().toString();
        password = et_password.getText().toString();
        ip = et_ip.getText().toString().trim();
        port = Integer.valueOf(et_port.getText().toString().trim());
        Toast.makeText(getActivity(), "修改了设置参数", Toast.LENGTH_SHORT).show();
        saveShareDb(userName, password, ip, port);
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


    public interface onCofimLister {
        void onSuccess();
    }

    public void setOnCofimLister(RingSettingFragmentDialog.onCofimLister onCofimLister) {
        this.onCofimLister = onCofimLister;
    }


    private void saveShareDb(String username, String password, String ip, int port) {

        SharedDB.putStringValue(getContext(), Constant.RING_USERNAME, username);
        SharedDB.putStringValue(getContext(), Constant.RING_PASSWORD, password);
        SharedDB.putStringValue(getContext(), Constant.RING_IP, ip);
        SharedDB.putIntValue(getContext(), Constant.RING_PORT, port);

    }
}
