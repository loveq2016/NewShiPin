package com.xue.liang.app.v3.activity.ring;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.sadapter.RecyclerAdapter;
import com.xue.liang.app.v3.adapter.sadapter.RecyclerViewHolder;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.ring.RingBean;
import com.xue.liang.app.v3.utils.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import comtom.com.realtimestream.ComtomSpeak;
import comtom.com.realtimestream.bean.Term;
import comtom.com.realtimestream.bean.TermGroup;
import comtom.com.realtimestream.exchangedata.ErrorMessage;
import comtom.com.realtimestream.listener.OnLoadTermDataListener;
import comtom.com.realtimestream.listener.OnStartSpeakListener;
import comtom.com.realtimestream.listener.OnStopSpeakListener;

/**
 * Created by jikun on 17/4/26.
 */

public class RingTermActivity extends BaseActivity implements OnLoadTermDataListener, OnStartSpeakListener, OnStopSpeakListener {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.bt_speak)
    Button bt_speak;
    RecyclerAdapter<RingBean> adapter;

    boolean isPlay = false;

    ComtomSpeak comtomSpeak;




    private List<RingBean> ringBeanList = new ArrayList<>();
    ArrayList<Term> mSelectTermList = new ArrayList<Term>();

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_ring_term;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tv_title.setText("村村响");
        ringBeanList.clear();
        mSelectTermList.clear();

        initRecyclerView();
        comtomSpeak= ComtomSpeak.Instance();
        comtomSpeak.getTermList(this);
        showProgressDialog();

    }

    private void initRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.addItemDecoration(new RecyclerViewDecoration(getApplicationContext(), RecyclerViewDecoration.VERTICAL_LIST));
        adapter = new RecyclerAdapter<RingBean>(null, R.layout.adapter_ring_term) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, RingBean item) {
                holder.setText(R.id.tv_title, item.getTerm().getName());

                holder.setChecked(R.id.cb_check, item.isChecked());


            }
        };

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {

                RingBean ringBean = ringBeanList.get(position);
                ringBean.setChecked(!ringBean.isChecked());
                if (ringBean.isChecked()) {
                    mSelectTermList.add(ringBean.getTerm());
                } else {
                    mSelectTermList.remove(ringBean.getTerm());
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onLoadTermSuccessful(ArrayList<Term> arrayList, ArrayList<TermGroup> arrayList1) {
        dimissProgressDialog();
        ringBeanList.clear();
        for (Term term : arrayList) {
            RingBean bean = new RingBean();
            bean.setChecked(false);
            bean.setTerm(term);
            ringBeanList.add(bean);
        }
        adapter.refreshWithNewData(ringBeanList);


    }

    @Override
    public void onLoadTermFailed(ErrorMessage errorMessage) {
        dimissProgressDialog();
        String errorInfo = "错误信息";
        if (null != errorMessage && TextUtils.isEmpty(errorMessage.getErrorMessage())) {
            errorInfo = errorInfo + errorMessage.getErrorMessage();
        }
        Toast.makeText(getApplicationContext(), errorInfo, Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.bt_speak)
    public void beginSpeak(){
        if (!isPlay) {
            if(mSelectTermList.size()==0){
                showToast("请选中一个说话列表");
                return;
            }
            comtomSpeak.startSpeak(mSelectTermList, this);
        } else {
            comtomSpeak.stopSpeak(this);
        }
    }

    @Override
    public void startSpeakSuccessful() {
        isPlay = true;
        bt_speak.setText("停止讲话");

    }

    @Override
    public void startSpeakFailed(ErrorMessage errorMessage) {
        showToast("开启讲话失败"+errorMessage.getErrorMessage());
    }

    @Override
    public void stopSpeakSuccessful() {
        isPlay = false;
        bt_speak.setText("开启讲话");
    }

    @Override
    public void stopSpeakFailed(ErrorMessage errorMessage) {
        showToast("停止对讲失败"+errorMessage.getErrorMessage());
    }
}
