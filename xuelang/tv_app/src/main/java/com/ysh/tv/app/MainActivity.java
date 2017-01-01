package com.ysh.tv.app;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ysh.tv.app.mingshengfuwu.MingShengFuWuMyActivity;
import com.ysh.tv.app.pinganjiayuan.PingAnJiaYuanMyActivity;
import com.ysh.tv.app.sifayangguan.SiFaYangGuanMyActivity;
import com.ysh.tv.app.wendatoupiao.WenDaTouPiaoMyActivity;
import com.ysh.tv.app.wenxinbobao.WenXinBoBaoMyActivity;
import com.ysh.tv.app.yangguangduanwu.YangGuanDangWuMainActivity;

import reco.frame.tv.view.TvRelativeLayoutAsGroup;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
    private int height;
    private int screenW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_layout);


        initview();
        setonClick();

        int screenH = getWindowManager().getDefaultDisplay().getHeight();
        screenW = getWindowManager().getDefaultDisplay().getWidth();
        // 获取状态栏的高
        int statusBarH = xutils.getStatusBar(MainActivity.this);
        height = screenH - statusBarH;
        height = (height - 20) / 3;
        screenW = (screenW - 40) / 5;
        setParams();


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


    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.pinganjiayuan) {
            Intent pinganjiayuanIntent = new Intent(MainActivity.this, PingAnJiaYuanMyActivity.class);
            if (pinganjiayuanIntent != null) {
                pinganjiayuanIntent.getIntExtra("data", 3);
                startActivity(pinganjiayuanIntent);
            } else {
                Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.yangguansifa) {
            Intent yangguansifaIntent = new Intent(MainActivity.this, SiFaYangGuanMyActivity.class);

            if (yangguansifaIntent != null) {
                yangguansifaIntent.putExtra("data", 5);
                startActivity(yangguansifaIntent);

            } else {
                Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.dianshijiemu) {
            Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.jiatinyinyuan) {
            Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.dianzishangcheng) {

            Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.wendatoupiao) {
            Intent wendatoupiaoIntent = new Intent(MainActivity.this, WenDaTouPiaoMyActivity.class);
            // Intent wendatoupiaoIntent = getPackageManager().getLaunchIntentForPackage("com.ysh.helloworld51");
            if (wendatoupiaoIntent != null) {
                wendatoupiaoIntent.putExtra("data", 2);
                startActivity(wendatoupiaoIntent);
            } else {
                Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
            }


        } else if (v.getId() == R.id.wenxinbobao) {

            //Intent wenxinbobaoIntent = getPackageManager().getLaunchIntentForPackage("helloworld.ysh.com.helloworld03");
            Intent wenxinbobaoIntent = new Intent(MainActivity.this, WenXinBoBaoMyActivity.class);
            if (wenxinbobaoIntent != null) {
                wenxinbobaoIntent.putExtra("data", 4);
                startActivity(wenxinbobaoIntent);
            } else {
                Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.jiankong) {

            Intent jiankong = getPackageManager().getLaunchIntentForPackage("com.xue.liang.app");
            if (jiankong != null) {
                startActivity(jiankong);
            } else {
                Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.mingshengfuwu) {
            Intent mingshengfuwuIntent = new Intent(MainActivity.this, MingShengFuWuMyActivity.class);
            if (mingshengfuwuIntent != null) {
                mingshengfuwuIntent.putExtra("data", 6);
                startActivity(mingshengfuwuIntent);
            } else {
                Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
            }


        } else if (v.getId() == R.id.yangguangduanwu) {

                // Intent yangguangduanwuIntent = getPackageManager().getLaunchIntentForPackage("com.ysh.demo");
                Intent yangguangduanwuIntent = new Intent(MainActivity.this, YangGuanDangWuMainActivity.class);
                yangguangduanwuIntent.putExtra("data", 1);
                if (yangguangduanwuIntent != null) {
                    startActivity(yangguangduanwuIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
                }




        }


//        switch (v.getId()) {
//            case R.id.pinganjiayuan:
//
//                Intent pinganjiayuanIntent = new Intent(MainActivity.this, PingAnJiaYuanMyActivity.class);
//                if (pinganjiayuanIntent != null) {
//                    pinganjiayuanIntent.getIntExtra("data", 3);
//                    startActivity(pinganjiayuanIntent);
//                } else {
//                    Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.yangguansifa:
//                Intent yangguansifaIntent = new Intent(MainActivity.this, SiFaYangGuanMyActivity.class);
//
//                if (yangguansifaIntent != null) {
//                    yangguansifaIntent.putExtra("data", 5);
//                    startActivity(yangguansifaIntent);
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
//            }
//
//            break;
//            case R.id.wendatoupiao:
//                Intent wendatoupiaoIntent = new Intent(MainActivity.this, WenDaTouPiaoMyActivity.class);
//                // Intent wendatoupiaoIntent = getPackageManager().getLaunchIntentForPackage("com.ysh.helloworld51");
//                if (wendatoupiaoIntent != null) {
//                    wendatoupiaoIntent.putExtra("data", 2);
//                    startActivity(wendatoupiaoIntent);
//                } else {
//                    Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//            case R.id.wenxinbobao:
//                //Intent wenxinbobaoIntent = getPackageManager().getLaunchIntentForPackage("helloworld.ysh.com.helloworld03");
//                Intent wenxinbobaoIntent = new Intent(MainActivity.this, WenXinBoBaoMyActivity.class);
//                if (wenxinbobaoIntent != null) {
//                    wenxinbobaoIntent.putExtra("data", 4);
//                    startActivity(wenxinbobaoIntent);
//                } else {
//                    Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_LONG).show();
//                }
//
//                break;
//            case R.id.jiankong:
//                Intent jiankong = getPackageManager().getLaunchIntentForPackage("com.xue.liang.app");
//                if (jiankong != null) {
//                    startActivity(jiankong);
//                } else {
//                    Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//            case R.id.mingshengfuwu:
//                Intent mingshengfuwuIntent = new Intent(MainActivity.this, MingShengFuWuMyActivity.class);
//                if (mingshengfuwuIntent != null) {
//                    mingshengfuwuIntent.putExtra("data", 6);
//                    startActivity(mingshengfuwuIntent);
//                } else {
//                    Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//            case R.id.yangguangduanwu:
//                // Intent yangguangduanwuIntent = getPackageManager().getLaunchIntentForPackage("com.ysh.demo");
//                Intent yangguangduanwuIntent = new Intent(MainActivity.this, YangGuanDangWuMainActivity.class);
//                yangguangduanwuIntent.putExtra("data", 1);
//                if (yangguangduanwuIntent != null) {
//                    startActivity(yangguangduanwuIntent);
//                } else {
//                    Toast.makeText(getApplicationContext(), "暂无", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    private void setParams() {

        pinganjiayuan(pinganjiayuan, screenW, height, 0, 0, 0, 0);
        yangguanshifa(yangguansifa, screenW, height, 0, 10, 0, 10);
        yangguandangwu(yangguangduanwu, screenW * 2 + 10, height * 2 + 10, 10, 0, 10, 10);
        mingshengfuwu(mingshengfuwu, 2 * screenW + 10, height, 0, 0, 0, 0);
        xueyangjiangkong(jiankong, 2 * screenW + 10, height, 0, 10, 0, 10);

        dianshijiemu(dianshijiemu, screenW, height, 0, 0, 0, 0);
        jiatingyinyuan(jiatinyinyuan, screenW, height, 10, 0, 0, 0);
        dianzhishangchen(dianzishangcheng, screenW, height, 10, 0, 0, 0);
        wendatoupiao(wendatoupiao, screenW, height, 10, 0, 0, 0);
        wenxinbobao(wenxinbobao, screenW, height, 10, 0, 0, 0);
    }

    public static void pinganjiayuan(View v, int layOutWidth, int layOutHeight,
                                     int left, int top, int right, int buttom) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                layOutWidth, layOutHeight);
        lp.setMargins(left, top, right, buttom);
        v.setLayoutParams(lp);
    }

    public static void yangguanshifa(View v, int layOutWidth, int layOutHeight,
                                     int left, int top, int right, int buttom) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                layOutWidth, layOutHeight);
        lp.setMargins(left, top, right, buttom);
        lp.addRule(RelativeLayout.BELOW, R.id.pinganjiayuan);
        v.setLayoutParams(lp);
    }

    public static void yangguandangwu(View v, int layOutWidth, int layOutHeight,
                                      int left, int top, int right, int buttom) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                layOutWidth, layOutHeight);
        lp.setMargins(left, top, right, buttom);
        lp.addRule(RelativeLayout.RIGHT_OF, R.id.pinganjiayuan);
        //lp.addRule(RelativeLayout.LEFT_OF,R.id.pinganjiayuan);
        v.setLayoutParams(lp);
    }

    public static void mingshengfuwu(View v, int layOutWidth, int layOutHeight,
                                     int left, int top, int right, int buttom) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                layOutWidth, layOutHeight);
        lp.setMargins(left, top, right, buttom);
        lp.addRule(RelativeLayout.RIGHT_OF, R.id.yangguangduanwu);
        v.setLayoutParams(lp);
    }

    public static void xueyangjiangkong(View v, int layOutWidth, int layOutHeight,
                                        int left, int top, int right, int buttom) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                layOutWidth, layOutHeight);
        lp.setMargins(left, top, right, buttom);
        lp.addRule(RelativeLayout.BELOW, R.id.mingshengfuwu);
        lp.addRule(RelativeLayout.RIGHT_OF, R.id.yangguangduanwu);
        lp.addRule(RelativeLayout.ALIGN_LEFT, R.id.mingshengfuwu);


        v.setLayoutParams(lp);
    }

    public static void dianshijiemu(View v, int layOutWidth, int layOutHeight,
                                    int left, int top, int right, int buttom) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                layOutWidth, layOutHeight);
        lp.setMargins(left, top, right, buttom);
        lp.addRule(RelativeLayout.BELOW, R.id.yangguansifa);
        lp.addRule(RelativeLayout.ALIGN_LEFT, R.id.yangguansifa);

        v.setLayoutParams(lp);
    }

    public static void jiatingyinyuan(View v, int layOutWidth, int layOutHeight,
                                      int left, int top, int right, int buttom) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                layOutWidth, layOutHeight);
        lp.setMargins(left, top, right, buttom);
        lp.addRule(RelativeLayout.RIGHT_OF, R.id.dianshijiemu);
        lp.addRule(RelativeLayout.BELOW, R.id.yangguangduanwu);
        v.setLayoutParams(lp);
    }

    public static void dianzhishangchen(View v, int layOutWidth, int layOutHeight,
                                        int left, int top, int right, int buttom) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                layOutWidth, layOutHeight);
        lp.setMargins(left, top, right, buttom);
        lp.addRule(RelativeLayout.RIGHT_OF, R.id.jiatinyinyuan);
        lp.addRule(RelativeLayout.BELOW, R.id.yangguangduanwu);
        v.setLayoutParams(lp);
    }

    public static void wendatoupiao(View v, int layOutWidth, int layOutHeight,
                                    int left, int top, int right, int buttom) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                layOutWidth, layOutHeight);
        lp.setMargins(left, top, right, buttom);
        lp.addRule(RelativeLayout.RIGHT_OF, R.id.dianzishangcheng);
        lp.addRule(RelativeLayout.BELOW, R.id.jiankong);
        v.setLayoutParams(lp);
    }

    public static void wenxinbobao(View v, int layOutWidth, int layOutHeight,
                                   int left, int top, int right, int buttom) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                layOutWidth, layOutHeight);
        lp.setMargins(left, top, right, buttom);
        lp.addRule(RelativeLayout.RIGHT_OF, R.id.wendatoupiao);
        lp.addRule(RelativeLayout.BELOW, R.id.jiankong);
        v.setLayoutParams(lp);
    }

}
