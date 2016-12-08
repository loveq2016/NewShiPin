package com.xue.liang.app.v3.fragment.help;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jph.takephoto.model.TResult;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.adapter.AlarmAdapter;
import com.xue.liang.app.v3.base.BaseTakePhotoFragment;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpReq;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpResp;
import com.xue.liang.app.v3.config.TestData;
import com.xue.liang.app.v3.constant.LoginInfoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/2.
 */
public class HelpFragment extends BaseTakePhotoFragment implements HelpContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.et_info)
    EditText et_info;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private List<String> mdata;

    private AlarmAdapter alarmAdapter;

    private String emptyString = "";

    private int currentChoose = 0;


    private MaterialDialog updateFileMaterialDialog;

    private HelpPresenter helpPresenter;


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
        tv_title.setText("报警求助");
        initData();
        setUpRecyclerView();
        helpPresenter = new HelpPresenter(this);

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_help;
    }

    private AlarmAdapter.OnItemClickListener<String> onItemClickListener = new AlarmAdapter.OnItemClickListener<String>() {
        @Override
        public void onitemClick(int position, String data) {
            currentChoose = position;
            getTakePhoto().onPickFromGallery();

        }
    };

    private void initData() {
        //默认初始化1个数据
        mdata = new ArrayList<>();
        mdata.add(emptyString);

    }

    private void setUpRecyclerView() {
        alarmAdapter = new AlarmAdapter(getContext(), mdata);
        alarmAdapter.setOnitemClickListener(onItemClickListener);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(alarmAdapter);
    }

    @Override
    public void takeSuccess(TResult result) {
        if (currentChoose < mdata.size()) {

            if (TextUtils.isEmpty(mdata.get(currentChoose)) && mdata.size() < 6) {
                mdata.add(emptyString);
            }
            mdata.set(currentChoose, result.getImage().getPath());

            alarmAdapter.setData(mdata);
            alarmAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void onUpdateStartFile() {
        showUpdateProgressDialog();

    }

    @Override
    public void onUpdateProgressUpFile(float progress, long total, int id) {
        if (updateFileMaterialDialog != null) {
            int p = (int) (progress * 100);
            updateFileMaterialDialog.setProgress(p);
        }

    }

    @Override
    public void onUpdateFileFail(String errorinfo) {
        dimissUpdateProgressDialog();
        Toast.makeText(getActivity(), "上传图片失败" + errorinfo, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpdateFileSuccess(List<String> fileList) {
        dimissUpdateProgressDialog();
        Toast.makeText(getActivity(), "上传图片成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAlarmSuccess(AlarmForHelpResp resp) {

        Toast.makeText(getActivity(), "报警成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAlarmFail() {
        Toast.makeText(getActivity(), "报警失败", Toast.LENGTH_SHORT).show();


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
        //上传报警 中没有调用
    }


    protected void showUpdateProgressDialog() {
        boolean showMinMax = true;
        updateFileMaterialDialog = new MaterialDialog.Builder(getActivity())
                .title("上传中")
                .content("请等待")
                .progress(false, 100, showMinMax)
                .show();
        updateFileMaterialDialog.setCancelable(false);
    }

    protected void dimissUpdateProgressDialog() {
        if (updateFileMaterialDialog != null & updateFileMaterialDialog.isShowing()) {
            updateFileMaterialDialog.dismiss();
        }
    }

    @OnClick(R.id.bt_now_alarm)
    public void updatefile() {


        String text = et_info.getText().toString();
        if (TextUtils.isEmpty(text)) {
            text = "";
        }

        AlarmForHelpReq bean = new AlarmForHelpReq();
        bean.setAlarm_text(text);
        bean.setTermi_type("2");
        bean.setUser_id(LoginInfoUtils.getInstance().getLoginRespBean().getUser_id());


        List<String> filelist = new ArrayList<>();
        for (String data : mdata) {
            if (!TextUtils.isEmpty(data)) {
                filelist.add(data);
            }
        }
        if (!filelist.isEmpty()) {
            helpPresenter.updateFileAndAlarm(filelist, bean);
        } else {
            helpPresenter.doAlarmAfterUpdataFile(bean);
        }

    }


}
