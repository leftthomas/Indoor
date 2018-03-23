package com.left.indoor.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;

import com.left.indoor.bean.Configure;
import com.left.indoor.bean.IndoorUser;
import com.left.indoor.map.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

public class MapsettingActivity extends Activity {
	
	private ImageView back;
	private Switch satellitemapstatus;
	private Switch trafficmapstatus;
	private Switch heatmapstatus;
	private Switch voice_navigationstatus;
	private IndoorUser cuser;
	private Configure cu_Configure;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapsetting);
		back=(ImageView) findViewById(R.id.back);
		satellitemapstatus=(Switch) findViewById(R.id.satellitemapstatus);
		trafficmapstatus=(Switch) findViewById(R.id.trafficmapstatus);
		heatmapstatus=(Switch) findViewById(R.id.heatmapstatus);
		voice_navigationstatus=(Switch) findViewById(R.id.voice_navigationstatus);
		cuser = BmobUser.getCurrentUser(MapsettingActivity.this,IndoorUser.class);
		findMyConfigures();
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                //保存数据
                cu_Configure.update(MapsettingActivity.this);
				finish();
			}
		});
		satellitemapstatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {  
					cu_Configure.setSatellitemapstatus(true);
                } 
				else {  
					cu_Configure.setSatellitemapstatus(false);
                }  
			}
		});
		trafficmapstatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
			@Override
	        public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {  
					cu_Configure.setTrafficmapstatus(true);
				}
				else {  
					cu_Configure.setTrafficmapstatus(false);
				}  
			}
		});
		heatmapstatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {  
					cu_Configure.setHeatmapstatus(true);
				} 
				else {  
					cu_Configure.setHeatmapstatus(false);
				}  
			}
		});
		voice_navigationstatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {  
					cu_Configure.setVoice_navigationstatus(true);
				}
				else {  
					cu_Configure.setVoice_navigationstatus(false);
				}  
			}
		});
	}

    //从本地获取用户地图设置，进行初始化
    public void init(){
		satellitemapstatus.setChecked(cu_Configure.getSatellitemapstatus());
		trafficmapstatus.setChecked(cu_Configure.getTrafficmapstatus());
		heatmapstatus.setChecked(cu_Configure.getHeatmapstatus());
		voice_navigationstatus.setChecked(cu_Configure.getVoice_navigationstatus());
	}

	@Override
	protected void onDestroy() {
        //保存数据
        cu_Configure.update(this);
		super.onDestroy();
	}
	
	/**
     * 查询我的所有配置信息
     */
	private void findMyConfigures(){
	    BmobQuery<Configure> configures = new BmobQuery<Configure>();
	    /**
         * 注意这里的查询条件
         * 第一个参数：是User表中的configures字段名
         * 第二个参数：是指向User表中的某个用户的BmobPoint
         * er对象
         */
	    configures.addWhereRelatedTo("configures", new BmobPointer(cuser));
	    configures.findObjects(this, new FindListener<Configure>() {

	        @Override
	        public void onSuccess(List<Configure> arg0) {     
	            for (Configure configure : arg0) {
	            	cu_Configure=configure;
                    //进行设置初始化，加载当前用户的地图设置
                    init();
	            }
	        }

	        @Override
	        public void onError(int arg0, String arg1) {
	        }
	    });
	}
}
