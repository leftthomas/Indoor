package com.left.indoor.situation;

import java.util.ArrayList;
import java.util.List;
import com.left.indoor.map.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

public class SituationActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mDatas;
	private TextView friendsstatus;
	private TextView allplan;
	private TextView particularconcern;
	//private BadgeView mBadgeView;//用以显示右上角的消息数目或者通知数目，(来自Github)
	//private LinearLayout friendsstatusLinearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_situation); 
		initView();
	}
	
	private void initView() {
		mViewPager=(ViewPager) findViewById(R.id.situationviewpager);
		friendsstatus=(TextView) findViewById(R.id.friendsstatus);
		allplan=(TextView) findViewById(R.id.allplan);
		particularconcern=(TextView) findViewById(R.id.particularconcern);
		mDatas=new ArrayList<Fragment>();
		FriendsstatusFragment friendsstatusFragment =new FriendsstatusFragment();
		AllplanFragment allplanFragment =new AllplanFragment();
		ParticularconcernFragment particularconcernFragment =new ParticularconcernFragment();
		mDatas.add(friendsstatusFragment);
		mDatas.add(allplanFragment);
		mDatas.add(particularconcernFragment);
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
					friendsstatus.setTextColor(Color.GREEN);
					break;
					
				case 1:
					allplan.setTextColor(Color.GREEN);
					break;
				
				case 2:
					particularconcern.setTextColor(Color.GREEN);
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
		friendsstatus.setTextColor(Color.BLACK);
		allplan.setTextColor(Color.BLACK);
		particularconcern.setTextColor(Color.BLACK);
	}
}
