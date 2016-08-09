package com.xue.liang.app.alarm;

import android.app.ProgressDialog;

import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xue.liang.app.R;
import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.UpdateResp;
import com.xue.liang.app.update.PostFile;
import com.xue.liang.app.utils.Pathutil;
import com.xue.liang.app.utils.ToastUtil;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016/8/8.
 */
@EActivity(R.layout.activity_alarm)
public class AlarmActivity extends FragmentActivity {

    private ProgressDialog pd;
    @ViewById(R.id.bt_image)
    public Button bt_image;
    private final String IMAGE_TYPE = "image/*";


    private static int SELECT_PIC_KITKAT = 5;
    private static int SELECT_PIC = 2;


    @Click(R.id.bt_image)
    public void setImage() {
        // TODO Auto-generated method stub
        //使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片


        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent, SELECT_PIC_KITKAT);//4.4版本
        } else {
            startActivityForResult(intent, SELECT_PIC);//4.4以下版本，先不处理
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String picturePath = "";
        if (resultCode == RESULT_OK
                && null != data) {
            if (requestCode == SELECT_PIC_KITKAT) {
                Uri selectedImage = data.getData();
                picturePath = Pathutil.getPath(getApplicationContext(), selectedImage);

            } else {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
            }
        }
        ToastUtil.showToast(getApplicationContext(), picturePath,
                Toast.LENGTH_SHORT);
        if (new File(picturePath).exists()) {
            Log.d("测试代码", "测试代码文件存在");
        } else {
            Log.d("测试代码", "测试代码文件不存在");
        }
        updateFile(picturePath);

    }


    Map<String, String> params;
    Map<String, File> files;
    String result;

    private void updateFile(String path) {
        pd = new ProgressDialog(this);
        pd.setMessage("正在上传...");
        pd.show();
        params = new HashMap<String, String>();
        params.put("pictureName", "aaaa.jpg");
        files = new HashMap<String, File>();
        files.put("picturePath", new File(path));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    result = PostFile.post(Config.getUpdateFile(), params, files);
                } catch (IOException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }).start();
    }


    @Click(R.id.bt_back)
    public void close() {
        finish();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("测试代码", "测试代码+Json" + msg.obj);
            pd.dismiss();
            UpdateResp updateResp= new Gson().fromJson(msg.obj.toString(),UpdateResp.class);
            if(updateResp.getRet_code()==0){
               ToastUtil.showToast(getApplicationContext(),"上传文件接口返回成功",Toast.LENGTH_SHORT);
            }else{
                ToastUtil.showToast(getApplicationContext(),updateResp.getRet_string(),Toast.LENGTH_SHORT);
            }

            super.handleMessage(msg);
        }
    };
}
