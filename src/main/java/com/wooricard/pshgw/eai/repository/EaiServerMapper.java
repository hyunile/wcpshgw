package com.wooricard.pshgw.eai.repository;

import org.apache.ibatis.annotations.Mapper;

import com.wooricard.pshgw.eai.dto.EaiDTO;

@Mapper
public interface EaiServerMapper {
	int insertApvData(EaiDTO dto);
	String selectEaiMsg(String code);
}