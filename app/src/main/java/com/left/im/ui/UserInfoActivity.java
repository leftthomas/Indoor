package com.left.im.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.left.im.R;
import com.left.im.base.ImageLoaderFactory;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.bean.AddFriendMessage;
import com.left.im.bean.User;
import com.left.im.util.Util;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 用户资料
 */
public class UserInfoActivity extends ParentWithNaviActivity {

    @Bind(R.id.layout_all)
    LinearLayout layout_all;
    @Bind(R.id.iv_avator)
    ImageView iv_avator;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.tv_sex)
    TextView tv_sex;

    @Bind(R.id.btn_add_friend)
    Button btn_add_friend;
    @Bind(R.id.btn_chat)
    Button btn_chat;

    User user;
    BmobIMUserInfo info;
    Context context;
    //头像Bitmap
    Bitmap head;

    @Override
    protected String title() {
        return "个人资料";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initNaviView();
        context = this;
        user = (User) getBundle().getSerializable("u");
        if (user.getObjectId().equals(getCurrentUid())) {
            btn_add_friend.setVisibility(View.GONE);
            btn_chat.setVisibility(View.GONE);
        } else {
            btn_add_friend.setVisibility(View.VISIBLE);
            btn_chat.setVisibility(View.VISIBLE);
        }
        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
        info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
        ImageLoaderFactory.getLoader().loadAvator(iv_avator, user.getAvatar(), R.mipmap.head);
        tv_name.setText(user.getUsername());
        tv_sex.setText(user.getSex());
    }


    @OnClick(R.id.layout_head)
    public void onHeadClick(View view) {
        if (user.getObjectId().equals(getCurrentUid())) {
            //从相册里面取照片
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 1);
        }
    }

    @OnClick(R.id.layout_sex)
    public void onSexClick(View view) {
        if (user.getObjectId().equals(getCurrentUid())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择您的性别");
            builder.setPositiveButton("女", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    User newUser = new User();
                    newUser.setSex("女");
                    newUser.update(context, user.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Snackbar.make(layout_all, "已将性别修改为女", Snackbar.LENGTH_SHORT).show();
                            tv_sex.setText("女");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            log(s);
                        }
                    });
                }
            });
            builder.setNegativeButton("男", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    User newUser = new User();
                    newUser.setSex("男");
                    newUser.update(context, user.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Snackbar.make(layout_all, "已将性别修改为男", Snackbar.LENGTH_SHORT).show();
                            tv_sex.setText("男");
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
    }

    @OnClick(R.id.btn_add_friend)
    public void onAddClick(View view) {
        sendAddFriendMessage();
    }

    /**
     * 发送添加好友的请求
     */
    private void sendAddFriendMessage() {
        //启动一个会话，如果isTransient设置为true,则不会创建在本地会话表中创建记录，
        //设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        AddFriendMessage msg = new AddFriendMessage();
        User currentUser = BmobUser.getCurrentUser(this, User.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String, Object> map = new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
        map.put("avatar", currentUser.getAvatar());//发送者的头像
        map.put("uid", currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    toast("好友请求发送成功，等待验证");
                } else {//发送失败
                    toast("发送失败:" + e.getMessage());
                }
            }
        });
    }

    @OnClick(R.id.btn_chat)
    public void onChatClick(View view) {
        //启动一个会话，设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, false, null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("c", c);
        startActivity(ChatActivity.class, bundle, false);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    //裁剪图片
                    Util.cropPhoto(data.getData(), this);
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        final BmobFile bmobFile = new BmobFile(Util.saveBitmap2file(head, user));
                        bmobFile.uploadblock(this, new UploadFileListener() {
                            @Override
                            public void onSuccess() {
                                //记得更新对应user的头像
                                User newUser = new User();
                                newUser.setAvatar(bmobFile.getFileUrl(context));
                                newUser.update(context, user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        //用ImageView显示出来
                                        ImageLoaderFactory.getLoader().loadAvator(iv_avator, bmobFile.getFileUrl(context), R.mipmap.head);
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        log(s);
                                    }
                                });
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
