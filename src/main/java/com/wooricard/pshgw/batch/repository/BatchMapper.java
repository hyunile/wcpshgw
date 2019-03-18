package com.wooricard.pshgw.batch.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatchMapper {
	int deleteHeader(String batchId);
	int deleteItems(String batchId);
	
	int insertHeader(Map<String, String> header);
	int insertD2030Items(List<HashMap<String, Object>> items);
	int insertD40Items(List<HashMap<String, Object>> items);
	int insertD50Items(List<HashMap<String, Object>> items);
	int insertB20Items(List<HashMap<String, Object>> items);
	int insertCmsItems(List<HashMap<String, Object>> items);
	
	long selectMaxSeqNo(HashMap<String, Long> params);
	int selectBatchFileCount(String fileName);
	ResData selectResInfo(String transCode);
	List<ResData> selectResInfoList(String transCode);
	
	int deleteOldDataFromPushMaster(int dataDays);
	int deleteOldDataFromPushSuccess(int dataDays);
	int deleteOldDataFromPushFail(int dataDays);
	int deleteOldDataFromRealtimeCompleted(int dataDays);
	int deleteOldDataFromBatchInfo(int dataDays);
	int deleteOldDataFromBatchCompleted(int dataDays);
	int deleteOldDataFromBatchLog(int dataDays);
	
	List<Map<String, Object>> selectSingleCompletedMessagesByHour(DateData data);
	List<Map<String, Object>> selectBatchCompletedMessagesByHour(DateData data);
//	int selectBatchCompletedMsgCount(DateData data);
//	int selectRealTimeCompletedMsgCount(DateData data); 
	int updateSmsSent(String params);
	int updateSmsAdmin(String params);
	
	List<Map<String, String>> selectFailSmsList();
	
	int updateBatchStatus(Map<String, String> data);
	int insertBatchStatus(Map<String, String> data);
	int insertBatchLog(Map<String, Object> data);
	
	Map<String, String> selectAllowedTime();
	int updateAutoPauseToSend();
	int updateSendToAutoPause();
	
	int selectErrCnt(String timeCnt);
	Map<String, Object> selectSmsTimeErrList();
	List<Map<String, String>> selectSmsPhoneList();
	int smsDupCheck(Map<String, String> paramMap);
	
	List<Map<String, String>> selectBatchJobLogList();
	
	int batchSmsDupCheck(Map<String, String> paramMap);
	
	int dropPartition(Map<String, String> params);
	List<String> selectPartitionList(Map<String, String> params);	
	
	List<String> selectCanceledBatch(); 
	int deleteCanceledBatch(String batchId);

} 