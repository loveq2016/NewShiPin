package com.ysh.tv.app;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ysh.tv.app.entity.Constant;
import com.ysh.tv.app.entity.ScreenInfo;

import java.lang.reflect.Field;

import reco.frame.tv.util.Utils;

/**
 * Created by Administrator on 2016/11/10.
 */
public class xutils {

    /**
     * 获取屏幕宽高
     */
    public static void getScreenInfo(Activity activity) {
       /* ScreenInfo screen = DbService.getScreen(activity);
        if (screen == null) {
            // 获取屏幕的高和宽
            int screenH = activity.getWindowManager().getDefaultDisplay().getHeight();
            int screenW = activity.getWindowManager().getDefaultDisplay().getWidth();
            // 获取状态栏的高
            int statusBarH = xutils.getStatusBar(activity);

            Constant.W = screenW;
            Constant.H = screenH - statusBarH;
            Constant.ScreenH = screenH;

            ScreenInfo sc = new ScreenInfo();
            sc.setWidth(screenW);
            sc.setHeight(screenH - statusBarH);
            sc.setFull_height(screenH);

        } else {
            Constant.W = screen.getWidth();
            Constant.H = screen.getHeight();
            Constant.ScreenH = screen.getFull_height();

        }*/
    }

    public static int getStatusBar(Context context) {
        int StatusBar_H = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field f = c.getField("status_bar_height");
            int x = Integer.parseInt(f.get(o).toString());
            StatusBar_H =context.getResources().getDimensionPixelSize(x);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return StatusBar_H;
    }

    public static void initView(View v, int layOutWidth, int layOutHeight,
                                int left, int top, int right, int buttom) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                layOutWidth, layOutHeight);
        lp.setMargins(left, top, right, buttom);
        v.setLayoutParams(lp);
    }

}
