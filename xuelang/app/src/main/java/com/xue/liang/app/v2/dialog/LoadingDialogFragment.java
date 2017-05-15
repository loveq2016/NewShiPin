package com.xue.liang.app.v2.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.base.BaseDialogFragment;


/**
 * Created by jk on 2016/8/2.
 */

public class LoadingDialogFragment extends BaseDialogFragment {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.loading_dialog_fragment;
    }



    @Override
    protected void initViews(Bundle savedInstanceState) {
        setCancelable(false);

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
