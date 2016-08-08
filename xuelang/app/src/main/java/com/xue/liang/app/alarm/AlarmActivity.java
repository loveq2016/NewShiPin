package com.xue.liang.app.alarm;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import com.xue.liang.app.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

/**
 * Created by Administrator on 2016/8/8.
 */
@EActivity(R.layout.activity_alarm)
public class AlarmActivity extends FragmentActivity {


    @ViewById(R.id.bt_image)
    public Button bt_image;
    private final String IMAGE_TYPE = "image/*";

    private final int IMAGE_CODE = 0;   //这里的IMAGE_CODE是自己任意定义的


    @Click(R.id.bt_image)
    public void setImage() {
        // TODO Auto-generated method stub
        //使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片


        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);

        getAlbum.setType(IMAGE_TYPE);

        startActivityForResult(getAlbum, IMAGE_CODE);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量

            Log.e("TAG->onresult", "ActivityResult resultCode error");

            return;

        }


        Bitmap bm = null;


        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口

        ContentResolver resolver = getContentResolver();


        //此处的用于判断接收的Activity是不是你想要的那个

        if (requestCode == IMAGE_CODE) {

            try {

                Uri originalUri = data.getData();        //获得图片的uri


                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                //显得到bitmap图片
                //imgShow.setImageBitmap(bm);
                Drawable drawable = new BitmapDrawable(bm);
                //设置EditText中文字左边的显示图标，上面 、右边 、下面 没有则置空null
                bt_image.setCompoundDrawables(null, null, null, drawable);


//    这里开始的第二部分，获取图片的路径：


                String[] proj = {MediaStore.Images.Media.DATA};


                //好像是android多媒体数据库的封装接口，具体的看Android文档

                Cursor cursor = managedQuery(originalUri, proj, null, null, null);

                //按我个人理解 这个是获得用户选择的图片的索引值

                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                //将光标移至开头 ，这个很重要，不小心很容易引起越界

                cursor.moveToFirst();

                //最后根据索引值获取图片路径

                String path = cursor.getString(column_index);
                // imgPath.setText(path);
            } catch (IOException e) {

                Log.e("TAG-->Error", e.toString());

            }

        }


    }
    @Click(R.id.bt_back)
    public void close() {
        finish();
    }
}
