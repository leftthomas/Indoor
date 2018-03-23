package com.left.indoor.guide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.left.indoor.bean.Configure;
import com.left.indoor.bean.IndoorUser;
import com.left.indoor.map.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import me.drakeet.materialdialog.MaterialDialog;

@SuppressLint("InflateParams") public class ImprovepersonaldataActivity extends Activity {

	IndoorUser cuser;
	Configure configure;
	private Button ok;
	private TextView age;
	private TextView sex;
	private TextView profession;
	private MaterialDialog mMaterialDialog;
	private int agechoice;
	private int sexchoice;
	private int size;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_improvepersonaldata);
		ok=(Button) findViewById(R.id.ok);
		age=(TextView) findViewById(R.id.age);
		sex=(TextView) findViewById(R.id.sex);
		profession=(TextView) findViewById(R.id.profession);
		cuser = BmobUser.getCurrentUser(this,IndoorUser.class);
		init();
		findMyConfigures();
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				cuser.update(ImprovepersonaldataActivity.this);
				Intent tomainintent;
				tomainintent=new Intent(ImprovepersonaldataActivity.this,SetsculptureActivity.class);
				startActivity(tomainintent);
				finish();
			}
		});
		age.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mMaterialDialog = new MaterialDialog(ImprovepersonaldataActivity.this);
				// 自定义布局
				final View view = LayoutInflater.from(ImprovepersonaldataActivity.this).inflate(
						R.layout.ageview, null);
				NumberPicker agePicker = (NumberPicker) view.findViewById(R.id.agePicker);

				//初始化数字选择器各项参数以及监听事件，用以年龄选择 
				agePicker.setMinValue(0);
				agePicker.setMaxValue(120);
				agePicker.setValue(cuser.getAge());
				agePicker.getChildAt(0).setFocusable(false);
				agePicker.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker arg0, int oldvalue, int newvalue) {
						agechoice=newvalue;
					}
				});
				mMaterialDialog.setView(view)
						.setPositiveButton("确定", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							age.setText(agechoice + "岁");
							cuser.setAge(agechoice);
							mMaterialDialog.dismiss();
						}
						}).setNegativeButton("取消", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mMaterialDialog.dismiss();
						}
					}).setCanceledOnTouchOutside(false).show();	
			}
		});
		sex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMaterialDialog = new MaterialDialog(ImprovepersonaldataActivity.this);
				// 自定义布局
				final View view = LayoutInflater.from(ImprovepersonaldataActivity.this).inflate(
						R.layout.sexview, null);
				NumberPicker sexPicker = (NumberPicker) view.findViewById(R.id.sexPicker);

				//初始化数字选择器各项参数以及监听事件，用以性别选择 
				String[] sexop = {"男", "女"};
				sexPicker.setDisplayedValues(sexop);
				sexPicker.setMinValue(0);
				sexPicker.setMaxValue(sexop.length - 1);
				if (cuser.getGender().equals("男"))
					sexPicker.setValue(0);
				else 
					sexPicker.setValue(1);
				sexPicker.getChildAt(0).setFocusable(false);
				sexPicker.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker arg0, int oldvalue, int newvalue) {
						sexchoice=newvalue;
					}
				});
				mMaterialDialog.setView(view)
						.setPositiveButton("确定", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if(sexchoice==0){
								sex.setText("男");
								cuser.setGender("男");
							}
							else{
								sex.setText("女");
								cuser.setGender("女");
							}
							mMaterialDialog.dismiss();
						}
						}).setNegativeButton("取消", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mMaterialDialog.dismiss();
						}
					}).setCanceledOnTouchOutside(false).show();	
			}
		});
		profession.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMaterialDialog = new MaterialDialog(ImprovepersonaldataActivity.this);
				// 自定义布局
				final View view = LayoutInflater.from(ImprovepersonaldataActivity.this).inflate(
						R.layout.professionview, null);
				mMaterialDialog.setView(view)
						.setPositiveButton("确定", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
					        EditText professioneditText = (EditText) view.findViewById(R.id.professioneditText);
					        if(!professioneditText.getText().toString().isEmpty()){
					        	profession.setText(professioneditText.getText().toString());
					        	cuser.setProfession(professioneditText.getText().toString());
					        }
							mMaterialDialog.dismiss();
						}
						}).setNegativeButton("取消", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mMaterialDialog.dismiss();
						}
					}).setCanceledOnTouchOutside(false).show();		
			}
		});
	}
	
	private void init() {
		age.setText(cuser.getAge() + "岁");
		sex.setText(cuser.getGender());
		profession.setText(cuser.getProfession());
   }

	//创建默认的配置信息表
	private void saveConfigureInfo(){
		configure =new Configure();
		configure.setSatellitemapstatus(false);
		configure.setTrafficmapstatus(false);
		configure.setHeatmapstatus(false);
		configure.setVoice_navigationstatus(false);
		configure.setSpecialreminderstatus(true);
		configure.setMyplan_reminderstatus(true);
		configure.setDistancelessstatus(true);
		configure.setSexstatus(1);
		configure.setAgestatus(1);
		configure.setPositionstatus(1);
		configure.setProfessionstatus(1);
		configure.setDistancelessdistance(10);
		configure.setWhichwallpaper(0);
		configure.setUser(cuser);
		configure.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				addConfigureToUser();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
			}
		});
	}

	// 添加配置信息到用户的configures信息中
	private void addConfigureToUser(){
		BmobRelation configures = new BmobRelation();
		configures.add(configure);
		cuser.setConfigures(configures);
		cuser.update(this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
			}
		});
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
	            size=arg0.size();
	            if(size==0)
	            	saveConfigureInfo();
	        }

	        @Override
	        public void onError(int arg0, String arg1) {
	        }
	    });
	}
}
