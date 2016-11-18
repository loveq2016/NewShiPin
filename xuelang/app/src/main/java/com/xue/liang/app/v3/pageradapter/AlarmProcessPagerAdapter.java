package com.xue.liang.app.v3.pageradapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.fragment.alarmprocesse.StayPendingAlarmFragment;
import com.xue.liang.app.v3.fragment.help.HelpFragment;
import com.xue.liang.app.v3.fragment.device.DeviceFragment;

/**
 * Created by jikun on 2016/11/3.
 */

public class AlarmProcessPagerAdapter extends FragmentPagerAdapter {


    private Context mContext;

    private LoginRespBean mloginRespBean;


    public AlarmProcessPagerAdapter(FragmentManager fm, Context context,LoginRespBean loginRespBean) {
        super(fm);
        mContext = context;
        mloginRespBean=loginRespBean;

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            StayPendingAlarmFragment stayPendingAlarmFragment = StayPendingAlarmFragment.newInstance(mloginRespBean);
            return stayPendingAlarmFragment;
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
