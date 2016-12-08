package com.xue.liang.app.v3.fragment.help;

import com.xue.liang.app.v3.base.BasePresenter;
import com.xue.liang.app.v3.base.BaseView;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpReq;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpResp;

import java.util.List;

/**
 * Created by jikun on 2016/11/4.
 */

public class HelpContract {

    public interface View extends BaseView {


        void onUpdateStartFile();

        void onUpdateProgressUpFile(float progress, long total, int id);

        void onUpdateFileFail(String errorinfo);

        void onUpdateFileSuccess( List<String> fileList);

        void onAlarmSuccess(AlarmForHelpResp resp);

        void onAlarmFail();


    }

    interface Presenter extends BasePresenter {


        void updateFileAndAlarm(List<String> paths,AlarmForHelpReq alarmForHelpReq);
    }
}
