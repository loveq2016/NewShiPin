package com.xue.liang.app.v2.updateapk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;
import com.xue.liang.app.v2.R;

/**
    * 更新弹窗demo
    */
    public class UpgradeActivity extends Activity {
//        private TextView tv_title;
//        private TextView version;
//        private TextView size;
//        private TextView time;
//        private TextView tv_content;
//        private Button bt_cancle;
//        private Button bt_update;
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            setContentView(R.layout.activity_upgrade);
//            tv_title = getView(R.id.tv_title);
//            time = getView(R.id.time);
//            tv_content = getView(R.id.tv_content);
//            bt_cancle = getView(R.id.bt_cancle);
//            bt_update = getView(R.id.bt_update);
//
//            /*获取下载任务，初始化界面信息*/
//            updateBtn(Beta.getStrategyTask());
//            tv.setText(tv.getText().toString() + Beta.getStrategyTask().getSavedLength() + "");
//
//            /*获取策略信息，初始化界面信息*/
//            tv_title.setText(tv_title.getText().toString() + Beta.getUpgradeInfo().title);
//            version.setText(version.getText().toString() + Beta.getUpgradeInfo().versionName);
//            size.setText(size.getText().toString() + Beta.getUpgradeInfo().fileSize + "");
//            time.setText(time.getText().toString() + Beta.getUpgradeInfo().publishTime + "");
//            tv_content.setText(Beta.getUpgradeInfo().newFeature);
//
//            /*为下载按钮设置监听*/
//            bt_update.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DownloadTask task = Beta.startDownload();
//                    updateBtn(task);
//                    if (task.getStatus() == DownloadTask.DOWNLOADING) {
//                        finish();
//                    }
//                }
//            });
//
//            /*为取消按钮设置监听*/
//            bt_cancle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Beta.cancelDownload();
//                    finish();
//                }
//            });
//
//            /*注册下载监听，监听下载事件*/
//            Beta.registerDownloadListener(new DownloadListener() {
//                @Override
//                public void onReceive(DownloadTask task) {
//                    updateBtn(task);
//                    tv.setText(task.getSavedLength() + "");
//                }
//
//                @Override
//                public void onCompleted(DownloadTask task) {
//                    updateBtn(task);
//                    tv.setText(task.getSavedLength() + "");
//                }
//
//                @Override
//                public void onFailed(DownloadTask task, int code, String extMsg) {
//                    updateBtn(task);
//                    tv.setText("failed");
//
//                }
//            });
//        }
//
//        @Override
//        protected void onResume() {
//            super.onResume();
//        }
//
//        @Override
//        protected void onStop() {
//            super.onStop();
//        }
//
//        @Override
//        protected void onDestroy() {
//            super.onDestroy();
//
//            /*注销下载监听*/
//            Beta.unregisterDownloadListener();
//        }
//
//
//        public void updateBtn(DownloadTask task) {
//
//            /*根据下载任务状态设置按钮*/
//            switch (task.getStatus()) {
//                case DownloadTask.INIT:
//                case DownloadTask.DELETED:
//                case DownloadTask.FAILED: {
//                    start.setText("开始下载");
//                }
//                break;
//                case DownloadTask.COMPLETE: {
//                    start.setText("安装");
//                }
//                break;
//                case DownloadTask.DOWNLOADING: {
//                    start.setText("暂停");
//                }
//                break;
//                case DownloadTask.PAUSED: {
//                    start.setText("继续下载");
//                }
//                break;
//            }
//        }
//
//        public <T extends View> T getView(int id) {
//            return (T) findViewById(id);
//        }
    }