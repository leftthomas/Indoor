package com.left.im.bean;

import cn.bmob.v3.BmobObject;

public class Blog extends BmobObject {


    private String brief;
    //帖子的发布者，这里体现的是一对一的关系
    private User author;
    //帖子图片
    private String photo;

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}
