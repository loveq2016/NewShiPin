package com.ysh.tv.app.entity;


/**
 * 屏幕的宽高
 *
 * @author lr
 * @version 1.0
 * @2014-6-20 下午01:13:18
 */

public class ScreenInfo extends EntityBase {

    //屏幕的宽

    private int width;
    //屏幕的高

    private int height;
    //全屏的高
    private int full_height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFull_height() {
        return full_height;
    }

    public void setFull_height(int full_height) {
        this.full_height = full_height;
    }

}
