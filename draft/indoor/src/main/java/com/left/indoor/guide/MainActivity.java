package com.left.indoor.guide;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.left.indoor.chat.ContactActivity;
import com.left.indoor.map.MapActivity;
import com.left.indoor.map.R;
import com.left.indoor.personal.PersonalActivity;
import com.left.indoor.release.ReleaseActivity;
import com.left.indoor.situation.SituationActivity;
import com.left.indoor.utils.MyApplication;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	
	private TabHost tabHost;
    OnTabChangeListener tablisener = new OnTabChangeListener() {

        @Override
        public void onTabChanged(String tabId) {
            View v1 = tabHost.getTabWidget().getChildAt(0);
            View v2 = tabHost.getTabWidget().getChildAt(1);
            View v3 = tabHost.getTabWidget().getChildAt(2);
            View v4 = tabHost.getTabWidget().getChildAt(3);
            View v5 = tabHost.getTabWidget().getChildAt(4);
            if (tabId.equals("tab_map")) {
                v1.setBackgroundResource(R.drawable.mapblue);
                v2.setBackgroundResource(R.drawable.contactgrey);
                v3.setBackgroundResource(R.drawable.releasegrey);
                v4.setBackgroundResource(R.drawable.situationgrey);
                v5.setBackgroundResource(R.drawable.personalgrey);
            } else if (tabId.equals("tab_contact")) {
                v2.setBackgroundResource(R.drawable.contactblue);
                v1.setBackgroundResource(R.drawable.mapgrey);
                v3.setBackgroundResource(R.drawable.releasegrey);
                v4.setBackgroundResource(R.drawable.situationgrey);
                v5.setBackgroundResource(R.drawable.personalgrey);
            } else if (tabId.equals("tab_release")) {
                v3.setBackgroundResource(R.drawable.releaseblue);
                v1.setBackgroundResource(R.drawable.mapgrey);
                v2.setBackgroundResource(R.drawable.contactgrey);
                v4.setBackgroundResource(R.drawable.situationgrey);
                v5.setBackgroundResource(R.drawable.personalgrey);
            } else if (tabId.equals("tab_situation")) {
                v4.setBackgroundResource(R.drawable.situationblue);
                v1.setBackgroundResource(R.drawable.mapgrey);
                v2.setBackgroundResource(R.drawable.contactgrey);
                v3.setBackgroundResource(R.drawable.releasegrey);
                v5.setBackgroundResource(R.drawable.personalgrey);
            } else if (tabId.equals("tab_personal")) {
                v5.setBackgroundResource(R.drawable.personalblue);
                v1.setBackgroundResource(R.drawable.mapgrey);
                v2.setBackgroundResource(R.drawable.contactgrey);
                v3.setBackgroundResource(R.drawable.releasegrey);
                v4.setBackgroundResource(R.drawable.situationgrey);
            }
        }
    };
    private Intent mapIntent;
	private Intent contactIntent;
	private Intent releaseIntent;
	private Intent situationIntent;
    private Intent personalIntent;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);
		tabHost = getTabHost();
		initIntent();
		addSpec();
		tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 150;
		tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 150;
		tabHost.getTabWidget().getChildAt(2).getLayoutParams().height = 150;
		tabHost.getTabWidget().getChildAt(3).getLayoutParams().height = 150;
		tabHost.getTabWidget().getChildAt(4).getLayoutParams().height = 150;
		tabHost.setOnTabChangedListener(tablisener);
	}

    //初始化各个tab标签对应的intent
    private void initIntent() {
		mapIntent = new Intent(this, MapActivity.class);
		contactIntent = new Intent(this, ContactActivity.class);
		releaseIntent = new Intent(this, ReleaseActivity.class);
		situationIntent = new Intent(this, SituationActivity.class);
		personalIntent = new Intent(this, PersonalActivity.class);
	    }

    //为tabHost添加各个标签项
    private void addSpec() {
		tabHost.addTab(this.buildTagSpec("tab_map",R.drawable.tabbackground, mapIntent));
	    tabHost.addTab(this.buildTagSpec("tab_contact",R.drawable.tabbackground, contactIntent));
	    tabHost.addTab(this.buildTagSpec("tab_release",R.drawable.tabbackground, releaseIntent));
	    tabHost.addTab(this.buildTagSpec("tab_situation",R.drawable.tabbackground,situationIntent));
	    tabHost.addTab(this.buildTagSpec("tab_personal",R.drawable.tabbackground, personalIntent));
	    View v1 = tabHost.getTabWidget().getChildAt(0);
	    v1.setBackgroundResource(R.drawable.mapblue);
	    View v2 = tabHost.getTabWidget().getChildAt(1);
        v2.setBackgroundResource(R.drawable.contactgrey);
        View v3 = tabHost.getTabWidget().getChildAt(2);
	    v3.setBackgroundResource(R.drawable.releasegrey);
	    View v4 = tabHost.getTabWidget().getChildAt(3);
        v4.setBackgroundResource(R.drawable.situationgrey);
        View v5 = tabHost.getTabWidget().getChildAt(4);
	    v5.setBackgroundResource(R.drawable.personalgrey);
	    }

    //自定义创建标签项的方法
    private TabHost.TabSpec buildTagSpec(String tagName,int icon, Intent content) {
	       return tabHost
	    		   .newTabSpec(tagName)
	    		   .setIndicator("",
	    		   getResources().getDrawable(icon)).setContent(content);
     }
}
