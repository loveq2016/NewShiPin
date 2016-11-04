package com.xue.liang.app.v3.fragment.device;

import com.xue.liang.app.v3.bean.device.DeviceReqBean;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;
import com.xue.liang.app.v3.httputils.retrofit2.RetrofitFactory;
import com.xue.liang.app.v3.httputils.retrofit2.service.GetDeviceListService;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/26.
 */
public class DevicePresenter implements DeviceContract.Presenter {

    private DeviceContract.View mView;

    public Subscription subscrip;

    public DevicePresenter(DeviceContract.View view) {
        mView = view;
    }

    @Override
    public void loadData(DeviceReqBean bean) {

        String GET_API_URL = "http://182.150.56.73:9003/";
        mView.showLoadingView("");
        Retrofit retrofit= RetrofitFactory.creatorGsonRetrofit(GET_API_URL);
        GetDeviceListService service=    retrofit.create(GetDeviceListService.class);
        subscrip=   service.getDeviceListService(bean).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeviceRespBean>() {
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
                        mView.onError(e.toString());
                    }

                    @Override
                    public void onNext(DeviceRespBean bean) {
                        mView.hideLoadingView();
                        if(bean.getRet_code()==0){
                            mView.onSuccess(bean);
                        }else{
                            mView.onFail(bean);
                        }
                    }
                });


    }
}
