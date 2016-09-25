package com.xue.liang.app.login;

import android.content.Intent;
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
import com.xue.liang.app.main.MainActivity_;
import com.xue.liang.app.utils.DeviceUtil;
import com.xue.liang.app.utils.SharedDB;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login)
public class LoginActivity extends FragmentActivity {

    @ViewById(R.id.login_edittext)
    public EditText login_edittext;
    private String phoneNum;

    private String key = "PHONENUM";

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

        if (!isMobileNO(phoneNum)) {
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

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
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
