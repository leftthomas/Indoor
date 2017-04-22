package com.indoor.more;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.indoor.im.R;
import com.indoor.im.ui.BaseActivity;
import com.indoor.more.view.LabelView;
import com.indoor.more.view.LabelView.OnItemClickListener;

public class FindMoreActivity extends BaseActivity {

    private LabelView mLabelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_more);
        initTopBarForLeft("发现");
        mLabelView = (LabelView) findViewById(R.id.lv);
        mLabelView.setLabels(new String[]{"片刻", "每日一文", "乐流", "平行世界", "MONO", "一种", "emo",
                "催眠大师", "how_old"});
        mLabelView.setColorSchema(new int[]{Color.DKGRAY, Color.CYAN, Color.GREEN, Color.LTGRAY,
                Color.MAGENTA, Color.RED});
        mLabelView.setSpeeds(new int[][]{{1, 2}, {1, 1}, {2, 1}, {2, 3}, {3, 1}, {3, 4}, {4, 1}, {4, 5}});
        mLabelView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int index, String label) {
                if (label.equals("每日一文"))
                    startActivity(new Intent(FindMoreActivity.this, DailyArticleActivity.class));
                if (label.equals("how_old"))
                    startActivity(new Intent(FindMoreActivity.this, HowOldActivity.class));
                if (label.equals("催眠大师"))
                    startActivity(new Intent(FindMoreActivity.this, HypnotistActivity.class));
                if (label.equals("片刻"))
                    startActivity(new Intent(FindMoreActivity.this, AmomentActivity.class));
            }
        });
    }
}
