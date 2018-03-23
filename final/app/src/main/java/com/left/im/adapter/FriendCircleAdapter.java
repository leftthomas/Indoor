package com.left.im.adapter;

import android.content.Context;

import com.left.im.R;
import com.left.im.adapter.base.BaseRecyclerAdapter;
import com.left.im.adapter.base.BaseRecyclerHolder;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.bean.Blog;

import java.util.Collection;

/**
 * 朋友圈
 *
 * @author :left
 * @project:FriendCircleAdapter
 * @date :2017-04-23-14:18
 */
public class FriendCircleAdapter extends BaseRecyclerAdapter<Blog> {

    public FriendCircleAdapter(Context context, IMutlipleItem<Blog> items, Collection<Blog> datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, Blog blog, int position) {
        //千万注意！！！！！只要布局是RelativeLayout里面嵌套LinearLayout，这时候给ImageView设置图片，
        // 必须要有background属性，不然不会显示图片！！！
        holder.setImageView(blog.getPhoto(), R.mipmap.background, R.id.iv_photo);
        holder.setText(R.id.tv_brief, blog.getBrief());
        holder.setText(R.id.tv_friend, blog.getAuthor().getUsername());
        holder.setImageView(blog.getAuthor().getAvatar(), R.mipmap.head, R.id.iv_friend);
    }

}
