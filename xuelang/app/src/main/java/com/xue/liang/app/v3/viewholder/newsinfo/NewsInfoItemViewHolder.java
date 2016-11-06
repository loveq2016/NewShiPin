package com.xue.liang.app.v3.viewholder.newsinfo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.NewInfoAdapter;
import com.xue.liang.app.v3.bean.notice.NoticeRespBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jikun on 2016/11/3.
 */

public class NewsInfoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tv_tite)
    TextView tv_tite;

    @BindView(R.id.tv_time)
    TextView tv_time;

    private NoticeRespBean.ResponseBean mResponseBean;

    private int mposition;

    protected NewInfoAdapter.RecyclerItemClickListener<NoticeRespBean.ResponseBean> mItemClickLister = null;

    public NewsInfoItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);
    }

    public void bindView(NoticeRespBean.ResponseBean responseBean, int position) {
        mResponseBean = responseBean;
        mposition = position;
        tv_tite.setText(responseBean.getTitle());
        tv_time.setText(responseBean.getTime());

    }

    @Override
    public void onClick(View v) {

        mItemClickLister.onItemClick(v, 0, mposition, mResponseBean);

    }

    public void setmItemClickLister(NewInfoAdapter.RecyclerItemClickListener<NoticeRespBean.ResponseBean> mItemClickLister) {
        this.mItemClickLister = mItemClickLister;
    }
}
