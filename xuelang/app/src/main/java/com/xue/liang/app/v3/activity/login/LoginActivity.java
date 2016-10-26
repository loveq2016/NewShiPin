package com.xue.liang.app.v3.activity.login;

import android.os.Bundle;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.login.LoginReqBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/26.
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {


    private LoginPresenter loginPresenter;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_test;
    }

    @Override
    protected void initViews() {
        loginPresenter = new LoginPresenter(this);

    }

    @OnClick(R.id.test)
    public void http() {
        LoginReqBean loginReqBean = new LoginReqBean();
        loginPresenter.loadData(loginReqBean);
    }

    @Override
    public void onSuccess(LoginRespBean userInfo) {

    }

    @Override
    public void showLoadingView(String msg) {

    }

    @Override
    public void hideLoadingView() {

    }
}
