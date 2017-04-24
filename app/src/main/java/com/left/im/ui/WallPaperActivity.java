package com.left.im.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.gc.materialdesign.widgets.SnackBar;
import com.left.im.R;
import com.left.im.adapter.OnRecyclerViewListener;
import com.left.im.adapter.WallPaperAdapter;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.bean.User;
import com.left.im.bean.WallPapers;

import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 背景设置
 *
 * @author :left
 * @project:WallPaperActivity
 * @date :2017-04-25-18:23
 */
public class WallPaperActivity extends ParentWithNaviActivity {

    @Bind(R.id.ll_root)
    LinearLayout ll_root;
    @Bind(R.id.rc_view)
    RecyclerView rc_view;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    WallPaperAdapter adapter;
    LinearLayoutManager layoutManager;
    User user;
    Activity activity;
    Context context;

    @Override
    protected String title() {
        return "背景设置";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_paper);
        initNaviView();
        context = this;
        activity = this;
        user = (User) getBundle().getSerializable("u");
        IMutlipleItem<WallPapers> mutlipleItem = new IMutlipleItem<WallPapers>() {

            @Override
            public int getItemViewType(int postion, WallPapers w) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_wall_paper;
            }

            @Override
            public int getItemCount(List<WallPapers> list) {
                return list.size();
            }
        };
        adapter = new WallPaperAdapter(this, mutlipleItem, null);
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
            public void onItemClick(final int position) {
                log("点击：" + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("你想将此壁纸设为？");
                builder.setPositiveButton("空间背景", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User newUser = new User();
                        newUser.setSpace_background(adapter.getItem(position).getPicture());
                        newUser.update(context, user.getObjectId(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                new SnackBar(activity, "已将此图设为空间背景", "", null).show();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                log(s);
                            }
                        });
                    }
                });
                builder.setNegativeButton("聊天背景", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User newUser = new User();
                        newUser.setChat_background(adapter.getItem(position).getPicture());
                        newUser.update(context, user.getObjectId(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                new SnackBar(activity, "已将此图设为聊天背景", "", null).show();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                log(s);
                            }
                        });
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
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
     * 查询壁纸
     */
    public void query() {
        BmobQuery<WallPapers> query = new BmobQuery<WallPapers>();
        //返回100条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(this, new FindListener<WallPapers>() {
            @Override
            public void onSuccess(List<WallPapers> list) {
                adapter.bindDatas(list);
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
