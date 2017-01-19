package com.xue.liang.app.v2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xue.liang.app.v2.R;


import java.util.List;

/**
 * Created by Administrator on 2016/9/16.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {


    private List<String> data;
    private LayoutInflater layoutInflater;

    private OnItemClickListener<String> onitemClickListener;

    public AlarmAdapter(Context context,List<String> data) {
        this.data=data;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_alarm_item, parent, false);

        return new AlarmViewHolder(view, onitemClickListener);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        holder.bindDataView(position);

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }


    public OnItemClickListener<String> getOnitemClickListener() {
        return onitemClickListener;
    }

    public void setOnitemClickListener(OnItemClickListener<String> onitemClickListener) {
        this.onitemClickListener = onitemClickListener;
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private ImageLoader imageLoader;
        private ImageSize imageSize;
        private OnItemClickListener monitemClickListener;

        private int mposition;

        public AlarmViewHolder(View itemView, OnItemClickListener onitemClickListener) {
            super(itemView);
            monitemClickListener = onitemClickListener;
            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.image);
            imageLoader = ImageLoader.getInstance(); // Get singleton instance
            imageSize = new ImageSize(100, 100);

        }

        public void bindDataView(int position) {
            mposition = position;
            String picturePath = data.get(position);
            if (TextUtils.isEmpty(picturePath)) {
                image.setImageResource(R.mipmap.image_upto);
                //image.setBackgroundResource(R.mipmap.image_upto);
            } else {
                imageLoader.displayImage("file://" + picturePath, image, imageSize);
            }

        }

        @Override
        public void onClick(View v) {
            monitemClickListener.onitemClick(mposition, data.get(mposition));

        }
    }

    public static interface OnItemClickListener<T> {
        void onitemClick(int position, T data);
    }
}
