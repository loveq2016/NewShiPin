package com.xue.liang.app.http.manager;

import com.xue.liang.app.common.Config;
import com.xue.liang.app.utils.Constant;
import com.xue.liang.app.utils.Des3DesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.MediaType;

/**
 * Created by Administrator on 2016/10/11.
 */
public class XmlHttpManger {


    public void sendCall(String phonenum,int type, StringCallback callback) {

        try {
            String url = Config.getSendCallAlarmUrl();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Action=SendCall");
            stringBuilder.append("&Account=Xlgc");
            stringBuilder.append("&Password=Xlgc");
            stringBuilder.append("&SendTel=" + phonenum);
            stringBuilder.append("&SendType="+type);//1-语音、2-短信、3-多方通话
            stringBuilder.append("&Content=报警求助");
            String pamars = stringBuilder.toString();
            String encrypInfo = "";
            encrypInfo = Des3DesUtils.encryptThreeDESECB(pamars, Constant.KEY);


            OkHttpUtils
                    .postString()
                    .url(url)
                    .content(encrypInfo)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute(callback);


        } catch (Exception e) {
            if (callback != null) {
                callback.onError(null, null, 1);
            }
            e.printStackTrace();
        }
    }


    public void doGetGroupMemberHttp(String phonenum, StringCallback callback) {


        try {
            String url = Config.getGetGroupMemberUrl();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Action=GetGroupMember");
            stringBuilder.append("&Account=Xlgc");
            stringBuilder.append("&Password=Xlgc");
            stringBuilder.append("&UserNumber=" + phonenum);
            String pamars = stringBuilder.toString();
            String encrypInfo = "";
            encrypInfo = Des3DesUtils.encryptThreeDESECB(pamars, Constant.KEY);


            OkHttpUtils
                    .postString()
                    .url(url)
                    .content(encrypInfo)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute(callback);


        } catch (Exception e) {
            if (callback != null) {
                callback.onError(null, null, 1);
            }
            e.printStackTrace();
        }
    }

    public void addAddMemberHttp(String phonenum, String addPhonenum, String addName, StringCallback callback) {

        try {
            String url = Config.getAddGroupMemberUrl();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Action=AddMember");
            stringBuilder.append("&Account=Xlgc");
            stringBuilder.append("&Password=Xlgc");
            stringBuilder.append("&UserNumber=" + phonenum);
            stringBuilder.append("&DestUserNumber=" + addPhonenum);
            stringBuilder.append("|"+addName);
            String pamars = stringBuilder.toString();
            String encrypInfo = "";
            encrypInfo = Des3DesUtils.encryptThreeDESECB(pamars, Constant.KEY);


            OkHttpUtils
                    .postString()
                    .url(url)
                    .content(encrypInfo)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute(callback);


        } catch (Exception e) {
            if (callback != null) {
                callback.onError(null, null, 1);
            }
            e.printStackTrace();
        }
    }
}
