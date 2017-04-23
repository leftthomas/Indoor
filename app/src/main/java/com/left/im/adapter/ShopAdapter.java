package com.left.im.adapter;

import android.content.Context;

import com.left.im.R;
import com.left.im.adapter.base.BaseRecyclerAdapter;
import com.left.im.adapter.base.BaseRecyclerHolder;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.bean.Goods;

import java.util.Collection;

/**
 * 购物
 *
 * @author :left
 * @project:ContactNewAdapter
 * @date :2017-04-23-14:18
 */
public class ShopAdapter extends BaseRecyclerAdapter<Goods> {

    public ShopAdapter(Context context, IMutlipleItem<Goods> items, Collection<Goods> datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, Goods good, int position) {

        holder.setImageView(good == null ? null : good.getPhoto(), R.mipmap.good, R.id.iv_good);
        holder.setText(R.id.tv_name, good == null ? "未知" : good.getName());
        holder.setText(R.id.tv_price, good == null ? "未知" : good.getPrice());
    }

}
