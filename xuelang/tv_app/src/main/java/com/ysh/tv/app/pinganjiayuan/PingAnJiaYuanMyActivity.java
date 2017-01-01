package com.ysh.tv.app.pinganjiayuan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import com.ysh.tv.app.MyListView;
import com.ysh.tv.app.R;
import com.ysh.tv.app.ReadFileActivity;
import com.ysh.tv.app.adapter.MyAdapter;
import com.ysh.tv.app.adapter.SubAdapter;


public class PingAnJiaYuanMyActivity extends Activity {
    private MyListView listView;
   // private ListView listView;
    private MyListView subListView;
    private MyAdapter myAdapter;
    private SubAdapter subAdapter;

    private static Boolean isfrist=true;


    String cities[][] = new String[][] {
            new String[] {"1、国家安全法", "2、婚姻法","3、反恐怖主义法","4、道路交通安全法","5、反家庭暴力法","6、食品安全法","7、农村宅基地管理条例","8、社会治安管理条例"},
            new String[] {"1、四川省依法治市领导小组办公室关于印发《举办全市“法律七进”暨基层法治示范创建工作业务培训班工作方案》的通知",
                    "2、四川省依法治市领导小组办公室关于做好创新工作和专项治理工作的通知"},
            new String[] {"1、什么是网格化服务", "2、网格化服务的好处","3、平安网格“五五”标准","4、1+2+x服务模式"},

    };
    String foods[] =new String[]{"法律法规","依法治省","网格化服务管理"};
    private int data;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinganjiayuan_main);
        init();
        myAdapter=new MyAdapter(getApplicationContext(), foods);
        listView.setAdapter(myAdapter);
        subAdapter=new SubAdapter(getApplicationContext(), cities);
        subListView.setAdapter(subAdapter);
      if(isfrist)
        {
            myAdapter.setSelectItem(0);
            myAdapter.notifyDataSetChanged();
            subListView.setSelection(0);
            subAdapter.notifyDataSetChanged();
        }
        data = getIntent().getIntExtra("data",3);

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

        switch (keyCode)
        {

            case KeyEvent.KEYCODE_DPAD_LEFT:
               int pis=  subListView.getSelectedItemPosition();
               int ps=  listView.getSelectedItemPosition();
                Message msg=new Message();
                msg.what=0;
                msg.obj=ps;
                msg.arg1=pis;
                handler.sendMessage(msg);



                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Message msg1=new Message();
                msg1.what=1;
                msg1.obj=0;
                handler.sendMessage(msg1);


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
                Intent intent = new Intent(PingAnJiaYuanMyActivity.this, ReadFileActivity.class);
                int pst=   subAdapter.getPosition();

                Log.v("ssjklf pst===" + pst, "11111");

                switch (pst)
                {
                    case 0:
                        switch (position)
                        {
                            case 0:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt01);
                                intent.putExtra("title", cities[pst][position]);
                                intent.putExtra("data",data);
                                isfrist = false;
                                startActivity(intent);
                                break;
                            case 1:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt02);
                                intent.putExtra("title", cities[pst][position]);
                                intent.putExtra("data",data);
                                isfrist = false;
                                startActivity(intent);
                                break;

                            case 2:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt03);
                                intent.putExtra("title", cities[pst][position]);
                                intent.putExtra("data",data);
                                isfrist = false;
                                startActivity(intent);
                                break;
                            case 3:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt04);
                                intent.putExtra("title", cities[pst][position]);
                                intent.putExtra("data",data);
                                isfrist = false;
                                startActivity(intent);
                                break;
                            case 4:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt05);
                                intent.putExtra("title", cities[pst][position]);
                                intent.putExtra("data",data);
                                isfrist = false;
                                startActivity(intent);
                                break;
                            case 5:
                            intent.putExtra("str", PingAnJiaYuanConstant.txt06);
                            intent.putExtra("title", cities[pst][position]);
                                intent.putExtra("data",data);
                            isfrist = false;
                            startActivity(intent);
                            break;
                            case 6:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt07);
                                intent.putExtra("title", cities[pst][position]);
                                intent.putExtra("data",data);
                                isfrist = false;
                                startActivity(intent);
                                break;
                            case 7:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt08);
                                intent.putExtra("title", cities[pst][position]);
                                intent.putExtra("data",data);
                                isfrist = false;
                                startActivity(intent);
                                break;


                        }



                        break;
                    case 1:
                        switch (position)
                        {
                            case 0:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt21);
                                intent.putExtra("data",data);
                                intent.putExtra("title", cities[pst][position]);
                                isfrist = false;
                                startActivity(intent);
                                break;
                            case 1:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt22);
                                intent.putExtra("data",data);
                                intent.putExtra("title", cities[pst][position]);
                                isfrist = false;
                                startActivity(intent);
                                break;

                        }




                        break;
                    case 2:
                        switch (position)
                        {
                            case 0:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt31);
                                intent.putExtra("data",data);
                                intent.putExtra("title", cities[pst][position]);
                                isfrist = false;
                                startActivity(intent);
                                break;
                            case 1:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt32);
                                intent.putExtra("data",data);
                                intent.putExtra("title", cities[pst][position]);
                                isfrist = false;
                                startActivity(intent);
                                break;
                            case 2:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt33);
                                intent.putExtra("data",data);
                                intent.putExtra("title", cities[pst][position]);
                                isfrist = false;
                                startActivity(intent);
                                break;
                            case 3:
                                intent.putExtra("str", PingAnJiaYuanConstant.txt34);
                                intent.putExtra("data",data);
                                intent.putExtra("title", cities[pst][position]);
                                isfrist = false;
                                startActivity(intent);
                                break;

                        }


                        break;



                }

            }
        });
    }



}
