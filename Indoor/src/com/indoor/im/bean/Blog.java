package com.indoor.im.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Blog extends BmobObject {

	private static final long serialVersionUID = 1L;

	private String brief;
	
	private User author;//帖子的发布者，这里体现的是一对一的关系，一个用户发表一个帖子

    private BmobRelation likes;//多对多关系：用于存储喜欢该帖子的所有用户
	
	private BmobFile photo; //帖子图片

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public BmobFile getPhoto() {
		return photo;
	}

	public void setPhoto(BmobFile photo) {
		this.photo = photo;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public BmobRelation getLikes() {
		return likes;
	}

	public void setLikes(BmobRelation likes) {
		this.likes = likes;
	}

}
