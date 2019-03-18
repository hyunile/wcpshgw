package com.wooricard.pshgw.mobile.service;

import com.wooricard.pshgw.mobile.dto.MobileDTO;

public interface MobileService {
	int getMsgList(MobileDTO dto);
	int initBadgeNo(MobileDTO dto);
	
	//List<MobileDTO> selectMsgList(MobileDTO dto); 
}
