package com.xue.liang.app.v2.dialog;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.base.BaseDialogFragment;
import com.xue.liang.app.v2.base.BasePresenter;
import com.xue.liang.app.v2.base.Constant;
import com.xue.liang.app.v2.utils.ShareprefUtils;
import com.xue.liang.app.v2.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragmentDialog extends BaseDialogFragment {

    @BindView(R.id.et_ip)
    EditText et_ip;
    @BindView(R.id.et_port)
    EditText et_port;


    @BindView(R.id.cb_6995)
    CheckBox cb_6995;


    private onCofimLister onCofimLister;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.setting_dialog_fragment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        initData();

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }


    private void initData() {
        Constant.IP = ShareprefUtils.get("key_ip",
                Constant.DEFAULT_IP);
        Constant.PORT = ShareprefUtils.get("key_port",
                Constant.DEFAULT_PORT);
        Constant.is6995Open = ShareprefUtils.get("key_is_6995_open",
                Constant.Default_IS_6995);
        et_ip.setText(Constant.IP);
        et_port.setText(Constant.PORT);
        cb_6995.setChecked(Constant.is6995Open);
    }


    @OnClick(R.id.bt_confim)
    public void confim() {
        String ip = et_ip.getText().toString().trim();
        String port = et_port.getText().toString().trim();
        boolean is6995Open = cb_6995.isChecked();

        setData(ip, port, is6995Open);
        if (onCofimLister != null) {
            ToastUtils.show("修改了IP正在退出程序");
            onCofimLister.onSuccess();
        }
        dismissAllowingStateLoss();

    }

    @OnClick(R.id.bt_rest)
    public void restData() {


        setData(Constant.DEFAULT_IP, Constant.DEFAULT_PORT, Constant.Default_IS_6995);

    }

    private void setData(String ip, String port, boolean isopen) {
        ShareprefUtils.put("key_ip", ip);
        ShareprefUtils.put("key_port", port);
        ShareprefUtils.put("key_is_6995_open", isopen);
        Constant.IP = ip;
        Constant.PORT = port;
        Constant.is6995Open = isopen;
        et_ip.setText(ip);
        et_port.setText(port);

        cb_6995.setChecked(isopen);
    }

    @OnClick(R.id.bt_cancle)
    public void dimiss() {
        dismissAllowingStateLoss();
    }


    public interface onCofimLister {
        void onSuccess();
    }

    public void setOnCofimLister(SettingFragmentDialog.onCofimLister onCofimLister) {
        this.onCofimLister = onCofimLister;
    }
}
