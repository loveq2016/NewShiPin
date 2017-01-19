package com.xue.liang.app.v2.group;

import com.xue.liang.app.v2.base.BasePresenter;
import com.xue.liang.app.v2.base.BaseView;

/**
 * Created by Administrator on 2016/10/11.
 */
public class GroupContract {

    interface View<T> extends BaseView<T> {
      void  onAddSuccess();
    }


    interface Presenter extends BasePresenter {

        void getGroupMemberList(String phoneNum);


        void addGroupMemberList(String phoneNum,String addPhoneNum,String addName);

    }
}
