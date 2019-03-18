package com.wooricard.pshgw.batch.repository;

public class SmsData {
	
	private String TEL_NO;
	private String SMS_SENT;
	private String SEQ_NO;
	private String MESSAGE;
	private String SUB_TITLE;
	
	
	public String getTEL_NO() {
		return TEL_NO;
	}
	public void setTEL_NO(String tEL_NO) {
		TEL_NO = tEL_NO;
	}
	public String getSMS_SENT() {
		return SMS_SENT;
	}
	public void setSMS_SENT(String sMS_SENT) {
		SMS_SENT = sMS_SENT;
	}
	public String getSEQ_NO() {
		return SEQ_NO;
	}
	public void setSEQ_NO(String sEQ_NO) {
		SEQ_NO = sEQ_NO;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
	public String getSUB_TITLE() {
		return SUB_TITLE;
	}
	public void setSUB_TITLE(String sUB_TITLE) {
		SUB_TITLE = sUB_TITLE;
	}
}