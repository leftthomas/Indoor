package com.indoor.im.adapter.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.indoor.im.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.bmob.im.util.BmobLog;

/**
 * 基础的适配器
 * 
 * @ClassName: BaseListAdapter
 * @Description: TODO
 * @author smile
 * @date 2014-6-19 上午11:04:01
 * @param <E>
 */
@SuppressLint("UseSparseArrays")
public abstract class BaseListAdapter<E> extends BaseAdapter {

	public List<E> list;

	public Context mContext;

	public LayoutInflater mInflater;

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void add(E e) {
		this.list.add(e);
		notifyDataSetChanged();
	}

	public void addAll(List<E> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		this.list.remove(position);
		notifyDataSetChanged();
	}

	public BaseListAdapter(Context context, List<E> list) {
		super();
		this.mContext = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = bindView(position, convertView, parent);
		// 绑定内部点击监听
		addInternalClickListener(convertView, position, list.get(position));
		return convertView;
	}

	public abstract View bindView(int position, View convertView,
			ViewGroup parent);

	// adapter中的内部点击事件
	public Map<Integer, onInternalClickListener> canClickItem;

	private void addInternalClickListener(final View itemV, final Integer position,final Object valuesMap) {
		if (canClickItem != null) {
			for (Integer key : canClickItem.keySet()) {
				View inView = itemV.findViewById(key);
				final onInternalClickListener inviewListener = canClickItem.get(key);
				if (inView != null && inviewListener != null) {
					inView.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							inviewListener.OnClickListener(itemV, v, position,
									valuesMap);
						}
					});
				}
			}
		}
	}

	public void setOnInViewClickListener(Integer key,
			onInternalClickListener onClickListener) {
		if (canClickItem == null)
			canClickItem = new HashMap<Integer, onInternalClickListener>();
		canClickItem.put(key, onClickListener);
	}

	public interface onInternalClickListener {
		public void OnClickListener(View parentV, View v, Integer position,
				Object values);
	}

	//显示提示消息
	public void showTag(String msg,Effects effect,int layoutid){
		Configuration cfg=new Configuration.Builder()
	      .setAnimDuration(700)
	      .setDispalyDuration(1500)
	      .setBackgroundColor("#FFFFFFFF")
	      .setTextColor("#FF444444")
	      .setIconBackgroundColor("#FFFFFFFF")
	      .setTextPadding(5)                      //dp
	      .setViewHeight(48)                      //dp
	      .setTextLines(2)                        //You had better use setViewHeight and setTextLines together
	      .setTextGravity(Gravity.CENTER)         //only text def  Gravity.CENTER,contain icon Gravity.CENTER_VERTICAL
	      .build();
		NiftyNotificationView.build((Activity) mContext,msg, effect,layoutid,cfg)
	      .setIcon(R.drawable.ic_launcher)               //remove this line ,only text
	      .show();
	}

	/**
	 * 打Log ShowLog
	 * @return void
	 * @throws
	 */
	public void ShowLog(String msg) {
		BmobLog.i(msg);
	}

}
