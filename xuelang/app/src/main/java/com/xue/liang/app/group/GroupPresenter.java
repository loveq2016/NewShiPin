package com.xue.liang.app.group;

import com.xue.liang.app.data.reponse.YiDongAlarmResp;
import com.xue.liang.app.data.reponse.YidongGroupMemberResp;
import com.xue.liang.app.http.manager.XmlHttpManger;
import com.xue.liang.app.utils.Constant;
import com.xue.liang.app.utils.Des3DesUtils;
import com.xue.liang.app.utils.XmlAnalysisUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/11.
 */
public class GroupPresenter implements GroupContract.Presenter {

    private XmlHttpManger xmlHttpManger;

    private GroupContract.View view;

    public GroupPresenter(GroupContract.View view) {
        this.view = view;
        xmlHttpManger = new XmlHttpManger();
    }

    @Override
    public void getGroupMemberList(String phoneNum) {

        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (null != view) {
                    String errorInfo = "";
                    if (null != e) {
                        errorInfo = e.toString();
                    }
                    view.onFail(errorInfo);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    String testinfo = Des3DesUtils.decryptThreeDESECB(response, Constant.KEY);
                    YidongGroupMemberResp yiDongAlarmResp = XmlAnalysisUtils.getGroupMemberFromXml(testinfo);
                    if (null != view) {
                        if (yiDongAlarmResp.getYiDongAlarmResp().getResultCode().equals("0")) {
                            view.onSuccess(yiDongAlarmResp);
                        } else {
                            view.onFail(yiDongAlarmResp.getYiDongAlarmResp().getResultMsg());
                        }

                    }
                } catch (Exception e) {
                    if (null != view) {
                        view.onFail(e.toString());
                    }
                    e.printStackTrace();
                }
            }
        };
        xmlHttpManger.doGetGroupMemberHttp(phoneNum, callback);

    }

    @Override
    public void addGroupMemberList(String phoneNum, String addPhoneNum, String addName) {

        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (null != view) {
                    String errorInfo = "";
                    if (null != e) {
                        errorInfo = e.toString();
                    }
                    view.onFail(errorInfo);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    String testinfo = Des3DesUtils.decryptThreeDESECB(response, Constant.KEY);
                    YiDongAlarmResp yiDongAlarmResp = XmlAnalysisUtils.analysisYiDongSendCallXML(testinfo);
                    if (null != view) {
                        if (yiDongAlarmResp.getResultCode().equals("200")) {
                            view.onSuccess(yiDongAlarmResp);
                        } else {
                            view.onFail(yiDongAlarmResp.getResultMsg());
                        }

                    }
                } catch (Exception e) {
                    if (null != view) {
                        view.onFail(e.toString());
                    }
                    e.printStackTrace();
                }
            }
        };
        xmlHttpManger.addAddMemberHttp(phoneNum, addPhoneNum, addName, callback);

    }
}
