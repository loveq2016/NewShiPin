package com.xue.liang.app.v3.activity.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.activity.TestActivity;
import com.xue.liang.app.v3.activity.main.MainActivity;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.login.LoginReqBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.constant.BundleConstant;
import com.xue.liang.app.v3.constant.LoginInfoUtils;
import com.xue.liang.app.v3.utils.Constant;
import com.xue.liang.app.v3.utils.DeviceUtil;
import com.xue.liang.app.v3.utils.PhoneNumCheckUtils;
import com.xue.liang.app.v3.utils.SharedDB;
import com.xue.liang.app.v3.widget.SettingFragmentDialog;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2016/10/26.
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {


    private LoginPresenter loginPresenter;

    @BindView(R.id.login_edittext)
    EditText login_edittext;

    private String phoneNum;

    private String macAddress;

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

    @OnClick(R.id.bt_setting)
    public void showSettingDialog() {

        SettingFragmentDialog msettingFragmentDialog = new SettingFragmentDialog();
        msettingFragmentDialog.setOnCofimLister(new SettingFragmentDialog.onCofimLister() {
            @Override
            public void onSuccess() {

            }
        });
        msettingFragmentDialog.show(getSupportFragmentManager(),
                "dialog");
    }


    @Override
    public void onSuccess(LoginRespBean userInfo) {
        savePhoneNumber();
        LoginInfoUtils.getInstance().setLoginRespBean(userInfo);
        LoginInfoUtils.getInstance().setPhoneNum(phoneNum);
        LoginInfoUtils.getInstance().setMacAdrress(macAddress);
        toMainActivity(userInfo);

    }

    @Override
    public void onFail(LoginRespBean userInfo) {
//        Intent intent=new Intent(this, MainActivity.class);
//        startActivity(intent);
        String info = "";
        if (null != userInfo && !TextUtils.isEmpty(userInfo.getRet_string())) {
            info = userInfo.getRet_string();
        }
        showToast(info);

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
        if (TextUtils.isEmpty(info)) {
            info = "";
        }
        showToast(info);
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

        if (!PhoneNumCheckUtils.isMobileNO(phoneNum)) {
            Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = DeviceUtil.getWhickPhoneType(getApplicationContext());

        macAddress = DeviceUtil.getMacAddress(getApplicationContext());
//        mac = TestData.termi_unique_code;
//        phoneNum = TestData.phoneNum;

        LoginReqBean loginReqBean = generateLoginReqBean(type, phoneNum, macAddress);
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

    private void readIdFromShareDb() {

    }
}
