package com.xue.liang.app.v3.activity.newinfo;

import android.os.Bundle;
import android.widget.TextView;

import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseActivity;
import com.xue.liang.app.v3.bean.noticedetail.NoticeDetailReqBean;
import com.xue.liang.app.v3.bean.noticedetail.NoticeDetailRespBean;
import com.xue.liang.app.v3.constant.BundleConstant;
import com.xue.liang.app.v3.constant.LoginInfoUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class NewInfoDetailActivity extends BaseActivity implements NewInfoDetailContract.View {

    @BindView(R.id.text_content)
    TextView text_content;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private NewInfoDetailPresenter newInfoDetailPresenter;


    private String mGuidId = "";


    @Override
    protected void getBundleExtras(Bundle extras) {
        mGuidId = extras.getString(BundleConstant.GUID);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_new_info_detail;
    }

    @Override
    protected void initViews() {
        tv_title.setText(getResources().getString(R.string.newinfodetail));
        newInfoDetailPresenter = new NewInfoDetailPresenter(this);
        NoticeDetailReqBean noticeDetailReqBean = new NoticeDetailReqBean();
        noticeDetailReqBean.setGuid(mGuidId);
        noticeDetailReqBean.setTermi_unique_code(LoginInfoUtils.getInstance().getMacAdrress());
        noticeDetailReqBean.setUser_id(LoginInfoUtils.getInstance().getLoginRespBean().getUser_id());
        newInfoDetailPresenter.loadData(noticeDetailReqBean);
    }

    @Override
    public void onSuccess(NoticeDetailRespBean responseben) {
        text_content.setText(responseben.getContent());
    }

    @Override
    public void onFail(NoticeDetailRespBean responseben) {

    }

    @Override
    public void showLoadingView(String msg) {
        showProgressDialog();

    }

    @Override
    public void hideLoadingView() {
        dimissProgressDialog();

    }

    @Override
    public void onError(String info) {

    }

    @OnClick(R.id.bt_back)
    public void back() {
        finish();
    }
}
