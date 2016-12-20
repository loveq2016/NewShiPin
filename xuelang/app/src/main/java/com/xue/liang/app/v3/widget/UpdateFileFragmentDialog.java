package com.xue.liang.app.v3.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.fragment.help.HelpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jikun on 2016/12/20.
 */

public class UpdateFileFragmentDialog extends DialogFragment {

    @BindView(R.id.fragment_containers)
    RelativeLayout fragment_containers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


//        getDialog().getWindow().setBackgroundDrawableResource(
//                android.R.color.transparent);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return inflater.inflate(R.layout.dialog_update_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initFragment();
    }

    private void initFragment() {
        Fragment fragment = Fragment.instantiate(getContext(), HelpFragment.class.getName());
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_containers, fragment).commit();
    }
}
