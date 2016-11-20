package com.xue.liang.app.v3.fragment.newinfo;

import com.xue.liang.app.v3.base.BasePresenter;
import com.xue.liang.app.v3.base.BaseView;
import com.xue.liang.app.v3.bean.notice.NoticeReqBean;
import com.xue.liang.app.v3.bean.notice.NoticeRespBean;

/**
 * Created by jikun on 2016/11/5.
 */

public class NewInfoContract {
    interface View extends BaseView {

        void onSuccess(NoticeRespBean noticeRespBean);

        void onFail();


        void onSuccessMore(NoticeRespBean noticeRespBean);

        void onFailMore();


    }

    interface Presenter extends BasePresenter {
        void loadData(NoticeReqBean noticeReqBean);
        void loadMoreData(NoticeReqBean noticeReqBean);
    }
}
