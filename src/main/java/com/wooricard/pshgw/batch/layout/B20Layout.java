package com.wooricard.pshgw.batch.layout;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wooricard.pshgw.batch.base.ByteBatchLayout;
import com.wooricard.pshgw.batch.repository.BatchRepository;
import com.wooricard.pshgw.common.Constants;
import com.wooricard.pshgw.utils.Utils;

public class B20Layout extends ByteBatchLayout {
	private static final Logger logger = LoggerFactory.getLogger(B20Layout.class);

	private static long SEQ_NO_MAX = 6000000000000L;
	private static long SEQ_NO_MIN = 5000000000000L;
	
	protected static byte TYPE_HEADER 	= 'H';
	protected static byte TYPE_BODY 		= 'D';
	protected static byte TYPE_FOOTER 	= 'T';
	
	private static long currentSeqNo = 0;

	private static String[] HEADER_ITEM_NAME = { 
		"code", 
		"processDate", 
		"blank" 	
	};
	
	private static String[] BODY_ITEM_NAME = { 
		"code", 
		"nameNo", 
		"telNo", 
		"transDate", 
		"name", 
		"appRg", 
		"cuid", 
		"blank" 
	};
	
	private static int[] HEADER_ITEM_SIZE = { 1, 8, 91 };
	private static int[] BODY_ITEM_SIZE = { 1, 13, 12, 8, 20, 1, 32, 13 };
	private static int[] FOOTER_ITEM_SIZE = { 1, 7, 92 };
	
	private int month;
	
	public B20Layout(BatchRepository repository) {
		super(Constants.TRANS_CODE_B20, repository, HEADER_ITEM_SIZE, BODY_ITEM_SIZE, FOOTER_ITEM_SIZE, logger);
	}

	@Override
	protected Map<String, String> parseHeader(byte[] array) {
		if(array[0] != TYPE_HEADER) {
			logger.error("parseHeader : array[0] != TYPE_HEADER");
			return null;
		}
		
		HashMap<String, String> result = new HashMap<String, String>();
		
		try {
			int offset = 0;
			for(int i = 0; i < HEADER_ITEM_SIZE.length; i++) {
				String temp = new String(Arrays.copyOfRange(array, offset, offset + HEADER_ITEM_SIZE[i]), charset);
				result.put(HEADER_ITEM_NAME[i], temp.trim());
				offset += HEADER_ITEM_SIZE[i];
			}
			
			result.put("transCode", Constants.TRANS_CODE_B20);
			result.put("batchId", batchId);
			result.put("fileName", file.getName());
			result.put("appId", appId);
			result.put("useTemplete", "N");
			result.put("templete", "-");
			result.put("batchTitle", res.getTitle());
			result.put("pageId", res.getPageId());
			result.put("imgUrl1", res.getImage());
			result.put("imgUrl2", res.getImage2());
			result.put("imgUrl3", res.getImage3());
			result.put("linkUrl1", res.getLinkUrl1());
			result.put("linkUrl2", res.getLinkUrl2());
			result.put("linkUrl3", res.getLinkUrl3());
			result.put("type1", res.getType1());
			result.put("type2", res.getType2());
			result.put("type3", res.getType3());
			
			Date now = new Date();
			result.put("processDate", Utils.dateToStr(now, Utils.FRMT_DATE));			
			String reserveTime = res.getReserveTime();
			if(Utils.isEmpty(reserveTime)) {
				reserveTime = options.getReserveTime(Constants.TRANS_CODE_B20);
			}
			
			if(Utils.isEmpty(reserveTime)) {
				reserveTime = Utils.dateToStr(now, "HHmm");
			}
			result.put("processTime", reserveTime);			
			
			String processDate = result.get("processDate");
			month = Integer.valueOf(processDate.substring(4, 6));
//			today = Utils.dateToStr(new Date(), Utils.FRMT_DATE);
		}
		catch(Exception e) {
			logger.error("parseHeader : ", e);
			result = null;
		}
		
		return result;
	}

	@Override
	protected HashMap<String, Object> parseItem(byte[] array) {
		if(array[0] != TYPE_BODY) {
			logger.error("parseItem : array[0] != TYPE_BODY");
			return null;
		}
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try {
			int offset = 0;
			for(int i = 0; i < BODY_ITEM_SIZE.length; i++) {
				String temp = new String(Arrays.copyOfRange(array, offset, offset + BODY_ITEM_SIZE[i]), charset);
				result.put(BODY_ITEM_NAME[i], temp.trim());
				offset += BODY_ITEM_SIZE[i];
			}
			
			result.put("seqNo", String.valueOf(currentSeqNo++));
			result.put("batchId", batchId);
			result.put("pushType", res.getPushType());
			result.put("senderId", senderId);
			result.put("serviceCode", serviceCode);
			result.put("message", String.format(Constants.PUSH_MSG_B20, month));

//			Date d = Utils.strToDate(today + reserveTime + "00");
//			result.put("reserveDate", d != null ? d : "");
//			d = Utils.strToDate(today + limitTime + "00");
//			result.put("sendTimeLimit", d != null ? d : "");
			
			String cuid = (String) result.get("cuid");
			if(Utils.isEmpty(cuid)) {
				logger.error("parseItem : CUID missing !!!");
				result = null;
			}
		}
		catch(IndexOutOfBoundsException e) {
			logger.error("parseItem : ", e);
			result = null;
		}
		
		return result;
}

	@Override
	protected boolean isFooter(byte[] array) {
		return array[0] == TYPE_FOOTER;
	}

	@Override
	protected boolean isTotalCountCorrect(byte[] array, long count) {
		int offset = FOOTER_ITEM_SIZE[0];
		int size = FOOTER_ITEM_SIZE[1];
		
		loaded = count;
		String str = null;
		
		try {
			str = new String(Arrays.copyOfRange(array, offset, offset + size));
			total = Long.valueOf(str);

			if(total == count)
				return true;
		}
		catch(NumberFormatException e) {
			logger.error("isTotalCountCorrect :  array = " + str + ", count = " + count);
		}
		
		return false;
	}

	@Override
	protected int insertHeader(BatchRepository repository, Map<String, String> header) {
		header.put("total", String.valueOf(total));
		header.put("loaded", String.valueOf(loaded));
		
		return repository.insertHeader(header);
	}

	@Override
	protected long insertItems(BatchRepository repository, List<HashMap<String, Object>> items) {
		return repository.insertB20Items(items);
	}

	@Override
	protected void getNextSeqNo(BatchRepository repository) {
		currentSeqNo = repository.getNextSeqNo(SEQ_NO_MAX, SEQ_NO_MIN);
	}
}
