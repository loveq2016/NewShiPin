package com.xue.liang.app.v2.base;

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

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jikun on 17/3/24.
 */

public abstract class BaseDialogFragment extends DialogFragment {
    private Unbinder unbinder;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        View view = null;
        if (getContentViewLayoutID() != 0) {
            view = inflater.inflate(getContentViewLayoutID(), container, false);

        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews(savedInstanceState);
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
    protected abstract void initViews(Bundle savedInstanceState);
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
}
