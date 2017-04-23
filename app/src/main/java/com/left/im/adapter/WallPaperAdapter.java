package com.left.im.adapter;

import android.content.Context;

import com.left.im.R;
import com.left.im.adapter.base.BaseRecyclerAdapter;
import com.left.im.adapter.base.BaseRecyclerHolder;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.bean.WallPapers;

import java.util.Collection;

/**
 * 壁纸
 *
 * @author :left
 * @project:WallPaperAdapter
 * @date :2017-04-23-14:18
 */
public class WallPaperAdapter extends BaseRecyclerAdapter<WallPapers> {

    public WallPaperAdapter(Context context, IMutlipleItem<WallPapers> items, Collection<WallPapers> datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, WallPapers wallPapers, int position) {
        holder.setImageView(wallPapers == null ? null : wallPapers.getPicture(), R.mipmap.wallpaper, R.id.iv_wall_paper);
    }

}
