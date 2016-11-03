package com.xue.liang.app.v3.pageradapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xue.liang.app.v3.fragment.help.HelpFragment;
import com.xue.liang.app.v3.fragment.player.PlayerFragment;

/**
 * Created by jikun on 2016/11/3.
 */

public class AlarmProcessPagerAdapter extends FragmentPagerAdapter {


    private Context mContext;


    public AlarmProcessPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return Fragment.instantiate(mContext, PlayerFragment.class.getName());
        } else {
            return Fragment.instantiate(mContext, HelpFragment.class.getName());
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "待处理";
        } else {
            return "已处理";
        }
    }
}
