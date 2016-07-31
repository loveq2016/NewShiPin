package com.xue.liang.app.login;

import android.support.v4.app.FragmentActivity;

import com.xue.liang.app.R;
import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.RegisterRp;
import com.xue.liang.app.data.request.RegisterRq;
import com.xue.liang.app.http.manager.HttpManager;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.data.HttpRequest;
import com.xue.liang.app.http.manager.listenter.HttpListenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity extends FragmentActivity {


    @AfterViews
    protected void initView() {


    }

    @Click(R.id.login_btn)
    public void doLogin() {
        Test test = new Test();
        String url = Config.getRegisterUrl();


        HttpListenter httpListenter = new HttpListenter() {
            @Override
            public void onFailed(HttpRequest httpRequest) {

            }

            @Override
            public void onSuccess(HttpReponse httpReponse) {

            }
        };
        HttpManager<RegisterRq, RegisterRp> httpManager = new HttpManager.HttpBuilder().
                buildUrl(url).buildRequestClazz(RegisterRq.class).
                buildResponseClazz(RegisterRp.class).
                buildHttpListenter
                        (new HttpListenter<RegisterRq, RegisterRp>() {
                             @Override
                             public void onFailed(HttpRequest<RegisterRq> httpRequest) {

                             }

                             @Override
                             public void onSuccess(HttpReponse<RegisterRp> httpReponse) {

                             }
                         }
                        ).build();
        RegisterRq registerRq = new RegisterRq();
        httpManager.dopost(registerRq);

    }


}
