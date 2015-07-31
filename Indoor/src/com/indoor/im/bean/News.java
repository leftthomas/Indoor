package com.indoor.im.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

@SuppressWarnings("serial")
public class News extends BmobObject {

	private String title; // 新闻标题
	private String describe; // 新闻描述
	private BmobFile photo = null; // 新闻图片
	private String details; // 新闻的具体内容
	private String[] category;//新闻所属类目

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public BmobFile getPhoto() {
		return photo;
	}

	public void setPhoto(BmobFile photo) {
		this.photo = photo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String[] getCategory() {
		return category;
	}

	public void setCategory(String[] category) {
		this.category = category;
	}
}

