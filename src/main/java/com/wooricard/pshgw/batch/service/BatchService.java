package com.wooricard.pshgw.batch.service;

public interface BatchService {
	int processD20();
	int processD30();
	int processD40();
	int processB20();
	int processD50();
	int processCMS();
	int deleteOldData();
	int processDW(String date);
	int replaceSMS();
	
	void saveBatchStatus();
	int applySendAllowedTime();
	
	int dropPartition();
	
}
