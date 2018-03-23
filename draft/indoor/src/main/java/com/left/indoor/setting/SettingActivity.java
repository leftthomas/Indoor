package com.left.indoor.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.left.indoor.chat.ChatActivity;
import com.left.indoor.guide.LoginActivity;
import com.left.indoor.map.R;
import com.left.indoor.utils.MyApplication;

public class SettingActivity extends Activity {

	private ImageView back;
	private RelativeLayout aboutusLayout;
	private RelativeLayout mapsettingLayout;
	private RelativeLayout privacysettingLayout;
	private RelativeLayout situationsettingLayout;
	private RelativeLayout personalsettingLayout;
	private RelativeLayout feedbackLayout;
	private RelativeLayout wallpapersettingLayout;
	private RelativeLayout logout_layout;
	private RelativeLayout lingdang_layout; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		back=(ImageView) findViewById(R.id.back);
		aboutusLayout= (RelativeLayout) findViewById(R.id.aboutus_layout);
		mapsettingLayout= (RelativeLayout) findViewById(R.id.mapsetting_layout);
		privacysettingLayout= (RelativeLayout) findViewById(R.id.privacysetting_layout);
		situationsettingLayout= (RelativeLayout) findViewById(R.id.situationsetting_layout);
		personalsettingLayout= (RelativeLayout) findViewById(R.id.personalprofile_layout);
		feedbackLayout= (RelativeLayout) findViewById(R.id.feedback_layout);
		wallpapersettingLayout= (RelativeLayout) findViewById(R.id.wallpapersetting_layout);
		logout_layout= (RelativeLayout) findViewById(R.id.logout_layout);
		lingdang_layout= (RelativeLayout) findViewById(R.id.lingdang_layout);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		aboutusLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent aboutIntent;
				aboutIntent=new Intent(SettingActivity.this,AboutusActivity.class);
				startActivity(aboutIntent);
			}
		});
		mapsettingLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent mapsettingIntent;
				mapsettingIntent=new Intent(SettingActivity.this,MapsettingActivity.class);
				startActivity(mapsettingIntent);
			}
		});
		privacysettingLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent privacysettingIntent;
				privacysettingIntent=new Intent(SettingActivity.this,PrivacysettingActivity.class);
				startActivity(privacysettingIntent);
			}
		});
		situationsettingLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent situationsettingIntent;
				situationsettingIntent=new Intent(SettingActivity.this,SituationsettingActivity.class);
				startActivity(situationsettingIntent);
			}
		});
		personalsettingLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent personalsettingIntent;
				personalsettingIntent=new Intent(SettingActivity.this,PersonalsettingActivity.class);
				startActivity(personalsettingIntent);
			}
		});
		feedbackLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent feedbackIntent;
				feedbackIntent=new Intent(SettingActivity.this,FeedbackActivity.class);
				startActivity(feedbackIntent);
			}
		});
		wallpapersettingLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent wallpapersettingIntent;
				wallpapersettingIntent=new Intent(SettingActivity.this,WallpapersettingActivity.class);
				startActivity(wallpapersettingIntent);
			}
		});
		lingdang_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent lingdangIntent;
				lingdangIntent=new Intent(SettingActivity.this,ChatActivity.class);
				startActivity(lingdangIntent);
			}
		});
		logout_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent logoutIntent;
				logoutIntent=new Intent(SettingActivity.this,LoginActivity.class);
				startActivity(logoutIntent);
				finish();
                //清除主界面的那些activity
                MyApplication.getInstance().AppExit();
			}
		});
	}
}
