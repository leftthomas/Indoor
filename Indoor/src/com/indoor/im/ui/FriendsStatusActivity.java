package com.indoor.im.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.indoor.im.CustomApplcation;
import com.indoor.im.R;
import com.indoor.im.adapter.FriendsStatusAdapter;
import com.indoor.im.bean.Blog;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

@SuppressLint("SimpleDateFormat") public class FriendsStatusActivity extends ActivityBase implements OnRefreshListener2<ListView>{

	private ImageView gotomystatus;
	private PullToRefreshListView mExpandList;
    private LinkedList<Blog> mListItems;
    private FriendsStatusAdapter mAdapter;  
    private int blognum=0;
    private ArrayList<String> friendsnames;
    private int flag=0;//用来判断是下拉刷新还是上拉加载 ,PullDown:1;PullUp:2
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_status);
		initTopBarForBoth("朋友圈", R.drawable.base_action_bar_add_bg_selector,new onRightImageButtonClickListener() {
			
			@Override
			public void onClick() {
				startActivity(new Intent(FriendsStatusActivity.this, PublishStatusActivity.class));
			}
		} );
		gotomystatus=(ImageView) findViewById(R.id.gotomystatus);
		gotomystatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(FriendsStatusActivity.this, MyStatusActivity.class));
			}
		});
		
		//一定要通过此种方式获取好友列表
		friendsnames=new ArrayList<String>();
		Map<String,BmobChatUser> users = CustomApplcation.getInstance().getContactList();
		List<BmobChatUser> friends=CollectionUtils.map2list(users);
		for(int i=0;i<friends.size();i++){
			friendsnames.add(friends.get(i).getObjectId());
		}
		
		mExpandList = (PullToRefreshListView)findViewById(R.id.expand_list); 
		mExpandList.setMode(Mode.BOTH);  
		mExpandList.setOnRefreshListener(this);
		mListItems=new LinkedList<Blog>();
		mAdapter = new FriendsStatusAdapter(this, mListItems);
        mExpandList.setAdapter(mAdapter); 
        mExpandList.getLoadingLayoutProxy(false, true).setPullLabel("加载更多...");  
		mExpandList.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在载入..."); 
	    mExpandList.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新...");    
	    mExpandList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				
			}
		});
	    if(friendsnames.size()==0){
	    	 showTag("你还没有任何好友，快去添加好友吧！", Effects.standard, R.id.friends_status);
	    }
	    else{
	    	 updateList();
	    }
	}
	
	//加载好友们的动态
	private void updateList() {
		BmobQuery<Blog> query = new BmobQuery<Blog>();
		query.addWhereContainedIn("author", friendsnames);    // 查询所有好友的blog
		
		if(flag==1){
			String dateString= mListItems.get(0).getCreatedAt();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			Date date=null;
		    try {
				date=sdf.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    query.order("createdAt");
		    //查询最新数据，即date之后的数据
			query.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(date));
			//因为总会把最头上那张也查出来，所以这里跳过
			query.setSkip(1);
		}
		else{
			query.order("-createdAt");
			query.setLimit(20);
			query.setSkip(mListItems.size());
		}	
		query.findObjects(this, new FindListener<Blog>() {
		    @Override
		    public void onSuccess(List<Blog> object) {
		    	if(blognum==0 && (object.size()==0 || object==null)){
		    		 showTag("你的好友们还没有发布任何动态，快邀请他们发布动态吧！", Effects.standard, R.id.friends_status);
		    	}
		    	else if(blognum!=0 && (object.size()==0 || object==null)){
		    		showTag("你的所有好友动态已全部加载完！", Effects.standard, R.id.friends_status);
		    	}
		    	else{
		    		if(flag==1){
		    			for(int i=0;i<object.size();i++){
		    				mListItems.addFirst(object.get(i));
				    	} 
		    		}
		    		else{
		    			for(int i=0;i<object.size();i++){
				    		mListItems.addLast(object.get(i));
				    	} 
		    		}
		    		blognum=mListItems.size();
			    	//通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合  
		            mAdapter.notifyDataSetChanged();  
		    	}
	            // Call onRefreshComplete when the list has been refreshed.  
	            mExpandList.onRefreshComplete();  
		    }

		    @Override
		    public void onError(int code, String msg) {
		       showTag("请确认网络是否连接！", Effects.standard, R.id.friends_status);
		       mExpandList.onRefreshComplete();  
		    }
		});
	}
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		 String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),  
	                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
	     refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
	     flag=1;
         new GetDataTask().execute();  
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		 String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),  
	                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
         refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
         flag=2;
         new GetDataTask().execute(); 
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String> {  
		  
	        //后台处理部分  
	        @Override  
	        protected String doInBackground(Void... params) {  
	            try {  
	                Thread.sleep(1000); 
	                updateList();
	            } catch (InterruptedException e) {  
	            }  
	            String str="Added after refresh...I add";   
	            return str;  
	        }  
	  
	        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中  
	        //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值  
	        @Override  
	        protected void onPostExecute(String result) {  
	        	 
	            super.onPostExecute(result);  
	        }   
	}
}
