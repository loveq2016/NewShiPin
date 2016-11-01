package com.xue.liang.app.v3.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/11/1.
 */
public class MainActivity extends BaseActivity {
    private AHBottomNavigationAdapter navigationAdapter;


    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;


    private int nIndex = 0;

    private Fragment[] mFragments = null;

    private SparseArray<Fragment> mFragmentMap = new SparseArray<>();

    private Fragment mCurrentFragement;

    private Context mContext;


    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        int[] tabColors;
        tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
        navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.main_menu_bar);
        bottomNavigation.setForceTitlesDisplay(true);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                Log.e("测试代码", "测试代码" + position + "----------" + wasSelected);

                if (!wasSelected) {
                    //setFragmentIndicator(position, 0);
                }
                return true;
            }
        });

        mContext = getApplicationContext();
    }

    /**
     * 初始化Fragment in MainActivy
     */
    private void initFragment(String className) {
        mFragments = new Fragment[5];

        mFragmentMap.put(2, Fragment.instantiate(mContext, className));


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, mFragmentMap.get(2));
        fragmentTransaction.commit();
        mFragmentMap.get(2).setUserVisibleHint(true);
        mCurrentFragement = mFragmentMap.get(2);

        nIndex = 2;
    }

    public void setFragmentIndicator(int which, int index) {

        if (which == nIndex) {
            return;
        }

        Fragment fragment = mFragmentMap.get(which);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
//            if (which == 0) {
//                fragment = Fragment.instantiate(mContext, LeaguesContainerFragment.class.getName());
//            } else if (which == 1) {
//                fragment = Fragment.instantiate(mContext, PlayerContainerFragment.class.getName());
//            } else if (which == 2) {
//                fragment = Fragment.instantiate(mContext, LiveContainerFragment.class.getName());
//            } else if (which == 3) {
//                fragment = Fragment.instantiate(mContext, AttentionFragment.class.getName());
//
//            } else if (which == 4) {
//                fragment = Fragment.instantiate(mContext, CompareFragment.class.getName());
//            }
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