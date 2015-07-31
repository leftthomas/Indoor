package com.indoor.im.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import com.indoor.im.R;
import com.indoor.im.CustomApplcation;
import com.indoor.im.ui.AboutActivity;
import com.indoor.im.ui.BlackListActivity;
import com.indoor.im.ui.LoginActivity;
import com.indoor.im.ui.SetMyInfoActivity;
import com.indoor.im.ui.SetWallpaperActivity;
import com.indoor.im.util.SharePreferenceUtil;
import com.zcw.togglebutton.ToggleButton;

/**
 * 设置
 * 
 * @ClassName: SetFragment
 * @Description: TODO
 * @author smile
 * @date 2014-6-7 下午1:00:27
 */
@SuppressLint("SimpleDateFormat")
public class SettingsFragment extends FragmentBase implements OnClickListener{

	Button btn_logout;
	TextView tv_set_name;
	RelativeLayout layout_info, layout_blacklist,layout_wallpaper,layout_swithNotification,layout_swithVoice,
	layout_swithVibrate,layout_aboutus;
	ToggleButton notificationButton,voiceButton,vibrateButton;
	View view1,view2;
	SharePreferenceUtil mSharedUtil;
	boolean isnotification,isvoice,isvibrate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedUtil = mApplication.getSpUtil();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_set, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		initTopBarForOnlyTitle("设置");
		layout_info = (RelativeLayout) findViewById(R.id.layout_info);
		//黑名单列表
		layout_blacklist = (RelativeLayout) findViewById(R.id.layout_blacklist);
		layout_wallpaper= (RelativeLayout) findViewById(R.id.layout_wallpaper);
		layout_aboutus = (RelativeLayout) findViewById(R.id.aboutus);
		layout_swithNotification=(RelativeLayout) findViewById(R.id.switch_notification);
		layout_swithVoice=(RelativeLayout) findViewById(R.id.switch_voice);
		layout_swithVibrate=(RelativeLayout) findViewById(R.id.switch_vibrate);
		notificationButton = (ToggleButton) findViewById(R.id.rl_switch_notification);
		voiceButton = (ToggleButton) findViewById(R.id.rl_switch_voice);
		vibrateButton = (ToggleButton) findViewById(R.id.rl_switch_vibrate);
		view1 = (View) findViewById(R.id.view1);
		view2 = (View) findViewById(R.id.view2);
		tv_set_name = (TextView) findViewById(R.id.tv_set_name);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);
		layout_info.setOnClickListener(this);
		layout_blacklist.setOnClickListener(this);
        layout_wallpaper.setOnClickListener(this);
        layout_aboutus.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
        voiceButton.setOnClickListener(this);
        vibrateButton.setOnClickListener(this);
		// 初始化
		boolean isAllowNotify = mSharedUtil.isAllowPushNotify();
		isnotification=isAllowNotify;
		if (isAllowNotify) {
			notificationButton.setToggleOn();
		} else {
			notificationButton.setToggleOff();
		}
		boolean isAllowVoice = mSharedUtil.isAllowVoice();
		isvoice=isAllowVoice;
		if (isAllowVoice) {
			voiceButton.setToggleOn();
		} else {
			voiceButton.setToggleOff();
		}
		boolean isAllowVibrate = mSharedUtil.isAllowVibrate();
		isvibrate=isAllowVibrate;
		if (isAllowVibrate) {
			vibrateButton.setToggleOn();
		} else {
			vibrateButton.setToggleOff();
		}
	}

	private void initData() {
		tv_set_name.setText(BmobUserManager.getInstance(getActivity())
				.getCurrentUser().getUsername());
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_blacklist:// 启动到黑名单页面
			startAnimActivity(new Intent(getActivity(),BlackListActivity.class));
			break;
		case R.id.layout_wallpaper:// 启动到聊天背景设置页面
			startAnimActivity(new Intent(getActivity(),SetWallpaperActivity.class));
			break;
		case R.id.layout_info:// 启动到个人资料页面
			Intent intent =new Intent(getActivity(),SetMyInfoActivity.class);
			intent.putExtra("from", "me");
			startActivity(intent);
			break;
		case R.id.aboutus:// 启动到关于我们页面
			startActivity(new Intent(getActivity(),AboutActivity.class));
			break;	
		case R.id.btn_logout://登出当前账号
			CustomApplcation.getInstance().logout();
			getActivity().finish();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;	
		case R.id.rl_switch_notification://总通知开关
			if (isnotification) {
				notificationButton.setToggleOff();
				isnotification=false;
				mSharedUtil.setPushNotifyEnable(false);
				layout_swithVibrate.setVisibility(View.GONE);
				layout_swithVoice.setVisibility(View.GONE);
				view1.setVisibility(View.GONE);
				view2.setVisibility(View.GONE);
			} else {
				notificationButton.setToggleOn();
				isnotification=true;
				mSharedUtil.setPushNotifyEnable(true);
				layout_swithVibrate.setVisibility(View.VISIBLE);
				layout_swithVoice.setVisibility(View.VISIBLE);
				view1.setVisibility(View.VISIBLE);
				view2.setVisibility(View.VISIBLE);
			}
			break;	
		case R.id.rl_switch_voice://语音通知开关
			if (isvoice) {
				voiceButton.setToggleOff();
				isvoice=false;
				mSharedUtil.setAllowVoiceEnable(false);
			} else {
				voiceButton.setToggleOn();
				isvoice=true;
				mSharedUtil.setAllowVoiceEnable(true);
			}
			break;	
		case R.id.rl_switch_vibrate://震动通知开关
			if (isvibrate) {
				vibrateButton.setToggleOff();
				isvibrate=false;
				mSharedUtil.setAllowVibrateEnable(false);
			} else {
				vibrateButton.setToggleOn();
				isvibrate=true;
				mSharedUtil.setAllowVibrateEnable(true);
			}
			break;	
		}	
	}
}
