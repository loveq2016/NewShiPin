package com.xue.liang.app.v3.fragment.device;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.PlayerAdapter;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.bean.device.DeviceReqBean;
import com.xue.liang.app.v3.bean.device.DeviceRespBean;
import com.xue.liang.app.v3.config.TestData;
import com.xue.liang.app.v3.event.UrlEvent;
import com.xue.liang.app.v3.fragment.player.PlayerFragment;
import com.xue.liang.app.v3.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/11/2.
 */
public class DeviceFragment extends BaseFragment implements DeviceContract.View {
    @BindView(R.id.listview)
    ListView listview;

    private MaterialDialog materialDialog;


    private PlayerAdapter playerAdapter;
    private DevicePresenter devicePresenter;

    private List<DeviceRespBean.ResponseBean> dataList;

    @Override
    protected void onFirstUserVisible() {


    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void initViews() {
        devicePresenter = new DevicePresenter(this);

        setupListView();
        initPlayerFragment();

        onRefreshData();


    }

    private void onRefreshData() {
        String type = DeviceUtil.getWhickPhoneType(getContext());
        DeviceReqBean deviceReqBean = new DeviceReqBean();
        deviceReqBean.setReg_tel(TestData.phoneNum);
        deviceReqBean.setTermi_type(type);
        deviceReqBean.setTermi_unique_code(TestData.termi_unique_code);

        devicePresenter.loadData(deviceReqBean);

    }

    private void setupListView() {
        dataList = new ArrayList<>();

        playerAdapter = new PlayerAdapter(getContext(), dataList);
        listview.setAdapter(playerAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                listview.setItemChecked(position, true);
                String deviceid = dataList.get(position).getDev_id();
                String devicename = dataList.get(position).getDev_name();
                String url = dataList.get(position).getDev_url();
                EventBus.getDefault().post(new UrlEvent(url));

            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_player;
    }

    @Override
    public void onSuccess(DeviceRespBean deviceRespBean) {

        if (deviceRespBean != null) {
            dataList=deviceRespBean.getResponse();
            playerAdapter.reshData(dataList);
        }


    }

    @Override
    public void onFail(DeviceRespBean userInfo) {

    }

    @Override
    public void showLoadingView(String msg) {

showIndeterminateProgressDialog(false);
    }

    @Override
    public void hideLoadingView() {
        if(materialDialog.isShowing()){
            materialDialog.dismiss();
        }

    }

    @Override
    public void onError(String info) {

    }

    private void initPlayerFragment() {
        PlayerFragment playerFragment = new PlayerFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.playerFrament, playerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void showIndeterminateProgressDialog(boolean horizontal) {
        materialDialog= new MaterialDialog.Builder(getActivity())
                .title("title")
                .content("content")
                .progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .show();
    }
}
