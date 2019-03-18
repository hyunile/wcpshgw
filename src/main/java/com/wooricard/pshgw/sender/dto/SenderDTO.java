package com.wooricard.pshgw.sender.dto;

import com.wooricard.pshgw.sender.vo.SenderParams;

public class SenderDTO {
	
	private long seqNo;
	private String type;	// #{transType}
	private String cuid;   //#{cuid}
	private String cmpgnid; //#{cmpgnId}
	private String dnis;	//#{dnis} 
	private String title;	//#{subTitle}
	private String msg;		//#{message}
	private String url;  //#{linkUrl}
	private String serviceCode;
	private String senderCode;

	private String transCode;
	private String appId;
	
	/*  #{seqNo}
  	, #{cmpgnId}
  	, #{dnis}
  	, #{linkUrl}
  	, #{appId} 
  	, #{message}
  	, #{ext}
  	, #{senderCode}
  	, #{serviceCode}
  	, #{subTitle}
  	, #{cuid}
  	, #{transType}
  	, SYSDATE
  	, SYSDATE*/
	
	public SenderDTO(SenderParams params) {
		// TODO Auto-generated constructor stub
		type = params.getType();
		cuid = params.getCuid();
		cmpgnid = params.getCmpgnid();
		dnis = params.getDnis();
		title = params.getTitle();
		msg = params.getMsg();
		url = params.getUrl();
	}
	
	public long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(long seqNo) {
		this.seqNo = seqNo;
	}

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

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getSenderCode() {
		return senderCode;
	}

	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}
