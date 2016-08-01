package com.xue.liang.app.info;

import android.support.v4.app.FragmentActivity;
import android.widget.ListView;

import com.xue.liang.app.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_info_list)
public class InfoListActivity extends FragmentActivity {


    @ViewById(R.id.listview)
    protected ListView listView;


    @Click(R.id.bt_back)
    public void closeActivity() {
        finish();

    }

    @Click(R.id.btn_alarmwarning)
    public void toAlermActivity() {

    }

    @AfterViews
    public void initView() {

    }

}
