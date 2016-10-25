package com.xue.liang.app.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.xue.liang.app.R;

import butterknife.ButterKnife;


/**
 * Created by jk on 2016/8/2.
 */
public class LoadingDialogFragment extends DialogFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.loading_dialog_fragment, container, false);
        ButterKnife.bind(view);
        initView();
        return view;
    }


    public void initView() {
        setCancelable(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    public static LoadingDialogFragment showDialog(FragmentManager fragmentManager, String tag) {
        LoadingDialogFragment loadingDialogFragment = new LoadingDialogFragment();
        if (fragmentManager != null && !fragmentManager.isDestroyed()) {

            loadingDialogFragment.show(fragmentManager, tag);
        }

        return loadingDialogFragment;
    }

    public static void dimissDialg(LoadingDialogFragment dialogFragment) {
        dialogFragment.dismissAllowingStateLoss();
    }
}
