package com.xue.liang.app.v2.utils;

import android.app.Fragment;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by yu on 2016/10/31.
 */
public class ImageLoader {

    public static void load(Context aty, ImageView targetView, String url) {
        Glide.with(aty).load(url).centerCrop().into(targetView);
    }
    public static void load(Context aty, ImageView targetView, int resId) {
        Glide.with(aty).load(resId).centerCrop().into(targetView);
    }

    public static void load(Context aty, ImageView targetView, String url, int errorId) {
        Glide.with(aty).load(url).error(errorId).centerCrop().into(targetView);
    }

    public static void load(Fragment fmt, ImageView targetView, String url) {
        Glide.with(fmt).load(url).centerCrop().into(targetView);
    }

    public static void load(Fragment fmt, ImageView targetView, String url, int errorId) {
        Glide.with(fmt).load(url).error(errorId).centerCrop().into(targetView);
    }

}
