package com.left.im.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.left.im.R;
import com.left.im.adapter.NearbyAdapter;
import com.left.im.adapter.OnRecyclerViewListener;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.bean.Friend;
import com.left.im.bean.User;
import com.left.im.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 附近的人
 *
 * @author :left
 * @project:NearbyPeopleActivity
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
    User user;
    Context context;
    String condition = "全部";
    private CharSequence[] items = {"全部", "只看男", "只看女", "全部（不包括好友）",
            "只看男（不包括好友）", "只看女（不包括好友）"};

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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("设置筛选条件");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        condition = items[item].toString();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_people);
        user = (User) getBundle().getSerializable("u");
        context = this;
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
        List<BmobQuery<User>> queries = new ArrayList<BmobQuery<User>>();
        queries.clear();
        if (condition.equals("只看男")) {
            BmobQuery<User> eq1 = new BmobQuery<User>();
            eq1.addWhereEqualTo("sex", "男");
            queries.add(eq1);
        } else if (condition.equals("只看女")) {
            BmobQuery<User> eq2 = new BmobQuery<User>();
            eq2.addWhereEqualTo("sex", "女");
            queries.add(eq2);
        } else if (condition.equals("全部（不包括好友）")) {
            BmobQuery<User> eq1 = new BmobQuery<User>();
            final ArrayList objectid_list = new ArrayList<String>();
            UserModel.getInstance().queryFriends(new FindListener<Friend>() {
                @Override
                public void onSuccess(List<Friend> list) {
                    for (Friend u : list) {
                        objectid_list.add(u.getFriendUser().getObjectId());
                    }
                }

                @Override
                public void onError(int i, String s) {
                    log(s);
                }
            });
            eq1.addWhereNotContainedIn("objectId", objectid_list);
            queries.add(eq1);
        } else if (condition.equals("只看男（不包括好友）")) {
            BmobQuery<User> eq1 = new BmobQuery<User>();
            final ArrayList objectid_list = new ArrayList<String>();
            UserModel.getInstance().queryFriends(new FindListener<Friend>() {
                @Override
                public void onSuccess(List<Friend> list) {
                    for (Friend u : list) {
                        objectid_list.add(u.getFriendUser().getObjectId());
                    }
                }

                @Override
                public void onError(int i, String s) {
                    log(s);
                }
            });
            eq1.addWhereNotContainedIn("objectId", objectid_list);
            queries.add(eq1);
            BmobQuery<User> eq2 = new BmobQuery<User>();
            eq2.addWhereEqualTo("sex", "男");
            queries.add(eq2);
        } else if (condition.equals("只看女（不包括好友）")) {
            BmobQuery<User> eq1 = new BmobQuery<User>();
            final ArrayList objectid_list = new ArrayList<String>();
            UserModel.getInstance().queryFriends(new FindListener<Friend>() {
                @Override
                public void onSuccess(List<Friend> list) {
                    for (Friend u : list) {
                        objectid_list.add(u.getFriendUser().getObjectId());
                    }
                }

                @Override
                public void onError(int i, String s) {
                    log(s);
                }
            });
            eq1.addWhereNotContainedIn("objectId", objectid_list);
            queries.add(eq1);
            BmobQuery<User> eq2 = new BmobQuery<User>();
            eq2.addWhereEqualTo("sex", "女");
            queries.add(eq2);
        }
        //所有查询记得加上这一条，要把当前用户自己给排除掉
        BmobQuery<User> eq3 = new BmobQuery<User>();
        eq3.addWhereNotEqualTo("objectId", user.getObjectId());
        queries.add(eq3);
        //查询符合整个and条件的人
        BmobQuery<User> query = new BmobQuery<User>();
        query.and(queries);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
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
