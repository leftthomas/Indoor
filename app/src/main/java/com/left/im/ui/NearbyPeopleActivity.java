package com.left.im.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.left.im.R;
import com.left.im.adapter.NearbyAdapter;
import com.left.im.adapter.OnRecyclerViewListener;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.bean.User;

import java.util.List;

import butterknife.Bind;

/**
 * 附近的人
 *
 * @author :left
 * @project:BlackListActivity
 * @date :2017-04-25-18:23
 */
public class NearbyPeopleActivity extends ParentWithNaviActivity {

    @Bind(R.id.ll_root)
    LinearLayout ll_root;
    @Bind(R.id.rc_view)
    RecyclerView rc_view;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    NearbyAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected String title() {
        return "附近的人";
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
                finish();
            }

            @Override
            public void clickRight() {
                // TODO: 2017/4/24
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_people);
        initNaviView();
        //单一布局
        IMutlipleItem<User> mutlipleItem = new IMutlipleItem<User>() {

            @Override
            public int getItemViewType(int postion, User u) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_nearby_people;
            }

            @Override
            public int getItemCount(List<User> list) {
                return list.size();
            }
        };
        adapter = new NearbyAdapter(this, mutlipleItem, null);
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        setListener();
    }

    private void setListener() {
        ll_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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
                bundle.putSerializable("u", adapter.getItem(position));
                startActivity(UserInfoActivity.class, bundle);
                log("点击：" + position);
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
     * 查询附近的人
     */
    public void query() {


//        adapter.bindDatas();
        adapter.notifyDataSetChanged();
        sw_refresh.setRefreshing(false);
    }


}
