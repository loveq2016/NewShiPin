package com.xue.liang.app.v3.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/11.
 */

public class FileUtils {

    /**
     * 保存从相机返回的照片到本地文件中去
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public static List<String> saveCamraFille(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.v("TestFile",
                        "SD card is not avaiable/writeable right now.");
                // ToastUtil.showToast(getApplicationContext(), "SD CARD不存在", Toast.LENGTH_SHORT);
                return null;
            }

            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            FileOutputStream b = null;
            File file = new File("/sdcard/xueliang/");
            file.mkdirs();// 创建文件夹，名称为myimage

            //照片的命名，目标文件夹下，以当前时间数字串为名称，即可确保每张照片名称不相同。网上流传的其他Demo这里的照片名称都写死了，则会发生无论拍照多少张，后一张总会把前一张照片覆盖。细心的同学还可以设置这个字符串，比如加上“ＩＭＧ”字样等；

            // 　　　然后就会发现ｓｄ卡中ｍｙｉｍａｇｅ这个文件夹下，会保存刚刚调用相机拍出来的照片，照片名称不会重复。
            String str = null;
            Date date = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
            date = new Date();
            str = format.format(date);
            String fileName = "/sdcard/xueliang/" + str + ".jpg";
            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            File file1 = new File(fileName);
            if (file1.exists()) {
                List<String> filelist = new ArrayList<>();
                filelist.add(fileName);
                return filelist;
            }
        }
        return null;

    }
}
