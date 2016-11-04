package com.xue.liang.app.v3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;

import java.util.List;


public class PlayerAdapter extends BaseAdapter {

    private List<DeviceRespBean.ResponseBean> data;
    private LayoutInflater mInflater = null;

    public PlayerAdapter(Context context, List<DeviceRespBean.ResponseBean> data) {
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

    public void reshData(List<DeviceRespBean.ResponseBean> data) {
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
                    .inflate(R.layout.player_item, parent, false);
            holer.urltextview = (TextView) convertView
                    .findViewById(R.id.play_item_url_tv);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holer);
        } else {
            holer = (ViewHolder) convertView.getTag();
        }
        holer.urltextview.setText(data.get(position).getDev_name());
        return convertView;
    }

    static class ViewHolder {
        public TextView urltextview;

    }

}
