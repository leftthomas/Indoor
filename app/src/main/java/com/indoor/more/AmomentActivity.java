package com.indoor.more;

import android.os.Bundle;

import com.indoor.im.R;
import com.indoor.im.ui.ActivityBase;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;

public class AmomentActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amoment);
        initTopBarForBoth("片刻", R.drawable.base_action_bar_more_bg_n, new onRightImageButtonClickListener() {

            @Override
            public void onClick() {
                // TODO Auto-generated method stub

            }
        });
    }
}
