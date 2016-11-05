package com.xue.liang.app.v3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.bean.notice.NoticeRespBean;
import com.xue.liang.app.v3.viewholder.newsinfo.NewsInfoItemViewHolder;

import java.util.List;

/**
 * Created by jikun on 2016/11/3.
 */

public class NewInfoAdapter extends RecyclerView.Adapter {
    private List<NoticeRespBean.ResponseBean> dataList;
    private LayoutInflater layoutInflater;
    private Context context;

    public NewInfoAdapter(Context context,List<NoticeRespBean.ResponseBean> dataList) {
        this.context=context;
        this.dataList = dataList;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_new_info, parent, false);

        return new NewsInfoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void reshData(List<NoticeRespBean.ResponseBean> dataList){
        this.dataList=dataList;
        notifyDataSetChanged();

    }
}
