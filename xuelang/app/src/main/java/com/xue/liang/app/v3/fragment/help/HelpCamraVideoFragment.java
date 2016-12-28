package com.xue.liang.app.v3.fragment.help;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xue.liang.app.R;
import com.xue.liang.app.v3.base.BaseFragment;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpReq;
import com.xue.liang.app.v3.bean.updatealarm.AlarmForHelpResp;
import com.xue.liang.app.v3.constant.LoginInfoUtils;
import com.xue.liang.app.v3.utils.XPermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by jikun on 16/12/24.
 */

public class HelpCamraVideoFragment extends BaseFragment implements HelpContract.View  {


    public static final int CARMERA = 1;//拍照

    public static final int VIDEO = 2;//录像


    private String imageFilePath;


    private String videoFilePath;

    private HelpPresenter helpPresenter;

    private MaterialDialog updateFileMaterialDialog;


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

        helpPresenter = new HelpPresenter(this);

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_help_camra_video;
    }

    @OnClick(R.id.bt_now_alarm_camar)
    public void openCamre() {
        XPermissionUtils.requestPermissions(getActivity(), 1, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory() + "/Images");
                if (!file.exists()) {
                    file.mkdirs();
                }
                imageFilePath = Environment.getExternalStorageDirectory() + "/Images/" +
                        "cameraImg" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                Uri mUri = Uri.fromFile(
                        new File(imageFilePath));
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);
                cameraIntent.putExtra("return-data", true);
                startActivityForResult(cameraIntent, CARMERA);

            }

            @Override
            public void onPermissionDenied() {
                showToast("相机权限被禁止");

            }
        });

    }


    @OnClick(R.id.bt_now_alarm_video)
    public void openVideo(){
        XPermissionUtils.requestPermissions(getActivity(), 1, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory() + "/Video");
                if (!file.exists()) {
                    file.mkdirs();
                }
                videoFilePath = Environment.getExternalStorageDirectory() + "/Video/" +
                        "VideoImg" + String.valueOf(System.currentTimeMillis()) + ".mp4";
                Uri mUri = Uri.fromFile(
                        new File(videoFilePath));
                videoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);
                videoIntent.putExtra("return-data", true);
                startActivityForResult(videoIntent, VIDEO);

            }

            @Override
            public void onPermissionDenied() {
                showToast("相机权限被禁止");

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CARMERA && resultCode == Activity.RESULT_OK) {
            List<String> fileList = new ArrayList<>();
            //Uri uri = data.getParcelableExtra(android.provider.MediaStore.EXTRA_OUTPUT);

            fileList.add(imageFilePath);

            AlarmForHelpReq bean = new AlarmForHelpReq();
            bean.setAlarm_text("");
            bean.setTermi_type("2");
            bean.setUser_id(LoginInfoUtils.getInstance().getLoginRespBean().getUser_id());
            helpPresenter.updateFileAndAlarm(fileList, bean);

        }
        else if (requestCode == VIDEO&& resultCode == Activity.RESULT_OK) {
            List<String> fileList = new ArrayList<>();
            //Uri uri = data.getParcelableExtra(android.provider.MediaStore.EXTRA_OUTPUT);

            fileList.add(videoFilePath);

            AlarmForHelpReq bean = new AlarmForHelpReq();
            bean.setAlarm_text("");
            bean.setTermi_type("2");
            bean.setUser_id(LoginInfoUtils.getInstance().getLoginRespBean().getUser_id());
            helpPresenter.updateFileAndAlarm(fileList, bean);
        }
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
        Toast.makeText(getActivity(), "上传文件失败" + errorinfo, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateFileSuccess(List<String> fileList) {
        dimissUpdateProgressDialog();
        Toast.makeText(getActivity(), "上传文件成功", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void onError(String info) {

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
}
