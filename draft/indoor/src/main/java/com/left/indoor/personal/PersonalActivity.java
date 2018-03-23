package com.left.indoor.personal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.left.indoor.map.R;
import com.left.indoor.media.NewsmediaFragment;
import com.left.indoor.setting.SettingActivity;
import com.left.indoor.shopping.Online_shoppingFragment;
import com.left.indoor.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mDatas;
	private TextView newsmedia;
	private TextView personal_information;
	private TextView Online_shopping;
	private ImageView setting;
	//private TextView nickname;
	//private TextView location;
	//private TextView signature;
    //private BadgeView mBadgeView;//用以显示右上角的消息数目或者通知数目，(来自Github)
    //private LinearLayout friendsstatusLinearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_personal);
		MyApplication.getInstance().addActivity(this);
		initView();	
		setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent tosetting=new Intent(PersonalActivity.this,SettingActivity.class);
				startActivity(tosetting);
			}
		});
	}
	
	private void initView() {
		mViewPager=(ViewPager) findViewById(R.id.personalviewpager);
		personal_information=(TextView) findViewById(R.id.personal_information);
		newsmedia=(TextView) findViewById(R.id.newsmedia);
		Online_shopping=(TextView) findViewById(R.id.Online_shopping);
		setting=(ImageView) findViewById(R.id.setting);
		mDatas=new ArrayList<Fragment>();
		Personal_informationFragment personal_informationFragment =new Personal_informationFragment();
		NewsmediaFragment newsmediaFragment =new NewsmediaFragment();
		Online_shoppingFragment online_shoppingFragment =new Online_shoppingFragment();
		mDatas.add(newsmediaFragment);
		mDatas.add(personal_informationFragment);
		mDatas.add(online_shoppingFragment);
		mAdapter =new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				
				return mDatas.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				
				return mDatas.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				resetTextView();
				switch (arg0) {
				case 0:
					/*if(mBadgeView!=null){
                        statuspraesensLinearLayout.removeView(mBadgeView);
					}
					mBadgeView=new BadgeView(ReleaseActivity.this);
					mBadgeView.setBadgeCount(7);//设置消息数目
					statuspraesensLinearLayout.addView(mBadgeView);//将消息通知数目添加到布局上*/
					newsmedia.setTextColor(Color.GREEN);
					break;
					
				case 1:
					personal_information.setTextColor(Color.GREEN);
					
					break;
				
				case 2:
					Online_shopping.setTextColor(Color.GREEN);
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	
	protected void resetTextView() {
		personal_information.setTextColor(Color.BLACK);
		newsmedia.setTextColor(Color.BLACK);
		Online_shopping.setTextColor(Color.BLACK);
	}
	
}
