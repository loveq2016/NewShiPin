package com.xue.liang.app.v3.activity.login;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.activity.main.MainActivity;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.login.LoginReqBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.config.TestData;
import com.xue.liang.app.v3.constant.BundleConstant;
import com.xue.liang.app.v3.constant.LoginInfoUtils;
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
        savePhoneNumber();
        LoginInfoUtils.getInstance().setLoginRespBean(userInfo);
        toMainActivity(userInfo);

    }

    @Override
    public void onFail(LoginRespBean userInfo) {
//        Intent intent=new Intent(this, MainActivity.class);
//        startActivity(intent);
        showToast("登陆失败");

    }

    @Override
    public void showLoadingView(String msg) {
        showProgressDialog("登陆中", "请稍后");

    }

    @Override
    public void hideLoadingView() {
        dimissProgressDialog();

    }

    @Override
    public void onError(String info) {
        showToast("登陆失败");
//        LoginRespBean loginRespBean = new LoginRespBean();
//        loginRespBean.setUser_id("1234");
//        loginRespBean.setAlias_id("123678");
//        loginRespBean.setApp_key("22222");
//        loginRespBean.setRet_code(200);
//        loginRespBean.setRet_string("222");
//        toMainActivity(loginRespBean);
    }


    @OnClick(R.id.login_btn)
    public void doLogin() {

        phoneNum = login_edittext.getText().toString();

//        if (!PhoneNumCheckUtils.isMobileNO(phoneNum)) {
//            Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//            return;
//        }

        String type = DeviceUtil.getWhickPhoneType(getApplicationContext());

        String mac = DeviceUtil.getMacAddress(getApplicationContext());
        mac = TestData.termi_unique_code;
        phoneNum = TestData.phoneNum;

        LoginReqBean loginReqBean = generateLoginReqBean(type, phoneNum, mac);
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
        SharedDB.putStringValue(getApplicationContext(), Constant.ShareDbKey.KEY_PHONE_NUMBER, phoneNum);
    }

    private void toMainActivity(LoginRespBean loginRespBean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleConstant.BUNDLE_LOGIN_DATA, loginRespBean);
        readyGo(MainActivity.class, bundle);
    }
}
