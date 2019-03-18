package com.wooricard.pshgw.eai.service;

public interface EaiServerService {
	int saveData(byte[] data, StringBuilder code);
	byte[] makeAckData(byte[] msg, int result);
}