package com.wooricard.pshgw.mobile.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wooricard.pshgw.mobile.dto.MobileDTO;
import com.wooricard.pshgw.mobile.dto.MsgDTO;

@Repository
public class MobileRepository {

	@Autowired
	MobileMapper mapper;
	
	public int initBadgeNo(MobileDTO dto) {
		return mapper.initBadgeNo(dto);
	}
	
	public int totalCount(MobileDTO dto) {
		return mapper.totalCount(dto);
	}
	
	List<MsgDTO> selectMsgList(MobileDTO dto){
		return mapper.selectMsgList(dto);
	}
}