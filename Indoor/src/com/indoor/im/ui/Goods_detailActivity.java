package com.indoor.im.ui;

import com.indoor.im.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


@SuppressLint({ "HandlerLeak", "SetJavaScriptEnabled" }) public class Goods_detailActivity extends ActivityBase {
	
	WebView edit_describe;
	String describe = "";      // 商品描述
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_goods_detail);
	    edit_describe = (WebView) findViewById(R.id.edit_describe);
	    initTopBarForLeft("商品详情");
	    initData();
	}	
	
	public void initData() {
		describe = getIntent().getStringExtra("details");
		//加载富文本
		edit_describe.loadDataWithBaseURL("", describe, "text/html", "UTF-8",""); 
		edit_describe.setHorizontalScrollBarEnabled(false);//水平不显示滚动条
		edit_describe.setVerticalScrollBarEnabled(false); //垂直不显示滚动条
		//使能JavaScript
		edit_describe.getSettings().setJavaScriptEnabled(true);
		//使其html中的链接点击不采用系统浏览器或第三方浏览器进行跳转，而采用自身打开
		edit_describe.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                    return false;        
            }
        });
	} 
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		startAnimActivity(MainActivity.class);
	}
}
