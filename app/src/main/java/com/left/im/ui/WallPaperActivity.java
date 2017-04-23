package com.left.im.ui;

import android.os.Bundle;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;

/**
 * 背景设置
 *
 * @author :left
 * @project:WallPaperActivity
 * @date :2017-04-25-18:23
 */
public class WallPaperActivity extends ParentWithNaviActivity {


    @Override
    protected String title() {
        return "背景设置";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_paper);
        initNaviView();
    }

}
