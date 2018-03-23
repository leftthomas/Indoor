package com.left.indoor.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.left.indoor.bean.Friend;
import com.left.indoor.bean.IndoorUser;
import com.left.indoor.chat.SideBar.OnTouchingLetterChangedListener;
import com.left.indoor.map.R;
import com.left.indoor.utils.BaseAdapterHelper;
import com.left.indoor.utils.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import static com.left.indoor.map.R.id.friendsname;
import static com.left.indoor.map.R.id.friendsphoto;
import static com.left.indoor.map.R.id.friendsposition;

@SuppressLint("DefaultLocale") public class ContactActivity extends Activity {
	
	IndoorUser cuser;
	private ImageView addfriends;
	private ClearEditText mClearEditText;
	private SideBar sideBar;
	// 汉字转换成拼音的类
	private CharacterParser characterParser;
	//根据拼音来排列ListView里面的数据类
	private PinyinComparator pinyinComparator;
	private TextView dialog;
	ListView listview;
	public QuickAdapter<Friend> FriendsAdapter;
	RelativeLayout progress;
	LinearLayout layout_no;
	TextView tv_no;
	List<Friend> FriendsList = new ArrayList<Friend>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		cuser = BmobUser.getCurrentUser(this,IndoorUser.class);
		initViews();
		initData();
	}
	
	private void initViews() {
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		progress = (RelativeLayout) findViewById(R.id.progress);
		layout_no = (LinearLayout) findViewById(R.id.layout_no);
		tv_no = (TextView) findViewById(R.id.tv_no);
		addfriends =(ImageView) findViewById(R.id.Add);
		listview = (ListView) findViewById(R.id.country_lvcountry);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		//进入添加好友界面
		addfriends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent AddIntent;
				AddIntent=new Intent(ContactActivity.this,AddFriendsActivity.class);
				startActivity(AddIntent);
			}
		});
		
		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				/*int position = getPositionForSection(s.charAt(0));
				if(position != -1){
					listview.setSelection(position);
				}	*/
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
				//Toast.makeText(getApplication(), ((Friend)FriendsAdapter.
						//getItem(position)).getUsername(), Toast.LENGTH_SHORT).show();
			}
		});
		
		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				//filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	// 初始化数据
	public void initData() { 
		if (FriendsAdapter == null) {
			FriendsAdapter = new QuickAdapter<Friend>(this,
					R.layout.item) {	
				@Override
				protected void convert(BaseAdapterHelper helper, Friend friend) {
					helper.setText(friendsname, friend.getUsername()).setText(
							friendsposition, friend.getPosition());
					if (friend.getSculpture() != null) { 
						// 如果图片文件非空
						helper.setImageUrl(friendsphoto, "http://file.bmob.cn/"
								+ friend.getSculpture().getUrl());
					}
				}
			};
		}
		listview.setAdapter(FriendsAdapter);
		queryFriends();
	}
	
	private void showView() {
		listview.setVisibility(View.VISIBLE);
		layout_no.setVisibility(View.GONE);
	}

	//查询好友表
	private void queryFriends() {
		showView(); 
		BmobQuery<Friend> friends = new BmobQuery<Friend>();
	    /**
	     * 注意这里的查询条件
	     * 第一个参数：是User表中的friends字段名
	     * 第二个参数：是指向User表中的某个用户的BmobPointer对象
	     */
	    friends.addWhereRelatedTo("friends", new BmobPointer(cuser));
	    friends.setLimit(1000);
	    friends.findObjects(this, new FindListener<Friend>() {

	        @Override
	        public void onSuccess(List<Friend> friends) {
	        	FriendsAdapter.clear();
				if (friends == null || friends.size() == 0) {
					showErrorView(0);
					FriendsAdapter.notifyDataSetChanged();
					return;
				}
				progress.setVisibility(View.GONE);
				FriendsAdapter.addAll(friends);
				FriendsList.clear();
				FriendsList.addAll(friends);
	        }
	        @Override
	        public void onError(int arg0, String arg1) {
	        }
	    });
	}
	
	// 无内容的时候
	private void showErrorView(int tag) {
		progress.setVisibility(View.GONE);
		listview.setVisibility(View.GONE);
		layout_no.setVisibility(View.VISIBLE);
	    tv_no.setText(getResources().getText(R.string.list_no_data_lost));
	}	
}
