package com.xue.liang.app.v3.fragment.help;

import android.util.Log;

import com.google.gson.Gson;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpReq;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpResp;
import com.xue.liang.app.v3.bean.update.UpdateFileResp;
import com.xue.liang.app.v3.config.UriHelper;
import com.xue.liang.app.v3.httputils.retrofit2.RetrofitFactory;
import com.xue.liang.app.v3.httputils.retrofit2.service.UpdateFileAlarmService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/26.
 */
public class HelpPresenter implements HelpContract.Presenter {

    private final String FORM_HEADER_NAME = "mFile";

    private HelpContract.View mView;

    public Subscription subscrip;

    private AlarmForHelpReq malarmForHelpReq;

    public HelpPresenter(HelpContract.View view) {
        mView = view;
    }


    @Override
    public void updateFileAndAlarm(List<String> paths, AlarmForHelpReq alarmForHelpReq) {
        malarmForHelpReq = alarmForHelpReq;

        PostFormBuilder postFormBuilder = OkHttpUtils.post();
        for (String path : paths) {
            File file = new File(path);
            String filename = file.getName();
            postFormBuilder.addFile(FORM_HEADER_NAME, filename, file);
            postFormBuilder.addHeader("Content-Type","application/octet-stream");
        }
        postFormBuilder.url(UriHelper.getUpdateFile());

        mView.onUpdateStartFile();

        postFormBuilder.build().execute(new StringCallback() {

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                Log.e("测试代码", "测试代码" + progress);
                int mp = (int) (progress * 100);
                mView.onUpdateProgressUpFile(progress, total, id);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("测试代码", "测试代码---" + e.toString());
                String msg = "";
                if (null != e) {
                    msg = e.toString();
                }
                mView.onUpdateFileFail(msg);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("测试代码", "测试代码---" + response);
                String errormsg = "";
                try {
                    UpdateFileResp updateResp = new Gson().fromJson(response, UpdateFileResp.class);
                    if (updateResp.getRet_code() == 0) {
                        List<String> fileList = new ArrayList<>();
                        if (null != updateResp && null != updateResp.getResponse()) {
                            for (UpdateFileResp.UpdateFile updateFile : updateResp.getResponse()) {
                                fileList.add(updateFile.getFile_id());
                            }

                        }
                        mView.onUpdateFileSuccess(fileList);
                        malarmForHelpReq.setFilelist(fileList);
                        doAlarmAfterUpdataFile(malarmForHelpReq);//开始报警

                    } else {

                        if (updateResp != null && updateResp.getRet_string() != null) {
                            errormsg = updateResp.getRet_string();
                        }
                        mView.onUpdateFileFail(errormsg);
                    }
                } catch (Exception e) {
                    if (null != e && null != e.toString()) {
                        errormsg = e.toString();
                    }
                    mView.onUpdateFileFail(errormsg);
                }

            }
        });

    }

    public void doAlarmAfterUpdataFile(AlarmForHelpReq bean) {

        String GET_API_URL = UriHelper.getStartUrl();

        mView.showLoadingView("");
        Retrofit retrofit = RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        UpdateFileAlarmService service = retrofit.create(UpdateFileAlarmService.class);
        subscrip = service.getAlarmForHelpService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AlarmForHelpResp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                        try {
//                            // Your onError handling code
//                            RetrofitError ex = (RetrofitError) e;
//                            Response res = ex.getResponse();
//
//                        } catch (Exception ex) {
//                            // Catch the culprit who's causing this whole problem
//                        }
                        mView.hideLoadingView();
                        mView.onAlarmFail();
                    }

                    @Override
                    public void onNext(AlarmForHelpResp bean) {
                        mView.hideLoadingView();
                        if (bean.getRet_code() == 0) {
                            mView.onAlarmSuccess(bean);
                        } else {
                            mView.onAlarmFail();
                        }
                    }
                });


    }
}
