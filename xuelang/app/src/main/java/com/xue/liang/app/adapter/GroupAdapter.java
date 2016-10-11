package com.xue.liang.app.adapter;

import android.content.Context;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.data.bean.MemberInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/25.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private List<MemberInfo> data;
    private Context mcontext;
    private LayoutInflater layoutInflater;

    public GroupAdapter(Context context, List<MemberInfo> data) {
        mcontext = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(mcontext);
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.item_group, parent, false);

        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        holder.bindData(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_phonenum;

        public GroupViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_phonenum = (TextView) itemView.findViewById(R.id.tv_phonenum);

        }

        public void bindData(MemberInfo info) {
            tv_name.setText(info.getTrueName());

            tv_phonenum.setText(info.getTel());
        }
    }

    public void reshListData(List<MemberInfo> memberInfoList) {
        this.data = memberInfoList;
        notifyDataSetChanged();
    }
}
