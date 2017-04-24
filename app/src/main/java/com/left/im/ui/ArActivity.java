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


    @Override
    protected String title() {
        return "AR导航";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        initNaviView();
        // TODO: 2017/4/23
    }

}
