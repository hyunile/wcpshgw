package com.wooricard.pshgw.eai.service.imp;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wooricard.pshgw.batch.repository.BatchMapper;
import com.wooricard.pshgw.batch.repository.ResData;
import com.wooricard.pshgw.common.Constants;
import com.wooricard.pshgw.common.Result;
import com.wooricard.pshgw.eai.dto.EaiDTO;
import com.wooricard.pshgw.eai.repository.EaiServerRepository;
import com.wooricard.pshgw.eai.service.EaiServerService;
import com.wooricard.pshgw.utils.Utils;

@Service
public class EaiServerServiceImpl implements EaiServerService {
	private static final Logger logger = LoggerFactory.getLogger(EaiServerServiceImpl.class);
	
	private static final int HEADER_SIZE = 55;
	private static final int[] HEADER_ITEM_SIZE = { 5, 9, 3, 4, 12, 14, 2, 6 };
	
	private static final String[] HEADER_ITEM_TEXT = {
		"packetSize",
		"transCode", 
		"typeChar", 
		"msgCode", 
		"transUnique", 
		"processDate", 
		"responseCode", 
		"blank"
	};
			
	private static final int[] BODY_ITEM_SIZE = { 16, 20, 13, 12, 20, 2, 2, 2, 10, 16, 40, 12, 1, 40, 1, 32, 5 };
	
	private static final String[] BODY_ITEM_TEXT = {
		"cardNo", 
		"apvNo", 
		"nameNo", 
		"telNo", 
		"name", 
		"transType", 
		"apvMonth", 
		"check", 
		"apvAmount", 
		"chkBl", 
		"mchName", 
		"transDate", 
		"appInstYn", 
		"rejectReason", 
		"appRgYn", 
		"cuid", 
		"filter05"
	};

	private static final String ACK_MSG_CODE 		= "0807";
	private static final int ACK_PACKET_SIZE 		= 100;
	private static final int TRANS_DATE_POS 		= 153;
	
	private static final String ACK_RES_OK 			= "00";
	private static final String ACK_RES_FAIL 		= "99";
	private static final String ACK_RESULT_SUCCESS 	= "0001";
	private static final String ACK_RESULT_FAIL 	= "0002";
	
	
	@Autowired
	private EaiServerRepository repository;
	
	@Autowired
	private BatchMapper batchMapper;
	
    @Value("${eai.sender.id}")
    private String senderId;

    @Value("${eai.service.code}")
    private String serviceCode;
    
    @Value("${eai.appid}")
    private String appId;

    private static Charset charset = Charset.forName(Constants.CHARSET);
    
	@Override
	public int saveData(byte[] data, StringBuilder code) {
		
		int result = Result.ERR_UNKNOWN;
				
		try {
			HashMap<String, String> header = new HashMap<String, String>();
			header = parseHeader(data);
			if(header == null) {
				return Result.ERR_DATA_FORMAT;
			}
			
			String uniqueCode = header.get("transUnique");
			if(Utils.isEmpty(uniqueCode)) {
				logger.error("saveData : TransUnique is empty !!!");
				return Result.ERR_DATA_FORMAT;
			}
			
			code.append(uniqueCode);
			
			if(repository.dataExists(uniqueCode)) {
				logger.error("saveData : Request duplicated !!! : code = " + uniqueCode);
				return Result.ERR_DATA_FORMAT;
			}
			
			HashMap<String, String> body = new HashMap<String, String>();
			body = parseItem(data);			
			if(body == null) {
				return Result.ERR_DATA_FORMAT;
			}
			
			code.append(" : " + body.get("transType"));
			code.append(" : " + body.get("apvMonth"));
			code.append(" : " + body.get("check"));
			
			EaiDTO vo = new EaiDTO(header, body);	
			vo.setMsg("-");
			vo.setAppId(appId);
			vo.setSenderId(senderId);
			vo.setServiceCode(serviceCode);
			
			if(Utils.isEmpty(vo.getAppId()) || Utils.isEmpty(vo.getCuid())) {
				logger.error("saveData : Parameter missing : appId = " + vo.getAppId()
					+ ", userId = " + vo.getCuid());
				
				return Result.ERR_PARAM_MISSING;
			}
			
			List<ResData> resList = batchMapper.selectResInfoList(vo.getTransCode());
			if(resList == null || resList.isEmpty()) {
				logger.error("saveData : selectResInfoList() : Resource data missing.");			
				return Result.ERR_DATABASE;	
			}
			
			ResData res = findRes(body, resList);
			
			vo.setPushType(res.getPushType());
			vo.setTitle(res.getTitle());
			vo.setPageId(res.getPageId());
//			String img1 = ! Utils.isEmpty(res.getImage()) ? "[1]" + res.getImage() : "";
//			String img2 = ! Utils.isEmpty(res.getImage2()) ? "[2]" + res.getImage2() : "";
//			String img = "";
//			if(! Utils.isEmpty(img1))
//				img = img1 + ",";
//			if(! Utils.isEmpty(img2))
//				img += img2;
			
			vo.setImage(res.getImage());
			vo.setImage2(res.getImage2());
			vo.setImage3(res.getImage3());
			vo.setLinkUrl1(res.getLinkUrl1());
			vo.setLinkUrl2(res.getLinkUrl2());
			vo.setLinkUrl3(res.getLinkUrl3());
			vo.setType1(res.getType1());
			vo.setType2(res.getType2());
			vo.setType3(res.getType3());
			
			logger.debug("saveApvData : cuid = " + vo.getCuid() + ", mhn = " + vo.getMchName());
			result = repository.saveApvData(vo);
//			result = 1;
			if(result > 0) {
				result = Result.SUCCESS;				
			}	
			else {
				logger.error("saveApvData : result = " + result);
			}
		}
		catch(Exception e) {
			logger.error("saveData : ", e);
			result = Result.ERR_DATABASE;
		}	
		
		return result;
	}
	
	@Override
	public byte[] makeAckData(byte[] msg, int result) {
		byte[] ack = new byte[ACK_PACKET_SIZE];
		
		byte[] temp = String.format("%05d", ACK_PACKET_SIZE - HEADER_ITEM_SIZE[0]).getBytes();
		System.arraycopy(temp, 0, ack, 0, temp.length);
		
		//--
		int offset = HEADER_ITEM_SIZE[0];
		int size = 42;		// [1] ~ [5]
		System.arraycopy(msg, offset, ack, offset, size);		
		ack[20] = '7';
		
//		int offset = HEADER_ITEM_SIZE[0];
//		int size = HEADER_ITEM_SIZE[1] + HEADER_ITEM_SIZE[2];
//		System.arraycopy(msg, offset, ack, offset, size);
//		
//		offset += size;
//		temp = ACK_MSG_CODE.getBytes();
//		System.arraycopy(temp, 0, ack, offset, HEADER_ITEM_SIZE[3]);
//		
//		offset += HEADER_ITEM_SIZE[3];
//		size = HEADER_ITEM_SIZE[4] + HEADER_ITEM_SIZE[5];
//		System.arraycopy(msg, offset, ack, offset, size); 
		
//		offset += size;
		offset = 47;
		//--
		
		String r = result == Result.SUCCESS ? ACK_RES_OK : ACK_RES_FAIL;
		temp = r.getBytes();
		System.arraycopy(temp, 0, ack, offset, HEADER_ITEM_SIZE[6]);
		
		offset = HEADER_SIZE;		// sum(HEADER_ITEM_SIZE);
		size = BODY_ITEM_SIZE[0];
		System.arraycopy(msg, offset, ack, offset, size);
		
		offset += size;
		size = BODY_ITEM_SIZE[2];
		System.arraycopy(msg, offset + BODY_ITEM_SIZE[1], ack, offset, size);
		
		offset += size;
		size = BODY_ITEM_SIZE[11];
		temp = r.getBytes();
		System.arraycopy(msg, TRANS_DATE_POS, ack, offset, size);
		
		offset += size;
		r = result == Result.SUCCESS ? ACK_RESULT_SUCCESS : ACK_RESULT_FAIL;
		temp = r.getBytes();
		System.arraycopy(temp, 0, ack, offset, r.length());
		
		return ack;
	}
	
	private HashMap<String, String> parseHeader(byte[] array) {
		HashMap<String, String> result = new HashMap<String, String>();
		
		try {
			int offset = 0;
			for(int i = 0; i < HEADER_ITEM_SIZE.length; i++) {
				String temp = new String(Arrays.copyOfRange(array, offset, offset + HEADER_ITEM_SIZE[i]));
				result.put(HEADER_ITEM_TEXT[i], Utils.isEmpty(temp) ? "" : temp.trim());
				offset += HEADER_ITEM_SIZE[i];
			}
		}
		catch(Exception e) {
			logger.error("parseHeader : ", e);
			result = null;
		}
		
		return result;
	}
	
	private HashMap<String, String> parseItem(byte[] array) {
		HashMap<String, String> result = new HashMap<String, String>();
		
//		int headerSize = sum(HEADER_ITEM_SIZE);
		
		try {
			int offset = HEADER_SIZE;		// headerSize;
			for(int i = 0; i < BODY_ITEM_SIZE.length; i++) {
				String temp = new String(Arrays.copyOfRange(array, offset, offset + BODY_ITEM_SIZE[i]), charset);
				result.put(BODY_ITEM_TEXT[i], Utils.isEmpty(temp) ? "" : temp.trim());
				offset += BODY_ITEM_SIZE[i];
			}
			
			String name = result.get("name");
			if(!Utils.isEmpty(name) && name.length() > Constants.STR_CUT_NAME) {
				result.put("name", name.substring(0, Constants.STR_CUT_NAME));
			}
			
			String reject = result.get("rejectReason");
			if(!Utils.isEmpty(reject) && reject.length() > Constants.STR_CUT_REJECT) {
				result.put("rejectReason", reject.substring(0, Constants.STR_CUT_REJECT));
			}
			
			String mchName = (String) result.get("mchName");
			String type = (String) result.get("transType");
			boolean abroad = "09".equals(type) || "19".equals(type);
			
			if(! Utils.isEmpty(mchName) && 
				((! abroad && mchName.length() > Constants.STR_CUT_MCHNAME)
				|| (abroad && ! mchName.matches("\\A\\p{ASCII}*\\z")))
				) {
				result.put("mchName", mchName.substring(0, Constants.STR_CUT_MCHNAME));
			}
		}
		catch(Exception e) {
			logger.error("parseItem : ", e);
			result = null;
		}
		
		return result;
	}
	
	private ResData findRes(HashMap<String, String> body, List<ResData> resList) {
		for(ResData res : resList) {
			if(res.getDescription().contains(body.get("transType"))) {
				return res;
			}
		}
		
		return resList.get(0);
	}
}
