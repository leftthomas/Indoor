package com.left.im.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.left.im.R;
import com.left.im.adapter.FriendCircleAdapter;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.base.ImageLoaderFactory;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.bean.Blog;
import com.left.im.bean.Friend;
import com.left.im.bean.User;
import com.left.im.model.UserModel;
import com.left.im.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 朋友圈
 *
 * @author :left
 * @project:BlackListActivity
 * @date :2017-04-25-18:23
 */
public class FriendCircleActivity extends ParentWithNaviActivity {


    @Bind(R.id.ll_root)
    LinearLayout ll_root;
    @Bind(R.id.rc_view)
    RecyclerView rc_view;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @Bind(R.id.iv_avatar)
    ImageView iv_avatar;
    @Bind(R.id.tv_username)
    TextView tv_username;
    FriendCircleAdapter adapter;
    LinearLayoutManager layoutManager;
    User user;
    //图片
    Bitmap photo;
    Context context;

    @Override
    protected String title() {
        return "朋友圈";
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
                finish();
            }

            @Override
            public void clickRight() {
                //从相册里面取照片
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_circle);
        initNaviView();
        context = this;
        user = (User) getBundle().getSerializable("u");
        IMutlipleItem<Blog> mutlipleItem = new IMutlipleItem<Blog>() {

            @Override
            public int getItemViewType(int postion, Blog b) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_friend_circle;
            }

            @Override
            public int getItemCount(List<Blog> list) {
                return list.size();
            }
        };
        adapter = new FriendCircleAdapter(this, mutlipleItem, null);
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        tv_username.setText(user.getUsername());
        ImageLoaderFactory.getLoader().loadAvator(iv_avatar, user.getAvatar(), R.mipmap.head);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
    }

    /**
     * 查询动态
     */
    public void query() {
        final ArrayList<User> friends = new ArrayList<>();
        UserModel.getInstance().queryFriends(new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                for (Friend friend : list) {
                    friends.add(friend.getFriendUser());
                }
            }

            @Override
            public void onError(int i, String s) {
                log(s);
            }
        });
        BmobQuery<Blog> query1 = new BmobQuery<Blog>();
        query1.addWhereContainedIn("author", friends);
        BmobQuery<Blog> query2 = new BmobQuery<Blog>();
        // 查询当前用户的所有帖子
        query2.addWhereEqualTo("author", user);
        //最后组装完整的条件
        List<BmobQuery<Blog>> orQuerys = new ArrayList<BmobQuery<Blog>>();
        orQuerys.add(query1);
        orQuerys.add(query2);
        //查询符合整个条件的人
        BmobQuery<Blog> query = new BmobQuery<Blog>();
        query.or(orQuerys);
        query.order("-updatedAt");
        // 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.include("author");
//        query.setLimit(50);
        query.findObjects(this, new FindListener<Blog>() {
            @Override
            public void onSuccess(List<Blog> list) {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data != null) {
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    } catch (IOException e) {
                        log(e.getMessage());
                    }
                    if (photo != null) {
                        /**
                         * 上传服务器代码
                         */
                        final BmobFile bmobFile = new BmobFile(Util.saveBitmap2file(photo, user));
                        bmobFile.uploadblock(this, new UploadFileListener() {
                            @Override
                            public void onSuccess() {
                                final EditText inputServer = new EditText(context);
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("输入文字").setView(inputServer).setNegativeButton("取消", null);
                                builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        String s = inputServer.getText().toString();
                                        if (s.equals("")) {
                                            toast("请输入文字！");
                                        } else {
                                            //记得更新对应blog的图片
                                            Blog blog = new Blog();
                                            blog.setPhoto(bmobFile.getFileUrl(context));
                                            blog.setAuthor(user);
                                            blog.setBrief(s);
                                            blog.save(context, new SaveListener() {
                                                @Override
                                                public void onSuccess() {
                                                    sw_refresh.setRefreshing(true);
                                                    query();
                                                }

                                                @Override
                                                public void onFailure(int i, String s) {
                                                    log(s);
                                                }
                                            });
                                        }
                                    }
                                });
                                builder.show();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                log(s);
                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
