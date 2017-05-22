package com.xue.liang.app.v2.rx;

import com.xue.liang.app.v2.base.Constant;
import com.xue.liang.app.v2.entity.DeviceEntity;
import com.xue.liang.app.v2.http.ApiException;
import com.xue.liang.app.v2.http.HttpResult;
import com.xue.liang.app.v2.http.RetrofitManager;
import com.xue.liang.app.v2.utils.ShareprefUtils;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by jikun on 17/5/19.
 */

public class ApiFun1Builder {
    /**
     * 用来生成多次请求的Fuc1
     */

    public Func1 getDeviceListFuc1(Map<String, String> map) {


        Func1<HttpResult<List<DeviceEntity>>, Observable<HttpResult<List<DeviceEntity>>>> func1 = listHttpResult -> {
            if (null == listHttpResult || null == listHttpResult.getResponse() || listHttpResult.getResponse().size() == 0) {
                ShareprefUtils.put(Constant.Key_Termi_Unique_Code, map.get("termi_unique_code"));
                return RetrofitManager.getInstance().getApiService()
                        .getDeviceList(map);
            } else {
                return Observable.just(listHttpResult);
            }


        };
        return func1;
    }


    public Func1 getSameResultData() {
        Func1<HttpResult<List<DeviceEntity>>, Observable<HttpResult<List<DeviceEntity>>>> func1 = new Func1<HttpResult<List<DeviceEntity>>, Observable<HttpResult<List<DeviceEntity>>>>() {
            @Override
            public Observable<HttpResult<List<DeviceEntity>>> call(HttpResult<List<DeviceEntity>> httpResult) {
                if (0 == httpResult.getRet_code())
                    return Observable.just(httpResult);
                return Observable.error(new ApiException(httpResult.getRet_code() + "", httpResult.getRet_string()));


            }
        };
        return func1;
    }
}
