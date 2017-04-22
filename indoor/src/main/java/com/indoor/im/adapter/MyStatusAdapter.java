package com.indoor.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.indoor.im.R;
import com.indoor.im.adapter.base.BaseListAdapter;
import com.indoor.im.adapter.base.ViewHolder;
import com.indoor.im.bean.Blog;
import com.indoor.im.bean.User;
import com.indoor.im.util.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * 我的动态
 *
 * @author left
 * @ClassName: MyStatusAdapter
 * @Description: TODO
 * @date 2015-5-30 晚上21:02:17
 */
public class MyStatusAdapter extends BaseListAdapter<Blog> {

    public MyStatusAdapter(Context context, List<Blog> list) {
        super(context, list);
    }

    @Override
    public View bindView(int arg0, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_mystatus, null);
        }
        final Blog blog = getList().get(arg0);

        TextView brief = ViewHolder.get(convertView, R.id.brieftext);
        TextView time = ViewHolder.get(convertView, R.id.time);
        final TextView num = ViewHolder.get(convertView, R.id.num);
        ImageView photo = ViewHolder.get(convertView, R.id.photo);


        String photouri = blog.getPhoto().getFileUrl(convertView.getContext());
        if (photouri != null && !photouri.equals("")) {
            ImageLoader.getInstance().displayImage(photouri, photo, ImageLoadOptions.getOptions());
        } else {
            photo.setImageResource(R.drawable.defalutimage);
        }
        brief.setText(blog.getBrief());
        time.setText(blog.getCreatedAt());
        // 查询喜欢这个帖子的所有用户，因此查询的是用户表
        BmobQuery<User> query = new BmobQuery<User>();
        //likes是Blog表中的字段，用来存储所有喜欢该帖子的用户
        query.addWhereRelatedTo("likes", new BmobPointer(blog));
        query.findObjects(convertView.getContext(), new FindListener<User>() {

            @Override
            public void onSuccess(List<User> object) {
                num.setText(object.size() + "");
            }

            @Override
            public void onError(int code, String msg) {

            }
        });
        return convertView;
    }
}
