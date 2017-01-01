package com.ysh.tv.app;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ysh.tv.app.entity.AppPackageInfo;
import com.ysh.tv.app.mingshengfuwu.MingShenFuWuConstant;
import com.ysh.tv.app.mingshengfuwu.MingShengFuWuMyActivity;
import com.ysh.tv.app.pinganjiayuan.PingAnJiaYuanMyActivity;
import com.ysh.tv.app.sifayangguan.SiFaYangGuanMyActivity;
import com.ysh.tv.app.wendatoupiao.WenDaTouPiaoMyActivity;
import com.ysh.tv.app.wenxinbobao.WenXinBoBaoMyActivity;
import com.ysh.tv.app.yangguangduanwu.YangGuanDangWuMainActivity;

import java.util.ArrayList;
import java.util.List;

import reco.frame.tv.view.TvRelativeLayoutAsGroup;

/**
 * Created by Administrator on 2016/11/9.
 */
public class TestActivity  extends AppCompatActivity implements View.OnClickListener{

    private Button pinganjiayuan;
    private Button yangguansifa;
    private Button dianshijiemu;
    private Button jiatinyinyuan;
    private Button dianzishangcheng;
    private Button wendatoupiao;
    private Button wenxinbobao;
    private Button jiankong;
    private Button mingshengfuwu;
    private Button yangguangduanwu;

    private ImageView currentImage;
    private int height;
    private int screenW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_layout);
        //setContentView(R.layout.content_main);

        initview();
        setonClick();

        method();

    }

    private void method() {
        ArrayList<AppPackageInfo> appList = new ArrayList<AppPackageInfo>();
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);

        for(int i=0;i<packages.size();i++) {
            PackageInfo packageInfo = packages.get(i);
            AppPackageInfo tmpInfo = new AppPackageInfo();
         //   tmpInfo.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            tmpInfo.setPackageName( packageInfo.packageName);
            tmpInfo.setVersionName( packageInfo.versionName);
            tmpInfo.setVersionCode(  packageInfo.versionCode);

            appList.add(tmpInfo);
            Log.i("tag", tmpInfo.toString());

        }
    }

    private void setonClick() {
        pinganjiayuan.setOnClickListener(this);
        yangguansifa.setOnClickListener(this);
        dianshijiemu.setOnClickListener(this);
        jiatinyinyuan.setOnClickListener(this);
        dianzishangcheng.setOnClickListener(this);
        wendatoupiao.setOnClickListener(this);
        jiankong.setOnClickListener(this);
        mingshengfuwu.setOnClickListener(this);
        yangguangduanwu.setOnClickListener(this);
        wenxinbobao.setOnClickListener(this);
    }

    //初始化控件
    private void initview() {
        pinganjiayuan = (Button) findViewById(R.id.pinganjiayuan);
        yangguansifa = (Button) findViewById(R.id.yangguansifa);
        dianshijiemu = (Button) findViewById(R.id.dianshijiemu);
        jiatinyinyuan = (Button) findViewById(R.id.jiatinyinyuan);
        dianzishangcheng = (Button) findViewById(R.id.dianzishangcheng);
        wendatoupiao = (Button) findViewById(R.id.wendatoupiao);
        wenxinbobao = (Button) findViewById(R.id.wenxinbobao);
        jiankong = (Button) findViewById(R.id.jiankong);
        mingshengfuwu = (Button) findViewById(R.id.mingshengfuwu);
        yangguangduanwu = (Button) findViewById(R.id.yangguangduanwu);
        final TvRelativeLayoutAsGroup  layout = (TvRelativeLayoutAsGroup) findViewById(R.id.layout);

        xutils.initView(pinganjiayuan,300,200,0,0,0,0);
        xutils.initView(yangguansifa,300,200,0,0,5,0);

        dianshijiemu.setVisibility(View.GONE);
        jiatinyinyuan.setVisibility(View.GONE);
        dianzishangcheng.setVisibility(View.GONE);
        wendatoupiao.setVisibility(View.GONE);
        wenxinbobao.setVisibility(View.GONE);
        jiankong.setVisibility(View.GONE);
        mingshengfuwu.setVisibility(View.GONE);
        yangguangduanwu.setVisibility(View.GONE);


    }


    @Override
    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.pinganjiayuan:
//
//                Intent pinganjiayuanIntent = new Intent(TestActivity.this, PingAnJiaYuanMyActivity.class);
//                // Intent pinganjiayuanIntent = getPackageManager().getLaunchIntentForPackage("com.ysh.helloworld04");
//                if (pinganjiayuanIntent!=null)
//                {
//                    pinganjiayuanIntent.getIntExtra("data",3);
//                    startActivity(pinganjiayuanIntent);
//                }else
//                {
//                    Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
//                }
//              /*  currentImage.setImageDrawable(null);
//                pinganjiayuan.setImageResource(R.drawable.boder_bg);
//                currentImage=pinganjiayuan;*/
//                break;
//            case R.id.yangguansifa:
//                Intent yangguansifaIntent =new Intent(TestActivity.this, SiFaYangGuanMyActivity.class);
//
//                if (yangguansifaIntent!=null)
//                {
//                    yangguansifaIntent.putExtra("data",5);
//                    startActivity(yangguansifaIntent);
//
//                }else
//                {
//                    Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
//                }
//                //   currentImage.setImageDrawable(null);
//                // currentImage=yangguansifa;
//
//                break;
//            case R.id.dianshijiemu:
//              /*  Intent dianshijiemu = getPackageManager().getLaunchIntentForPackage("helloworld.ysh.com.helloworld02");
//                if (dianshijiemu!=null)
//                {
//                    startActivity(dianshijiemu);
//                }else*/
//            {
//                Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
//            }
//
//            break;
//            case R.id.jiatinyinyuan:
//              /*  Intent jiatinyinyuan = getPackageManager().getLaunchIntentForPackage("helloworld.ysh.com.helloworld02");
//                if (jiatinyinyuan!=null)
//                {
//                    startActivity(jiatinyinyuan);
//                }else*/
//            {
//                Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
//            }
//
//            break;
//            case R.id.dianzishangcheng:
//              /*  Intent dianzishangcheng = getPackageManager().getLaunchIntentForPackage("helloworld.ysh.com.helloworld02");
//                if (dianzishangcheng!=null)
//                {
//                    startActivity(dianzishangcheng);
//                }else*/
//            {
//                Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
//            }
//
//            break;
//            case R.id.wendatoupiao:
//                Intent wendatoupiaoIntent =new Intent(TestActivity.this, WenDaTouPiaoMyActivity.class);
//                // Intent wendatoupiaoIntent = getPackageManager().getLaunchIntentForPackage("com.ysh.helloworld51");
//                if (wendatoupiaoIntent!=null)
//                {
//                    wendatoupiaoIntent.putExtra("data",2);
//                    startActivity(wendatoupiaoIntent);
//                }else
//                {
//                    Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
//                }
//
//                break;
//            case R.id.wenxinbobao:
//                //Intent wenxinbobaoIntent = getPackageManager().getLaunchIntentForPackage("helloworld.ysh.com.helloworld03");
//                Intent wenxinbobaoIntent =new Intent(TestActivity.this, WenXinBoBaoMyActivity.class);
//                if (wenxinbobaoIntent!=null)
//                {
//                    wenxinbobaoIntent.putExtra("data",4);
//                    startActivity(wenxinbobaoIntent);
//                }else
//                {
//                    Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
//                }
//
//                break;
//            case R.id.jiankong:
//              /*  Intent jiankong = getPackageManager().getLaunchIntentForPackage("helloworld.ysh.com.helloworld02");
//                if (jiankong!=null)
//                {
//                    startActivity(jiankong);
//                }else*/
//            {
//                Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
//            }
//
//            break;
//            case R.id.mingshengfuwu:
//                Intent mingshengfuwuIntent = new Intent(TestActivity.this, MingShengFuWuMyActivity.class);
//                if (mingshengfuwuIntent!=null)
//                {
//                    mingshengfuwuIntent.putExtra("data",6);
//                    startActivity(mingshengfuwuIntent);
//                }else
//                {
//                    Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
//                }
//
//                break;
//            case R.id.yangguangduanwu:
//                // Intent yangguangduanwuIntent = getPackageManager().getLaunchIntentForPackage("com.ysh.demo");
//                Intent yangguangduanwuIntent = new Intent(TestActivity.this, YangGuanDangWuMainActivity.class);
//                yangguangduanwuIntent.putExtra("data",1);
//                if (yangguangduanwuIntent!=null)
//                {
//                    startActivity(yangguangduanwuIntent);
//                }else
//                {
//                    Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
//                }
//
//                break;
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
   }

