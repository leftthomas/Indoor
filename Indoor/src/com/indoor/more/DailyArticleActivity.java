package com.indoor.more;

import java.util.List;
import java.util.Random;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.bean.DailyArticle;
import com.indoor.im.ui.BaseActivity;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;
import com.indoor.more.util.GestureListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class DailyArticleActivity extends BaseActivity {
	
	private WebView article;
	private CircleProgressBar progress;
	private String describe = "";   //内容
	private int skipnum=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_article);
		initTopBarForBoth("每日一文", R.drawable.base_action_bar_more_bg_n, new onRightImageButtonClickListener() {
			
			@Override
			public void onClick() {
				showTag("随机一篇", Effects.flip, R.id.essay);
				getCount();
			}
		});
		article = (WebView) findViewById(R.id.edit_describe);
		progress=(CircleProgressBar) findViewById(R.id.progress);
		 //setLongClickable是必须的  
        article.setLongClickable(true);  
        article.setOnTouchListener(new MyGestureListener(this));  
		initData();
	}
	
	public void initData() {
		BmobQuery<DailyArticle> query = new BmobQuery<DailyArticle>();
		if(skipnum<0){
		   showTag("已是最新一篇！", Effects.flip, R.id.essay);
		   skipnum=0;
		}
		else{
			progress.setVisibility(View.VISIBLE);
			query.setSkip(skipnum); 
			query.order("-createdAt");
			//执行查询方法
			query.findObjects(this, new FindListener<DailyArticle>() {
			        @Override
			        public void onSuccess(List<DailyArticle> essay) {
			        	if(essay.size()>0){
			        		describe=essay.get(0).getEssay();
					         //加载富文本
					   		article.loadDataWithBaseURL("", describe, "text/html", "UTF-8",""); 
					   		article.setHorizontalScrollBarEnabled(false);//水平不显示滚动条
					   		progress.setVisibility(View.GONE);
			        	}
			        	else{
			        		showTag("没有更多文章了！", Effects.flip, R.id.essay);
			        		skipnum--;
			        		progress.setVisibility(View.GONE);
			        	}
			           
			        }
			        @Override
			        public void onError(int code, String msg) {
			           showTag("请确认网络是否连接！", Effects.flip, R.id.essay);
			           progress.setVisibility(View.GONE);
			        }
			});
		}	
	} 
	
	/** 
     * 继承GestureListener，重写left和right方法 
     */  
    private class MyGestureListener extends GestureListener {  
        public MyGestureListener(Context context) {  
            super(context);  
        }  
  
        @Override  
        public boolean left() {  
        	//往后查
        	skipnum--;
        	initData();  
            return super.left();  
        }  
  
        @Override  
        public boolean right() {  
        	//查询历史文章，往前查
        	skipnum++;
        	initData(); 
            return super.right();  
        }  
    }  
    
    private void getCount(){
		String bql = "select count(*),* from DailyArticle";//查询DailyArticle表中总记录数并返回所有记录信息
		new BmobQuery<DailyArticle>().doSQLQuery(this,bql, new SQLQueryListener<DailyArticle>(){
			
			@Override
			public void done(BmobQueryResult<DailyArticle> result, BmobException e) {
			   if(e==null){
		            Random random = new Random();
		    		//因为序号从1开始编起，所以要+1
		            skipnum=random.nextInt(result.getCount())+1;
		        	initData();
		         }else{
		        	  showTag("网络错误，请确认网络是否连通！", Effects.flip, R.id.essay);
		        }
			}
		});
	}
}
