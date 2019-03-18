package com.wooricard.pshgw.batch.base;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;

import com.wooricard.pshgw.batch.repository.BatchRepository;
import com.wooricard.pshgw.common.Constants;
import com.wooricard.pshgw.common.OptionReader;
import com.wooricard.pshgw.utils.Utils;

public abstract class BatchLayoutBase {
	public static final int INSERT_MAX_ITEMS = 500;
	public static final int MAX_BUFFER_SIZE = 4096;
	public static final int PROCESS_LOG_INTERVAL = 10000;

	protected BatchRepository repository;
	protected File file;
	protected String batchId;
	
	protected Map<String, String> header;
//	protected List<HashMap<String, String>> items; 

	protected String senderId;
	protected String serviceCode;
	protected String appId;
	protected long total;
	protected long loaded;
	
	protected Logger logger;
	protected Charset charset;
	
	protected String transCode;
	
	protected OptionReader options;

	public BatchLayoutBase(String transCode, BatchRepository repository, Logger logger) {
		this.transCode = transCode;
		this.repository = repository;
		this.logger = logger;
//		items = new ArrayList<HashMap<String, String>>();
		
		senderId = Constants.BATCH_DEFAULT_SENDER;
		serviceCode = Constants.BATCH_DEFAULT_SERVICE;
		
		charset = Charset.forName(Constants.CHARSET);
//		SortedMap<String, Charset> map = Charset.availableCharsets();
//		Set<Entry<String, Charset>> set = map.entrySet();
//		Iterator<Entry<String, Charset>> itr = set.iterator();
//		while(itr.hasNext()) {
//			Entry<String, Charset> entry = itr.next();
//			logger.info("Charset : " + entry.getKey());
//		}
	}
	
	public void setOptionReader(OptionReader options) {
		this.options = options;
	}

	public void setFile(File file) {
		this.file = file;
		header = null;
//		items.clear();
		batchId = UUID.randomUUID().toString().replace("-", "");
		total = 0L;
		
		getNextSeqNo(repository);
	}
	
	public File getFile() {
		return file;
	}

	public void setRepository(BatchRepository repository) {
		this.repository = repository;
	}

	public int rollback(String batchId) {
		return repository.rollback(batchId);
	}
	
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppId() {
		return appId;
	}

	public String getBatchId() {
		return batchId;
	}

	public String getTransCode() {
		return transCode;
	}
	
	public long getTotal() {
		return total;
	}

	public long getLoaded() {
		return loaded;
	}

	protected void checkProcessDate(HashMap<String, String> map) {
		if(Utils.isEmpty(map.get("processDate"))) {
			String now = Utils.dateToStr(new Date(), Utils.FRMT_DATE);
			logger.warn(String.format("checkProcessDate : %s :PROCESS DATE not set. Setting default value... %s",
					map.get("fileName"), now));
			
			map.put("processDate", now);
		}
	}

	protected void printProcessLog(long count) {
		if(count % PROCESS_LOG_INTERVAL == 0)
			logger.info("Batch processing : " + count);
	}

	public abstract boolean save() throws Exception; 
	
	protected abstract void getNextSeqNo(BatchRepository repository);
	protected abstract int insertHeader(BatchRepository repository, Map<String, String> header);
}
