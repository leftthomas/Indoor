package com.left.indoor.media;

import static com.left.indoor.map.R.id.tv_describe;
import static com.left.indoor.map.R.id.tv_title;
import static com.left.indoor.map.R.id.tv_photo;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.left.indoor.bean.News;
import com.left.indoor.map.R;
import com.left.indoor.utils.BaseAdapterHelper;
import com.left.indoor.utils.QuickAdapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "InflateParams", "HandlerLeak" }) public class NewsmediaFragment extends Fragment implements
OnItemClickListener{
	
	RelativeLayout layout_action;
	PullToRefreshListView  mPullRefreshListView;  
	ListView listview;
	public QuickAdapter<News> NewsAdapter;
	View newsdetial;
	RelativeLayout progress;
	LinearLayout layout_no;
	TextView tv_no;
	int position; // 新闻list中的第几个
	int maxcount;
	boolean iffirst;
	View loadmoreView;
	View rootView;
	Button loadMoreButton;
	Handler handler = new Handler();
	List<News> losts2 = new ArrayList<News>();
	private static final int REFRESH_COMPLETE = 0X110;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
		ViewGroup container, Bundle savedInstanceState) {
		//rootView为NewsmediaFragment中所有控件集合
		rootView = inflater.inflate(R.layout.newsmediafragment, container,false);  		
		loadmoreView = getActivity().getLayoutInflater().inflate(R.layout.load_more, null);
		iffirst=true;
		initViews();
		initListeners();
		initData();
		return rootView;
	}
	
	
	public void initViews() {
		 //实例化控件
		progress = (RelativeLayout) rootView.findViewById(R.id.progress);
		layout_no = (LinearLayout) rootView.findViewById(R.id.layout_no);
		tv_no = (TextView) rootView.findViewById(R.id.tv_no);
		layout_action = (RelativeLayout) rootView.findViewById(R.id.layout_action);
		mPullRefreshListView = (PullToRefreshListView)  rootView.findViewById(R.id.list_lost);
		 mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {  
           @Override  
           public void onRefresh(PullToRefreshBase<ListView> refreshView) {  
               String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),  
                       DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
               refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
               new GetDataTask().execute();  
           }

       });  
       listview =  mPullRefreshListView.getRefreshableView();

       
		//分页加载
		loadMoreButton = (Button) loadmoreView
				.findViewById(R.id.loadMoreButton);
		loadMoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMoreButton.setText("加载中......");
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						loadData();
						NewsAdapter.notifyDataSetChanged();
						loadMoreButton.setText("显示下20条");
					}

				}, 1000);
			}
		});
		
	}
	
	 public class GetDataTask extends AsyncTask<Void, Void, String> {  
		  
	        @Override  
	        protected String doInBackground(Void... params) {  
	            // Simulates a background job.  
	            try {  
	                Thread.sleep(1000);  
	            } catch (InterruptedException e) {  
	            }  
	            String str="Added after refresh...I add";  
	            return str;  
	        }  
	  
	        @Override  
	        protected void onPostExecute(String result) {  
	          initData();
	      		mPullRefreshListView.onRefreshComplete();
	  
	            super.onPostExecute(result);  
	        }  
	    }  
	 
	public void initListeners() {
		listview.setOnItemClickListener(this);
	}
	
	public void initData() { // 初始化数据
		if (NewsAdapter == null) {
			NewsAdapter = new QuickAdapter<News>(getActivity().getApplicationContext(),
					R.layout.item_list) {
				@Override
				protected void convert(BaseAdapterHelper helper, News lost) {
					helper.setText(tv_title, lost.getTitle()).setText(
							tv_describe, lost.getDescribe());
					if (lost.getPhoto() != null) { 
						// 如果图片文件非空
						helper.setImageUrl(tv_photo, "http://file.bmob.cn/"
								+ lost.getPhoto().getUrl());
					}
				}
			};
		}
		listview.setAdapter(NewsAdapter);
		queryNews();
	}
	
	/************** 下拉刷新部分 ****************/
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				initData();
				break;
			}
		};
	};
	
	public void onRefresh() {
		mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, 
			long arg3) {
		position = arg2;
		onEdit(arg1);
	}
	
	
	public void onEdit(View v) {
		Intent intent = new Intent(getActivity().getApplicationContext(), AddActivity.class);
		String details = NewsAdapter.getItem(position-1).getDetails();
		intent.putExtra("details", details);
		startActivity(intent);
	}
	
	private void showView() {
		listview.setVisibility(View.VISIBLE);
		layout_no.setVisibility(View.GONE);
	}

	private void queryNews() {
		showView();
		BmobQuery<News> query = new BmobQuery<News>();
		query.order("-updatedAt");// 按时间顺序排序
		query.setLimit(1000);
		query.findObjects(getActivity().getApplicationContext(), new FindListener<News>() {

			@Override
			public void onSuccess(List<News> losts) {
				NewsAdapter.clear();
				if (losts == null || losts.size() == 0) {
					showErrorView(0);
					NewsAdapter.notifyDataSetChanged();
					return;
				}

				progress.setVisibility(View.GONE);
				if (iffirst) { // 如果这是第一次,非刷新，那么设置页脚
					listview.addFooterView(loadmoreView);
					iffirst = false;
				}

				maxcount = losts.size();
				if (losts.size() < 20)
					NewsAdapter.addAll(losts);
				else {
					for (int i = 0; i < 20; i++) {
						NewsAdapter.add(losts.get(i));
					}
				}
				losts2.clear();
				losts2.addAll(losts);
			}

			@Override
			public void onError(int code, String arg0) {
			}
		});
	}
	
	// 无内容的时候
	private void showErrorView(int tag) {
		progress.setVisibility(View.GONE);
		listview.setVisibility(View.GONE);
		layout_no.setVisibility(View.VISIBLE);
	    tv_no.setText(getResources().getText(R.string.list_no_data_lost));
	}
	
	public void loadData() {
		if (maxcount <= NewsAdapter.getCount()) {
			Toast.makeText(getActivity().getApplicationContext(), "没有更多新闻了",
					Toast.LENGTH_LONG).show();
		} else if (maxcount > NewsAdapter.getCount()
				&& maxcount <= NewsAdapter.getCount() + 20) {
			for (int i = NewsAdapter.getCount(); i < maxcount; i++) {
				NewsAdapter.add(losts2.get(i));
			}
		} else if (maxcount > NewsAdapter.getCount()
				&& maxcount > NewsAdapter.getCount() + 20) {
			NewsAdapter.add(losts2.get(NewsAdapter.getCount()));

			int size = NewsAdapter.getCount();
			for (int i = size; i < size + 19; i++) {
				NewsAdapter.add(losts2.get(i));
			}
		}
	}

}

