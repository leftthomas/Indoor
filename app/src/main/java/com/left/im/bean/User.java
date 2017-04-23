package com.left.im.bean;

import com.left.im.db.NewFriend;

import cn.bmob.v3.BmobUser;

/**
 * @author :smile
 * @project:User
 * @date :2016-01-22-18:11
 */
public class User extends BmobUser {

    private String avatar;
    private String chat_background;
    private String space_background;

    public User() {
    }

    public User(NewFriend friend) {
        setObjectId(friend.getUid());
        setUsername(friend.getName());
        setAvatar(friend.getAvatar());
    }

    public String getChat_background() {
        return chat_background;
    }

    public void setChat_background(String chat_background) {
        this.chat_background = chat_background;
    }

    public String getSpace_background() {
        return space_background;
    }

    public void setSpace_background(String space_background) {
        this.space_background = space_background;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}