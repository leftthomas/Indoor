package com.indoor.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.indoor.im.R;
import com.indoor.im.adapter.base.BaseListAdapter;
import com.indoor.im.adapter.base.ViewHolder;
import com.indoor.im.bean.WallPapers;
import com.indoor.im.util.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 壁纸设置
 *
 * @author left
 * @ClassName: WallPaperAdapter
 * @Description: TODO
 * @date 2014-6-24 下午5:27:14
 */
public class WallPaperAdapter extends BaseListAdapter<WallPapers> {

    public WallPaperAdapter(Context context, List<WallPapers> list) {
        super(context, list);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View bindView(int arg0, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_wallpaper, null);
        }
        final WallPapers wallpaper = getList().get(arg0);
        ImageView piture = ViewHolder.get(convertView, R.id.piture);
        if (wallpaper.getPicture() != null) {
            ImageLoader.getInstance().displayImage("http://file.bmob.cn/"
                            + wallpaper.getPicture().getUrl(), piture,
                    ImageLoadOptions.getOptions());
        } else {
            piture.setImageResource(R.drawable.default_head);
        }
        return convertView;
    }

}
