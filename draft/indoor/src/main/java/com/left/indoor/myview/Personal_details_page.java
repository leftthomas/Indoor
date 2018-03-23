package com.left.indoor.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.left.indoor.map.R;

@SuppressLint("InflateParams") public class Personal_details_page  extends CardView{

    private View Personal_details_pagelayout;//Personal_details_page的布局
    private String nickname;//昵称
    private String relation;//关系
    private String age;//年龄
    private String profession;//职业
    private String location;//位置
    private String mail;//邮箱
    private Bitmap userphoto;//头像
    private boolean layoutcolor;//顶部布局背景色

    //#CE20AC粉红色，性别为女，则用
    //#4EE4E4淡蓝色，性别为男，则用
    public Personal_details_page(Context context) {
		super(context);
		initView(context);
	}

	public Personal_details_page(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public Personal_details_page(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

    //初始化界面，添加布局文件到News中
    private void initView(Context context){
		LayoutInflater inflater=LayoutInflater.from(context);
		Personal_details_pagelayout=inflater.inflate(R.layout.personal_details_page,null);
		this.addView(Personal_details_pagelayout);			
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
		TextView textView=(TextView) findViewById(R.id.nickname);
		textView.setText(nickname);
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
		TextView textView=(TextView) findViewById(R.id.relation);
		textView.setText(relation);
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
		TextView textView=(TextView) findViewById(R.id.age);
		textView.setText(age);
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
		TextView textView=(TextView) findViewById(R.id.profession);
		textView.setText(profession);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
		TextView textView=(TextView) findViewById(R.id.location);
		textView.setText(location);
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
		TextView textView=(TextView) findViewById(R.id.mail);
		textView.setText(mail);
	}

	public Bitmap getUserphoto() {
		return userphoto;
	}

	public void setUserphoto(Bitmap userphoto) {
		this.userphoto = userphoto;
		RoundImageView roundImageView=(RoundImageView) findViewById(R.id.userphoto);
		roundImageView.setImageBitmap(userphoto);
	}

	public boolean isLayoutcolor() {
		return layoutcolor;
	}

	public void setLayoutcolor(boolean layoutcolor) {
		this.layoutcolor = layoutcolor;
		int color;
		if(layoutcolor==false)
            color = Color.argb(255, 206, 32, 172);//女性
        else
            color = Color.argb(255, 78, 228, 228);//男性
        FrameLayout frameLayout=(FrameLayout) findViewById(R.id.personal_framelayout);
		frameLayout.setBackgroundColor(color);
	}
	  
}
