package com.xue.liang.app.v3.activity.login;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.login.LoginReqBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.utils.Constant;
import com.xue.liang.app.v3.utils.DeviceUtil;
import com.xue.liang.app.v3.utils.PhoneNumCheckUtils;
import com.xue.liang.app.v3.utils.SharedDB;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/26.
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {


    private LoginPresenter loginPresenter;

    @BindView(R.id.login_edittext)
    EditText login_edittext;

    private String phoneNum;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        getShareDbNumber();


        loginPresenter = new LoginPresenter(this);

    }


    @Override
    public void onSuccess(LoginRespBean userInfo) {

    }

    @Override
    public void onFail(LoginRespBean userInfo) {

    }

    @Override
    public void showLoadingView(String msg) {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void onError(String info) {

    }


    @OnClick(R.id.login_btn)
    public void doLogin() {
        phoneNum = login_edittext.getText().toString();

        if (!PhoneNumCheckUtils.isMobileNO(phoneNum)) {
            Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = DeviceUtil.getWhickPhoneType(getApplicationContext());

        String mac = DeviceUtil.getMac();

        LoginReqBean loginReqBean = generateLoginReqBean(phoneNum, type, mac);
        loginPresenter.loadData(loginReqBean);

    }

    private LoginReqBean generateLoginReqBean(String termi_type, String reg_tel, String mac) {
        LoginReqBean loginReqBean = new LoginReqBean();
        loginReqBean.setReg_tel(reg_tel);
        loginReqBean.setTermi_type(termi_type);
        loginReqBean.setTermi_unique_code(mac);
        return loginReqBean;
    }

    /**
     * 获取上一次登陆成功存储的电话号码
     */
    private void getShareDbNumber() {
        phoneNum = SharedDB.getStringValue(getApplicationContext(), Constant.ShareDbKey.KEY_PHONE_NUMBER, "");
        login_edittext.setText(phoneNum);
    }

    private void savePhoneNumber() {

    }
}
