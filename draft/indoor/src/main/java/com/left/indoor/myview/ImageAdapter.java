package com.left.indoor.myview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@SuppressWarnings("deprecation")
//用于壁纸设置
public class ImageAdapter extends BaseAdapter{

	private int[]res;
	private Context context;
	
	public ImageAdapter(int[]res,Context context){
		this.res=res;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int arg0) {
		return res[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ImageView image=new ImageView(context);
		image.setBackgroundResource(res[arg0%res.length]);
		image.setLayoutParams(new Gallery.LayoutParams(200, 300));
		image.setScaleType(ScaleType.FIT_XY);
		return image;
	}

}
