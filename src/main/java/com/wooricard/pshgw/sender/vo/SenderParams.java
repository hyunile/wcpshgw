package com.wooricard.pshgw.sender.vo;

public class SenderParams {
	private String type;
	private String cuid;
	private String cmpgnid;
	private String dnis;
	private String title;
	private String text;
	private String msg;
	private String url;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCuid() {
		return cuid;
	}
	
	public void setCuid(String cuid) {
		this.cuid = cuid;
	}
	
	public String getCmpgnid() {
		return cmpgnid;
	}
	
	public void setCmpgnid(String cmpgnid) {
		this.cmpgnid = cmpgnid;
	}
	
	public String getDnis() {
		return dnis;
	}
	
	public void setDnis(String dnis) {
		this.dnis = dnis;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}