package com.xue.liang.app.v2.presenter;

import com.xue.liang.app.v2.base.BaseView;
import com.xue.liang.app.v2.entity.DeviceEntity;
import com.xue.liang.app.v2.entity.GroupMember6995Entity;
import com.xue.liang.app.v2.entity.SendCall6995Entity;
import com.xue.liang.app.v2.http.HttpResult;

import java.util.List;


/**
 * Created by jikun on 17/5/19.
 */

public interface IMain {
    interface MianView extends BaseView {
        void getDeviceListSuccess(HttpResult<List<DeviceEntity>> result);

        void postAlermSuccess();


        void post6995AlarmSuccess(SendCall6995Entity entity);

        void get6995GroupMemberSuccess(GroupMember6995Entity entity);

        void add6995GroupMemberSuccess(SendCall6995Entity entity);

    }

    interface MainPresenter {

        void getDeviceCameraList(String businessID,String lineMac,String wifiMac);

        void getPostalerm(String mac, String type, String userId, DeviceEntity entity);

        void get6995sendCall(String phonenum, int type);

        void get6995GroupMember(String phonenum);

        void get6995AddMember(String phonenum, String addPhonenum, String addName);

    }
}
