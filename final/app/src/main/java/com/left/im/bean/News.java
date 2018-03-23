package com.left.im.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class News extends BmobObject implements Serializable {

    private String title; // 新闻标题
    private String describe; // 新闻描述
    private String photo; // 新闻图片
    private String details; // 新闻的具体内容
    private String[] category;//新闻所属类目

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
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

