package com.wooricard.pshgw.auth.service;

import com.wooricard.pshgw.mobile.dto.MobileDTO;

public interface AuthService {
	int issueAuthKey(MobileDTO dto);
	int authenticate(MobileDTO dto);
}