package com.left.im.ui;

import android.os.Bundle;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;

/**
 * 附近的人
 *
 * @author :left
 * @project:BlackListActivity
 * @date :2017-04-25-18:23
 */
public class NearbyPeopleActivity extends ParentWithNaviActivity {


    @Override
    protected String title() {
        return "附近的人";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_people);
        initNaviView();
        // TODO: 2017/4/23
    }

}
