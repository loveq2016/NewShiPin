package com.xue.liang.app.update.okupdateFile;

import com.xue.liang.app.update.okupdateFile.data.ProgressRequestBody;
import com.xue.liang.app.update.okupdateFile.listener.ProgressRequestListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ProgressHelper {


    /**
     * 包装请求体用于上传文件的回调
     * @param requestBody 请求体RequestBody
     * @param progressRequestListener 进度回调接口
     * @return 包装后的进度回调请求体
     */
    public static ProgressRequestBody addProgressRequestListener(RequestBody requestBody, ProgressRequestListener progressRequestListener){
        //包装请求体
        return new ProgressRequestBody(requestBody,progressRequestListener);
    }
}
