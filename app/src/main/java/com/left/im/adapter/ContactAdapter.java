package com.left.im.adapter;

import android.content.Context;
import android.view.View;

import com.left.im.R;
import com.left.im.adapter.base.BaseRecyclerAdapter;
import com.left.im.adapter.base.BaseRecyclerHolder;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.bean.Friend;
import com.left.im.bean.User;
import com.left.im.db.NewFriendManager;

import java.util.Collection;

/**
 * 联系人
 * 一种简洁的Adapter实现方式，可用于多种Item布局的recycleView实现，不用再写ViewHolder啦
 *
 * @author :smile
 * @project:ContactNewAdapter
 * @date :2016-04-27-14:18
 */
public class ContactAdapter extends BaseRecyclerAdapter<Friend> {

    public static final int TYPE_NEW_FRIEND = 0;
    public static final int TYPE_NEARBY_PEOPLE = 1;
    public static final int TYPE_FRIEND_DISTRIBUTION = 2;
    public static final int TYPE_FRIEND_CIRCLE = 3;
    public static final int TYPE_LIVING_SURROUNDINGS = 4;
    public static final int TYPE_DISCOVERY = 5;
    public static final int TYPE_ITEM = 6;

    public ContactAdapter(Context context, IMutlipleItem<Friend> items, Collection<Friend> datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, Friend friend, int position) {
        if (holder.layoutId == R.layout.item_contact) {
            User user = friend.getFriendUser();
            //好友头像
            holder.setImageView(user.getAvatar(), R.mipmap.head, R.id.iv_recent_avatar);
            //好友名称
            holder.setText(R.id.tv_recent_name, user.getUsername());
        } else if (holder.layoutId == R.layout.header_new_friend) {
            if (NewFriendManager.getInstance(context).hasNewFriendInvitation()) {
                holder.setVisible(R.id.iv_msg_tips, View.VISIBLE);
            } else {
                holder.setVisible(R.id.iv_msg_tips, View.GONE);
            }
        }
    }

}
