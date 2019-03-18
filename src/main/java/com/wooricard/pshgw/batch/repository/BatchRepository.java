package com.wooricard.pshgw.batch.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wooricard.pshgw.batch.base.BatchLayoutBase;
import com.wooricard.pshgw.common.Constants;
import com.wooricard.pshgw.utils.Utils;

@Repository
public class BatchRepository {
	private static final Logger logger = LoggerFactory.getLogger(BatchRepository.class);

	@Autowired
	private BatchMapper mapper;
	
	public int rollback(String batchId) {
		int result = mapper.deleteItems(batchId);
		result += mapper.deleteHeader(batchId);
		return result;
	}

	public int insertHeader(Map<String, String> header) {
		return mapper.insertHeader(header);
	}

	public int insertD2030Items(List<HashMap<String, Object>> items) {
		return mapper.insertD2030Items(items);
	}

	public int insertD40Items(List<HashMap<String, Object>> items) {
		return mapper.insertD40Items(items);
	}	
	
	public int insertD50Items(List<HashMap<String, Object>> items) {
		return mapper.insertD50Items(items);
	}
	

	public int insertB20Items(List<HashMap<String, Object>> items) {
		return mapper.insertB20Items(items);
	}
	
	public int insertCmsItems(List<HashMap<String, Object>> items) {
		return mapper.insertCmsItems(items);
	}
	
	public long getNextSeqNo(long max, long min) {
		HashMap<String, Long> params = new HashMap<String, Long>();
		params.put("min", min);
		params.put("max", max);
		
		long seqNo = mapper.selectMaxSeqNo(params);
		return seqNo + 1;
	}

	public ResData getResInfo(String transCode) {
		return mapper.selectResInfo(transCode);
	}

	public int selectBatchFileCount(String name) {
		return mapper.selectBatchFileCount(name);
	}

	public int deleteOldData(int dataDays) {
		logger.info(String.format("deleteOldData : delete data before %d days.", dataDays));
		
		int total = 0;
		int result = mapper.deleteOldDataFromPushMaster(dataDays);
		logger.info("deleteOldData : deleteOldDataFromPushMaster : result = " + result);
		total += result;
		
		result = mapper.deleteOldDataFromPushSuccess(dataDays);
		logger.info("deleteOldData : deleteOldDataFromPushSuccess : result = " + result);
		total += result;
		
		result = mapper.deleteOldDataFromPushFail(dataDays);
		logger.info("deleteOldData : deleteOldDataFromPushFail : result =  " + result);
		total += result;
		
		result = mapper.deleteOldDataFromRealtimeCompleted(dataDays);
		logger.info("deleteOldData : deleteOldDataFromRealtimeCompleted : result = " + result);
		total += result;
		
		result = mapper.deleteOldDataFromBatchInfo(dataDays);
		logger.info("deleteOldData : deleteOldDataFromBatchInfo : result = " + result);
		total += result;
		
		result = mapper.deleteOldDataFromBatchCompleted(dataDays);
		logger.info("deleteOldData : deleteOldDataFromBatchCompleted : result = " + result);
		total += result;
				
		result = mapper.deleteOldDataFromBatchLog(dataDays);
		logger.info("deleteOldData : deleteOldDataFromBatchLog : result = " + result);
		total += result;
		
		return total;
	}

	public void saveFailedFileInfo(BatchLayoutBase layout) {
		Map<String, String> header = new HashMap<String, String>();
		header.put("batchId", UUID.randomUUID().toString().replace("-", ""));
		header.put("transCode", layout.getTransCode());
		header.put("fileName", layout.getFile().getName());
		header.put("appId", layout.getAppId());
		header.put("processDate", Utils.dateToStr(new Date(), Utils.FRMT_DATE));
		header.put("useTemplete", "N");
		header.put("templete", "-");
		header.put("total", "0");
		header.put("loaded", "0");
		
		mapper.insertHeader(header);
		logger.info("saveFailedFileInfo : file = " + layout.getFile().getName());
	}

	public List<Map<String, Object>> getSingleCompletedMessagesByHour(String current, String next) {
		DateData data = new DateData();		
		data.setCurrent(Utils.strToDate(current, "yyyyMMddHH"));
		data.setNext(Utils.strToDate(next, "yyyyMMddHH"));
		
		return mapper.selectSingleCompletedMessagesByHour(data);
	}

	public List<Map<String, Object>> getBatchCompletedMessagesByHour(String current, String next) {
		DateData data = new DateData();		
		data.setCurrent(Utils.strToDate(current, "yyyyMMddHH"));
		data.setNext(Utils.strToDate(next, "yyyyMMddHH"));
		
		return mapper.selectBatchCompletedMessagesByHour(data);
	}

//	public int getCompletedMessageCount(String current, String next) {
//		DateData data = new DateData();
//		data.setCurrent(current);
//		data.setNext(next);
//		
//		int result = mapper.selectRealTimeCompletedMsgCount(data);
//		result += mapper.selectBatchCompletedMsgCount(data);
//		return result;
//	}
	
	public List<Map<String, String>> selectFailSmsList(){
		return mapper.selectFailSmsList();
	}
	
	public int updateSmsSent(String param) {
		return mapper.updateSmsSent(param);
	}

	public void updateBatchStatus(Map<String, String> map) {
		try {
			if(mapper.updateBatchStatus(map) < 1) {
				mapper.insertBatchStatus(map);
			}
		}
		catch(Exception e) {
			logger.error(String.format("updateBatchStatus : %s : %s : %s",
				map.get("code"), e.getClass().getName(), e.getMessage()));
		}
	}

	public void saveBatchProcessLog(BatchLayoutBase layout, String path, String result, Date start, Date finish) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", layout.getTransCode());
		map.put("dir", path);
		map.put("file", layout.getFile().getName());
		map.put("result", result);
		map.put("start", start);
		map.put("finish", finish);
		map.put("total", layout.getTotal());
		map.put("loaded", layout.getLoaded());
		
		mapper.insertBatchLog(map);
	}

	public int applySendAllowedTime() {
		int result = 0;
		Map<String, String> map = mapper.selectAllowedTime();
		String allow = map.get("ALLOW");
		
		if(! "Y".equalsIgnoreCase(allow)) {
			logger.info("applySendAllowedTime : use allow settings = N");
			return result;
		}
		
		String begin = map.get("BEGINTIME");
		String end = map.get("ENDTIME");
		String now = Utils.dateToStr(new Date(), "HHmm");

		if(now.compareTo(begin) >= 0 && now.compareTo(end) < 0) {
			result = mapper.updateAutoPauseToSend();
			if(result > 0) {
				logger.info("applySendAllowedTime : Auto Paused to Send : result = " + result);
			}
		}
		else {
			result = mapper.updateSendToAutoPause();
			if(result > 0) {
				logger.info("applySendAllowedTime : Send to Auto Pause : result = " + result);
			}
		}

		return result;
	}
	
	// 관리자에게 SMS발송을 위해 에러 체크
	public int selectErrCnt(String timeCnt) {
		return mapper.selectErrCnt(timeCnt);
	}
	
	// 관리자화면에서 세팅한 SMS발송설정을 가져온다.
	public Map<String, Object> selectSmsTimeErrList(){
		return mapper.selectSmsTimeErrList();
	}
	
	// 관리자의 전화번호를 가져온다.
	public List<Map<String, String>> selectSmsPhoneList(){
		return mapper.selectSmsPhoneList();
	}
	
	public int updateSmsAdmin(String param) {
		return mapper.updateSmsAdmin(param);
	}
	
	// 관리자 중복발송체크
	public int smsDupCheck(Map<String, String> map) {
		return mapper.smsDupCheck(map);
	}

	//T_PUSH_BATCHJOB_LOG 리스트
	public List<Map<String, String>> selectBatchJobLogList(){
		return mapper.selectBatchJobLogList();
	}
	
	// 관리자 중복발송체크
	public int batchSmsDupCheck(Map<String, String> map) {
		return mapper.batchSmsDupCheck(map);
	}
	
	public int dropPartition(int months) {
		int total = 0;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -months);
		String month = Utils.dateToStr(cal.getTime(), "yyyyMM") + "01";

		for(int i = 0; i < Constants.PARTITION_TABLES.length; i++) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("table", Constants.PARTITION_TABLES[i]);
			params.put("partition", Constants.PARTITION_PREFIX[i] + month);

			List<String> partitionList = mapper.selectPartitionList(params);
			if(partitionList != null && ! partitionList.isEmpty()) {
				for(String part : partitionList) {
					params.put("partition", part);
					mapper.dropPartition(params);
					
					logger.info(String.format("deletePartition : table = %s, partition = %s : dropped.",
						Constants.PARTITION_TABLES[i], part));

					total++;
				}
			}
		}
		
		return total;
	}

	public List<String> getCanceledBatch() {
		return mapper.selectCanceledBatch();
	}
	
	public int deleteCanceledBatchMsgs(List<String> batchIds) {
		int result = 0;
		
		for(String id : batchIds) {
			int count = mapper.deleteCanceledBatch(id);
			logger.info(String.format("deleteCanceledBatchMsgs : batchId = %s, result = %d", id, count));
			result += count;
		}
		
		return result;
	}
}
