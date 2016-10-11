package com.xue.liang.app.group;

import com.xue.liang.app.base.BasePresenter;
import com.xue.liang.app.base.BaseView;

/**
 * Created by Administrator on 2016/10/11.
 */
public class GroupContract {

    interface View<T> extends BaseView<T> {
      void  onAddSuccess(T t);
    }


    interface Presenter extends BasePresenter {

        void getGroupMemberList(String phoneNum);


        void addGroupMemberList(String phoneNum,String addPhoneNum,String addName);

    }
}
