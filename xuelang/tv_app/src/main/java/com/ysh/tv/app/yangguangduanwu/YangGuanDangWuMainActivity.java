package com.ysh.tv.app.yangguangduanwu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.ysh.tv.app.MyListView;
import com.ysh.tv.app.R;
import com.ysh.tv.app.ReadFileActivity;
import com.ysh.tv.app.adapter.MyAdapter;
import com.ysh.tv.app.adapter.SubAdapter;

public class YangGuanDangWuMainActivity extends AppCompatActivity {

    private MyListView listView;
    // private ListView listView;
    private MyListView subListView;
    private MyAdapter myAdapter;
    private SubAdapter subAdapter;

    private static Boolean isfrist=true;


    String cities[][] = new String[][] {
            new String[] {"1、被执行人须知", "2、中国共产党发展党员工作细则", "3、四川省农村基层干部工作手册",
                    "4、习近平出席全国国有企业党的建设工作会议并发表重要讲话","5、习近平在纪念红军长征胜利80周年大会上发表重要讲话",
            "6、“学习贯彻党的十八届六中全会精神”","7、习近平系列重要讲话精神——讲奉献有作为是立党之基"},
    };
    String foods[] =new String[]{"政务服务","便民服务","科技服务","咨询电话","农产品交易信"};
    private int data;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yangguandanwu_layout);

        init();
        myAdapter=new MyAdapter(getApplicationContext(), foods);
        listView.setAdapter(myAdapter);
        subAdapter=new SubAdapter(getApplicationContext(), cities);
        subListView.setAdapter(subAdapter);
        data = getIntent().getIntExtra("data",1);
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

        if(keyCode==event.KEYCODE_BACK)
        {
            YangGuanDangWuMainActivity.this.finish();
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
                Intent intent = new Intent(YangGuanDangWuMainActivity.this, ReadFileActivity.class);

                switch (position) {
                    case 0:
                        intent.putExtra("str", YangGuanDanWuConstant.txt01);
                        intent.putExtra("title", cities[0][position]);
                        intent.putExtra("data", data);
                        isfrist = false;
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("str", YangGuanDanWuConstant.txt02);
                        intent.putExtra("title", cities[0][position]);
                        intent.putExtra("data", data);
                        isfrist = false;
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("title", cities[0][position]);
                        intent.putExtra("str", YangGuanDanWuConstant.txt03);
                        intent.putExtra("str2", YangGuanDanWuConstant.text04);
                        intent.putExtra("data", data);
                        isfrist = false;
                        startActivity(intent);
                        break;
                    case 3:
                        intent.putExtra("title", cities[0][position]);
                        intent.putExtra("str", YangGuanDanWuConstant.text05);
                        intent.putExtra("data", data);
                        isfrist = false;
                        startActivity(intent);
                        break;
                    case 4:
                        intent.putExtra("title", cities[0][position]);
                        intent.putExtra("str", YangGuanDanWuConstant.text06);
                        intent.putExtra("data", data);
                        isfrist = false;
                        startActivity(intent);
                        break;
                    case 5:
                        intent.putExtra("title", cities[0][position]);
                        intent.putExtra("str", YangGuanDanWuConstant.text07);
                        intent.putExtra("data", data);
                        isfrist = false;
                        startActivity(intent);
                        break;
                    case 6:
                        intent.putExtra("title", cities[0][position]);
                        intent.putExtra("str", YangGuanDanWuConstant.text08);
                        intent.putExtra("data", data);
                        isfrist = false;
                        startActivity(intent);
                        break;


                }

            }

        });
    }
}
