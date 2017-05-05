package com.xue.liang.app.v3.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.xue.liang.app.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jikun on 17/3/24.
 */

public abstract class BaseDialogFragment extends DialogFragment {
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view;
        if (getContentViewLayoutID() != 0) {

            view = inflater.inflate(getContentViewLayoutID(),
                    container, false);

        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        init();
        initViews(view, savedInstanceState);
    }


    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();


    /**
     * init all views and add events
     */
    protected abstract void initViews(View view, Bundle savedInstanceState);


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }

    private void init() {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);// 设置Dialog为无标题模式
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 隐藏软键盘
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置Dialog为无标题模式
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置Dialog背景色为透明
    }


    public void setCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
        /**
         * dialog.setCanceledOnTouchOutside(false);
         * dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
         */
        getDialog().setCanceledOnTouchOutside(isCanceledOnTouchOutside); // 注意在DialogFrament中必须这样设置才能不弹出Dialog

    }

//
//    public void setCancelable(boolean isCanceledOnTouchOutside) {
//
//        /**
//         * dialog.setCancelable(false);
//         * dialog弹出后会点击屏幕或物理返回键，dialog不消失
//         */
//        super.setCancelable(isCanceledOnTouchOutside);
//
//    }
}
