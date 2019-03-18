package com.wooricard.pshgw.mobile.vo;

public class MobileParams {
	private String appid; //appId -> appid
	private String cuid;
	//private String max; 
	private String asc;
	private String device;
	
	public String getAppid() {
		return appid;
	}
	
	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	public String getCuid() {
		return cuid;
	}
	
	public void setCuid(String cuid) {
		this.cuid = cuid;
	}
	
	public String getAsc() {
		return asc;
	}
	
	public void setAsc(String asc) {
		this.asc = asc;
	}	
	
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "MobileParams [appid=" + appid + ", cuid=" + cuid + ", asc="+ asc + "]";
	}
}
