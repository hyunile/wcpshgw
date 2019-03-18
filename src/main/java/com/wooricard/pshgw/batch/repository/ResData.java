package com.wooricard.pshgw.batch.repository;

import com.wooricard.pshgw.utils.Utils;

public class ResData {
	private String transCode;
	private String pushType;
	private String title;
	private String pageId;
	private String image;
	private String image2;
	private String image3;
	private String linkUrl1;
	private String linkUrl2;
	private String linkUrl3;
	private String type1;
	private String type2;
	private String type3;
	private String popup;
	private String description;
	private String reserveTime;
	private String sendLimit;
	
	public String getTransCode() {
		return Utils.safeStr(transCode);
	}
	
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	
	public String getPushType() {
		return Utils.safeStr(pushType);
	}
	
	public void setPushType(String pushType) {
		this.pushType = pushType;
	}
	
	public String getTitle() {
		return Utils.safeStr(title);
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPageId() {
		return Utils.safeStr(pageId);
	}
	
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	public String getImage() {
		return Utils.safeStr(image);
	}
	
	public void setImage(String image) {
		this.image = image;
	}

	public String getImage2() {
		return Utils.safeStr(image2);
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getPopup() {
		return Utils.safeStr(popup);
	}

	public void setPopup(String popup) {
		this.popup = popup;
	}

	public String getDescription() {
		return Utils.safeStr(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage3() {
		return Utils.safeStr(image3);
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getLinkUrl1() {
		return Utils.safeStr(linkUrl1);
	}

	public void setLinkUrl1(String linkUrl1) {
		this.linkUrl1 = linkUrl1;
	}

	public String getLinkUrl2() {
		return Utils.safeStr(linkUrl2);
	}

	public void setLinkUrl2(String linkUrl2) {
		this.linkUrl2 = linkUrl2;
	}

	public String getLinkUrl3() {
		return Utils.safeStr(linkUrl3);
	}

	public void setLinkUrl3(String linkUrl3) {
		this.linkUrl3 = linkUrl3;
	}

	public String getType1() {
		return Utils.safeStr(type1);
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return Utils.safeStr(type2);
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getType3() {
		return Utils.safeStr(type3);
	}

	public void setType3(String type3) {
		this.type3 = type3;
	}

	public String getReserveTime() {
		return Utils.safeStr(reserveTime);
	}

	public void setReserveTime(String reserveTime) {
		this.reserveTime = reserveTime;
	}

	public String getSendLimit() {
		return Utils.safeStr(sendLimit);
	}

	public void setSendLimit(String sendLimit) {
		this.sendLimit = sendLimit;
	}
}