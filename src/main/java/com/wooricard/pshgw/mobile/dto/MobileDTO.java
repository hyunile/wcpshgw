package com.wooricard.pshgw.mobile.dto;

import java.util.List;

import com.wooricard.pshgw.mobile.vo.MobileParams;

public class MobileDTO {
	private String appid;
	private String cuid;
	
	private String device;
	private String auth;
	
	//private int max;
	private boolean asc;
	private int total;
	private List<MsgDTO> msgList;

	public MobileDTO(MobileParams params) {
		this.appid = params.getAppid();
		this.cuid = params.getCuid();
		this.device = params.getDevice();
		//this.max = Integer.valueOf(params.getMax()); 
	}

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

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<MsgDTO> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<MsgDTO> msgList) {
		this.msgList = msgList;
	}
	
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	@Override
	public String toString() {
		return "MobileDTO [appid=" + appid + ", cuid=" + cuid + ", asc="+ asc + "]";
	}
}
