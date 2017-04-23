package com.left.im.ui;

import android.os.Bundle;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;

/**
 * how old
 *
 * @author :left
 * @project:HowOldActivity
 * @date :2017-04-25-18:23
 */
public class HowOldActivity extends ParentWithNaviActivity {


    @Override
    protected String title() {
        return "how old";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_old);
        initNaviView();
        // TODO: 2017/4/23
    }

}
