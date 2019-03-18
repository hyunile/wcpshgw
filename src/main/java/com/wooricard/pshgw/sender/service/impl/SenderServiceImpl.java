package com.wooricard.pshgw.sender.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wooricard.pshgw.common.Result;
import com.wooricard.pshgw.sender.dto.SenderDTO;
import com.wooricard.pshgw.sender.repository.SenderRepository;
import com.wooricard.pshgw.sender.service.SenderService;

@Service
public class SenderServiceImpl implements SenderService {
	private final Logger logger = LoggerFactory.getLogger(SenderServiceImpl.class);
	
	@Autowired
	private SenderRepository repository;
	
	@Override
	public int sendWpayPush(SenderDTO dto) {
		int result = repository.sendWpayPush(dto);
		if(result < 1) {
			result = Result.ERR_DATABASE;
		}
		else {
			result = Result.SUCCESS;
		}
		return result;
	}

	@Override
	public int sendSmartPush(SenderDTO dto) {
		int result = repository.sendSmartPush(dto);
		if(result < 1) {
			result = Result.ERR_DATABASE;
		}
		else {
			result = Result.SUCCESS;
		}
		return result;
	}

}