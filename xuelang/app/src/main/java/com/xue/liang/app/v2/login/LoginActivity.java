package com.xue.liang.app.v2.login;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.common.Config;
import com.xue.liang.app.v2.data.reponse.RegisterResp;
import com.xue.liang.app.v2.data.request.RegisterReq;
import com.xue.liang.app.v2.dialog.SettingFragmentDialog;
import com.xue.liang.app.v2.http.manager.HttpManager;
import com.xue.liang.app.v2.http.manager.data.HttpReponse;
import com.xue.liang.app.v2.http.manager.listenter.HttpListenter;
import com.xue.liang.app.v2.http.manager.listenter.LoadingHttpListener;

import com.xue.liang.app.v2.main.MainActivity_;
import com.xue.liang.app.v2.utils.DeviceUtil;
import com.xue.liang.app.v2.utils.MacUtil;
import com.xue.liang.app.v2.utils.PhoneNumCheckUtils;
import com.xue.liang.app.v2.utils.SharedDB;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login)
public class LoginActivity extends FragmentActivity {

    @ViewById(R.id.login_edittext)
    public EditText login_edittext;
    private String phoneNum;
    private String mac;

    private String key = "PHONENUM";

    private String key_mac="MAC_KEY";

    @AfterViews
    protected void initView() {
        DeviceUtil.initConfig(getApplicationContext());
        phoneNum = SharedDB.getStringValue(getApplicationContext(), key, "");
        login_edittext.setText(phoneNum);
    }


    public void toMainAcitivty() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity_.class);
        startActivity(intent);
    }


    @Click(R.id.login_btn)
    public void doLogin() {
        phoneNum = login_edittext.getText().toString();

        if (!PhoneNumCheckUtils.isMobileNO(phoneNum)) {
            Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        Config.TEST_PHONE_NUMBER = phoneNum;



        mac=SharedDB.getStringValue(getApplicationContext(),key_mac,"123");
        if(mac.equals("123")){
            if(MacUtil.isPhone(getApplicationContext())){
                mac= MacUtil.getMacAddress(getApplicationContext());

            }else{
                mac= MacUtil.getMacAddressByFile();
            }
        }


        Config.TEST_MAC=mac;




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
                        SharedDB.putStringValue(getApplicationContext(), key_mac,mac);
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

    @Click(R.id.btn_setting)
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



}
