package com.indoor.im.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.im.task.BRequest;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.adapter.NearPeopleAdapter;
import com.indoor.im.bean.User;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;
import com.indoor.im.view.xlist.XListView;
import com.indoor.im.view.xlist.XListView.IXListViewListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

/**
 * 附近的人列表
 * 
 * @ClassName: NewFriendActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-6 下午4:28:09
 */
public class NearPeopleActivity extends ActivityBase implements IXListViewListener,OnItemClickListener {

	XListView mListView;
	NearPeopleAdapter adapter;
	String from = "";
	CircleProgressBar progress;
	private CharSequence[] items = {"全部","只看男", "只看女","全部（不包括好友）","只看男（不包括好友）", "只看女（不包括好友）" }; 
	private int itemnum=0;//用来判断搜索条件
	boolean isshowfriend=true;
	String Property=null;
	Object isman=null;
	List<User> nears = new ArrayList<User>();

	private double QUERY_KILOMETERS = 10;//默认查询10公里范围内的人
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near_people);
		initView();
	}

	private void initView() {
		initTopBarForBoth("附近的人", R.drawable.base_action_bar_more_bg_n, new onRightImageButtonClickListener() {
			
			@Override
			public void onClick() {
				AlertDialog.Builder builder = new AlertDialog.Builder(NearPeopleActivity.this);
				builder.setTitle("设置筛选条件");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	itemnum=item;
				    	if(itemnum==0){
							isshowfriend=true;
							Property=null;
							isman=null;
						}
						else if(itemnum==1){
							isshowfriend=true;
							Property="sex";
							isman=true;
						}
						else if(itemnum==2){
							isshowfriend=true;
							Property="sex";
							isman=false;
						}
						else if(itemnum==3){
							isshowfriend=false;
							Property=null;
							isman=null;
						}
						else if(itemnum==4){
							isshowfriend=false;
							Property="sex";
							isman=true;
						}
						else if(itemnum==5){
							isshowfriend=false;
							Property="sex";
							isman=false;
						}
				    	nears.clear();
				    	initNearByList(false);
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
		initXListView();
	}

	private void initXListView() {
		mListView = (XListView) findViewById(R.id.list_near);
		progress=(CircleProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		mListView.setOnItemClickListener(this);
		// 首先不允许加载更多
		mListView.setPullLoadEnable(false);
		// 允许下拉
		mListView.setPullRefreshEnable(true);
		// 设置监听器
		mListView.setXListViewListener(this);
		//
		mListView.pullRefreshing();
		
		adapter = new NearPeopleAdapter(this, nears);
		mListView.setAdapter(adapter);
		initNearByList(false);
	}

	
	int curPage = 0;
	private void initNearByList(final boolean isUpdate){
		if(!isUpdate){
			progress.setVisibility(View.VISIBLE);
		}
		
		if(!mApplication.getLatitude().equals("")&&!mApplication.getLongtitude().equals("")){
			double latitude = Double.parseDouble(mApplication.getLatitude());
			double longtitude = Double.parseDouble(mApplication.getLongtitude());
			//封装的查询方法，当进入此页面时 isUpdate为false，当下拉刷新的时候设置为true就行。
			//此方法默认每页查询10条数据,若想查询多于10条，可在查询之前设置BRequest.QUERY_LIMIT_COUNT，如：BRequest.QUERY_LIMIT_COUNT=20
			// 此方法是新增的查询指定10公里内的用户列表，默认包含好友列表
			userManager.queryKiloMetersListByPage(isUpdate,0,"location", longtitude, latitude, isshowfriend,QUERY_KILOMETERS,Property,isman,new FindListener<User>() {

				@Override
				public void onSuccess(List<User> arg0) {
					if (CollectionUtils.isNotNull(arg0)) {
						if(isUpdate){
							nears.clear();
						}
						adapter.addAll(arg0);
						if(arg0.size()<BRequest.QUERY_LIMIT_COUNT){
							mListView.setPullLoadEnable(false);
							showTag("附近的人搜索完成!",Effects.thumbSlider,R.id.nearpeople);
						}else{
							mListView.setPullLoadEnable(true);
						}
					}else{
						showTag("暂无附近的人!",Effects.thumbSlider,R.id.nearpeople);
					}
					
					if(!isUpdate){
						progress.setVisibility(View.GONE);
					}else{
						refreshPull();
					}
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					showTag("暂无附近的人!",Effects.thumbSlider,R.id.nearpeople);
					mListView.setPullLoadEnable(false);
					if(!isUpdate){
						progress.setVisibility(View.GONE);
					}else{
						refreshPull();
					}
				}

			});
		}else{
			showTag("暂无附近的人!",Effects.thumbSlider,R.id.nearpeople);
			progress.setVisibility(View.GONE);
			refreshPull();
		}
		
	}
	
	/** 查询更多
	  * @Title: queryMoreNearList
	  * @Description: TODO
	  * @param @param page 
	  * @return void
	  * @throws
	  */
	private void queryMoreNearList(int page){
		double latitude = Double.parseDouble(mApplication.getLatitude());
		double longtitude = Double.parseDouble(mApplication.getLongtitude());
		userManager.queryKiloMetersListByPage(true,page,"location", longtitude, latitude, isshowfriend,QUERY_KILOMETERS,Property,isman,new FindListener<User>() {
	
			@Override
			public void onSuccess(List<User> arg0) {
				if (CollectionUtils.isNotNull(arg0)) {
					adapter.addAll(arg0);
				}
				refreshLoad();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				ShowLog("查询更多附近的人出错:"+arg1);
				mListView.setPullLoadEnable(false);
				refreshLoad();
			}

		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		User user = (User) adapter.getItem(position-1);
		Intent intent =new Intent(this,SetMyInfoActivity.class);
		intent.putExtra("from", "add");
		intent.putExtra("username", user.getUsername());
		startAnimActivity(intent);		
	}

	@Override
	public void onRefresh() {
		initNearByList(true);
	}

	private void refreshLoad(){
		if (mListView.getPullLoading()) {
			mListView.stopLoadMore();
		}
	}
	
	private void refreshPull(){
		if (mListView.getPullRefreshing()) {
			mListView.stopRefresh();
		}
	}
	@Override
	public void onLoadMore() {
		double latitude = Double.parseDouble(mApplication.getLatitude());
		double longtitude = Double.parseDouble(mApplication.getLongtitude());
		userManager.queryKiloMetersTotalCount(User.class, "location", longtitude, latitude, isshowfriend,QUERY_KILOMETERS,Property,isman,new CountListener() {

			@Override
			public void onSuccess(int arg0) {
				if(arg0 >nears.size()){
					curPage++;
					queryMoreNearList(curPage);
				}else{
					showTag("数据加载完成",Effects.thumbSlider,R.id.nearpeople);
					mListView.setPullLoadEnable(false);
					refreshLoad();
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ShowLog("查询附近的人总数失败"+arg1);
				refreshLoad();
			}
		});
	}
}
