package com.xue.liang.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.RegisterResp;
import com.xue.liang.app.data.request.RegisterReq;
import com.xue.liang.app.http.manager.HttpManager;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.listenter.HttpListenter;
import com.xue.liang.app.http.manager.listenter.LoadingHttpListener;

import com.xue.liang.app.main.MainActivity;
import com.xue.liang.app.utils.DeviceUtil;
import com.xue.liang.app.utils.PhoneNumCheckUtils;
import com.xue.liang.app.utils.SharedDB;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends FragmentActivity {

    @BindView(R.id.login_edittext)
    public EditText login_edittext;
    private String phoneNum;

    private String key = "PHONENUM";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }


    protected void initView() {
        DeviceUtil.initConfig(getApplicationContext());
        phoneNum = SharedDB.getStringValue(getApplicationContext(), key, "");
        login_edittext.setText(phoneNum);
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



}
