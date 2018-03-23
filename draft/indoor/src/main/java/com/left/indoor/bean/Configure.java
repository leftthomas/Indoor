package com.left.indoor.bean;

import cn.bmob.v3.BmobObject;

public class Configure extends BmobObject{

	private static final long serialVersionUID = 1L;
	private Boolean satellitemapstatus; 
	private Boolean trafficmapstatus; 
	private Boolean heatmapstatus; 
	private Boolean voice_navigationstatus; 
	private Boolean specialreminderstatus; 
	private Boolean myplan_reminderstatus; 
	private Boolean distancelessstatus; 
	private int sexstatus; 
	private int agestatus; 
	private int positionstatus; 
	private int professionstatus; 
	private int distancelessdistance; 
	private int whichwallpaper; 
    private IndoorUser user;

    public IndoorUser getUser() {
        return user;
    }
    public void setUser(IndoorUser user) {
        this.user = user;
    }
	public Boolean getSatellitemapstatus() {
		return satellitemapstatus;
	}
	public void setSatellitemapstatus(Boolean satellitemapstatus) {
		this.satellitemapstatus = satellitemapstatus;
	}
	public Boolean getTrafficmapstatus() {
		return trafficmapstatus;
	}
	public void setTrafficmapstatus(Boolean trafficmapstatus) {
		this.trafficmapstatus = trafficmapstatus;
	}
	public Boolean getHeatmapstatus() {
		return heatmapstatus;
	}
	public void setHeatmapstatus(Boolean heatmapstatus) {
		this.heatmapstatus = heatmapstatus;
	}
	public Boolean getVoice_navigationstatus() {
		return voice_navigationstatus;
	}
	public void setVoice_navigationstatus(Boolean voice_navigationstatus) {
		this.voice_navigationstatus = voice_navigationstatus;
	}
	public Boolean getSpecialreminderstatus() {
		return specialreminderstatus;
	}
	public void setSpecialreminderstatus(Boolean specialreminderstatus) {
		this.specialreminderstatus = specialreminderstatus;
	}
	public Boolean getMyplan_reminderstatus() {
		return myplan_reminderstatus;
	}
	public void setMyplan_reminderstatus(Boolean myplan_reminderstatus) {
		this.myplan_reminderstatus = myplan_reminderstatus;
	}
	public Boolean getDistancelessstatus() {
		return distancelessstatus;
	}
	public void setDistancelessstatus(Boolean distancelessstatus) {
		this.distancelessstatus = distancelessstatus;
	}
	public int getSexstatus() {
		return sexstatus;
	}
	public void setSexstatus(int sexstatus) {
		this.sexstatus = sexstatus;
	}
	public int getAgestatus() {
		return agestatus;
	}
	public void setAgestatus(int agestatus) {
		this.agestatus = agestatus;
	}
	public int getPositionstatus() {
		return positionstatus;
	}
	public void setPositionstatus(int positionstatus) {
		this.positionstatus = positionstatus;
	}
	public int getProfessionstatus() {
		return professionstatus;
	}
	public void setProfessionstatus(int professionstatus) {
		this.professionstatus = professionstatus;
	}
	public int getDistancelessdistance() {
		return distancelessdistance;
	}
	public void setDistancelessdistance(int distancelessdistance) {
		this.distancelessdistance = distancelessdistance;
	}
	public int getWhichwallpaper() {
		return whichwallpaper;
	}
	public void setWhichwallpaper(int whichwallpaper) {
		this.whichwallpaper = whichwallpaper;
	}
}


