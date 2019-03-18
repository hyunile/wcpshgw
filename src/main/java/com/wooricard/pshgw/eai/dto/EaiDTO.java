package com.wooricard.pshgw.eai.dto;

import java.util.HashMap;

public class EaiDTO {
	private long seqNo;
	private String msg;
	
	private String transCode;
	private String msgCode;
	private String transUnique;
	private String processDate;
	
	private String cardNo;
	private String apvNo;
	private String nameNo;
	private String telNo;
	private String name;
	private String transType;
	private String apvMonth;
	private String check;
	private String apvAmount;
	private String chkBl;
	private String mchName;
	private String transDate;
	private String appInstYn;
	private String rejectReason;
	private String appRgYn;
	private String cuid;
	
	private String title;
	private String pageId;
	private String image;
	private String pushType;
	private String senderId;
	private String serviceCode;
	private String appId;
	
	private String popup;
	private String image2;
	private String image3;
	private String linkUrl1;
	private String linkUrl2;
	private String linkUrl3;
	private String type1;
	private String type2;
	private String type3;
	
	public EaiDTO(HashMap<String, String> header, HashMap<String, String> body) {
		transCode = header.get("transCode");
		msgCode = header.get("msgCode");
		transUnique = header.get("transUnique");
		processDate = header.get("processDate");
		
		cardNo = body.get("cardNo");
		apvNo = body.get("apvNo");
		nameNo = body.get("nameNo");
		telNo = body.get("telNo");
		name = body.get("name");
		transType = body.get("transType");
		apvMonth = body.get("apvMonth");
		check = body.get("check");
		apvAmount = body.get("apvAmount");
		chkBl = body.get("chkBl");
		mchName = body.get("mchName");
		transDate = body.get("transDate");
		appInstYn = body.get("appInstYn");
		rejectReason = body.get("rejectReason");
		appRgYn = body.get("appRgYn");
		cuid = body.get("cuid");
	}
	
	public long getSeqNo() {
		return seqNo;
	}
	
	public void setSeqNo(long seqNo) {
		this.seqNo = seqNo;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTransCode() {
		return transCode;
	}
	
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	
	public String getMsgCode() {
		return msgCode;
	}
	
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
	
	public String getTransUnique() {
		return transUnique;
	}
	
	public void setTransUnique(String transUnique) {
		this.transUnique = transUnique;
	}
	
	public String getProcessDate() {
		return processDate;
	}
	
	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}
	
	public String getCardNo() {
		return cardNo;
	}
	
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	public String getApvNo() {
		return apvNo;
	}
	
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}
	
	public String getNameNo() {
		return nameNo;
	}
	
	public void setNameNo(String nameNo) {
		this.nameNo = nameNo;
	}
	
	public String getTelNo() {
		return telNo;
	}
	
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTransType() {
		return transType;
	}
	
	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	public String getApvMonth() {
		return apvMonth;
	}
	
	public void setApvMonth(String apvMonth) {
		this.apvMonth = apvMonth;
	}
	
	public String getCheck() {
		return check;
	}
	
	public void setCheck(String check) {
		this.check = check;
	}
	
	public String getApvAmount() {
		return apvAmount;
	}
	
	public void setApvAmount(String apvAmount) {
		this.apvAmount = apvAmount;
	}
	
	public String getChkBl() {
		return chkBl;
	}
	
	public void setChkBl(String chkBl) {
		this.chkBl = chkBl;
	}
	
	public String getMchName() {
		return mchName;
	}
	
	public void setMchName(String mchName) {
		this.mchName = mchName;
	}
	
	public String getTransDate() {
		return transDate;
	}
	
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	
	public String getAppInstYn() {
		return appInstYn;
	}
	
	public void setAppInstYn(String appInstYn) {
		this.appInstYn = appInstYn;
	}
	
	public String getRejectReason() {
		return rejectReason;
	}
	
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	
	public String getAppRgYn() {
		return appRgYn;
	}
	
	public void setAppRgYn(String appRgYn) {
		this.appRgYn = appRgYn;
	}
	
	public String getCuid() {
		return cuid;
	}
	
	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPushType() {
		return pushType;
	}

	public void setPushType(String pushType) {
		this.pushType = pushType;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPopup() {
		return popup;
	}

	public void setPopup(String popup) {
		this.popup = popup;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getLinkUrl1() {
		return linkUrl1;
	}

	public void setLinkUrl1(String linkUrl1) {
		this.linkUrl1 = linkUrl1;
	}

	public String getLinkUrl2() {
		return linkUrl2;
	}

	public void setLinkUrl2(String linkUrl2) {
		this.linkUrl2 = linkUrl2;
	}

	public String getLinkUrl3() {
		return linkUrl3;
	}

	public void setLinkUrl3(String linkUrl3) {
		this.linkUrl3 = linkUrl3;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getType3() {
		return type3;
	}

	public void setType3(String type3) {
		this.type3 = type3;
	}
}