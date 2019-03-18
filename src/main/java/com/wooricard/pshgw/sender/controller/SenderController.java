package com.wooricard.pshgw.sender.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wooricard.pshgw.batch.service.BatchService;
import com.wooricard.pshgw.common.Constants;
import com.wooricard.pshgw.common.Result;
import com.wooricard.pshgw.sender.dto.SenderDTO;
import com.wooricard.pshgw.sender.service.SenderService;
import com.wooricard.pshgw.sender.vo.SenderParams;
import com.wooricard.pshgw.utils.Utils;

@RestController
@RequestMapping("/push")
public class SenderController {
    private final Logger logger = LoggerFactory.getLogger(SenderController.class);

    @Autowired
	private SenderService senderService;
    
    @Autowired
	private BatchService batchService;
	
    @Value("${wpay.service.code}") // resources/config/settings.properties 
    String wpayServiceCode;
    
    @Value("${wpay.sender.id}")
    String wpaySenderCode;

    @Value("${smart.service.code}") 
    String smartServiceCode;
    
    @Value("${smart.sender.id}")
    String smartSenderCode;

    @RequestMapping(value = "/result/make_file", method = RequestMethod.GET)
    public Object makePushResultFile(HttpServletRequest request, @RequestParam(required=false) String date) {

    	String url = request.getRequestURL().toString();
    	
    	int result = batchService.processDW(date);
    	result = result != -1 ? Result.SUCCESS : Result.ERR_SYSTEM;
    	
    	return Result.make(result, url, null);
    	
    }
    
    @RequestMapping(value = "/wpay/send_msg", method = RequestMethod.POST, produces= "application/json; charset=utf8")
    public Object sendWpayPushMsg(HttpServletRequest request, @RequestBody SenderParams params) {
    	
    	String url = request.getRequestURL().toString();
    	
    	int result = 1; //error
    	
    	String type = params.getType(); 	 	// Y (필수) 
    	String cuid = params.getCuid();			// Y
    	String cmpgnid = params.getCmpgnid();	// Y
    	String dnis = params.getDnis();			// N
    	String title = params.getTitle();		// Y
    	String msg = params.getMsg();			// Y
    	String reqUrl = params.getUrl();		// N
    	
    	if(Utils.isEmpty(type) || Utils.isEmpty(cuid) || Utils.isEmpty(cmpgnid) || Utils.isEmpty(msg) ) {
    		logger.info("ERR_PARAM_MISSING");
    		result = Result.ERR_PARAM_MISSING;
    		return Result.make(result, url, null);
    	}
    	if(Utils.isEmpty(dnis)) {
    		dnis = Constants.DNIS_DEFAULT;
    	}
    	if(Utils.isEmpty(title)) {
    		title = "";
    	}
    	SenderDTO dto = new SenderDTO(params);
    	dto.setType(type);						//PUSH_TYPE
    	dto.setCuid(cuid);						//CUID
    	dto.setCmpgnid(cmpgnid);				//CMPGN_ID
    	dto.setTitle(title); 					//SUB_TITLE
    	dto.setMsg(msg);						//MESSAGE
    	dto.setDnis(dnis);						//DNIS
    	dto.setUrl(reqUrl); 					//LINK_URL
    	dto.setServiceCode(wpayServiceCode);	//SERVICE_CODE
    	dto.setSenderCode(wpaySenderCode);		//SENDER_CODE
    	
    	result = senderService.sendWpayPush(dto);
    	
    	return Result.make(result, url, null);
    }

    @RequestMapping(value = "/smart/send_msg", method = RequestMethod.POST, produces= "application/json; charset=utf8")
    public Object sendSmartPushMsg(HttpServletRequest request, @RequestBody SenderParams params) {
    	
    	String url = request.getRequestURL().toString();
    	
    	String type = params.getType(); 	 	// Y (필수) 
    	String cuid = params.getCuid();			// Y
    	String cmpgnid = params.getCmpgnid();	// N
    	String title = params.getTitle();		// Y
    	String msg = params.getMsg();			// Y
//    	String reqUrl = params.getUrl();		// N
    	
    	if(Utils.isEmpty(type) || Utils.isEmpty(cuid) || Utils.isEmpty(msg) ) {
    		logger.info("ERR_PARAM_MISSING");
    		return Result.make(Result.ERR_PARAM_MISSING, url, null);
    	}
    	if(Utils.isEmpty(cmpgnid)) {
    		cmpgnid = "";
    	}
    	if(Utils.isEmpty(title)) {
    		title = "";
    	}
    	SenderDTO dto = new SenderDTO(params);
    	dto.setServiceCode(smartServiceCode);
    	dto.setSenderCode(smartSenderCode);	
    	
    	int result = senderService.sendSmartPush(dto);
    	
    	return Result.make(result, url, null);
    }
    
//    @RequestMapping(value = "/wpay/send_msg", method = RequestMethod.GET)
//    public String sendPushMsgByGet(HttpServletRequest request) {
//    	
//    	SenderParams params = new SenderParams();
//    	params.setCuid("1111");
//       	params.setType("CC");
//       	params.setCmpgnid("5352");
//       	params.setMsg("Test Push");
//    	
//    	String type = params.getType(); 	 	// Y (필수) 
//    	String cuid = params.getCuid();			// Y
//    	String cmpgnid = params.getCmpgnid();	// Y
//    	String dnis = params.getDnis();			// N
//    	String title = params.getTitle();		// Y
//    	String msg = params.getMsg();			// Y
//    	String reqUrl = params.getUrl();		// N
//    	
//    	if(Utils.isEmpty(type) || Utils.isEmpty(cuid) || Utils.isEmpty(cmpgnid) || Utils.isEmpty(msg) ) {
//    		logger.info("ERR_PARAM_MISSING");
//    		result = Result.ERR_PARAM_MISSING;
//    		
//    		return "0000";
////    		return Result.make(result, url, null);
//    	}
//    	if(Utils.isEmpty(dnis)) {
//    		dnis = Constants.DNIS_DEFAULT;
//    	}
//    	if(Utils.isEmpty(title)) {
//    		title = "";
//    	}
//    	SenderDTO dto = new SenderDTO(params);
//    	dto.setType(type);					//PUSH_TYPE
//    	dto.setCuid(cuid);					//CUID
//    	dto.setCmpgnid(cmpgnid);			//CMPGN_ID
//    	dto.setTitle(title); 				//SUB_TITLE
//    	dto.setMsg(msg);					//MESSAGE
//    	dto.setDnis(dnis);					//DNIS
//    	dto.setUrl(reqUrl); 				//LINK_URL
//    	dto.setServiceCode(serviceCode);	//SERVICE_CODE
//    	dto.setSenderCode(senderCode);		//SENDER_CODE
//    	result = senderService.sendPush(dto);
//    	
//		return "0000";
//    }
}
