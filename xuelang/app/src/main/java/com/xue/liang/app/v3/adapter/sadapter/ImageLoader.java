package com.xue.liang.app.v3.adapter.sadapter;

import android.app.Activity;
import android.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 *
 */
public class ImageLoader {

    public static void load(Activity aty, ImageView targetView, String url) {
        Glide.with(aty).load(url).centerCrop().into(targetView);
    }
    public static void load(Activity aty, ImageView targetView, int resId) {
        Glide.with(aty).load(resId).centerCrop().into(targetView);
    }

    public static void load(Activity aty, ImageView targetView, String url, int errorId) {
        Glide.with(aty).load(url).error(errorId).centerCrop().into(targetView);
    }

    public static void load(Fragment fmt, ImageView targetView, String url) {
        Glide.with(fmt).load(url).centerCrop().into(targetView);
    }

    public static void load(Fragment fmt, ImageView targetView, String url, int errorId) {
        Glide.with(fmt).load(url).error(errorId).centerCrop().into(targetView);
    }

}
