package com.xue.liang.app.v3.activity.newinfo;


import com.xue.liang.app.v3.base.BasePresenter;
import com.xue.liang.app.v3.base.BaseView;
import com.xue.liang.app.v3.bean.login.LoginReqBean;
import com.xue.liang.app.v3.bean.login.LoginRespBean;
import com.xue.liang.app.v3.bean.noticedetail.NoticeDetailReqBean;
import com.xue.liang.app.v3.bean.noticedetail.NoticeDetailRespBean;

/**
 * Created by Administrator on 2016/10/26.
 */
public class NewInfoDetailContract {

    interface View extends BaseView {

        void onSuccess(NoticeDetailRespBean responseben);

        void onFail(NoticeDetailRespBean responseben);


    }

    interface Presenter extends BasePresenter {
        void loadData(NoticeDetailReqBean bean);
    }
}
