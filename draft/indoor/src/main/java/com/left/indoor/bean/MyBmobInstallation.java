package com.left.indoor.bean;

import android.content.Context;
import cn.bmob.v3.BmobInstallation;

public class MyBmobInstallation extends BmobInstallation{
	
	private static final long serialVersionUID = 1L;
	private String userId;
	
	public MyBmobInstallation(Context arg0) {
		super(arg0);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
