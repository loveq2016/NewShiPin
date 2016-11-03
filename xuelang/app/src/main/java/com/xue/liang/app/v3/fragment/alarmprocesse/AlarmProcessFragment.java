package com.xue.liang.app.v3.fragment.alarmprocesse;

import android.support.v4.view.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.pageradapter.AlarmProcessPagerAdapter;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/11/2.
 */
public class AlarmProcessFragment extends BaseFragment {


    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;


    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViews() {
        setupViewPager();


    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_alarm_process;
    }



    private void setupViewPager() {

        AlarmProcessPagerAdapter adapter = new AlarmProcessPagerAdapter(getFragmentManager(), getContext());


        viewpager.setAdapter(adapter);


        viewpagertab.setViewPager(viewpager);
    }


}
