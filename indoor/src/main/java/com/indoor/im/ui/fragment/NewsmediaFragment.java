package com.indoor.im.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.indoor.im.R;
import com.indoor.im.adapter.QuickAdapter;
import com.indoor.im.adapter.base.BaseAdapterHelper;
import com.indoor.im.bean.News;
import com.indoor.im.ui.NewsContentActivity;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import static com.indoor.im.R.id.tv_describe;
import static com.indoor.im.R.id.tv_photo;
import static com.indoor.im.R.id.tv_title;

@SuppressLint({"InflateParams", "HandlerLeak"})
public class NewsmediaFragment extends FragmentBase implements
        OnItemClickListener {

    private static final int REFRESH_COMPLETE = 0X110;
    boolean iffirst;
    private PullToRefreshListView mPullRefreshListView;
    private ListView listview;
    private QuickAdapter<News> NewsAdapter;
    private CircleProgressBar progress;
    private LinearLayout layout_no;
    private TextView tv_no;
    private int maxcount;
    private View loadmoreView;
    private Button loadMoreButton;
    private Handler handler = new Handler();
    private List<News> losts2 = new ArrayList<News>();
    private String newsCategory = "头条";//默认展示头条新闻
    private CharSequence[] items = {"头条", "娱乐", "科技", "影视", "体育", "军事", "财经", "游戏", "旅游", "房产", "政务"};
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
        return inflater.inflate(R.layout.fragment_newsmedia, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadmoreView = getActivity().getLayoutInflater().inflate(R.layout.load_more, null);
        iffirst = true;
        initViews();
        initData();
        initListeners();
    }

    public void initViews() {
        //实例化控件
        initTopBarForRight("新闻媒体", R.drawable.base_action_bar_more_bg_n, new onRightImageButtonClickListener() {

            @Override
            public void onClick() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("选择新闻类别");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        newsCategory = items[item].toString();
                        initData();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        progress = (CircleProgressBar) findViewById(R.id.progress);
        layout_no = (LinearLayout) findViewById(R.id.layout_no);
        tv_no = (TextView) findViewById(R.id.tv_no);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.list_lost);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                new GetDataTask().execute();
            }
        });
        listview = mPullRefreshListView.getRefreshableView();

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

    public void initListeners() {
        listview.setOnItemClickListener(this);
    }

    public void initData() { // 初始化数据
        if (NewsAdapter == null) {
            NewsAdapter = new QuickAdapter<News>(getActivity().getApplicationContext(),
                    R.layout.item_news) {
                @Override
                protected void convert(BaseAdapterHelper helper, News lost) {
                    helper.setText(tv_title, lost.getTitle()).setText(
                            tv_describe, lost.getDescribe());
                    if (lost.getPhoto() != null) {
                        // 如果图片文件非空
                        helper.setImageUrl(tv_photo, lost.getPhoto().getFileUrl(getActivity().getApplicationContext()));
                    }
                }
            };
        }
        listview.setAdapter(NewsAdapter);
        queryNews();
    }

    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
        String details = NewsAdapter.getItem(position - 1).getDetails();
        intent.putExtra("details", details);
        startAnimActivity(intent);
    }

    private void showView() {
        listview.setVisibility(View.VISIBLE);
        layout_no.setVisibility(View.GONE);
    }

    private void queryNews() {
        showView();
        BmobQuery<News> query = new BmobQuery<News>();
        query.addWhereContains("category", newsCategory);
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
        tv_no.setText(getResources().getText(R.string.list_no_data));
    }

    public void loadData() {
        if (maxcount <= NewsAdapter.getCount()) {
            showTag("没有更多新闻了", Effects.standard, R.id.news);
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

    public class GetDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            String str = "Added after refresh...I add";
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

