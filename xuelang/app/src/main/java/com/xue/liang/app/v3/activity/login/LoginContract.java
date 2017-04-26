package com.xue.liang.app.v3.activity.login;


import com.xue.liang.app.v3.base.BasePresenter;
import com.xue.liang.app.v3.base.BaseView;
import com.xue.liang.app.v3.bean.login.LoginReqBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.bean.verifycode.VerifyCodeRespBean;

/**
 * Created by Administrator on 2016/10/26.
 */
public class LoginContract {

    interface View extends BaseView {

        void onSuccess(LoginRespBean userInfo);

        void onFail(LoginRespBean userInfo);

        void ongetVerifyCodeSuccess(VerifyCodeRespBean verifyCodeRespBean);
        void ongetVerifyCodeFail(VerifyCodeRespBean verifyCodeRespBean);


    }

    interface Presenter extends BasePresenter {
        void loadData(LoginReqBean bean);

        void getVerifyCode(String phoneNum,String mac);
    }
}
