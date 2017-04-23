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
import com.left.im.adapter.ContactAdapter;
import com.left.im.adapter.OnRecyclerViewListener;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.base.ParentWithNaviFragment;
import com.left.im.bean.Friend;
import com.left.im.bean.User;
import com.left.im.event.RefreshEvent;
import com.left.im.model.UserModel;
import com.left.im.ui.ChatActivity;
import com.left.im.ui.DiscoveryActivity;
import com.left.im.ui.FriendCircleActivity;
import com.left.im.ui.FriendDistributionActivity;
import com.left.im.ui.LivingSurroundingsActivity;
import com.left.im.ui.NearbyPeopleActivity;
import com.left.im.ui.NewFriendActivity;
import com.left.im.ui.SearchUserActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * 联系人界面
 *
 * @author :smile
 * @project:ContactFragment
 * @date :2016-04-27-14:23
 */
public class ContactFragment extends ParentWithNaviFragment {

    @Bind(R.id.rc_view)
    RecyclerView rc_view;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    ContactAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected String title() {
        return "联系人";
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_add_bg_selector;
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {
                startActivity(SearchUserActivity.class, null);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        IMutlipleItem<Friend> mutlipleItem = new IMutlipleItem<Friend>() {

            @Override
            public int getItemViewType(int position, Friend friend) {
                if (position == 0) {
                    return ContactAdapter.TYPE_NEW_FRIEND;
                } else if (position == 1) {
                    return ContactAdapter.TYPE_NEARBY_PEOPLE;
                } else if (position == 2) {
                    return ContactAdapter.TYPE_FRIEND_DISTRIBUTION;
                } else if (position == 3) {
                    return ContactAdapter.TYPE_FRIEND_CIRCLE;
                } else if (position == 4) {
                    return ContactAdapter.TYPE_LIVING_SURROUNDINGS;
                } else if (position == 5) {
                    return ContactAdapter.TYPE_DISCOVERY;
                } else {
                    return ContactAdapter.TYPE_ITEM;
                }
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                if (viewtype == ContactAdapter.TYPE_NEW_FRIEND) {
                    return R.layout.header_new_friend;
                } else if (viewtype == ContactAdapter.TYPE_NEARBY_PEOPLE) {
                    return R.layout.header_nearby_people;
                } else if (viewtype == ContactAdapter.TYPE_FRIEND_DISTRIBUTION) {
                    return R.layout.header_friend_distribution;
                } else if (viewtype == ContactAdapter.TYPE_FRIEND_CIRCLE) {
                    return R.layout.header_friend_circle;
                } else if (viewtype == ContactAdapter.TYPE_LIVING_SURROUNDINGS) {
                    return R.layout.header_living_surroundings;
                } else if (viewtype == ContactAdapter.TYPE_DISCOVERY) {
                    return R.layout.header_discovery;
                } else {
                    return R.layout.item_contact;
                }
            }

            @Override
            public int getItemCount(List<Friend> list) {
                return list.size() + 6;
            }
        };
        adapter = new ContactAdapter(getActivity(), mutlipleItem, null);
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
                if (position == 0) {
                    //跳转到新朋友页面
                    startActivity(NewFriendActivity.class, null);
                } else if (position == 1) {
                    //跳转到附近的人页面
                    startActivity(NearbyPeopleActivity.class, null);
                } else if (position == 2) {
                    //跳转到朋友分布页面
                    startActivity(FriendDistributionActivity.class, null);
                } else if (position == 3) {
                    //跳转到朋友圈页面
                    startActivity(FriendCircleActivity.class, null);
                } else if (position == 4) {
                    //跳转到生活周边页面
                    startActivity(LivingSurroundingsActivity.class, null);
                } else if (position == 5) {
                    //跳转到发现页面
                    startActivity(DiscoveryActivity.class, null);
                } else {
                    Friend friend = adapter.getItem(position);
                    User user = friend.getFriendUser();
                    BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                    //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    startActivity(ChatActivity.class, bundle);
                }
            }

            @Override
            public boolean onItemLongClick(final int position) {
                log("长按" + position);
                if (position == 0 || position == 1 || position == 2 || position == 3 || position == 4 || position == 5) {
                    return true;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("删除该联系人");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserModel.getInstance().deleteFriend(adapter.getItem(position), new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                adapter.remove(position);
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        //重新刷新列表
        log("---联系人界面接收到自定义消息---");
        adapter.notifyDataSetChanged();
    }

    /**
     * 查询本地会话
     */
    public void query() {
        UserModel.getInstance().queryFriends(new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                adapter.bindDatas(list);
                adapter.notifyDataSetChanged();
                sw_refresh.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
                adapter.bindDatas(null);
                adapter.notifyDataSetChanged();
                sw_refresh.setRefreshing(false);
            }
        });
    }

}
