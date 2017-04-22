package com.indoor.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.adapter.base.BaseListAdapter;
import com.indoor.im.adapter.base.ViewHolder;
import com.indoor.im.bean.Blog;
import com.indoor.im.bean.User;
import com.indoor.im.util.ImageLoadOptions;
import com.indoor.im.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 好友们的动态
 *
 * @author left
 * @ClassName: FriendsStatusAdapter
 * @Description: TODO
 * @date 2015-6-1 晚上22:36:17
 */
public class FriendsStatusAdapter extends BaseListAdapter<Blog> {

    private String headuri;
    private int likesnum = 0;

    public FriendsStatusAdapter(Context context, List<Blog> list) {
        super(context, list);
    }

    @Override
    public View bindView(int arg0, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_friendsstatus, null);
        }

        final String[] islike = new String[1];//用来记录是否赞过这条微博
        islike[0] = "false";
        final Blog blog = getList().get(arg0);
        final Context context = convertView.getContext();
        TextView brief = ViewHolder.get(convertView, R.id.edittext);
        TextView time = ViewHolder.get(convertView, R.id.time);
        final TextView name = ViewHolder.get(convertView, R.id.name);
        final TextView num = ViewHolder.get(convertView, R.id.num);
        final RoundImageView head = ViewHolder.get(convertView, R.id.head);
        ImageView photo = ViewHolder.get(convertView, R.id.photo);
        final ImageView like = ViewHolder.get(convertView, R.id.like);
        final User user = BmobUser.getCurrentUser(context, User.class);
        like.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (islike[0].equals("false")) {
                    //将当前用户添加到Blog表中的likes字段值中，表明当前用户喜欢该帖子
                    BmobRelation relation = new BmobRelation();
                    //将当前用户添加到多对多关联中
                    relation.add(user);
                    //多对多关联指向`blog`的`likes`字段
                    blog.setLikes(relation);
                    blog.update(context, new UpdateListener() {

                        @Override
                        public void onSuccess() {
                            likesnum++;
                            islike[0] = "true";
                            num.setText(likesnum + "");
                        }

                        @Override
                        public void onFailure(int arg0, String arg1) {

                        }
                    });
                } else {
                    showTag("你已赞过这条动态！", Effects.standard, R.id.friends_status);
                }
            }
        });

        String photouri = blog.getPhoto().getFileUrl(convertView.getContext());
        if (photouri != null && !photouri.equals("")) {
            ImageLoader.getInstance().displayImage(photouri, photo, ImageLoadOptions.getOptions());
        } else {
            photo.setImageResource(R.drawable.defalutimage);
        }

        //因为无法直接get到user的头像，所以需要到网络去拿
        String uID = blog.getAuthor().getObjectId();

        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(convertView.getContext(), uID, new GetListener<User>() {

            @Override
            public void onFailure(int arg0, String arg1) {

            }

            @Override
            public void onSuccess(User arg0) {
                headuri = arg0.getAvatar();
                if (headuri != null && !headuri.equals("")) {
                    ImageLoader.getInstance().displayImage(headuri, head, ImageLoadOptions.getOptions());
                } else {
                    head.setImageResource(R.drawable.default_head);
                }
                name.setText(arg0.getUsername());
            }
        });
        // 查询喜欢这个帖子的所有用户，因此查询的是用户表
        BmobQuery<User> query2 = new BmobQuery<User>();
        //likes是Blog表中的字段，用来存储所有喜欢该帖子的用户
        query2.addWhereRelatedTo("likes", new BmobPointer(blog));
        query2.findObjects(convertView.getContext(), new FindListener<User>() {

            @Override
            public void onSuccess(List<User> object) {
                likesnum = object.size();
                num.setText(likesnum + "");
                for (int i = 0; i < object.size(); i++) {
                    if (object.get(i).getObjectId().equals(user.getObjectId())) {
                        islike[0] = "true";
                        like.setImageResource(R.drawable.youlike);
                    }
                }
            }

            @Override
            public void onError(int code, String msg) {

            }
        });
        brief.setText(blog.getBrief());
        time.setText(blog.getCreatedAt());
        return convertView;
    }

}
