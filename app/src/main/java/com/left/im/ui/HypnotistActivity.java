package com.left.im.ui;

import android.os.Bundle;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;

/**
 * 催眠大师
 *
 * @author :left
 * @project:HypnotistActivity
 * @date :2017-04-25-18:23
 */
public class HypnotistActivity extends ParentWithNaviActivity {


    @Override
    protected String title() {
        return "催眠大师";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hypnotist);
        initNaviView();
        // TODO: 2017/4/23
    }

}
