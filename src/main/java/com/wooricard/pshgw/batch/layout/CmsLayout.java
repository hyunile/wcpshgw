package com.wooricard.pshgw.batch.layout;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wooricard.pshgw.batch.base.StringBatchLayout;
import com.wooricard.pshgw.batch.repository.BatchRepository;
import com.wooricard.pshgw.common.Constants;
import com.wooricard.pshgw.utils.Utils;

public class CmsLayout extends StringBatchLayout {
	private static final Logger logger = LoggerFactory.getLogger(CmsLayout.class);
	private static final String SEPARATOR = "\\|";

	private static String TYPE_HEADER 	= "HEAD";
	private static String TYPE_BODY 	= "BODY";
	private static String TYPE_DATA 	= "DATA";
	private static String TYPE_FOOTER	= "TAIL";
	
	private static String TEMPLETE_PREFIX = "{FIELD";
	
	private static long SEQ_NO_MAX = 7000000000000L;
	private static long SEQ_NO_MIN = 6000000000000L;
	
	private static long currentSeqNo = 0;
	
	private Date reserve;
	private Date limit;
	
	private static String[] HEADER_ITEM_NAME = { 
		"code", 
		"transCode", 
		"processDate", 
		"endDate", 
		"processTime", 
		"endTime",
		"batchTitle",
		"work",
		"login",
		"pageId",
		"key1",
		"key2",
		"imgUrl1",
		"imgUrl2",
	};
	
	private static final int TOKENS_HEADER_SIZE 	= 12;
	private static final int TOKENS_BODY_SIZE 		= 2;
	private static final int TOKENS_DATA_SIZE 		= 2;
	
	public CmsLayout(BatchRepository repository) {
		super(Constants.TRANS_CODE_CMS, repository, logger);
	}

	@Override
	protected Map<String, String> parseHeader(String str) {
		if(! str.startsWith(TYPE_HEADER)) {
			logger.error("parseHeader : ! str.startsWith(TYPE_HEADER)");
			return null;
		}
		
		String[] tokens = str.split(SEPARATOR);
		if(tokens == null || tokens.length < TOKENS_HEADER_SIZE) {
			logger.error("parseHeader : tokens == null || tokens.length != TOKENS_HEADER_SIZE");
			return null;
		}
		
		HashMap<String, String> result = new HashMap<String, String>();
				
		int size = Math.min(HEADER_ITEM_NAME.length, tokens.length);
		for(int i = 0; i < size; i++) {
			result.put(HEADER_ITEM_NAME[i], tokens[i]);
		}

		result.put("transCode", Constants.TRANS_CODE_CMS);
		result.put("batchId", batchId);
		result.put("fileName", file.getName());
		result.put("appId", appId);
		result.put("useTemplete", "N");
		
		result.put("pageId", result.get("work") + "|" + result.get("login") + "|" + result.get("pageId"));

		checkProcessDate(result);
		
		reserve = getReserveDate(result);
		limit = getLimitDate(result);
				
		return result;
	}

	@Override
	protected HashMap<String, Object> parseItem(String str) {
		HashMap<String, Object> result = null;
		
		if(str.startsWith(TYPE_BODY)) {
			String[] tokens = str.split(SEPARATOR);
			if(tokens != null && tokens.length == TOKENS_BODY_SIZE) {
				String msg = tokens[1].replace(Constants.CHARS_NEWLINE, "\n");
				header.put("templete", msg);
				header.put("useTemplete", tokens[1].contains(TEMPLETE_PREFIX) ? "Y" : "N");
			}			
		}
		else if(str.startsWith(TYPE_DATA)) {
			String[] tokens = str.split(SEPARATOR);
			if(tokens != null && tokens.length >= TOKENS_DATA_SIZE) {
				result = new HashMap<String, Object>();
				result.put("cuid", tokens[1]);
				String temp = null;
				if(tokens.length > TOKENS_DATA_SIZE) {
					temp = tokens[2];
					for(int i = 3; i < tokens.length; i++) {
						temp += "|" + tokens[i];
					}					
				}
				else {
					temp = header.get("templete");
				}
				
				if(Utils.isEmpty((String) result.get("cuid"))) {
					logger.error("parseItem : CUID missing !!!");
					result = null;
				}
				
				result.put("message", temp);
				
				result.put("seqNo", String.valueOf(currentSeqNo++));
				result.put("batchId", batchId);
				result.put("pushType", Constants.PUSH_TYPE_CMS);
				result.put("senderId", senderId);
				result.put("serviceCode", serviceCode);			
				
				result.put("reserveDate", reserve != null ? new Date(reserve.getTime()) : new Date());
				
				if(limit != null) {
					result.put("sendTimeLimit", new Date(limit.getTime()));
				}
			}			
		}
		
		return result;
	}

	@Override
	protected boolean isFooter(String str) {
		if(str.startsWith(TYPE_FOOTER)) {
			return true;
		}
		
		return false;
	}

	@Override
	protected boolean isTotalCountCorrect(String str, long count) {
		boolean result = false;
		String[] tokens = str.split(SEPARATOR);
		if(tokens != null && tokens.length == 2) {
			try {
				total = Long.valueOf(tokens[1]);
				if(total == count) {
					result = true;
				}
			}
			catch(NumberFormatException e) {
				logger.error("isTotalCountCorrect : footer format error.  " + e.getClass().getName());
			}
		}
		
		loaded = count;
		
		return result;
	}

	@Override
	protected int insertHeader(BatchRepository repository, Map<String, String> header) {
		header.put("total", String.valueOf(total));
		header.put("loaded", String.valueOf(loaded));
		
		return repository.insertHeader(header);
	}

	@Override
	protected long insertItems(BatchRepository repository, List<HashMap<String, Object>> items) {
		return repository.insertCmsItems(items);
	}

	@Override
	protected void getNextSeqNo(BatchRepository repository) {
		currentSeqNo = repository.getNextSeqNo(SEQ_NO_MAX, SEQ_NO_MIN);
	}
	
	private Date getReserveDate(Map<String, String> map) {
		String startDate = map.get("processDate");
		String startTime = map.get("processTime");
		
		Date date = null;
		if(! Utils.isEmpty(startDate) && ! Utils.isEmpty(startTime)) {
			date = Utils.strToDate(startDate + startTime, "yyyyMMddHHmm");
		}
		
		return date;
	}
	
	private Date getLimitDate(Map<String, String> map) {
		String endDate = map.get("endDate");
		String endTime = map.get("endTime");
		
		Date date = null;
		if(! Utils.isEmpty(endDate) && ! Utils.isEmpty(endTime)) {
			date = Utils.strToDate(endDate + endTime, "yyyyMMddHHmm");
		}
		
		return date;
	}
}
