package com.xue.liang.app.v3.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xue.liang.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikun on 2016/11/16.
 */

public class BottomBar extends LinearLayout {

    private List<BottomBarItem> mbottomBarItemList = new ArrayList<>();

    private List<View> itemViewList = new ArrayList<>();

    private LayoutInflater layoutInflater;

    private OnBottomItemOnListener onBottomItemOnListener;

    private List<Integer> unIndexList;


    public void setOnBottomItemOnListener(OnBottomItemOnListener onBottomItemOnListener) {
        this.onBottomItemOnListener = onBottomItemOnListener;
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        unIndexList = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
    }

    public void showHideBadgeView(int index, boolean ishow) {
        if (ishow) {
            if (null != itemViewList && index < itemViewList.size() - 1) {
                View view = itemViewList.get(index);
                ImageView iv_badge = (ImageView) view.findViewById(R.id.iv_badge);
                iv_badge.setVisibility(View.VISIBLE);
            }

        } else {
            if (null != itemViewList && index < itemViewList.size() - 1) {
                View view = itemViewList.get(index);
                ImageView iv_badge = (ImageView) view.findViewById(R.id.iv_badge);
                iv_badge.setVisibility(View.GONE);
            }
        }

    }


    public void setMbottomBarItemList(List<BottomBarItem> bottomBarItemList) {
        this.mbottomBarItemList = bottomBarItemList;
        removeAllViews();
        if (mbottomBarItemList != null && !mbottomBarItemList.isEmpty()) {
            for (int i = 0; i < mbottomBarItemList.size(); i++) {
                View view = addItem(i);
                itemViewList.add(view);
                setTitleAndImage(view, i);
                if (i == 0) {
                    setSelectedView(view, true);
                } else {
                    setSelectedView(view, false);
                }

            }
        }
    }

    private void setTitleAndImage(View view, int i) {
        ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);

        TextView iv_text = (TextView) view.findViewById(R.id.iv_text);
        if (mbottomBarItemList.get(i).getImageresid() != 0) {
            iv_item.setBackgroundResource(mbottomBarItemList.get(i).getImageresid());
        }
        iv_text.setText(mbottomBarItemList.get(i).getTitle());

    }

    private void setSelectedView(View view, boolean isselected) {
        ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
        TextView iv_text = (TextView) view.findViewById(R.id.iv_text);
        iv_item.setSelected(isselected);
        iv_text.setSelected(isselected);
    }


    private View addItem(int i) {
        View itemView = layoutInflater.inflate(R.layout.bottom_bar_item, null);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;
        setGravity(Gravity.CENTER);
        addView(itemView, layoutParams);
        itemView.setOnClickListener(new OnBarOnClickListener(i));
        return itemView;
    }

    public static class BottomBarItem {

        private String title;
        private int imageresid;

        public BottomBarItem(String title, int imageresid) {
            this.title = title;
            this.imageresid = imageresid;
        }

        public int getImageresid() {
            return imageresid;
        }

        public void setImageresid(int imageresid) {
            this.imageresid = imageresid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


    public class OnBarOnClickListener implements OnClickListener {

        private int postion = 0;

        public void setPostion(int i) {
            this.postion = i;
        }

        public OnBarOnClickListener(int postion) {
            this.postion = postion;

        }


        @Override
        public void onClick(View view) {

            boolean isisEnable = isEnableView(itemViewList.get(postion));

            if (isisEnable) {


                for (int i = 0; i < itemViewList.size(); i++) {
                    if (postion == i) {
                        setSelectedView(itemViewList.get(i), true);
                    } else {
                        setSelectedView(itemViewList.get(i), false);
                    }
                }
            } else {

            }


            if (onBottomItemOnListener != null) {
                onBottomItemOnListener.onItemSelected(postion, view, isisEnable);
            }


        }
    }

    public interface OnBottomItemOnListener {

        void onItemSelected(int i, View view, boolean isEnable);
    }

    public void setUnUseItem(int postion, boolean isEnabled) {

        for (int i = 0; i < itemViewList.size(); i++) {
            if (postion == i) {
                setEnableView(itemViewList.get(i), isEnabled);
            }
        }
    }

    private void setEnableView(View view, boolean isEnabled) {
        ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
        TextView iv_text = (TextView) view.findViewById(R.id.iv_text);
        iv_item.setEnabled(isEnabled);
        iv_text.setSelected(isEnabled);
    }

    private boolean isEnableView(View view) {
        ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
        return iv_item.isEnabled();
    }

}
