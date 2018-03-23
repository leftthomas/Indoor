package com.left.indoor.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.left.indoor.bean.Configure;
import com.left.indoor.bean.IndoorUser;
import com.left.indoor.map.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

public class SituationsettingActivity extends Activity {

	private ImageView back;
	private Switch specialreminderstatus;
	private Switch myplan_reminderstatus;
	private CheckBox distancelessstatus;
	private SeekBar distancelessdistance;
	private TextView radius;
	private IndoorUser cuser;
	private Configure cu_Configure;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_situationsetting);
		back=(ImageView) findViewById(R.id.back);
		specialreminderstatus=(Switch) findViewById(R.id.specialreminderstatus);
		myplan_reminderstatus=(Switch) findViewById(R.id.myplan_reminderstatus);
		distancelessstatus=(CheckBox) findViewById(R.id.distancelessstatus);
		distancelessdistance=(SeekBar) findViewById(R.id.distancelessdistance);
		radius=(TextView) findViewById(R.id.radius);
		cuser = BmobUser.getCurrentUser(SituationsettingActivity.this,IndoorUser.class);
		findMyConfigures();
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                //保存数据
                cu_Configure.update(SituationsettingActivity.this);
				finish();
			}
		});
		specialreminderstatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
					if (isChecked) {  
						cu_Configure.setSpecialreminderstatus(true);
	                } 
					else {  
						cu_Configure.setSpecialreminderstatus(false);
	                }  
				}
			});
		myplan_reminderstatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
					if (isChecked) {  
						cu_Configure.setMyplan_reminderstatus(true);
					} 
					else {  
						cu_Configure.setMyplan_reminderstatus(false);
					}  
				}
			});
		distancelessstatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {  
					cu_Configure.setDistancelessstatus(true);
				} 
				else {  
					cu_Configure.setDistancelessstatus(false);;
				}  
			}
		});
		distancelessdistance.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                //progress为当前数值的大小
                radius.setText("半径：" + progress + "m");
                cu_Configure.setDistancelessdistance(progress);
			}
		});
	}

    //从本地获取用户动态设置，进行本地初始化
    private void init() {
		specialreminderstatus.setChecked( cu_Configure.getSpecialreminderstatus());
		myplan_reminderstatus.setChecked(cu_Configure.getMyplan_reminderstatus());
		distancelessstatus.setChecked(cu_Configure.getDistancelessstatus());
		distancelessdistance.setProgress(cu_Configure.getDistancelessdistance());
        radius.setText("半径：" + cu_Configure.getDistancelessdistance() + "m");
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
                    //进行设置初始化，加载当前用户的动态设置
                    init();
	            }
	        }

	        @Override
	        public void onError(int arg0, String arg1) {
	        }
	    });
	}
}
