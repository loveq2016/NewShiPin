package com.xue.liang.app.v3.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xue.liang.app.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/26.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected MaterialDialog materialDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());

        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        ButterKnife.bind(this);
        initViews(savedInstanceState);
    }

    /**
     * get bundle data
     *
     * @param extras
     */
    protected abstract void getBundleExtras(Bundle extras);

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


    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
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
        Intent intent = new Intent(this, clazz);
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


        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        //  Snackbar.make(, msg, Snackbar.LENGTH_SHORT).show();

    }


    protected void showProgressDialog() {
        showProgressDialog("加载中", "请稍后");
    }


    public void setLeftTitleRightView(boolean ishowLeft, String text, boolean ishowRight) {
        if (null != findViewById(R.id.rl_titleinfo)) {
            Button left = (Button) findViewById(R.id.bt_back);
            Button rigth = (Button) findViewById(R.id.bt_setting);
            TextView textView = (TextView) findViewById(R.id.tv_title);
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

    public void onClickSetting() {

    }

    public void onClickBack() {
        finish();
    }

    protected void showProgressDialog(String title, String info) {
        if (materialDialog != null && materialDialog.isShowing()) {
            return;
        }
        materialDialog = new MaterialDialog.Builder(this)
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
