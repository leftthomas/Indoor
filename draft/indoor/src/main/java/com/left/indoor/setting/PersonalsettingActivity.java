package com.left.indoor.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.left.indoor.bean.IndoorUser;
import com.left.indoor.map.R;
import com.left.indoor.myview.RoundImageView;
import com.left.indoor.personal.Choosesculpture;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.ResetPasswordListener;
import me.drakeet.materialdialog.MaterialDialog;

@SuppressLint("InflateParams") public class PersonalsettingActivity extends Activity {
	
	private ImageView back;
	private TextView age;
	private TextView sex;
	private TextView profession;
	private TextView changepassword;
	private IndoorUser cuser;
	private MaterialDialog mMaterialDialog;
	private RoundImageView headsculpture;
	private int agechoice;
	private int sexchoice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalsetting);
		back=(ImageView) findViewById(R.id.back);
		age=(TextView) findViewById(R.id.age);
		sex=(TextView) findViewById(R.id.sex);
		profession=(TextView) findViewById(R.id.profession);
		changepassword=(TextView) findViewById(R.id.changepassword);
		headsculpture=(RoundImageView) findViewById(R.id.headsculpture);
		cuser = BmobUser.getCurrentUser(PersonalsettingActivity.this,IndoorUser.class);
		init();//进行设置初始化，加载当前用户的个人资料
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//保存数据
				cuser.update(PersonalsettingActivity.this);
				finish();
			}
		});
        headsculpture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent choose=new Intent(PersonalsettingActivity.this, Choosesculpture.class);
				startActivityForResult(choose, 0);
			}
		});
		age.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mMaterialDialog = new MaterialDialog(PersonalsettingActivity.this);
				// 自定义布局
				final View view = LayoutInflater.from(PersonalsettingActivity.this).inflate(
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
				mMaterialDialog = new MaterialDialog(PersonalsettingActivity.this);
				// 自定义布局
				final View view = LayoutInflater.from(PersonalsettingActivity.this).inflate(
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
				mMaterialDialog = new MaterialDialog(PersonalsettingActivity.this);
				// 自定义布局
				final View view = LayoutInflater.from(PersonalsettingActivity.this).inflate(
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
		
		changepassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				BmobUser.resetPassword(PersonalsettingActivity.this, cuser.getEmail(),new ResetPasswordListener() {
					
					@Override
					public void onSuccess() {
						Toast.makeText(PersonalsettingActivity.this,
								"已向您邮箱发送修改密码邮件，请及时修改", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						
					}
				});	
			}
		});
	}

	//用户个人资料，进行初始化
	private void init() {
		BmobFile bmobFile=cuser.getSculpture();
		bmobFile.loadImageThumbnail(this, headsculpture, 300, 300);
		age.setText(cuser.getAge() + "岁");    //note:int型数值不能直接传参给setText()
		sex.setText(cuser.getGender());
		profession.setText(cuser.getProfession());
	} 
	
	@Override
	protected void onDestroy() {
		//保存数据
		cuser.update(this);
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == 0) {
	    	init();
	    }
	}	
}
