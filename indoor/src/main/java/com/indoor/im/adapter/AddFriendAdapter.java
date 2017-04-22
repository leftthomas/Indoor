package com.indoor.im.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.adapter.base.BaseListAdapter;
import com.indoor.im.adapter.base.ViewHolder;
import com.indoor.im.util.ImageLoadOptions;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.PushListener;

/**
 * 查找好友
 *
 * @author smile
 * @ClassName: AddFriendAdapter
 * @Description: TODO
 * @date 2014-6-25 上午10:56:33
 */
@SuppressLint("InflateParams")
public class AddFriendAdapter extends BaseListAdapter<BmobChatUser> {

    public AddFriendAdapter(Context context, List<BmobChatUser> list) {
        super(context, list);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View bindView(int arg0, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_add_friend, null);
        }
        final BmobChatUser contract = getList().get(arg0);
        TextView name = ViewHolder.get(convertView, R.id.name);
        ImageView iv_avatar = ViewHolder.get(convertView, R.id.avatar);

        Button btn_add = ViewHolder.get(convertView, R.id.btn_add);
        final CircleProgressBar progress = ViewHolder.get(convertView, R.id.progress);
        progress.setVisibility(View.GONE);
        String avatar = contract.getAvatar();

        if (avatar != null && !avatar.equals("")) {
            ImageLoader.getInstance().displayImage(avatar, iv_avatar, ImageLoadOptions.getOptions());
        } else {
            iv_avatar.setImageResource(R.drawable.default_head);
        }

        name.setText(contract.getUsername());
        btn_add.setText("添加");
        btn_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                progress.setVisibility(View.VISIBLE);
                //发送tag请求
                BmobChatManager.getInstance(mContext).sendTagMessage(BmobConfig.TAG_ADD_CONTACT, contract.getObjectId(), new PushListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        progress.setVisibility(View.GONE);
                        showTag("发送请求成功，等待对方验证!", Effects.jelly, R.id.add_friend);
                    }

                    @Override
                    public void onFailure(int arg0, final String arg1) {
                        // TODO Auto-generated method stub
                        progress.setVisibility(View.GONE);
                        showTag("发送请求失败，请重新添加!", Effects.jelly, R.id.add_friend);
                        ShowLog("发送请求失败:" + arg1);
                    }
                });
            }
        });
        return convertView;
    }

}