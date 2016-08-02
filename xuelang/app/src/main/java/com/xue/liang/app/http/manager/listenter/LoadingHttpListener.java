package com.xue.liang.app.http.manager.listenter;


import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;


import com.xue.liang.app.dialog.LoadingDialogFragment;
import com.xue.liang.app.http.manager.data.HttpReponse;

/**
 * Created by jk on 2016/8/2.
 */
public class LoadingHttpListener<T> implements HttpListenter<T> {

    private Context mContext;
    private HttpListenter mlistener;
    private FragmentManager mfragmentManager;


    private LoadingDialogFragment loadingDialogFragment;


    public LoadingHttpListener( HttpListenter<T> listener, FragmentManager fragmentManager) {
        mlistener = listener;
       // mContext = context;
        mfragmentManager = fragmentManager;


    }

    public void showDialog() {
        loadingDialogFragment=LoadingDialogFragment.showDialog(mfragmentManager,"Loading");
       // simpleDialogFragment = SimpleDialogFragment.createBuilder(mContext, mfragmentManager).setMessage("加载中。。。。").show();
      //  simpleDialogFragment.show(mfragmentManager, "Loading");

    }

    public void dimissDialog() {
        if(loadingDialogFragment!=null) {
            loadingDialogFragment.dismissAllowingStateLoss();
        }
    }

    @Override
    public void onFailed(String msg) {
        dimissDialog();
        if (mlistener != null) {
            mlistener.onFailed(msg);
        }
    }

    @Override
    public void onSuccess(HttpReponse<T> httpReponse) {
        dimissDialog();
        if (mlistener != null) {
            mlistener.onSuccess(httpReponse);
        }
    }

    /**
     * 确保获取到一个LoadingResponseListener对象
     * 如果指定的listener不是LoadingResponseListener则创建一个LoadingResponseListener并返回
     *
     * @param listener
     * @param fragmentManager
     * @return
     */
    public static <T> HttpListenter<T> ensure(HttpListenter<T> listener, FragmentManager fragmentManager) {
        if (listener instanceof LoadingHttpListener) {
            return listener;
        }
        return new LoadingHttpListener<T>( listener, fragmentManager);
    }
}
