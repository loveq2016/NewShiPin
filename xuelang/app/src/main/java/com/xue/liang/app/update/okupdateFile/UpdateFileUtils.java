package com.xue.liang.app.update.okupdateFile;

import android.util.Log;

import com.xue.liang.app.common.Config;
import com.xue.liang.app.update.okupdateFile.handler.UIProgressRequestListener;
import com.xue.liang.app.update.okupdateFile.listener.ProgressRequestListener;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by Administrator on 2016/8/22.
 */
public class UpdateFileUtils {
    private static UpdateFileUtils instance;

    private UpdateFileUtils() {

    }

    public static UpdateFileUtils getInstance() {
        if (instance == null) {
            instance = new UpdateFileUtils();

        }
        return instance;
    }


    public void upload(Map<String, File> mapfiles) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        //此文件必须在手机上存在，实际情况下请自行修改，这个目录下的文件只是在我手机中存在。


        //这个是非ui线程回调，不可直接操作UI
        final ProgressRequestListener progressListener = new ProgressRequestListener() {
            @Override
            public void onRequestProgress(long bytesWrite, long contentLength, boolean done) {
                Log.e("TAG", "bytesWrite:" + bytesWrite);
                Log.e("TAG", "contentLength" + contentLength);
                Log.e("TAG", (100 * bytesWrite) / contentLength + " % done ");
                Log.e("TAG", "done:" + done);
                Log.e("TAG", "================================");
            }
        };


        //这个是ui线程回调，可直接操作UI
        final UIProgressRequestListener uiProgressRequestListener = new UIProgressRequestListener() {
            @Override
            public void onUIRequestProgress(long bytesWrite, long contentLength, boolean done) {
                Log.e("TAG", "bytesWrite:" + bytesWrite);
                Log.e("TAG", "contentLength" + contentLength);
                Log.e("TAG", (100 * bytesWrite) / contentLength + " % done ");
                Log.e("TAG", "done:" + done);
                Log.e("TAG", "================================");
                //ui层回调
                // uploadProgress.setProgress((int) ((100 * bytesWrite) / contentLength));
                //Toast.makeText(getApplicationContext(), bytesWrite + " " + contentLength + " " + done, Toast.LENGTH_LONG).show();
            }
        };

        MultipartBody.Builder builder=new  MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Map.Entry<String, File> mapfile : mapfiles.entrySet()) {
            String name=mapfile.getKey();
            File file=mapfile.getValue();
            RequestBody fileRequestBody=  RequestBody.create(MediaType.parse("application/octet-stream"), file);
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\""+name+"\";filename=\"+"+name+"+\""),fileRequestBody);
        }
        RequestBody requestBody =builder.build();
        //构造上传请求，类似web表单
//        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("hello", "android")
//                .addFormDataPart("photo", file.getName(), RequestBody.create(null, file))
//                .addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";filename=\"another.dex\""), RequestBody.create(MediaType.parse("application/octet-stream"), file))
//                .build();

        //进行包装，使其支持进度回调
        final Request request = new Request.Builder().url(Config.getUpdateFile()).post(ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener)).build();
        //开始请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "error ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG","测试代码最终返回值"+response.body().string());
            }

        });

    }


}
