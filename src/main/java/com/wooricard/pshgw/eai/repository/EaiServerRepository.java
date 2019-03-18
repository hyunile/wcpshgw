package com.wooricard.pshgw.eai.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wooricard.pshgw.eai.dto.EaiDTO;
import com.wooricard.pshgw.utils.Utils;

@Repository
public class EaiServerRepository {
	
	@Autowired
	private EaiServerMapper mapper;
	
	public int saveApvData(EaiDTO dto) {
		return mapper.insertApvData(dto);
	}
	
	public boolean dataExists(String code) {
		String str = mapper.selectEaiMsg(code);
		return ! Utils.isEmpty(str);
	}
}