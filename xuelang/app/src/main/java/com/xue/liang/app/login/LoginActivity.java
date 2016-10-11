package com.xue.liang.app.login;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.temobi.vcp.http.download.LogUtils;
import com.xue.liang.app.R;
import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.RegisterResp;
import com.xue.liang.app.data.request.RegisterReq;
import com.xue.liang.app.http.manager.HttpManager;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.listenter.HttpListenter;
import com.xue.liang.app.http.manager.listenter.LoadingHttpListener;
import com.xue.liang.app.main.MainActivity_;
import com.xue.liang.app.utils.Des3DesUtils;
import com.xue.liang.app.utils.DeviceUtil;
import com.xue.liang.app.utils.PhoneNumCheckUtils;
import com.xue.liang.app.utils.SharedDB;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    public void test123(View view) {
        //创建okHttpClient对象


        String pamars = "Action=SendCall&Account=Xlgc&Password=Xlgc&SendTel=13877149295&SendType=1&Content=报警求助";

        String key = "kingon!qaz@wsx#edc$rfv%^";
        String encrypInfo = "";
        try {
            encrypInfo = Des3DesUtils.encryptThreeDESECB(pamars, key);
        } catch (Exception e) {
            e.printStackTrace();
        }


        OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
        final Request request = new Request.Builder()
                .url("http://218.200.206.182:8005/Xlgc/MobileService.aspx?" + encrypInfo)
                .build();
//new call
        Call call = mOkHttpClient.newCall(request);
//请求加入调度
        call.enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.d(response.body().string());
            }


        });
    }

    public void test456(View view) {
        String pamars = "Action=SendCall&Account=Xlgc&Password=Xlgc&SendTel=13877149295&SendType=1&Content=报警求助";

        String key = "kingon!qaz@wsx#edc$rfv%^";
        String encrypInfo = "";
        try {
            encrypInfo = Des3DesUtils.encryptThreeDESECB(pamars, key);
        } catch (Exception e) {
            e.printStackTrace();
        }


        String url = "http://218.200.206.182:8005/Xlgc/MobileService.aspx";


        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("utf-8"), encrypInfo);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String key = "kingon!qaz@wsx#edc$rfv%^";
                String encrypInfo1 = response.body().string();
                try {
                    String testinfo = Des3DesUtils.decryptThreeDESECB(encrypInfo1, key);
                    LogUtils.e(testinfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

}
