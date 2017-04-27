package com.xue.liang.app.v3.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xue.liang.app.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/26.
 */
public abstract class BaseFragment extends Fragment {

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;

    protected MaterialDialog materialDialog;

    private View mView;

    /**
     * when fragment is visible for the first time, here we can do some initialized work or refresh data only once
     */
    protected abstract void onFirstUserVisible();

    /**
     * this method like the fragment's lifecycle method onResume()
     */
    protected abstract void onUserVisible();

    /**
     * when fragment is invisible for the first time
     */
    protected void onFirstUserInvisible() {
        // here we do not recommend do something
    }

    /**
     * this method like the fragment's lifecycle method onPause()
     */
    protected abstract void onUserInvisible();

    /**
     * init all views and add events
     */
    protected abstract void initViews();

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            return inflater.inflate(getContentViewLayoutID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initTitle(view);
        initViews();
        initPrepare();
    }


    public void setLeftTitleRightView(boolean ishowLeft, String text, boolean ishowRight) {
        if (null != mView) {
            Button left = (Button) mView.findViewById(R.id.bt_back);
            Button rigth = (Button) mView.findViewById(R.id.bt_setting);
            TextView textView = (TextView) mView.findViewById(R.id.tv_title);
            if (ishowLeft) {

                left.setVisibility(View.VISIBLE);
                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickBack();
                    }
                });
            } else {
                left.setVisibility(View.GONE);
            }


            if (ishowRight) {

                rigth.setVisibility(View.VISIBLE);
                rigth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickSetting();
                    }
                });
            } else {
                rigth.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(text)) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(text);
            }

        }


    }

    private void initTitle(View view) {
        mView = view;
    }

    public void onClickBack() {
        getActivity().finish();
    }

    public void onClickSetting() {


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }


    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * show toast
     *
     * @param msg
     */
    protected void showToast(String msg) {


        if (null != getActivity()) {
            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }


        //  Snackbar.make(, msg, Snackbar.LENGTH_SHORT).show();

    }

    protected void showProgressDialog() {

        showProgressDialog("加载中", "请稍后");
    }

    protected void showProgressDialog(String title, String info) {
        if (materialDialog != null && materialDialog.isShowing()) {
            return;
        }
        materialDialog = new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(info)
                .progress(true, 0)
                .progressIndeterminateStyle(false).cancelable(false)
                .show();
    }

    protected void dimissProgressDialog() {
        if (materialDialog != null && materialDialog.isShowing()) {
            materialDialog.dismiss();
        }

    }
}
