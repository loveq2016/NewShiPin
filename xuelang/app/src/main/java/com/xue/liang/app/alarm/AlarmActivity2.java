package com.xue.liang.app.alarm;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xue.liang.app.R;
import com.xue.liang.app.adapter.AlarmAdapter;
import com.xue.liang.app.common.Config;
import com.xue.liang.app.data.reponse.UpdateAlarmResp;
import com.xue.liang.app.data.reponse.UpdateResp;
import com.xue.liang.app.data.request.UpdateAlarmReq;
import com.xue.liang.app.http.manager.HttpManager;
import com.xue.liang.app.http.manager.data.HttpReponse;
import com.xue.liang.app.http.manager.listenter.HttpListenter;
import com.xue.liang.app.http.manager.listenter.LoadingHttpListener;
import com.xue.liang.app.utils.Constant;
import com.xue.liang.app.utils.Pathutil;
import com.xue.liang.app.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
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
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/15.
 */

@EActivity(R.layout.activity_alarm2)
public class AlarmActivity2 extends FragmentActivity {
    public static final int SELECT_PIC_KITKAT = 5;


    public static final int SELECT_PIC = 2;//相册选择

    public static final int CARMERA = 1;//相机选择
    public static final int VIDEO = 3;//视频选择


    private ProgressDialog pd;


    @ViewById(R.id.et_info)
    public EditText et_info;

    @ViewById(R.id.recyclerView)
    RecyclerView recyclerView;

    private AlarmAdapter alarmAdapter;

    private List<String> mdata;

    private String emptyString = "";

    private int currentChoose = 0;


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @AfterViews
    public void initView() {
        initData();
        alarmAdapter = new AlarmAdapter(getApplicationContext(), mdata);
        alarmAdapter.setOnitemClickListener(onItemClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(alarmAdapter);
        verifyStoragePermissions(0);

    }

    private void initData() {
        //默认初始化1个数据
        mdata = new ArrayList<>();
        mdata.add(emptyString);
    }

    private AlarmAdapter.OnItemClickListener<String> onItemClickListener = new AlarmAdapter.OnItemClickListener<String>() {

        @Override
        public void onitemClick(int position, String data) {

            currentChoose = position;

            String filepat = mdata.get(currentChoose);
            if (TextUtils.isEmpty(filepat)) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    startActivityForResult(intent, SELECT_PIC_KITKAT);//4.4版本
                } else {
                    startActivityForResult(intent, SELECT_PIC);//4.4以下版本，先不处理
                }
            } else {
                mdata.set(currentChoose, emptyString);
                alarmAdapter.setData(mdata);
                alarmAdapter.notifyDataSetChanged();
            }


        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PIC_KITKAT || requestCode == SELECT_PIC) {
            getDataFromImage(requestCode, resultCode, data);
        } else if (requestCode == CARMERA) {
            setdatasaveFille(requestCode, resultCode, data);
        } else if (requestCode == VIDEO) {
            setdataFromVideo(requestCode, resultCode, data);
        }

    }

    /**
     * 从相册界面获取图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void getDataFromImage(int requestCode, int resultCode, Intent data) {
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

            ToastUtil.showToast(getApplicationContext(), picturePath,
                    Toast.LENGTH_SHORT);
            if (new File(picturePath).exists()) {
                Log.d("测试代码", "测试代码文件存在");
            } else {
                Log.d("测试代码", "测试代码文件不存在");
            }

            if (currentChoose < mdata.size()) {
                if (mdata.size() < 6) {
                    mdata.add(emptyString);
                }
                mdata.set(currentChoose, picturePath);
                alarmAdapter.setData(mdata);
                alarmAdapter.notifyDataSetChanged();
            }
        }


    }

    @Click(R.id.bt_back)
    public void close() {
        finish();
    }

    private void updateFile(List<String> paths) {

        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在上传...");
        pd.setMax(100);
        pd.setCancelable(false);
        pd.show();

        PostFormBuilder postFormBuilder = OkHttpUtils.post();
        for (String path : paths) {
            File file = new File(path);
            String filename = file.getName();
            postFormBuilder.addFile("mFile", filename, file);
        }
        postFormBuilder.url(Config.getUpdateFile());

        postFormBuilder.build().execute(new StringCallback() {

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                Log.d("测试代码", "测试代码" + progress);
                int mp = (int) (progress * 100);
                pd.setProgress(mp);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("测试代码", "测试代码---" + e.toString());
                pd.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("测试代码", "测试代码---" + response);

                JIEXIupdateFile(response);
                pd.setProgress(100);
                pd.dismiss();
            }
        });

    }

    private void JIEXIupdateFile(String s) {
        try {
            UpdateResp updateResp = new Gson().fromJson(s, UpdateResp.class);
            if (updateResp.getRet_code() == 0) {
                List<String> fileList = new ArrayList<String>();
                for (UpdateResp.UpdateFile updateFile : updateResp.getResponse()) {
                    fileList.add(updateFile.getFile_id());
                }
                String text = et_info.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    text = "";
                }
                updateAlermHelp(getSupportFragmentManager(), text, fileList);

            } else {
                ToastUtil.showToast(getApplicationContext(), updateResp.getRet_string(), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ToastUtil.showToast(getApplicationContext(), "上传文件失败", Toast.LENGTH_SHORT);
        }
    }

    @Click(R.id.bt_alrm_right_now)
    public void updatefile() {
        List<String> filelist = new ArrayList<>();
        for (String data : mdata) {
            if (!TextUtils.isEmpty(data)) {
                filelist.add(data);
            }
        }

        if (!filelist.isEmpty()) {
            updateFile(filelist);
        } else {
            //如果没有上传的文件那么直接报警

            String text = et_info.getText().toString();
            if (TextUtils.isEmpty(text)) {
                text = "";
            }
            updateAlermHelp(getSupportFragmentManager(), text, new ArrayList<String>());
            //ToastUtil.showToast(getApplicationContext(), "请选择需要上传的文件", Toast.LENGTH_SHORT);
        }

    }

    private void updateAlermHelp(FragmentManager fragmentManager, String content, List<String> fileids) {


        HttpListenter httpListenter = LoadingHttpListener.ensure(new HttpListenter<UpdateAlarmResp>() {
            @Override
            public void onFailed(String msg) {
                ToastUtil.showToast(getApplicationContext(), "报警失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void onSuccess(HttpReponse<UpdateAlarmResp> httpReponse) {
                if (null != httpReponse && null != httpReponse.getData()) {
                    if (httpReponse.getData().getRet_code() == 0) {
                        ToastUtil.showToast(getApplicationContext(), "报警成功", Toast.LENGTH_SHORT);
                        clearUpdate();
                    } else {
                        ToastUtil.showToast(getApplicationContext(), "报警失败ret_code=" + httpReponse.getData().getRet_code(), Toast.LENGTH_SHORT);
                    }
                } else {
                    ToastUtil.showToast(getApplicationContext(), "报警失败", Toast.LENGTH_SHORT);
                }


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

    private void clearUpdate() {
        mdata.clear();
        mdata.add(emptyString);
        alarmAdapter.setData(mdata);
        alarmAdapter.notifyDataSetChanged();
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
                List<String> filelist = new ArrayList<>();
                filelist.add(fileName);
                updateFile(filelist);
            }
        }

    }

    private void setdataFromVideo(int requestCode, int resultCode, Intent data) {
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

            List<String> filelist = new ArrayList<>();
            filelist.add(videoPath);
            updateFile(filelist);
        }
    }

    @Click(R.id.bt_alrm_to_carme)
    public void toCarmera() {
        verifyStoragePermissions(CARMERA);
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


    /**
     * 8      * Checks if the app has permission to write to device storage
     * 9      *
     * 10      * If the app does not has permission then the user will be prompted to
     * 11      * grant permissions
     * 12      *
     * 13      * @param activity
     * 14
     */
    private void verifyStoragePermissions(int requestCode) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                    requestCode);
        }else{
            ToastUtil.showToast(getApplicationContext(),"获取SD卡权限成功，可以进行上传操作",Toast.LENGTH_SHORT);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            ToastUtil.showToast(getApplicationContext(),"获取SD卡权限成功，可以进行上传操作",Toast.LENGTH_SHORT);

        } else {

            ToastUtil.showToast(getApplicationContext(),"请求SD卡权限失败，不能进行报警操作，退出界面",Toast.LENGTH_SHORT);
            finish();
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }

    }
}
