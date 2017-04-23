package com.left.im.ui;

import android.os.Bundle;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;

/**
 * 关于我们
 *
 * @author :left
 * @project:AboutUsActivity
 * @date :2017-04-25-18:23
 */
public class AboutUsActivity extends ParentWithNaviActivity {


    @Override
    protected String title() {
        return "关于我们";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initNaviView();
    }

}
