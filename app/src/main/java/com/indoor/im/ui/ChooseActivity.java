package com.indoor.im.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.amap.api.services.core.PoiItem;
import com.indoor.im.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseActivity extends Activity {

    private ImageView back;
    private List<PoiItem> lists;
    private List<Map<String, Object>> list;
    private ListView listView;
    private SimpleAdapter simple_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        back = (ImageView) findViewById(R.id.back);
        // 匹配布局文件中的ListView控件
        listView = (ListView) findViewById(R.id.listview);
        getData();
        // 设置SimpleAdapter监听器
        simple_adapter = new SimpleAdapter(this,
                list, R.layout.list_item,
                new String[]{"busname", "buscontent", "busdistance"}, new int[]
                {R.id.busname, R.id.buscontent, R.id.busdistance});
        listView.setAdapter(simple_adapter);
        // 设置ListView的元素被选中时的事件处理监听器
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                final int what = position;
                //设置对话框标题
                new AlertDialog.Builder(ChooseActivity.this).setTitle(lists
                        .get(position).toString())
                        //设置显示的内容
                        .setMessage(lists.get(position).getSnippet())
                        //添加确定按钮
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            //确定按钮的响应事件
                            public void onClick(DialogInterface dialog, int which) {
                                Intent aintent = new Intent(ChooseActivity.this, MainActivity.class);
                                aintent.putExtra("listenB", what + "");
                                setResult(RESULT_OK, aintent);
                                finish();
                            }
                            //添加返回按钮
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    //响应事件
                    public void onClick(DialogInterface dialog, int which) {
                    }

                }).show();//在按键响应事件中显示此对话框
            }
        });
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    // 加载SimpleAdapter数据集
    @SuppressWarnings("unchecked")
    private void getData() {
        Intent intent = getIntent();
        lists = (List<PoiItem>) intent.getSerializableExtra("poiItems");
        list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < lists.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("busname", lists.get(i).toString());
            map.put("buscontent", lists.get(i).getSnippet());
            map.put("busdistance", lists.get(i).getDistance() + "米");
            list.add(map);
        }
    }
}
