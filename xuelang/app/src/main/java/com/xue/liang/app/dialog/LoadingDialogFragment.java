package com.xue.liang.app.dialog;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.xue.liang.app.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by jk on 2016/8/2.
 */
@EFragment(R.layout.loading_dialog_fragment)
public class LoadingDialogFragment extends DialogFragment {

    @AfterViews
    public  void init(){
        setCancelable(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    public static LoadingDialogFragment showDialog(FragmentManager fragmentManager,String tag){
        LoadingDialogFragment loadingDialogFragment=new LoadingDialogFragment_();
        if (fragmentManager != null && !fragmentManager.isDestroyed()){

            loadingDialogFragment.show(fragmentManager,tag);
        }

        return   loadingDialogFragment;
    }
    public static void dimissDialg(LoadingDialogFragment dialogFragment){
        dialogFragment.dismissAllowingStateLoss();
    }
}
