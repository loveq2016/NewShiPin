package com.ysh.tv.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysh.tv.app.R;

/**
 * Created by Administrator on 2016/7/31.
 */
public class SubAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    String[][] cities;
    public int foodpoition;

    public SubAdapter(Context context, String[][] cities,int position) {
        this.context = context;
        this.cities = cities;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.foodpoition = position;
    }

    public SubAdapter(Context context, String[][] cities) {
        this.context = context;
        this.cities = cities;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void setPosition(int position)
    {
        this.foodpoition=position;
    }

    public int getPosition()
    {
        return foodpoition;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cities[foodpoition].length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        final int location=position;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.sublist_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.textview1);
            viewHolder.mLayout=(LinearLayout)convertView.findViewById(R.id.sublist_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(cities[foodpoition][location]!=null)
            {
                viewHolder.textView.setText(cities[foodpoition][location]);
            }
         viewHolder.textView.setTextColor(Color.BLACK);
        if(selectItem==position)
        {
            viewHolder.mLayout.setBackgroundColor(context.getResources().getColor(R.color.select_bg));
        }


        return convertView;
    }

    public void setSelectItem(int selectItem)
    {
        this.selectItem=selectItem;
    }

    private int selectItem=-1;


    public static class ViewHolder {
        public TextView textView;
        public LinearLayout mLayout;
    }

}