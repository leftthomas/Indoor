package com.left.indoor.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Friend extends BmobObject{

	private static final long serialVersionUID = 1L;
	private String username;        //用户名
	private String email;           //邮箱
	private BmobFile sculpture;    //头像
	private int age;               //年龄
	private String gender;         //性别
	private String profession;      //职业
	private String position;        //位置
    private IndoorUser user;
    private String sortLetters;      //显示数据拼音的首字母  

    public IndoorUser getUser() {
        return user;
    }
    public void setUser(IndoorUser user) {
        this.user = user;
    }
	public BmobFile getSculpture() {
		return sculpture;
	}
	public void setSculpture(BmobFile sculpture) {
		this.sculpture = sculpture;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}

