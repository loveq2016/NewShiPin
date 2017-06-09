package com.xue.liang.app.v3.widget;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.sadapter.RecyclerAdapter;
import com.xue.liang.app.v3.adapter.sadapter.RecyclerViewHolder;
import com.xue.liang.app.v3.base.BaseDialogFragment;
import com.xue.liang.app.v3.bean.device.GirdInfoBean;
import com.xue.liang.app.v3.utils.RandomValueUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jikun on 17/5/5.
 */

public class ShowGirdFragmentDialog extends BaseDialogFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    RecyclerAdapter<GirdInfoBean> adapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_fragment_show_gird;
    }


    @Override
    protected void initViews(View view, Bundle savedInstanceState) {


        initRecyclerView();
        generateData();

    }

    private void initRecyclerView() {

        tv_title.setText("网格员");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new GridLayoutDecoration(getApplicationContext()));

        adapter = new RecyclerAdapter<GirdInfoBean>(null, R.layout.adapter_gird_show) {

            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, GirdInfoBean item) {

                if (item != null) {
                    String name = item.getUser_name();
                    if (name == null) {
                        name = "";
                    }
                    String phone = item.getUser_phonenum();
                    if (phone == null) {
                        phone = "";
                    }
                    holder.setText(R.id.tv_name, name);
                    holder.setText(R.id.tv_phone, phone);
                }


            }
        };

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {

            }
        });

    }

    private void generateData() {

        List<GirdInfoBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GirdInfoBean bean = new GirdInfoBean("网格员" + i, "imageUrl", RandomValueUtils.getChineseName(), RandomValueUtils.getTel());
            list.add(bean);
        }
        adapter.refreshWithNewData(list);

    }


}
