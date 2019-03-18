package com.wooricard.pshgw.mobile.dto;

import java.util.Date;

public class MsgDTO {
	private long msgId;
	private String title;
	private String text;
	private String payload;
	private Date date;
	private String regDate;
	
	private int TOTAL;
	private int RNUM;
	
	public MsgDTO() {

	}
	
/*	
	public MsgDTO(long msgId, String title, String text, String payload, Date date) {
		this.msgId = msgId;
		this.title = title;
		this.text = text;
		this.payload = payload;
		this.date = date;
	} 
	*/
	public int getTOTAL() {
		return TOTAL;
	}



	public void setTOTAL(int tOTAL) {
		TOTAL = tOTAL;
	}



	public int getRNUM() {
		return RNUM;
	}



	public void setRNUM(int rNUM) {
		RNUM = rNUM;
	}



	public long getMsgId() {
		return msgId;
	}
	
	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getPayload() {
		return payload;
	}
	
	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	
}
