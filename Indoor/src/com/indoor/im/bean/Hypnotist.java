package com.indoor.im.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
//¥ﬂ√ﬂ¥Û ¶µƒ“Ù¿÷Bean
@SuppressWarnings("serial")
public class Hypnotist extends BmobObject{
	
	private String musicName;
	private BmobFile musicPhoto;
	private BmobFile music;
	private int musicNum;
	
	public String getMusicName() {
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public BmobFile getMusicPhoto() {
		return musicPhoto;
	}
	public void setMusicPhoto(BmobFile musicPhoto) {
		this.musicPhoto = musicPhoto;
	}
	public BmobFile getMusic() {
		return music;
	}
	public void setMusic(BmobFile music) {
		this.music = music;
	}
	public int getMusicNum() {
		return musicNum;
	}
	public void setMusicNum(int musicNum) {
		this.musicNum = musicNum;
	}

}
