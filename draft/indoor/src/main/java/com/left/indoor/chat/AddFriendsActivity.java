package com.left.indoor.chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.left.indoor.bean.Friend;
import com.left.indoor.bean.IndoorUser;
import com.left.indoor.bean.MyBmobInstallation;
import com.left.indoor.map.R;
import com.left.indoor.myview.Personal_details_page;
import com.left.indoor.myview.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import me.drakeet.materialdialog.MaterialDialog;

public class AddFriendsActivity extends Activity {

	IndoorUser cuser;
	Friend friend;
	ImageView back;
	ProgressDialog mProgressDialog;
	BmobPushManager<MyBmobInstallation> bmobPushManager;
    private ClearEditText mClearEditText;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friends);
		cuser = BmobUser.getCurrentUser(this,IndoorUser.class);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		back=(ImageView) findViewById(R.id.back);
		bmobPushManager = new BmobPushManager<MyBmobInstallation>(this);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(getCurrentFocus()!=null)  
	            {  
	                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))  
	                .hideSoftInputFromWindow(getCurrentFocus()  
	                        .getWindowToken(),  
	                        InputMethodManager.HIDE_NOT_ALWAYS);   
	            }  
				finish();
			}
		});
		mClearEditText.setOnEditorActionListener(new OnEditorActionListener() {
	
			@SuppressWarnings("deprecation")
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
            if (arg1 == EditorInfo.IME_ACTION_DONE) {
            	mProgressDialog = new ProgressDialog(AddFriendsActivity.this);
            	// 设置mProgressDialog风格
            	mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);//圆形
            	// 设置mProgressDialog提示
            	mProgressDialog.setMessage("正在查找好友...");
            	// 是否可以按回退键取消
            	mProgressDialog.setCancelable(false);
            	// 设置mProgressDialog的一个Button
            	mProgressDialog.setButton("取消查找", new DialogInterface.OnClickListener()
            	{ 
            	  @Override 
            	  public void onClick(DialogInterface dialog, int which) 
            	  { 
            	    dialog.cancel(); 
            	   } 
            	});
            	//显示mProgressDialog
            	mProgressDialog.show();
            	SearchFriend(mClearEditText.getEditableText().toString());
				}
				return true;
			}
		});
	}
	
	// 创建一条好友信息到Friend表中,并关联到用户的friends信息中
	private void saveFriendInfo(String username, String email,int age,
			String gender,String profession,String position){
		friend = new Friend();
		friend.setUsername(username);
		friend.setEmail(email);
		friend.setAge(age);
		friend.setGender(gender);
		friend.setProfession(profession);
		friend.setPosition(position);
		friend.setUser(cuser);				//与当前用户相关联
		friend.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				addFriendToUser();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
			}
		});
	}
	
	// 添加好友到用户的friends信息中
	private void addFriendToUser(){
		BmobRelation friends = new BmobRelation();
		friends.add(friend);
		cuser.setFriends(friends);
		cuser.update(this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
			}
		});
	}
	
	//查找好友
	private void SearchFriend(String str) {
		BmobQuery<IndoorUser> eq1 = new BmobQuery<IndoorUser>();
		eq1.addWhereEqualTo("username", str);
		BmobQuery<IndoorUser> eq2 = new BmobQuery<IndoorUser>();
		eq2.addWhereEqualTo("email", str);
		List<BmobQuery<IndoorUser>> queries = new ArrayList<BmobQuery<IndoorUser>>();
		queries.add(eq1);
		queries.add(eq2);
		BmobQuery<IndoorUser> mainQuery = new BmobQuery<IndoorUser>();
		mainQuery.or(queries);
		mainQuery.setLimit(1000);
		mainQuery.findObjects(this, new FindListener<IndoorUser>() {
		    @Override
		    public void onSuccess(List<IndoorUser> object) {
		    	mProgressDialog.dismiss();
		    	if(object.size()==0)
		    		Toast.makeText(AddFriendsActivity.this, "没有此用户，请确认填写的好友昵称或邮箱是否正确", Toast.LENGTH_SHORT).show();
		    	else
		    		ShowPersonalDetail(object.get(0));
		    }
		    @Override
		    public void onError(int code, String msg) {
		    	 Toast.makeText(AddFriendsActivity.this, msg, Toast.LENGTH_SHORT).show();
		    }
		});
	}
	
	//展示搜索到的好友信息
	public void ShowPersonalDetail(IndoorUser user){
        final MaterialDialog mMaterialDialog = new MaterialDialog(this);
        // 自定义布局
		final Personal_details_page details_page=new 
				Personal_details_page(this);
		details_page.setNickname(user.getUsername());
		details_page.setRelation("aa");
		details_page.setAge(user.getAge()+"岁");
		details_page.setProfession(user.getProfession());
		details_page.setLocation(user.getPosition());
		details_page.setMail(user.getEmail());
		if(user.getGender().equals("男"))
			details_page.setLayoutcolor(true);
		else
			details_page.setLayoutcolor(false);
		RoundImageView imageView=(RoundImageView) details_page.findViewById(R.id.userphoto);
		user.getSculpture().loadImage(this, imageView);
        mMaterialDialog.setView(details_page)
                .setPositiveButton("发送添加请求", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mMaterialDialog.dismiss();
					String userId =cuser.getObjectId() ;
					BmobQuery<MyBmobInstallation> query = MyBmobInstallation.getQuery();
					query.addWhereEqualTo("userId", userId);
					bmobPushManager.setQuery(query);
					bmobPushManager.pushMessage(cuser.getUsername()+"请求添加你为好友");
				}
			}).setNegativeButton("取消", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mMaterialDialog.dismiss();
				}
			}).setCanceledOnTouchOutside(false).show();	
	}
}
