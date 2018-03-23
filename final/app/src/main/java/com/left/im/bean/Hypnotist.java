package com.left.im.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

//催眠大师的音乐Bean
@SuppressWarnings("serial")
public class Hypnotist extends BmobObject {

    private String musicName;
    private String musicPhoto;
    private BmobFile music;
    private int musicNum;

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicPhoto() {
        return musicPhoto;
    }

    public void setMusicPhoto(String musicPhoto) {
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
