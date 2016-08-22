package com.xue.liang.app.alarm;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.temobi.cache.memory.MD5;
import com.xue.liang.app.R;
import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.UpdateAlarmResp;
import com.xue.liang.app.data.reponse.UpdateResp;
import com.xue.liang.app.data.request.UpdateAlarmReq;
import com.xue.liang.app.http.manager.HttpManager;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.listenter.HttpListenter;
import com.xue.liang.app.http.manager.listenter.LoadingHttpListener;
import com.xue.liang.app.update.PostFile;
import com.xue.liang.app.update.okupdateFile.UpdateFileUtils;
import com.xue.liang.app.utils.Pathutil;
import com.xue.liang.app.utils.ToastUtil;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/8/8.
 */
@EActivity(R.layout.activity_alarm)
public class AlarmActivity extends FragmentActivity {

    private ProgressDialog pd;

    @ViewById(R.id.bt_image0)
    public ImageView bt_image0;

    @ViewById(R.id.bt_image1)
    public ImageView bt_image1;

    @ViewById(R.id.bt_image2)
    public ImageView bt_image2;

    @ViewById(R.id.bt_image3)
    public ImageView bt_image3;

    @ViewById(R.id.bt_image4)
    public ImageView bt_image4;


    private ImageView currentChooseImageView;


    private final String IMAGE_TYPE = "image/*";


    private static int SELECT_PIC_KITKAT = 5;
    private static int SELECT_PIC = 2;

    private static int CARMERA = 1;
    public static int VIDEO=3;


    private List<String> listFilePath = new ArrayList<String>();


    @Click({R.id.bt_image0, R.id.bt_image1, R.id.bt_image2, R.id.bt_image3, R.id.bt_image4})
    public void setImage(View view) {
        // TODO Auto-generated method stub
        //使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
        switch (view.getId()) {
            case R.id.bt_image0:
                currentChooseImageView = bt_image0;
                break;
            case R.id.bt_image1:
                currentChooseImageView = bt_image1;
                break;
            case R.id.bt_image2:
                currentChooseImageView = bt_image2;
                break;
            case R.id.bt_image3:
                currentChooseImageView = bt_image3;
                break;
            case R.id.bt_image4:
                currentChooseImageView = bt_image4;
                break;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            startActivityForResult(intent, SELECT_PIC_KITKAT);//4.4版本
//        } else {
            startActivityForResult(intent, SELECT_PIC);//4.4以下版本，先不处理
      //  }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PIC_KITKAT || requestCode == SELECT_PIC) {
            setdataFromImageList(requestCode, resultCode, data);
        } else if (requestCode == CARMERA) {
            setdatasaveFille(requestCode, resultCode, data);
        }else if(requestCode==VIDEO){
            setdataFromVideo(requestCode, resultCode, data);
        }

    }


    private void setdatasaveFille(int requestCode, int resultCode, Intent data) {

        int i = 0;

        if (resultCode == Activity.RESULT_OK) {

            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.v("TestFile",
                        "SD card is not avaiable/writeable right now.");
                ToastUtil.showToast(getApplicationContext(), "SD CARD不存在", Toast.LENGTH_SHORT);
                return;
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
                updateFile(fileName);
            }
        }

    }

    private void setdataFromVideo(int requestCode, int resultCode, Intent data){
        String videoPath = "";
        if (resultCode == RESULT_OK
                && null != data) {
            data.getData();

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            videoPath = cursor.getString(columnIndex);
            cursor.close();

            updateFile(videoPath);
        }
    }

    private void setdataFromImageList(int requestCode, int resultCode, Intent data) {
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

        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        ImageSize imageSize = new ImageSize(100, 100);
        imageLoader.displayImage("file://" + picturePath, currentChooseImageView, imageSize);
        if (!listFilePath.contains(picturePath)) {
            listFilePath.add(picturePath);
        }
    }

    @Click(R.id.bt_alrm_right_now)
    public void allupdateFile() {
        if (!listFilePath.isEmpty()) {
            updateFile(listFilePath);
        } else {
            ToastUtil.showToast(getApplicationContext(), "请选择需要上传的文件", Toast.LENGTH_SHORT);
        }

    }


    Map<String, String> params;
    Map<String, File> files;
    String result;

    private void updateFile(String path) {
        pd = new ProgressDialog(this);
        pd.setMessage("正在上传...");
        pd.setCancelable(false);
        pd.show();
        String t = MD5.toMD5(System.currentTimeMillis() + "");
        File file = new File(path);
        String filename = file.getName();
        params = new HashMap<String, String>();
        params.put(t, filename);  //params.put(t, "aaaa.jpg");
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

    private void updateFile(List<String> paths) {
        pd = new ProgressDialog(this);
        pd.setMessage("正在上传...");
        pd.setCancelable(false);
        pd.show();

        params = new HashMap<String, String>();
        files = new HashMap<String, File>();

        for (String path : paths) {
            String md5 = MD5.toMD5(System.currentTimeMillis() + "");
            File file = new File(path);
            String filename = file.getName();
            params.put(md5, filename);
            files.put(filename, new File(path));
        }

        UpdateFileUtils.getInstance().upload(files);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    result = PostFile.post(Config.getUpdateFile(), params, files);
//                } catch (IOException e) {
//                    pd.dismiss();
//                    e.printStackTrace();
//                }
//                Message msg = handler.obtainMessage();
//                msg.what = 1;
//                msg.obj = result;
//                handler.sendMessage(msg);
//            }
//        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("测试代码", "测试代码+Json" + msg.obj);
            pd.dismiss();
            UpdateResp updateResp = new Gson().fromJson(msg.obj.toString(), UpdateResp.class);
            if (updateResp.getRet_code() == 0) {
                List<String> fileList = new ArrayList<String>();
                for (UpdateResp.UpdateFile updateFile : updateResp.getResponse()) {
                    fileList.add(updateFile.getFile_id());
                }
                updateAlermHelp(getSupportFragmentManager(), "", fileList);

            } else {
                ToastUtil.showToast(getApplicationContext(), updateResp.getRet_string(), Toast.LENGTH_SHORT);
            }

            super.handleMessage(msg);
        }
    };

    private void updateAlermHelp(FragmentManager fragmentManager, String content, List<String> fileids) {


        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<UpdateAlarmResp>() {
            @Override
            public void onFailed(String msg) {
                ToastUtil.showToast(getApplicationContext(), "上传文件失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void onSuccess(HttpReponse<UpdateAlarmResp> httpReponse) {
                ToastUtil.showToast(getApplicationContext(), "上传文件接口返回成功", Toast.LENGTH_SHORT);

            }
        }, fragmentManager);

        String url = Config.getUpdateAlarmUrl();


        UpdateAlarmReq updateAlarmReq = new UpdateAlarmReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, content, fileids);
        //NoticeDetailReq noticeDetailReq = new NoticeDetailReq(Config.TEST_TYPE, Config.TEST_PHONE_NUMBER, Config.TEST_MAC, id);
        HttpManager.HttpBuilder<UpdateAlarmReq, UpdateAlarmResp> httpBuilder = new HttpManager.HttpBuilder<UpdateAlarmReq, UpdateAlarmResp>();
        httpBuilder.buildRequestValue(updateAlarmReq)
                .buildResponseClass(UpdateAlarmResp.class)
                .buildUrl(url)
                .buildHttpListenter(httpListenter)
                .build()
                .dopost("UpdateAlarm");
    }


    @Click(R.id.bt_alrm_to_carme)
    public void toCarmera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CARMERA);
    }
    @Click(R.id.bt_alrm_to_video)
    public void toVideo() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO);
    }

    @Click(R.id.bt_back)
    public void close() {
        finish();
    }
}
