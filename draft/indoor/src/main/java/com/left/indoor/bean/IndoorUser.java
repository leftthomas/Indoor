package com.left.indoor.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class IndoorUser extends BmobUser {

	private static final long serialVersionUID = 1L;
	//objectId、username、password、email、emailVerified为Bmob后端系统保留字段，可不重写，直接获取
	//基本个人资料
	private BmobFile sculpture;    //头像
	private int age;               //年龄
	private String gender;         //性别
	private String profession;      //职业
	private String position;        //位置
    private BmobRelation friends;   //朋友
    private BmobRelation configures;    //配置信息
    
	public BmobFile getSculpture() {
		return sculpture;
	}
	public void setSculpture(BmobFile sculpture) {
		this.sculpture = sculpture;
	}
	public int getAge(){
		return age;
	}
	public void setAge(int age){
		this.age=age;
	}
	public String getGender(){
		return gender;
	}
	public void setGender(String gender){
		this.gender=gender;
	}
	public String getProfession(){
		return profession;
	}
	public void setProfession(String profession){
		this.profession=profession;
	}
	public String getPosition(){
		return position;
	}
	public void setPosition(String position){
		this.position=position;
	}
	public BmobRelation getFriends() {
		return friends;
	}
	public void setFriends(BmobRelation friends) {
		this.friends = friends;
	}
	public BmobRelation getConfigures() {
		return configures;
	}
	public void setConfigures(BmobRelation configures) {
		this.configures = configures;
	}	
}
