package com.indoor.im.ui;

import com.indoor.im.R;
import android.os.Bundle;

public class AboutActivity extends ActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initTopBarForLeft("关于我们");
	}
}
