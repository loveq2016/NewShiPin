package com.xue.liang.app.v2.info.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.xue.liang.app.v2.R;
import com.xue.liang.app.v2.data.reponse.NoticeResp;

import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class InfoAdapter extends BaseAdapter {


    private List<NoticeResp.NoticeItem> data;
    private LayoutInflater mInflater = null;

    public InfoAdapter(Context context, List<NoticeResp.NoticeItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    public void reshData(List<NoticeResp.NoticeItem> data) {
        this.data = data;
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holer = null;
        if (convertView == null) {
            holer = new ViewHolder();
            convertView = mInflater
                    .inflate(R.layout.adatper_info_item, parent, false);
            holer.title_info = (TextView) convertView
                    .findViewById(R.id.title_info);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holer);
        } else {
            holer = (ViewHolder) convertView.getTag();
        }
        holer.title_info.setText(data.get(position).getTitle());
        return convertView;
    }

    static class ViewHolder {
        public TextView title_info;

    }
}
