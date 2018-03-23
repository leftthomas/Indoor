package com.left.indoor.shopping;

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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.left.indoor.bean.Goods;
import com.left.indoor.map.R;
import com.left.indoor.utils.BaseAdapterHelper;
import com.left.indoor.utils.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import static com.left.indoor.map.R.id.goodsname;
import static com.left.indoor.map.R.id.goodsphoto;
import static com.left.indoor.map.R.id.price;

@SuppressLint({ "InflateParams", "HandlerLeak" }) public class Online_shoppingFragment extends Fragment 
implements OnItemClickListener{

	private static final int REFRESH_COMPLETE = 0X110;
	public QuickAdapter<Goods> GoodsAdapter;
	PullToRefreshListView mPullRefreshListView;
	ListView listview;
	RelativeLayout progress;
	LinearLayout layout_no;
	TextView tv_no;
	int position;  //商品list中的第几个
	int maxcount;
	boolean iffirst;
	View loadmoreView;
	View rootView;
	Button loadMoreButton;
	Handler handler = new Handler();
	List<Goods> GoodsList = new ArrayList<Goods>();
	/************** 下拉刷新部分 ****************/
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case REFRESH_COMPLETE:
					initData();
					break;
			}
		}

		;
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
		ViewGroup container, Bundle savedInstanceState) {
		//rootView为online_shoppingfragment中所有控件集合
		rootView = inflater.inflate(R.layout.online_shoppingfragment, container, false);
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
		mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.list_goods);
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
						GoodsAdapter.notifyDataSetChanged();
						loadMoreButton.setText("显示下20条");
					}
				}, 1000);
			}
		});
	}
	 
	public void initListeners() {
		listview.setOnItemClickListener(this);
	}

	// 初始化数据
	public void initData() { 
		if (GoodsAdapter == null) {
			GoodsAdapter = new QuickAdapter<Goods>(getActivity().getApplicationContext(),
					R.layout.goodsitem) {	
				@Override
				protected void convert(BaseAdapterHelper helper, Goods good) {
					helper.setText(goodsname, good.getName()).setText(
							price, good.getPrice());
					if (good.getPhoto() != null) {
						// 如果图片文件非空
						helper.setImageUrl(goodsphoto, "http://file.bmob.cn/"
								+ good.getPhoto().getUrl());
					}
				}
			};
		}
		listview.setAdapter(GoodsAdapter);
		queryGoods();
	}
	
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
		Intent intent = new Intent(getActivity().getApplicationContext(), Goods_detailActivity.class);
		String details = GoodsAdapter.getItem(position-1).getDescribe();
		intent.putExtra("details", details);
		startActivity(intent);
	}
	
	private void showView() {
		listview.setVisibility(View.VISIBLE);
		layout_no.setVisibility(View.GONE);
	}

	private void queryGoods() {
		showView();
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.order("-updatedAt");// 按时间顺序排序
		query.setLimit(1000);
		query.findObjects(getActivity().getApplicationContext(), new FindListener<Goods>() {

			@Override
			public void onSuccess(List<Goods> goods) {
				GoodsAdapter.clear();
				if (goods == null || goods.size() == 0) {
					showErrorView(0);
					GoodsAdapter.notifyDataSetChanged();
					return;
				}

				progress.setVisibility(View.GONE);
				if (iffirst) {
					// 如果这是第一次,非刷新，那么设置页脚
					listview.addFooterView(loadmoreView);
					iffirst = false;
				}

				maxcount = goods.size();
				if (goods.size() < 20)
					GoodsAdapter.addAll(goods);
				else {
					for (int i = 0; i < 20; i++) {
						GoodsAdapter.add(goods.get(i));
					}
				}
				GoodsList.clear();
				GoodsList.addAll(goods);
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
		if (maxcount <= GoodsAdapter.getCount()) {
			Toast.makeText(getActivity().getApplicationContext(), "没有更多商品了",
					Toast.LENGTH_LONG).show();
		} else if (maxcount > GoodsAdapter.getCount()
				&& maxcount <= GoodsAdapter.getCount() + 20) {
			for (int i = GoodsAdapter.getCount(); i < maxcount; i++) {
				GoodsAdapter.add(GoodsList.get(i));
			}
		} else if (maxcount > GoodsAdapter.getCount()
				&& maxcount > GoodsAdapter.getCount() + 20) {
			GoodsAdapter.add(GoodsList.get(GoodsAdapter.getCount()));

			int size = GoodsAdapter.getCount();
			for (int i = size; i < size + 19; i++) {
				GoodsAdapter.add(GoodsList.get(i));
			}
		}
	}

	public class GetDataTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			String str = "";
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			initData();
			mPullRefreshListView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

}