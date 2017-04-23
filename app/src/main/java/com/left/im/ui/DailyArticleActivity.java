package com.left.im.ui;

import android.os.Bundle;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;

/**
 * 每日一文
 *
 * @author :left
 * @project:DailyArticleActivity
 * @date :2017-04-25-18:23
 */
public class DailyArticleActivity extends ParentWithNaviActivity {


    @Override
    protected String title() {
        return "每日一文";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_article);
        initNaviView();
        // TODO: 2017/4/23
    }

}
