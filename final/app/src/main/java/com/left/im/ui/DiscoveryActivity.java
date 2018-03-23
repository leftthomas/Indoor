package com.left.im.ui;

import android.graphics.Color;
import android.os.Bundle;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.util.LabelView;

import butterknife.Bind;

/**
 * 发现
 *
 * @author :left
 * @project:DiscoveryActivity
 * @date :2017-04-25-18:23
 */
public class DiscoveryActivity extends ParentWithNaviActivity {

    @Bind(R.id.lv)
    LabelView labelView;

    @Override
    protected String title() {
        return "发现";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        initNaviView();
        labelView.setLabels(new String[]{"片刻", "每日一文", "乐流", "平行世界", "MONO", "一种", "emo",
                "催眠大师", "how old"});
        labelView.setColorSchema(new int[]{Color.DKGRAY, Color.CYAN, Color.GREEN, Color.LTGRAY,
                Color.MAGENTA, Color.RED});
        labelView.setSpeeds(new int[][]{{1, 2}, {1, 1}, {2, 1}, {2, 3}, {3, 1}, {3, 4}, {4, 1}, {4, 5}});
        labelView.setOnItemClickListener(new LabelView.OnItemClickListener() {
            @Override
            public void onItemClick(int index, String label) {
                if (label.equals("每日一文"))
                    startActivity(DailyArticleActivity.class, null);
                if (label.equals("how old"))
                    startActivity(HowOldActivity.class, null);
                if (label.equals("催眠大师"))
                    startActivity(HypnotistActivity.class, null);
            }
        });
    }

}
