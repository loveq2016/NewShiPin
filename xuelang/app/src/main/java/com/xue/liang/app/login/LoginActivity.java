package com.xue.liang.app.login;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.xue.liang.app.R;
import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.RegisterResp;
import com.xue.liang.app.data.request.RegisterReq;
import com.xue.liang.app.http.manager.HttpManager;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.listenter.HttpListenter;
import com.xue.liang.app.main.MainActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity extends FragmentActivity {


    @AfterViews
    protected void initView() {


    }
    @Click(R.id.login_btn)
    public void toMainAcitivty(){
        Intent intent=new Intent();
        intent.setClass(this, MainActivity_.class);
        startActivity(intent);

    }
    //@Click(R.id.login_btn)
    public void doLogin() {

        String url = Config.getRegisterUrl();
        RegisterReq registerReq=new RegisterReq("","","");

        HttpManager.HttpBuilder<RegisterReq, RegisterResp> httpBuilder = new HttpManager.HttpBuilder<>();
        httpBuilder.buildRequestValue(registerReq).buildResponseClass(RegisterResp.class)
                .buildUrl(url)
                .buildHttpListenter(new HttpListenter<RegisterResp>() {
                    @Override
                    public void onFailed(String msg) {

                    }

                    @Override
                    public void onSuccess(HttpReponse<RegisterResp> httpReponse) {

                    }
                });

    }


}
