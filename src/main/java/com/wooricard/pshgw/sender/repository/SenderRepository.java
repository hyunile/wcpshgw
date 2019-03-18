package com.wooricard.pshgw.sender.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wooricard.pshgw.common.Constants;
import com.wooricard.pshgw.sender.dto.SenderDTO;

@Repository
public class SenderRepository {

	@Autowired
	private SenderMapper mapper;
	
	
	public int sendWpayPush(SenderDTO dto) {
		dto.setTransCode(Constants.WPAY_TRANSCODE);
		dto.setAppId(Constants.WPAY_APPID);
		
		return mapper.insertPushData(dto);
	}
	
	public int sendSmartPush(SenderDTO dto) {
		dto.setTransCode(Constants.SMART_TRANSCODE);
		dto.setAppId(Constants.SMART_APPID);
		
		return mapper.insertPushData(dto);
	}
}