package com.indoor.im.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.indoor.im.R;
import com.indoor.im.CustomApplcation;
import com.indoor.im.adapter.BlackListAdapter;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.view.HeaderLayout;

/**
 * 黑名单列表
 * 
 * @ClassName: BlackListActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-24 下午5:17:50
 */
public class BlackListActivity extends ActivityBase implements OnItemClickListener {

	ListView listview;
	BlackListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacklist);
		initView();
	}

	private void initView() {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		initTopBarForLeft("黑名单");
		adapter = new BlackListAdapter(this, BmobDB.create(this).getBlackList());
		listview = (ListView) findViewById(R.id.list_blacklist);
		listview.setOnItemClickListener(this);
		listview.setAdapter(adapter);
	}

	/** 显示移除黑名单对话框
	  * @Title: showRemoveBlackDialog
	  * @Description: TODO
	  * @param @param position
	  * @param @param invite 
	  * @return void
	  * @throws
	  */
	public void showRemoveBlackDialog(final int position, final BmobChatUser user) {
		final NiftyDialogBuilder dialogBuilder=new NiftyDialogBuilder(this,R.style.dialog_untran);
        dialogBuilder
                .withTitle("移出黑名单")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("你确定将"+user.getUsername()+"移出黑名单吗?")                             //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFF")                                //def
                .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(Effectstype.Flipv)                                         //def Effectstype.Slidetop
                .withButton1Text("确定")                                      //def gone
                .withButton2Text("取消")                                  //def gone
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	adapter.remove(position);
        				userManager.removeBlack(user.getUsername(),new UpdateListener() {
        					
        					@Override
        					public void onSuccess() {
        						showTag("移出黑名单成功",Effects.scale,R.id.blacklist);
        						//重新设置下内存中保存的好友列表
        						CustomApplcation.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(getApplicationContext()).getContactList()));	
        						dialogBuilder.dismiss();
        					}
        					
        					@Override
        					public void onFailure(int arg0, String arg1) {
        						showTag("移出黑名单失败:"+arg1,Effects.scale,R.id.blacklist);
        						dialogBuilder.dismiss();
        					}
        				});	
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	dialogBuilder.dismiss();
                    }
                })
                .show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		BmobChatUser invite = (BmobChatUser) adapter.getItem(arg2);
		showRemoveBlackDialog(arg2,invite);
	}


}
