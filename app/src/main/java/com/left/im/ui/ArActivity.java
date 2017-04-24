package com.left.im.ui;

import android.os.Bundle;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;

/**
 * AR导航
 *
 * @author :left
 * @project:ArActivity
 * @date :2017-04-25-18:23
 */
public class ArActivity extends ParentWithNaviActivity {
    String poi;

    @Override
    protected String title() {
        return poi;
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_more_bg_selector;
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {

//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("选择新闻类别");
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int item) {
//                        newsCategory = items[item].toString();
//                        sw_refresh.setRefreshing(true);
//                        query();
//                    }
//                });
//                AlertDialog alert = builder.create();
//                alert.show();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        poi = (String) getBundle().getSerializable("poi");
        initNaviView();
        // TODO: 2017/4/23
    }

}
