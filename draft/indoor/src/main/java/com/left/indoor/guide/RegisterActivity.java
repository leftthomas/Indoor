package com.left.indoor.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.left.indoor.bean.IndoorUser;
import com.left.indoor.map.R;

import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends Activity {
    IndoorUser user;
    private EditText username;
	private EditText password;
	private EditText cofirmpassword;
	private EditText email;
	private Button register;
	private String textusername;
	private String textpassword;
	private String textcofirmpassword;
	private String textmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		username=(EditText) findViewById(R.id.registerusername);
		password=(EditText) findViewById(R.id.registerpassword);
		cofirmpassword=(EditText) findViewById(R.id.confirmpassword);
		email=(EditText) findViewById(R.id.email);
		register=(Button) findViewById(R.id.registerbutton);
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                textusername = username.getText().toString();//获取用户昵称
                textpassword = password.getText().toString();//获取用户密码
                textcofirmpassword = cofirmpassword.getText().toString();//获取用户确认密码
                textmail = email.getText().toString();//获取用户邮箱
                if(textusername.length()<1)
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();

                else if(textpassword.length()<6)
                    Toast.makeText(RegisterActivity.this, "密码太短，请重新输入", Toast.LENGTH_SHORT).show();
                else if(!textcofirmpassword.equals(textpassword))
                    Toast.makeText(RegisterActivity.this, "两次密码输入不一致，请重试", Toast.LENGTH_SHORT).show();
                else if(textmail.length()<1)
                    Toast.makeText(RegisterActivity.this, "请输入邮箱号码", Toast.LENGTH_SHORT).show();
                else {
					user =new IndoorUser();
					user.setUsername(textusername);
					user.setPassword(textpassword);
					user.setEmail(textmail);
					user.setAge(21);
                    user.setGender("男");
                    user.setProfession("学生");
                    user.signUp(RegisterActivity.this, new SaveListener() {
						
						@Override
						public void onSuccess() {
                            Toast.makeText(RegisterActivity.this, "请及时验证邮箱，有效期为一周", Toast.LENGTH_SHORT).show();
                            Intent guideintent;
							guideintent=new Intent(RegisterActivity.this,ImprovepersonaldataActivity.class);
							startActivity(guideintent);
							finish();
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							if(arg1.contains("username")){
                                Toast.makeText(RegisterActivity.this, "用户名已存在,请重新注册", Toast.LENGTH_SHORT).show();
                            }
							else if(arg1.contains("valid")){
                                Toast.makeText(RegisterActivity.this, "请输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
                            }
							else if(arg1.contains("email")){
                                Toast.makeText(RegisterActivity.this, "邮箱已注册,请输入其他邮箱", Toast.LENGTH_SHORT).show();
                            }
						}
					});
				}
			}
		});
	}
	
}
