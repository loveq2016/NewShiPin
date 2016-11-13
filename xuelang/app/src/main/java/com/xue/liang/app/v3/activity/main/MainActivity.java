package com.xue.liang.app.v3.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.constant.BundleConstant;
import com.xue.liang.app.v3.fragment.alarmprocesse.AlarmProcessFragment;
import com.xue.liang.app.v3.fragment.easypeopleinfo.EasyPeopleInfoFragment;
import com.xue.liang.app.v3.fragment.help.HelpFragment;
import com.xue.liang.app.v3.fragment.newinfo.NewInfoFragment;
import com.xue.liang.app.v3.fragment.device.DeviceFragment;
import com.xue.liang.app.v3.utils.Constant;
import com.xue.liang.app.v3.utils.SharedDB;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/11/1.
 */
public class MainActivity extends BaseActivity {
    private AHBottomNavigationAdapter navigationAdapter;


    @BindView(R.id.bottomBar)
    BottomBar bottomBar;


    private int nIndex = 0;

    private Fragment[] mFragments = new Fragment[6];

    private SparseArray<Fragment> mFragmentMap = new SparseArray<>();

    private Fragment mCurrentFragement;

    private Context mContext;

    private LoginRespBean mLoginRespBean;


    @Override
    protected void getBundleExtras(Bundle extras) {

        if (extras != null) {
            mLoginRespBean = extras.getParcelable(BundleConstant.BUNDLE_LOGIN_DATA);
        }


    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {

        mContext = getApplicationContext();
        setUpBottomBar();


        initFragment();


    }

    private void setUpBottomBar() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_0:
                        setFragmentIndicator(0);
                        break;
                    case R.id.tab_1:
                        setFragmentIndicator(1);
                        break;
                    case R.id.tab_2:
                        setFragmentIndicator(2);
                        break;
                    case R.id.tab_3:
                        setFragmentIndicator(3);
                        break;
                    case R.id.tab_4:
                        setFragmentIndicator(4);
                        break;
                    case R.id.tab_5:
                        setFragmentIndicator(5);
                        break;
                }

            }
        });
    }


//    private void setUpBottomNavigation() {
//
//        int[] tabColors;
//        tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
//        navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.main_menu_bar);
//        bottomNavigation.setForceTitlesDisplay(true);
//        navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
//
//
//        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//                Log.e("测试代码", "测试代码" + position + "----------" + wasSelected);
//
//                if (!wasSelected) {
//                    setFragmentIndicator(position);
//                }
//                return true;
//            }
//        });
//
//    }

    /**
     * 初始化Fragment in MainActivy
     */
    private void initFragment() {
        mFragments = new Fragment[6];


        DeviceFragment deviceFragment = DeviceFragment.newInstance(mLoginRespBean);
        mFragmentMap.put(0, deviceFragment);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, mFragmentMap.get(0));
        fragmentTransaction.commit();
        mFragmentMap.get(0).setUserVisibleHint(true);
        mCurrentFragement = mFragmentMap.get(0);

        nIndex = 0;
    }

    public void setFragmentIndicator(int which) {

        if (which == nIndex) {
            return;
        }


        Fragment fragment = mFragmentMap.get(which);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            if (which == 0) {
                DeviceFragment deviceFragment = DeviceFragment.newInstance(mLoginRespBean);
                fragment = deviceFragment;
            } else if (which == 1) {
                String phoneNum = SharedDB.getStringValue(getApplicationContext(), Constant.ShareDbKey.KEY_PHONE_NUMBER, "");
                fragment = EasyPeopleInfoFragment.newInstance(phoneNum);
            } else if (which == 2) {
                fragment = Fragment.instantiate(mContext, HelpFragment.class.getName());

            } else if (which == 3) {
                fragment = Fragment.instantiate(mContext, NewInfoFragment.class.getName());

            } else if (which == 4) {
                fragment = Fragment.instantiate(mContext, AlarmProcessFragment.class.getName());
            } else if (which == 5) {
                fragment = Fragment.instantiate(mContext, NewInfoFragment.class.getName());
            }
            mFragmentMap.put(which, fragment);
            fragmentTransaction.add(R.id.fragment_container, fragment);
        } else {

        }

        fragmentTransaction.hide(mCurrentFragement).show(fragment);
        mCurrentFragement.setUserVisibleHint(false);
        fragment.setUserVisibleHint(true);


        fragmentTransaction.commitAllowingStateLoss();

        Log.d(MainActivity.class.getSimpleName(), "setFragmentIndicator");
        mCurrentFragement = fragment;
        nIndex = which;

        invalidateOptionsMenu();
    }

}
