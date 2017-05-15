package com.xue.liang.app.v2.dialog;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.base.BaseDialogFragment;
import com.xue.liang.app.v2.utils.PhoneNumCheckUtils;
import com.xue.liang.app.v2.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/9/25.
 */

public class AddGroupDialogFragment extends BaseDialogFragment {

    private OnAddGroupListener onAddGroupListener;


    @BindView(R.id.et_group_name)
    EditText et_group_name;

    @BindView(R.id.et_group_phone_num)
    EditText et_group_phone_num;

    @BindView(R.id.bt_add)
    Button bt_add;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_add_people;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setCancelable(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    @OnClick(R.id.bt_cancle)
    public void close() {
        if (onAddGroupListener != null) {
            onAddGroupListener.onCancle();
        }
        dismissAllowingStateLoss();
    }

    @OnClick(R.id.bt_add)
    public void add() {

        String name = et_group_name.getText().toString();
        String phonenum = et_group_phone_num.getText().toString();
        if (TextUtils.isEmpty(name)) {

            ToastUtil.showToast(getContext(), "账号不能为空", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(phonenum)) {
            ToastUtil.showToast(getContext(), "手机号不能为空", Toast.LENGTH_SHORT);
            return;
        }
        if (!PhoneNumCheckUtils.isMobileNO(phonenum)) {
            Toast.makeText(getContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        dismissAllowingStateLoss();
        if (onAddGroupListener != null) {
            onAddGroupListener.onAdd(name, phonenum);
        }

    }

    public interface OnAddGroupListener {
        void onAdd(String name, String phoneNum);

        void onCancle();

    }


    public OnAddGroupListener getOnAddGroupListener() {
        return onAddGroupListener;
    }

    public void setOnAddGroupListener(OnAddGroupListener onAddGroupListener) {
        this.onAddGroupListener = onAddGroupListener;
    }

    public static class Builder {
        private AddGroupDialogFragment addGroupDialogFragment;

        public Builder() {
            addGroupDialogFragment = new AddGroupDialogFragment();
        }

        public Builder setOnAddGroupListener(OnAddGroupListener l) {
            addGroupDialogFragment.setOnAddGroupListener(l);
            return this;

        }

        public AddGroupDialogFragment show(FragmentManager fragmentManager, String tag) {
            addGroupDialogFragment.show(fragmentManager, tag);
            return addGroupDialogFragment;
        }


    }
}
