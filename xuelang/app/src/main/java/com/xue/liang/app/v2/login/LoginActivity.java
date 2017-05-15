package com.xue.liang.app.v2.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.base.BaseActivity;
import com.xue.liang.app.v2.common.Config;
import com.xue.liang.app.v2.data.reponse.RegisterResp;
import com.xue.liang.app.v2.data.request.RegisterReq;
import com.xue.liang.app.v2.dialog.SettingFragmentDialog;
import com.xue.liang.app.v2.http.manager.HttpManager;
import com.xue.liang.app.v2.http.manager.data.HttpReponse;
import com.xue.liang.app.v2.http.manager.listenter.HttpListenter;
import com.xue.liang.app.v2.http.manager.listenter.LoadingHttpListener;
import com.xue.liang.app.v2.main.MainActivity;
import com.xue.liang.app.v2.utils.BusinessCodeUtils;
import com.xue.liang.app.v2.utils.DeviceUtil;
import com.xue.liang.app.v2.utils.MacUtil;
import com.xue.liang.app.v2.utils.PhoneNumCheckUtils;
import com.xue.liang.app.v2.utils.SharedDB;



import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_edittext)
    public EditText login_edittext;

    @BindView(R.id.bt_get_device)
    public Button bt_get_device;

    @BindView(R.id.tv_device_infp)
    public TextView tv_device_infp;


    private String phoneNum;
    private String mac;

    private String key = "PHONENUM";

    private String key_mac = "MAC_KEY";

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DeviceUtil.initConfig(getApplicationContext());
        phoneNum = SharedDB.getStringValue(getApplicationContext(), key, "");
        login_edittext.setText(phoneNum);
        getDevicieInfo();
    }


    public void toMainAcitivty() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.login_btn)
    public void doLogin() {
        phoneNum = login_edittext.getText().toString();

        if (!PhoneNumCheckUtils.isMobileNO(phoneNum)) {
            Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        Config.TEST_PHONE_NUMBER = phoneNum;


        mac = BusinessCodeUtils.getValue(getApplicationContext(), BusinessCodeUtils.USER_ID);
        if (TextUtils.isEmpty(mac)) {
            mac = MacUtil.getWifiMacAddress(getApplicationContext());
        }


        Config.TEST_MAC = mac;


        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<RegisterResp>() {
            @Override
            public void onFailed(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(HttpReponse<RegisterResp> httpReponse) {


                if (httpReponse != null && httpReponse.getData() != null && httpReponse.getData().getRet_code() != null) {
                    if (httpReponse.getData().getRet_code() == 0) {
                        SharedDB.putStringValue(getApplicationContext(), key, phoneNum);
                        //SharedDB.putStringValue(getApplicationContext(), key_mac,mac);
                        toMainAcitivty();

                    } else {
                        String errorinfo = httpReponse.getData().getRet_string();
                        if (TextUtils.isEmpty(errorinfo)) {
                            errorinfo = "错误信息为空";
                        }
                        Toast.makeText(getApplicationContext(), "登陆失败--" + errorinfo, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "登陆失败--返回值为null", Toast.LENGTH_SHORT).show();
                }


            }
        }, getSupportFragmentManager());

        String url = Config.getRegisterUrl();
        RegisterReq registerReq = new RegisterReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC);
        HttpManager.HttpBuilder<RegisterReq, RegisterResp> httpBuilder = new HttpManager.HttpBuilder<RegisterReq, RegisterResp>();
        httpBuilder.buildRequestValue(registerReq).buildResponseClass(RegisterResp.class).
                buildUrl(url).
                buildHttpListenter(httpListenter).
                build().
                dopost("Login");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String code = event.getKeyCode() + "";
        // ToastUtil.showToast(getApplicationContext(), "event.getKeyCode()===" + code + "keyCode===" + keyCode, Toast.LENGTH_SHORT);
        return super.onKeyDown(keyCode, event);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @OnClick(R.id.btn_setting)
    public void toSettingDialog() {
        SettingFragmentDialog msettingFragmentDialog = new SettingFragmentDialog();
        msettingFragmentDialog.setOnCofimLister(new SettingFragmentDialog.onCofimLister() {
            @Override
            public void onSuccess() {
                // finish();
            }
        });
        msettingFragmentDialog.show(getSupportFragmentManager(),
                "dialog");
    }

    @OnClick(R.id.bt_get_device)
    public void getDevicieInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("有线MAC地址：" + MacUtil.getWiredMacAddr() + "\n");
        stringBuilder.append("无线MAC地址：" + MacUtil.getWifiMacAddress(getApplicationContext()) + "\n");
        stringBuilder.append("业务账号：" + BusinessCodeUtils.getValue(getApplicationContext(), BusinessCodeUtils.USER_ID));
        tv_device_infp.setText(stringBuilder.toString());

    }


}
