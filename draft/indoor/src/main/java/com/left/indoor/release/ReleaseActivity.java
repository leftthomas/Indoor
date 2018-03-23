package com.left.indoor.release;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.left.indoor.map.R;
import com.left.indoor.myview.composerLayout;
import com.left.indoor.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class ReleaseActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mDatas;
	private TextView statuspraesens;
	private TextView travelplan;
	private TextView draftbox;

    //private BadgeView mBadgeView;//用以显示右上角的消息数目或者通知数目，(来自Github)
    //private LinearLayout statuspraesensLinearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_release);
		MyApplication.getInstance().addActivity(this);
		initView();
        // 引用控件
        composerLayout clayout = (composerLayout) findViewById(R.id.test);
		clayout.init(new int[] { R.drawable.mysituation,
				R.drawable.draft, R.drawable.photo},
				R.drawable.media,R.drawable.plan,
				composerLayout.LEFTBOTTOM, 600,300);
        // 加個點擊監聽，100+0對應composer_camera，100+1對應composer_music……如此類推你有幾多個按鈕就加幾多個。
        OnClickListener clickit = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.getId() == 100 + 0) {
					System.out.println("composer_camera");
				} else if (v.getId() == 100 + 1) {
					System.out.println("composer_music");
				} else if (v.getId() == 100 + 2) {
					System.out.println("composer_place");
				}
			}
		};
		clayout.setButtonsOnClickListener(clickit);

        // 下面呢幾句純粹攞嚟測試下父控件點唔點倒，實際用嘅時候可以去掉。
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlparent);
		rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                System.out.println("父控件可以點擊就即系冇吡截咗。");
            }
		});
	}
	
	private void initView() {
		mViewPager=(ViewPager) findViewById(R.id.releaseviewpager);
		statuspraesens=(TextView) findViewById(R.id.statuspraesens);
		travelplan=(TextView) findViewById(R.id.travelplans);
		draftbox=(TextView) findViewById(R.id.draftbox);
		mDatas=new ArrayList<Fragment>();
		StatuspraesensFragment statuspraesensFragment =new StatuspraesensFragment();
		TravelplansFragment travelplansFragment =new TravelplansFragment();
		DraftboxFragment draftboxFragment =new DraftboxFragment();
		mDatas.add(statuspraesensFragment);
		mDatas.add(travelplansFragment);
		mDatas.add(draftboxFragment);
		mAdapter =new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mDatas.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mDatas.get(arg0);
			}
		};
		
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				resetTextView();
				switch (arg0) {
				case 0:
					/*if(mBadgeView!=null){
                        statuspraesensLinearLayout.removeView(mBadgeView);
					}
					mBadgeView=new BadgeView(ReleaseActivity.this);
					mBadgeView.setBadgeCount(7);//设置消息数目
					statuspraesensLinearLayout.addView(mBadgeView);//将消息通知数目添加到布局上*/
					statuspraesens.setTextColor(Color.GREEN);
					break;
					
				case 1:
					travelplan.setTextColor(Color.GREEN);
					break;
				
				case 2:
					draftbox.setTextColor(Color.GREEN);
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	protected void resetTextView() {
		// TODO Auto-generated method stub
		statuspraesens.setTextColor(Color.BLACK);
		travelplan.setTextColor(Color.BLACK);
		draftbox.setTextColor(Color.BLACK);
	}
}
