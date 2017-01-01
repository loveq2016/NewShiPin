package com.ysh.tv.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class ReadFileActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readfilelayout);
        TextView textView=(TextView) findViewById(R.id.tv_file);
        TextView tvText=(TextView) findViewById(R.id.title);
        View top_bg=(View) findViewById(R.id.top_bg);
       String str=   getIntent().getStringExtra("str");
      String str02=   getIntent().getStringExtra("str2");
       String title=   getIntent().getStringExtra("title");
        int data=getIntent().getIntExtra("data",1);
        if(!TextUtils.isEmpty(str))
        {

            if(!TextUtils.isEmpty(str02))
            {
                String strr=str+"\n"+str02;
                textView.setText(strr);
            }
            else
            {
                textView.setText(str);

            }

        }
        if(!TextUtils.isEmpty(title))

        {
            tvText.setText(title);
        }

        switch (data)
        {
            case 1:
                //top_bg.setBackgroundColor(getResources().getColor(R.mipmap.yangguandanwu_top));
                top_bg.setBackgroundResource(R.mipmap.yangguandanwu_top);
                break;
            case 2:
                top_bg.setBackgroundResource(R.mipmap.wendatoupiao_top);
                break;
            case 3:
                top_bg.setBackgroundResource(R.mipmap.pinganjiayuan_top);
                break;
            case 4:
                top_bg.setBackgroundResource(R.mipmap.wenxinbobao_top);
                break;
            case 5:
                top_bg.setBackgroundResource(R.mipmap.sifayangguan_top);
                break;
            case 6:
                top_bg.setBackgroundResource(R.mipmap.mingshengfuwu_top);
                break;

        }




    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            ReadFileActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
