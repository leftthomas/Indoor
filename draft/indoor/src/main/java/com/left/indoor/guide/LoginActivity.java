package com.left.indoor.guide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.left.indoor.bean.IndoorUser;
import com.left.indoor.map.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface", "SdCardPath"}) public class LoginActivity extends Activity {
	
	WebView loginWebView;
	Bitmap bm;
	String filename;
	IndoorUser user=new IndoorUser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginWebView=(WebView) findViewById(R.id.loginwebview);
		loginWebView.getSettings().setJavaScriptEnabled(true);
		loginWebView.addJavascriptInterface(new UserData(), "UserData");
		loginWebView.setVisibility(View.GONE);   
		loginWebView.setWebViewClient(new WebViewClient(){  
		  
		    @Override  
		    public void onPageFinished(WebView view, String url) {  
		    	loginWebView.setVisibility(View.VISIBLE);  
		        super.onPageFinished(view, url);  
		    }        
		});  
		loginWebView.loadUrl("file:///android_asset/login/index.html");
		Bmob.initialize(this,"dfd76835fa9208696db407291e5eab51"); 
	}
	private class UserData
	{
        private String name = "";
        private String password = "";

		@JavascriptInterface
		public void toregister(){
			Intent registerintent;
			registerintent=new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(registerintent);
			finish();
		}

		@JavascriptInterface
		public void getUsername(String name){
			this.name=name;
		}
		@JavascriptInterface
		public void getPassword(String password){
			this.password=password;
		}
		@JavascriptInterface
		public void save(){	
			user.setUsername(name);
		    user.setPassword(password);
			user.login(LoginActivity.this, new SaveListener() {
				
				@Override
				public void onSuccess() {
					 Intent mainintent;
			 	     mainintent=new Intent(LoginActivity.this,MainActivity.class);
			 		 startActivity(mainintent);
			 		 finish();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误，请重试", Toast.LENGTH_SHORT).show();
                }
			});
		}
	}
	
}
