package com.indoor.im.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.UpdateListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.CustomApplcation;
import com.indoor.im.adapter.base.BaseListAdapter;
import com.indoor.im.adapter.base.ViewHolder;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.util.ImageLoadOptions;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/** 新的好友请求
  * @ClassName: NewFriendAdapter
  * @Description: TODO
  * @author smile
  * @date 2014-6-9 下午1:26:12
  */
public class NewFriendAdapter extends BaseListAdapter<BmobInvitation> {
	CircleProgressBar progress;
	public NewFriendAdapter(Context context, List<BmobInvitation> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("InflateParams") @SuppressWarnings("deprecation")
	@Override
	public View bindView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_add_friend, null);
		}
		final BmobInvitation msg = getList().get(arg0);
		TextView name = ViewHolder.get(convertView, R.id.name);
		ImageView iv_avatar = ViewHolder.get(convertView, R.id.avatar);
		
		final Button btn_add = ViewHolder.get(convertView, R.id.btn_add);
		progress=ViewHolder.get(convertView, R.id.progress);
		progress.setVisibility(View.GONE);
		String avatar = msg.getAvatar();

		if (avatar != null && !avatar.equals("")) {
			ImageLoader.getInstance().displayImage(avatar, iv_avatar, ImageLoadOptions.getOptions());
		} else {
			iv_avatar.setImageResource(R.drawable.default_head);
		}

		int status = msg.getStatus();
		if(status==BmobConfig.INVITE_ADD_NO_VALIDATION||status==BmobConfig.INVITE_ADD_NO_VALI_RECEIVED){
//			btn_add.setText("同意");
//			btn_add.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_login_selector));
//			btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_white));
			btn_add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					BmobLog.i("点击同意按钮:"+msg.getFromid());
					agressAdd(btn_add, msg);
				}
			});
		}else if(status==BmobConfig.INVITE_ADD_AGREE){
			btn_add.setText("已同意");
			btn_add.setBackgroundDrawable(null);
			btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_black));
			btn_add.setEnabled(false);
		}
		name.setText(msg.getFromname());
		
		return convertView;
	}

	
	/**添加好友
	  * agressAdd
	  * @Title: agressAdd
	  * @Description: TODO
	  * @param @param btn_add
	  * @param @param msg 
	  * @return void
	  * @throws
	  */
	private void agressAdd(final Button btn_add,final BmobInvitation msg){
		progress.setVisibility(View.VISIBLE);
		try {
			//同意添加好友
			BmobUserManager.getInstance(mContext).agreeAddContact(msg, new UpdateListener() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void onSuccess() {
					progress.setVisibility(View.GONE);
					btn_add.setText("已同意");
					btn_add.setBackgroundDrawable(null);
					btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_black));
					btn_add.setEnabled(false);
					//保存到application中方便比较
					CustomApplcation.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(mContext).getContactList()));	
				}
				
				@Override
				public void onFailure(int arg0, final String arg1) {
					progress.setVisibility(View.GONE);
					showTag("添加失败: " +arg1,Effects.jelly,R.id.add_friend);
				}
			});
		} catch (final Exception e) {
			progress.setVisibility(View.GONE);
			showTag("添加失败: " +e.getMessage(),Effects.jelly,R.id.add_friend);
		}
	}
	
}
