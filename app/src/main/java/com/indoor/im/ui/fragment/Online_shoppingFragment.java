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
import com.indoor.im.bean.Goods;
import com.indoor.im.ui.Goods_detailActivity;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import static com.indoor.im.R.id.goodsname;
import static com.indoor.im.R.id.goodsphoto;
import static com.indoor.im.R.id.price;

@SuppressLint({"InflateParams", "HandlerLeak"})
public class Online_shoppingFragment extends FragmentBase
        implements OnItemClickListener {

    private static final int REFRESH_COMPLETE = 0X110;
    public QuickAdapter<Goods> GoodsAdapter;
    PullToRefreshListView mPullRefreshListView;
    ListView listview;
    CircleProgressBar progress;
    LinearLayout layout_no;
    TextView tv_no;
    int maxcount;
    boolean iffirst;
    View loadmoreView;
    Button loadMoreButton;
    Handler handler = new Handler();
    List<Goods> GoodsList = new ArrayList<Goods>();
    private String GoodsCategory = "热门";//默认展示热门商品
    private CharSequence[] items = {"热门", "智能设备", "食品", "玩具", "体育用品", "家居用品", "箱包", "化妆品", "配饰", "家用电器"};
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
        return inflater.inflate(R.layout.fragment_online_shopping, container, false);
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
        initTopBarForRight("购物", R.drawable.base_action_bar_more_bg_n, new onRightImageButtonClickListener() {

            @Override
            public void onClick() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("选择商品类别");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        GoodsCategory = items[item].toString();
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
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.list_goods);
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
                    R.layout.item_goods) {
                @Override
                protected void convert(BaseAdapterHelper helper, Goods good) {
                    helper.setText(goodsname, good.getName()).setText(
                            price, good.getPrice());
                    if (good.getPhoto() != null) {
                        // 如果图片文件非空
                        helper.setImageUrl(goodsphoto, good.getPhoto().getFileUrl(getActivity().getApplicationContext()));
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
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        Intent intent = new Intent(getActivity(), Goods_detailActivity.class);
        String details = GoodsAdapter.getItem(position - 1).getDescribe();
        intent.putExtra("details", details);
        startAnimActivity(intent);
    }

    private void showView() {
        listview.setVisibility(View.VISIBLE);
        layout_no.setVisibility(View.GONE);
    }

    private void queryGoods() {
        showView();
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.addWhereContains("category", GoodsCategory);
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
        tv_no.setText(getResources().getText(R.string.list_no_data));
    }

    public void loadData() {
        if (maxcount <= GoodsAdapter.getCount()) {
            showTag("没有更多商品了", Effects.thumbSlider, R.id.quyu);
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