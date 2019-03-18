package com.wooricard.pshgw.mobile.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wooricard.pshgw.common.Result;
import com.wooricard.pshgw.mobile.dto.MobileDTO;
import com.wooricard.pshgw.mobile.dto.MsgDTO;
import com.wooricard.pshgw.mobile.repository.MobileMapper;
import com.wooricard.pshgw.mobile.service.MobileService;

@Service
public class MobileServiceImpl implements MobileService {
	 private final Logger logger = LoggerFactory.getLogger(MobileServiceImpl.class);
	@Autowired
	MobileMapper mapper;
	
	public int getMsgList(MobileDTO dto) {
		if(dto == null) {
			return Result.ERR_PARAM_MISSING;
		}
		
		int result = Result.SUCCESS;
		List<MsgDTO> selectList = null;
		try {
			selectList = mapper.selectMsgList(dto);
			int cnt = selectList.size();
			if(cnt > 0) {
				if(selectList != null && !selectList.isEmpty()) {
					List<MsgDTO> msgList = new ArrayList<MsgDTO>();
					for (MsgDTO msgDTO : selectList) {
						msgList.add(msgDTO);
					}
					dto.setMsgList(msgList);
					mapper.initBadgeNo(dto);  // 읽음처리
				} else {
					result = Result.ERR_DATA_NOT_FOUND;
				}
			} else {
				result = Result.ERR_DATA_NOT_FOUND;
			}
			
		} catch (Exception e) {
			logger.info("Exception e : {}", e.getMessage());
			result = Result.ERR_DATABASE;
		}
		
		return result;
	}
	
	@Override
	public int initBadgeNo(MobileDTO dto) {
		int result = mapper.initBadgeNo(dto);
		
		if(result < 1) {
			result = Result.ERR_DATABASE;
		}else {
			result = Result.SUCCESS;
			
		}
		return result;
	}
}