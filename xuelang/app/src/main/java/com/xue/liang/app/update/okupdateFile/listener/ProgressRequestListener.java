package com.xue.liang.app.update.okupdateFile.listener;

/**
     * 请求体进度回调接口，比如用于文件上传中
     */
    public interface ProgressRequestListener {
        void onRequestProgress(long bytesWritten, long contentLength, boolean done);
    }