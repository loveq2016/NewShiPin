package com.xue.liang.app.v3.fragment.ring;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.activity.ring.RingTermActivity;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.constant.BundleConstant;
import com.xue.liang.app.v3.fragment.alarmprocesse.AlarmProcessFragment;
import com.xue.liang.app.v3.utils.SharedDB;

import butterknife.BindView;
import butterknife.OnClick;
import comtom.com.realtimestream.ComtomFilePlayUtils;
import comtom.com.realtimestream.ComtomSpeak;
import comtom.com.realtimestream.listener.OnConnectServerListener;

/**
 * Created by jikun on 17/4/26.
 */

public class RingLoginFragment extends BaseFragment implements OnConnectServerListener {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_username)
    EditText et_username;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.et_ip)
    EditText et_ip;

    @BindView(R.id.et_port)
    EditText et_port;


    ComtomSpeak comtomSpeak;
    ComtomFilePlayUtils comtomFilePlayUtils;


    public static final String RING_USERNAME = "RingUserName";
    public static final String RING_PASSWORD = "Ring_Password";
    public static final String RING_IP = "Ring_Ip";
    public static final String RING_PORT = "Ring_Port";

    public static RingLoginFragment newInstance() {

        RingLoginFragment ringLoginFragment = new RingLoginFragment();

        return ringLoginFragment;
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViews() {
        tv_title.setText("村村响");
        initSdk();
        getShareDb();

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_ring_login;
    }

    private void initSdk() {

        comtomSpeak = ComtomSpeak.Instance();
        comtomFilePlayUtils = ComtomFilePlayUtils.Instance();

    }

    @Override
    public void connectServerFail() {
        // TODO Auto-generated method stub
        Toast.makeText(getContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.btn_login_ring)
    public void loginRing() {
        // TODO Auto-generated method stub
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String ip = et_ip.getText().toString().trim();
        String port = et_port.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(ip)) {
            Toast.makeText(getContext(), "请输入ip地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(port)) {
            Toast.makeText(getContext(), "请输入端口号", Toast.LENGTH_SHORT).show();
            return;
        }

        ComtomFilePlayUtils.Instance().init(username, password, ip,
                Integer.valueOf(port), this);
        ComtomSpeak.Instance().init(username, password, ip,
                Integer.valueOf(port), this);
        Intent intent = new Intent(getActivity(), RingTermActivity.class);

        startActivity(intent);
        saveShareDb();
    }

    private void getShareDb() {
        String username = SharedDB.getStringValue(getContext(), RING_USERNAME, "admin");
        String password = SharedDB.getStringValue(getContext(), RING_PASSWORD, "admin");
        String ip = SharedDB.getStringValue(getContext(), RING_IP, "171.216.84.160");
        int port = SharedDB.getIntValue(getContext(), RING_PORT, 8000);


        et_username.setText(username);

        et_password.setText(password);

        et_ip.setText(ip);

        et_port.setText(port + "");

    }

    private void saveShareDb() {



        String username=  et_username.getText().toString();

        String password= et_password.getText().toString();

        String ip=et_ip.getText().toString();

        int port=Integer.valueOf(et_port.getText().toString());


        SharedDB.putStringValue(getContext(), RING_USERNAME, username);
        SharedDB.putStringValue(getContext(), RING_PASSWORD, password);
        SharedDB.putStringValue(getContext(), RING_IP, ip);
         SharedDB.putIntValue(getContext(), RING_PORT, port);

    }
}
