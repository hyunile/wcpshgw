package com.wooricard.pshgw.sender.service;

import com.wooricard.pshgw.sender.dto.SenderDTO;

public interface SenderService {
	int sendWpayPush(SenderDTO dto);
	int sendSmartPush(SenderDTO dto);
}