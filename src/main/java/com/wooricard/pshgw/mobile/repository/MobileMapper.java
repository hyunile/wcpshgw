package com.wooricard.pshgw.mobile.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wooricard.pshgw.mobile.dto.MobileDTO;
import com.wooricard.pshgw.mobile.dto.MsgDTO;

@Mapper
public interface MobileMapper {
	int initBadgeNo(MobileDTO dto);
	int totalCount(MobileDTO dto);
	//int getMsgList(MobileDTO dto); 
	
	List<MsgDTO> selectMsgList(MobileDTO dto);
	
	
	
}
