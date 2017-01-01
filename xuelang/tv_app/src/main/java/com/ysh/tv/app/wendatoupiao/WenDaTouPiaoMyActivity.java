package com.ysh.tv.app.wendatoupiao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import com.ysh.tv.app.MyListView;
import com.ysh.tv.app.R;
import com.ysh.tv.app.ReadFileActivity;
import com.ysh.tv.app.adapter.MyAdapter;
import com.ysh.tv.app.adapter.SubAdapter;


public class WenDaTouPiaoMyActivity extends Activity {
    private MyListView listView;
   // private ListView listView;
    private MyListView subListView;
    private MyAdapter myAdapter;
    private SubAdapter subAdapter;

    private static Boolean isfrist=true;


    String cities[][] = new String[][] {
            new String[] {"1、法治问卷", "2、社会评价问卷", "3、平安建设群众满意度测评问卷"},

    };
    String foods[] =new String[]{"政务服务","便民服务","科技服务","咨询电话","农产品交易信"};
    private int data;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wendatoupiao_layout);
        init();
        myAdapter=new MyAdapter(getApplicationContext(), foods);
        listView.setAdapter(myAdapter);
        subAdapter=new SubAdapter(getApplicationContext(), cities);
        subListView.setAdapter(subAdapter);
        data = getIntent().getIntExtra("data", 2);
      if(isfrist)
        {
            myAdapter.setSelectItem(0);
            myAdapter.notifyDataSetChanged();
            subListView.setSelection(0);
            subAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        method();
    }

    private void init(){
        listView=(MyListView) findViewById(R.id.listView);
        subListView=(MyListView) findViewById(R.id.subListView);
    }


    private void method()
    {

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positions, long id) {

                myAdapter.setSelectItem(positions);
                myAdapter.notifyDataSetChanged();
                subAdapter.setPosition(positions);
                subAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                // TODO Auto-generated method stub



                myAdapter.setSelectedPosition(position);
                myAdapter.setSelectItem(position);
                myAdapter.notifyDataSetChanged();
                subAdapter.setPosition(position);
                subAdapter.notifyDataSetChanged();
                sublick();

            }
        });
      sublick();



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


       if(keyCode==KeyEvent.KEYCODE_BACK)
       {
           WenDaTouPiaoMyActivity.this.finish();
       }
        return super.onKeyDown(keyCode, event);
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int what=(Integer)msg.what;
            switch (what)
            {
                case 0:
                    listView.setSelection((Integer) msg.obj);
                    myAdapter.notifyDataSetChanged();

                    subListView.setSelection(0);
                    subListView.setSelected(false);
                    subListView.setPressed(false);

                    subAdapter.notifyDataSetChanged();

                   // subAdapter.notifyDataSetChanged();

                    break;
                case 1:
                    subListView.setSelection((Integer)msg.obj);
                    subAdapter.notifyDataSetChanged();
                    break;
            }

            return false;
        }
    });


    private void sublick()
    {
        subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long arg3) {



                Intent intent = new Intent(WenDaTouPiaoMyActivity.this, ReadFileActivity.class);
                int pst=   subAdapter.getPosition();


                        switch (position)
                        {
                            case 0:
                                intent.putExtra("str", WenDaTouPiaoConstant.txt01);
                                intent.putExtra("title", cities[pst][position]);
                                intent.putExtra("data",data);
                                isfrist = false;
                                startActivity(intent);
                                break;
                            case 1:
                                intent.putExtra("str", WenDaTouPiaoConstant.txt02);
                                intent.putExtra("title", cities[pst][position]);
                                intent.putExtra("data",data);
                                isfrist = false;
                                startActivity(intent);
                                break;
                            case 2:
                                intent.putExtra("str", WenDaTouPiaoConstant.txt03);
                                intent.putExtra("title", cities[pst][position]);
                                intent.putExtra("data",data);
                                isfrist = false;
                                startActivity(intent);
                                break;

                        }










            }
        });
    }



}
