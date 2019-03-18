package com.wooricard.pshgw.batch.repository;

import java.util.Date;

public class DateData {
	private Date current;
	private Date next;
	
	public Date getCurrent() {
		return current;
	}
	
	public void setCurrent(Date current) {
		this.current = current;
	}
	
	public Date getNext() {
		return next;
	}
	
	public void setNext(Date next) {
		this.next = next;
	}
}