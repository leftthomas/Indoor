package com.left.im.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.left.im.R;
import com.left.im.adapter.OnRecyclerViewListener;
import com.left.im.adapter.ShopAdapter;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.base.ParentWithNaviFragment;
import com.left.im.bean.Goods;
import com.left.im.ui.GoodActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 购物界面
 *
 * @author :left
 * @project:ShopFragment
 * @date :2017-04-23-18:23
 */
public class ShopFragment extends ParentWithNaviFragment {

    @Bind(R.id.rc_view)
    RecyclerView rc_view;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    LinearLayoutManager layoutManager;
    ShopAdapter adapter;
    //默认展示热门商品
    private String goodsCategory = "热门";
    private CharSequence[] items = {"热门", "智能设备", "食品", "玩具", "体育用品", "家居用品", "箱包", "化妆品", "配饰", "家用电器"};

    public ShopFragment() {
    }

    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String title() {
        return "购物";
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
                builder.setTitle("选择商品类别");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        goodsCategory = items[item].toString();
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
        rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        IMutlipleItem<Goods> mutlipleItem = new IMutlipleItem<Goods>() {

            @Override
            public int getItemViewType(int postion, Goods c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_shop;
            }

            @Override
            public int getItemCount(List<Goods> list) {
                return list.size();
            }
        };
        adapter = new ShopAdapter(getActivity(), mutlipleItem, null);
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
                bundle.putSerializable("good", adapter.getItem(position));
                startActivity(GoodActivity.class, bundle);
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
     * 查询商品信息
     */
    public void query() {

        BmobQuery<Goods> query = new BmobQuery<Goods>();
//        因为付费之后才能使用模糊查询，所以这条语句没法用了
//        query.addWhereContains("category", goodsCategory);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(getActivity(), new FindListener<Goods>() {
            @Override
            public void onSuccess(List<Goods> list) {
                List<Goods> goodList = new ArrayList<>();
                goodList.clear();
                for (Goods good : list) {
                    if (Arrays.asList(good.getCategory()).contains(goodsCategory))
                        goodList.add(good);
                }
                adapter.bindDatas(goodList);
                adapter.notifyDataSetChanged();
                sw_refresh.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
                adapter.bindDatas(null);
                adapter.notifyDataSetChanged();
                sw_refresh.setRefreshing(false);
                log(s);
            }
        });
    }
}
