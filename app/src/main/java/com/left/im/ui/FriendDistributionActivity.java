package com.left.im.ui;

import android.os.Bundle;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;

/**
 * 朋友分布
 *
 * @author :left
 * @project:BlackListActivity
 * @date :2017-04-25-18:23
 */
public class FriendDistributionActivity extends ParentWithNaviActivity {


    @Override
    protected String title() {
        return "朋友分布";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_distribution);
        initNaviView();
        // TODO: 2017/4/23
    }

}
