package com.indoor.im.ui;

import android.os.Bundle;

import com.indoor.im.R;

public class AboutActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initTopBarForLeft("关于我们");
    }
}
