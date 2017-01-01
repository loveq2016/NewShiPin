package com.ysh.tv.app.sifayangguan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import com.ysh.tv.app.MyListView;
import com.ysh.tv.app.R;
import com.ysh.tv.app.ReadFileActivity;
import com.ysh.tv.app.adapter.MyAdapter;
import com.ysh.tv.app.adapter.SubAdapter;


public class SiFaYangGuanMyActivity extends Activity {
    private MyListView listView;
   // private ListView listView;
    private MyListView subListView;
    private MyAdapter myAdapter;
    private SubAdapter subAdapter;

    private static Boolean isfrist=true;


    String cities[][] = new String[][] {
            new String[] {"1、被执行人须知", "2、争议诉讼案件举证须知", "3、离婚案件中常见的问题及对策",
                    "4、民事诉讼时效规定","5、诉讼须知","6、申请人民法院强制执行的条件及要求","7、当事人在执行程序中的主要权利和义务","8、医疗纠纷诉讼前须知"},
    };
    private int data;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sifayangguan_main);
        init();


        subAdapter=new SubAdapter(getApplicationContext(), cities);
        subListView.setAdapter(subAdapter);

        subListView.setSelection(0);
        data = getIntent().getIntExtra("data",5);


    }


    @Override
    protected void onResume() {
        super.onResume();
        method();
    }

    private void init(){

        subListView=(MyListView) findViewById(R.id.subListView);
    }


    private void method()
    {


      sublick();



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
           SiFaYangGuanMyActivity.this.finish();


        }
        return super.onKeyDown(keyCode, event);
    }

    private void sublick()
    {



            subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent=new Intent(SiFaYangGuanMyActivity.this,ReadFileActivity.class);
                    switch (position)
                    {
                        case 0:
                            intent.putExtra("str", SiFaYangGuanConstant.txt01);
                            intent.putExtra("title", cities[0][position]);
                            intent.putExtra("data",data);
                            isfrist = false;
                            startActivity(intent);
                            break;
                        case 1:
                            intent.putExtra("str", SiFaYangGuanConstant.txt02);
                            intent.putExtra("data",data);
                            intent.putExtra("title", cities[0][position]);
                            isfrist = false;
                            startActivity(intent);
                            break;
                        case 2:
                            intent.putExtra("str", SiFaYangGuanConstant.txt03);
                            intent.putExtra("data",data);
                            intent.putExtra("title", cities[0][position]);
                            isfrist = false;
                            startActivity(intent);
                            break;
                        case 3:
                            intent.putExtra("str", SiFaYangGuanConstant.txt04);
                            intent.putExtra("data",data);
                            intent.putExtra("title", cities[0][position]);
                            isfrist = false;
                            startActivity(intent);
                            break;
                        case 4:
                            intent.putExtra("str",SiFaYangGuanConstant.txt05);
                            intent.putExtra("data",data);
                            intent.putExtra("title", cities[0][position]);
                            isfrist = false;
                            startActivity(intent);
                            break;
                        case 5:
                            intent.putExtra("str",SiFaYangGuanConstant.txt06);
                            intent.putExtra("data",data);
                            intent.putExtra("title", cities[0][position]);
                            isfrist = false;
                            startActivity(intent);
                            break;
                        case 6:
                            intent.putExtra("str",SiFaYangGuanConstant.txt07);
                            intent.putExtra("data",data);
                            intent.putExtra("title", cities[0][position]);
                            isfrist = false;
                            startActivity(intent);
                            break;
                        case 7:
                            intent.putExtra("str",SiFaYangGuanConstant.txt08);
                            intent.putExtra("data",data);
                            intent.putExtra("title", cities[0][position]);
                            isfrist = false;
                            startActivity(intent);
                            break;
                    }

                }
            });



        /*subListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MyActivity.this,ReadFileActivity.class);
                switch (position)
                {
                    case 0:
                        intent.putExtra("str", Constant.txt01);
                        intent.putExtra("title", cities[0][position]);
                        isfrist = false;
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("str", Constant.txt02);
                        intent.putExtra("title", cities[0][position]);
                        isfrist = false;
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("str", Constant.txt03);
                        intent.putExtra("title", cities[0][position]);
                        isfrist = false;
                        startActivity(intent);
                        break;
                    case 3:
                        intent.putExtra("str", Constant.txt04);
                        intent.putExtra("title", cities[0][position]);
                        isfrist = false;
                        startActivity(intent);
                        break;
                    case 4:
                        intent.putExtra("str",Constant.txt05);
                        intent.putExtra("title", cities[0][position]);
                        isfrist = false;
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
      */

    }


}
