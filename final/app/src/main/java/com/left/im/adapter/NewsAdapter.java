package com.left.im.adapter;

import android.content.Context;

import com.left.im.R;
import com.left.im.adapter.base.BaseRecyclerAdapter;
import com.left.im.adapter.base.BaseRecyclerHolder;
import com.left.im.adapter.base.IMutlipleItem;
import com.left.im.bean.News;

import java.util.Collection;

/**
 * 新闻
 *
 * @author :left
 * @project:NewsAdapter
 * @date :2017-04-23-14:18
 */
public class NewsAdapter extends BaseRecyclerAdapter<News> {

    public NewsAdapter(Context context, IMutlipleItem<News> items, Collection<News> datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, News news, int position) {
        holder.setImageView(news.getPhoto(), R.mipmap.news, R.id.iv_news);
        holder.setText(R.id.tv_title, news.getTitle());
        holder.setText(R.id.tv_describe, news.getDescribe());
    }

}
