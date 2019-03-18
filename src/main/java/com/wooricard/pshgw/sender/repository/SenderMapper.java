package com.wooricard.pshgw.sender.repository;

import org.apache.ibatis.annotations.Mapper;

import com.wooricard.pshgw.sender.dto.SenderDTO;

@Mapper
public interface SenderMapper {
	int insertPushData(SenderDTO dto);
}