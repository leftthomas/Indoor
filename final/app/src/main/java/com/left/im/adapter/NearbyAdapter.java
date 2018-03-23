package com.left.im.adapter;

import android.content.Context;

import com.left.im.BmobIMApplication;
import com.left.im.R;
import com.left.im.adapter.base.BaseRecyclerAdapter;
import com.left.im.adapter.base.BaseRecyclerHolder;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.bean.User;

import java.util.Collection;

import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * 附近的人
 *
 * @author :left
 * @project:NearbyAdapter
 * @date :2017-04-23-14:18
 */
public class NearbyAdapter extends BaseRecyclerAdapter<User> {

    public NearbyAdapter(Context context, IMutlipleItem<User> items, Collection<User> datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, User user, int position) {
        holder.setImageView(user.getAvatar(), R.mipmap.head, R.id.iv_avatar);
        holder.setText(R.id.tv_name, user.getUsername());
        holder.setText(R.id.tv_time, user.getUpdatedAt());
        holder.setText(R.id.tv_distance, (user.getLocation() == null || BmobIMApplication.getCurrent_user_location()
                == null) ? "未知" : (int) (user.getLocation().distanceInMilesTo(new BmobGeoPoint(
                BmobIMApplication.getCurrent_user_location().getLongitude(),
                BmobIMApplication.getCurrent_user_location().getLatitude()))) + "米");
    }

}
