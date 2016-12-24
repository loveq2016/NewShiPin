package com.xue.liang.app.v3.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.constant.BundleConstant;
import com.xue.liang.app.v3.constant.UserType;
import com.xue.liang.app.v3.event.JpushEvent;
import com.xue.liang.app.v3.event.RegionCamraEvent;
import com.xue.liang.app.v3.event.UrlEvent;
import com.xue.liang.app.v3.fragment.alarmprocesse.AlarmProcessFragment;
import com.xue.liang.app.v3.fragment.device.DeviceFragment;
import com.xue.liang.app.v3.fragment.easypeopleinfo.EasyPeopleInfoFragment;
import com.xue.liang.app.v3.fragment.help.HelpFragment;
import com.xue.liang.app.v3.fragment.help.HelpPictureFragment;
import com.xue.liang.app.v3.fragment.newinfo.NewInfoFragment;
import com.xue.liang.app.v3.utils.Constant;
import com.xue.liang.app.v3.utils.SharedDB;
import com.xue.liang.app.v3.widget.BottomBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Administrator on 2016/11/1.
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.bottom_weight_bar)
    BottomBar bottom_weight_bar;


    private int nIndex = 0;

    private Fragment[] mFragments = new Fragment[6];

    private SparseArray<Fragment> mFragmentMap = new SparseArray<>();

    private Fragment mCurrentFragement;

    private Context mContext;

    private LoginRespBean mLoginRespBean;

    private String title[] = {"视频监控", "便民信息", "报警求助", "新闻公告", "报警处理", "村村响"};


    private int image[] = {R.drawable.icon_camra_check, R.drawable.icon_easy_people_check, R.drawable.icon_alarm_use_check, R.drawable.icon_news_info_check, R.drawable.icon_alarm_use_check, R.drawable.icon_ring_check};

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


        setupAliasId(mLoginRespBean);

        mContext = getApplicationContext();
        //  setUpBottomBar();
        EventBus.getDefault().register(this);

        initFragment();


        List<BottomBar.BottomBarItem> bottomBarItemList = new ArrayList<>();


        for (int i = 0; i < image.length; i++) {

            BottomBar.BottomBarItem bottomBarItem = new BottomBar.BottomBarItem(title[i], image[i]);
            bottomBarItemList.add(bottomBarItem);
        }

        bottom_weight_bar.setMbottomBarItemList(bottomBarItemList);


        if (mLoginRespBean.getUser_type() == UserType.USER_TYPE_NOMAL) {

            bottom_weight_bar.setUnUseItem(4, false);
            bottom_weight_bar.setUnUseItem(5, false);

        } else if (mLoginRespBean.getUser_type() == UserType.USER_TYPE_NOMAL) {

        }


        bottom_weight_bar.setOnBottomItemOnListener(new BottomBar.OnBottomItemOnListener() {
            @Override
            public void onItemSelected(int i, View view, boolean isenable) {

                if (!isenable) {
                    showToast("没有权限");
                    return;
                }

                if (i == 4) {
                    bottom_weight_bar.showHideBadgeView(4, false);
                }
                setFragmentIndicator(i);
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

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
                fragment = NewInfoFragment.newInstance(mLoginRespBean);

            } else if (which == 4) {
                fragment = AlarmProcessFragment.newInstance(mLoginRespBean);


            } else if (which == 5) {
                fragment = NewInfoFragment.newInstance(mLoginRespBean);

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

    @Subscribe
    public void onEventMainThread(JpushEvent event) {

        bottom_weight_bar.showHideBadgeView(4, true);
    }

    private void setupAliasId(LoginRespBean loginRespBean) {
        JPushInterface.setAlias(getApplicationContext(), loginRespBean.getAlias_id(), new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i == 0) {
                    // showToast("设置极光别名成功");
                } else {
                    // showToast("设置极光别名成功=" + s);
                }
            }
        });
    }
}
