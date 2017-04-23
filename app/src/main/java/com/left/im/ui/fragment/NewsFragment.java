package com.left.im.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.left.im.R;
import com.left.im.adapter.NewsAdapter;
import com.left.im.adapter.OnRecyclerViewListener;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.base.ParentWithNaviFragment;
import com.left.im.bean.News;
import com.left.im.ui.NewsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 新闻界面
 *
 * @author :left
 * @project:NewsFragment
 * @date :2017-04-23-18:23
 */
public class NewsFragment extends ParentWithNaviFragment {

    @Bind(R.id.rc_view)
    RecyclerView rc_view;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    LinearLayoutManager layoutManager;
    NewsAdapter adapter;
    //默认展示头条新闻
    private String newsCategory = "头条";
    private CharSequence[] items = {"头条", "娱乐", "科技", "影视", "体育", "军事",
            "财经", "政务"};

    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String title() {
        return "新闻";
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_more_bg_selector;
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("选择新闻类别");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        newsCategory = items[item].toString();
                        sw_refresh.setRefreshing(true);
                        query();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        IMutlipleItem<News> mutlipleItem = new IMutlipleItem<News>() {

            @Override
            public int getItemViewType(int postion, News n) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_news;
            }

            @Override
            public int getItemCount(List<News> list) {
                return list.size();
            }
        };
        adapter = new NewsAdapter(getActivity(), mutlipleItem, null);
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        setListener();
        return rootView;
    }

    private void setListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                query();
            }
        });
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("news", adapter.getItem(position));
                startActivity(NewsActivity.class, bundle);
            }

            @Override
            public boolean onItemLongClick(int position) {
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
    }

    /**
     * 查询新闻信息
     */
    public void query() {

        BmobQuery<News> query = new BmobQuery<News>();
        //返回100条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(100);
        //执行查询方法
        query.findObjects(getActivity(), new FindListener<News>() {
            @Override
            public void onSuccess(List<News> list) {
                List<News> newsList = new ArrayList<>();
                newsList.clear();
                for (News news : list) {
                    if (Arrays.asList(news.getCategory()).contains(newsCategory))
                        newsList.add(news);
                }
                adapter.bindDatas(newsList);
                adapter.notifyDataSetChanged();
                sw_refresh.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
                adapter.bindDatas(null);
                adapter.notifyDataSetChanged();
                sw_refresh.setRefreshing(false);
                Log.i("bmob", "失败：" + s + "," + i);
            }
        });
    }

}
