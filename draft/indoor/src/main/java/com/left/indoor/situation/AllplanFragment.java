package com.left.indoor.situation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.left.indoor.bean.IndoorUser;
import com.left.indoor.map.R;

import java.lang.ref.WeakReference;

import cn.bmob.v3.BmobUser;

@SuppressLint("InflateParams") public class AllplanFragment extends Fragment{

	//News card_view;
	TextView textView;
	Layout layout;
	SharedPreferences s;
	//private MaterialDialog mMaterialDialog;
	private IndoorUser cuser;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
		ViewGroup container, Bundle savedInstanceState) {
		//rootView为allplanfragment中所有控件集合
		View rootView = inflater.inflate(R.layout.allplanfragment, container,false);  
		cuser = BmobUser.getCurrentUser(getActivity().getApplicationContext(),IndoorUser.class);
		//获取SharedPreferences中的数据
		s = getActivity().getSharedPreferences(cuser.getUsername(), Context.MODE_PRIVATE);
		//获取TextView控件和News控件
		/*card_view=(News) rootView.findViewById(R.id.news);
		textView= (TextView) rootView.findViewById(R.id.qq);
		final LinearLayout ll = (LinearLayout)rootView.findViewById(R.id.allplan_layout);
		final int a=card_view.getChildCount();
		View no1=card_view.findViewById(R.id.newsphoto);
		no1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				textView.setText("hello+"+a); 
				
				News t = new News(getActivity().getApplicationContext());
				t.setTitle("haha");
				t.setDescribe("yes hello");
				ll.addView(t);
				mMaterialDialog = new MaterialDialog(getActivity());
				// 自定义布局	
				final Personal_details_page details_page=new 
						Personal_details_page(getActivity().getApplicationContext());
				details_page.setNickname(s.getString("username", "indoor"));
				details_page.setRelation("aa");
				details_page.setAge(s.getInt("age", 21)+"岁");
				details_page.setProfession(s.getString("profession", "学生"));
				details_page.setLocation(s.getString("position", "浙江杭州"));
				details_page.setMail(s.getString("email", "indoor@left.com"));
				if(s.getString("gender", "男").equals("男"))
					details_page.setLayoutcolor(true);
				else
					details_page.setLayoutcolor(false);
				RoundImageView imageView=(RoundImageView) details_page.findViewById(R.id.userphoto);
				Bitmap bm=convertToBitmap(s.getString("sculpture", ""), 800, 750);
				imageView.setImageBitmap(bm);
				mMaterialDialog
				.setView(details_page)
				.setPositiveButton("聊天", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mMaterialDialog.dismiss();
						}
					}).setNegativeButton("取消", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mMaterialDialog.dismiss();
						}
					}).setCanceledOnTouchOutside(false).show();	
				//ll.removeViewsInLayout(2, 2);//删除控件
			}
		});*/
		return rootView;
	}

	//将指定路径下的图片转换为Bitmap
			public Bitmap convertToBitmap(String path, int w, int h) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				// 设置为ture只获取图片大小
				opts.inJustDecodeBounds = true;
				opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
				// 返回为空
				BitmapFactory.decodeFile(path, opts);
				int width = opts.outWidth;
				int height = opts.outHeight;
				float scaleWidth = 0.f, scaleHeight = 0.f;
				if (width > w || height > h) {
					// 缩放
				scaleWidth = ((float) width) / w;
				scaleHeight = ((float) height) / h;
				}
				opts.inJustDecodeBounds = false;
				float scale = Math.max(scaleWidth, scaleHeight);
				opts.inSampleSize = (int)scale;
				WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
				return Bitmap.createScaledBitmap(weak.get(), w, h, true);
				}
}