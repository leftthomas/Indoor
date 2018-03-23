package com.left.indoor.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.left.indoor.bean.Configure;
import com.left.indoor.bean.IndoorUser;
import com.left.indoor.map.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

public class PrivacysettingActivity extends Activity {
	
	private ImageView back;
	private RadioGroup sexoption;
	private RadioGroup ageoption;
	private RadioGroup positionoption;
	private RadioGroup professionoption;
	private IndoorUser cuser;
	private Configure cu_Configure;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privacysetting);
		back=(ImageView) findViewById(R.id.back);
		sexoption= (RadioGroup)findViewById(R.id.sexoption);
		ageoption= (RadioGroup)findViewById(R.id.ageoption);
		positionoption= (RadioGroup)findViewById(R.id.positionoption);
		professionoption= (RadioGroup)findViewById(R.id.professionoption);
		cuser = BmobUser.getCurrentUser(PrivacysettingActivity.this,IndoorUser.class);
		findMyConfigures();
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                //保存数据
                cu_Configure.update(PrivacysettingActivity.this);
				finish();
			}
		});
		sexoption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int option) {
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
				 RadioButton button=(RadioButton) findViewById(radioButtonId);
			     String text = button.getText().toString();
                if (text.equals("保密")) {
                    cu_Configure.setSexstatus(0);
				 } else if (text.equals("仅好友可见")) {
                    cu_Configure.setSexstatus(1);
				 }
					
				 else{
					 cu_Configure.setSexstatus(2);
				 }	 
			}
		});
		ageoption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int option) {
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
				 RadioButton button=(RadioButton) findViewById(radioButtonId);
			     String text = button.getText().toString();
                if (text.equals("保密")) {
                    cu_Configure.setAgestatus(0);
				 } else if (text.equals("仅好友可见")) {
                    cu_Configure.setAgestatus(1);
				 }
					
				 else{
					 cu_Configure.setAgestatus(2);
				 }	 
			}
		});
		positionoption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int option) {
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
				 RadioButton button=(RadioButton) findViewById(radioButtonId);
			     String text = button.getText().toString();
                if (text.equals("保密")) {
                    cu_Configure.setPositionstatus(0);
				 } else if (text.equals("仅好友可见")) {
                    cu_Configure.setPositionstatus(1);
				 }
					
				 else{
					 cu_Configure.setPositionstatus(2);
				 }	 
			}
		});
		professionoption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int option) {
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
				 RadioButton button=(RadioButton) findViewById(radioButtonId);
			     String text = button.getText().toString();
                if (text.equals("保密")) {
                    cu_Configure.setProfessionstatus(0);
				 } else if (text.equals("仅好友可见")) {
                    cu_Configure.setProfessionstatus(1);
				 }
					
				 else{
					 cu_Configure.setProfessionstatus(2);
				 }	 
			}
		});
	}
	
	private void init() {
		RadioButton sexButton;
		if( cu_Configure.getSexstatus()==0)
			sexButton = (RadioButton) findViewById(R.id.sexsecret);
		else if( cu_Configure.getSexstatus()==1)
			sexButton = (RadioButton) findViewById(R.id.sexonly_friends);
		else
			sexButton = (RadioButton) findViewById(R.id.sexallvisible);
		sexoption.check(sexButton.getId()); 
		
		RadioButton ageButton;
		if(cu_Configure.getAgestatus()==0)
			ageButton = (RadioButton) findViewById(R.id.agesecret);
		else if(cu_Configure.getAgestatus()==1)
			ageButton = (RadioButton) findViewById(R.id.ageonly_friends);
		else
			ageButton = (RadioButton) findViewById(R.id.ageallvisible);
		ageoption.check(ageButton.getId()); 
		
		RadioButton positionButton;
		if(cu_Configure.getPositionstatus()==0)
			positionButton = (RadioButton) findViewById(R.id.positionsecret);
		else if(cu_Configure.getPositionstatus()==1)
			positionButton = (RadioButton) findViewById(R.id.positiononly_friends);
		else
			positionButton = (RadioButton) findViewById(R.id.positionallvisible);
		positionoption.check(positionButton.getId()); 
		
		RadioButton professionButton;
		if(cu_Configure.getProfessionstatus()==0)
			professionButton = (RadioButton) findViewById(R.id.professionsecret);
		else if(cu_Configure.getProfessionstatus()==1)
			professionButton = (RadioButton) findViewById(R.id.professiononly_friends);
		else
			professionButton = (RadioButton) findViewById(R.id.professionallvisible);
		professionoption.check(professionButton.getId()); 
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
                    //进行设置初始化，加载当前用户的隐私设置
                    init();
	            }
	        }

	        @Override
	        public void onError(int arg0, String arg1) {
	        }
	    });
	}
}
