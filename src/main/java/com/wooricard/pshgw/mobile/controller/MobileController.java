package com.wooricard.pshgw.mobile.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wooricard.pshgw.auth.service.AuthService;
import com.wooricard.pshgw.common.Result;
import com.wooricard.pshgw.mobile.dto.MobileDTO;
import com.wooricard.pshgw.mobile.service.MobileService;
import com.wooricard.pshgw.mobile.vo.MobileParams;
import com.wooricard.pshgw.mobile.vo.MsgListVO;
import com.wooricard.pshgw.utils.Utils;

@RestController
@RequestMapping("/app")
public class MobileController {
    private final Logger logger = LoggerFactory.getLogger(MobileController.class);

    @Autowired
	private AuthService authService;
    
    @Autowired
	private MobileService mobileService;
	
	@Value("${rest.auth.use}")
	private String useAuth;

    @RequestMapping(value = "/get_msgs", method = RequestMethod.POST)
    public Object getPushMsgs(HttpServletRequest request, @RequestBody MobileParams params) { // @ModelAttribute => @RequestBody Json
    	
    	String url = request.getRequestURL().toString();
    	
    	int result = Result.ERR_UNKNOWN;
    	
     	String appid = params.getAppid();
    	String cuid = params.getCuid();
    	//String strMax = params.getMax();
    	String strAsc = params.getAsc();
    	boolean asc = false;
    	if(Utils.isEmpty(appid) || Utils.isEmpty(cuid) /*||  Utils.isEmpty(strAsc)*/) {
    		result = Result.ERR_PARAM_MISSING;
    		return Result.make(result, url, null);
    	}
    		
    	MobileDTO dto = new MobileDTO(params);
    	dto.setAuth(request.getHeader("Authorization"));

    	if("Y".equalsIgnoreCase(useAuth)) {
        	if((result = authService.authenticate(dto)) != Result.SUCCESS) {
            	logger.error("getPushMsgs : authenticate() failed.");
        		return Result.make(result, url, null);
        	}    		
    	}
    	    	
    	if(Utils.isEmpty(strAsc)) {
    		asc = true;
    	}else {
    		asc = strAsc.equals("Y") ? true : false;	
    	}
    	
    	//int max = 0;
    	
/*    	
    	max = Integer.valueOf(strMax);
    	
    	if(max < 0 ) {
    		return Result.make(Result.ERR_PARAM_OUTOF_RANGE, url, null);
    	}
    	 */
    	//List<String> getList = mobileService.selectMsgList(dto);
    	
//    	dto.setAppid(appid);
//    	dto.setCuid(cuid);
    	//dto.setMax(max);
    	
    	dto.setAsc(asc);
    	result = mobileService.getMsgList(dto);
    	
    	//dto.setMsgList(result);
    	
    	if(result != Result.SUCCESS)
    		return Result.make(result, url, null);
    
    	MsgListVO vo = new MsgListVO(dto);
    	
    	return Result.make(Result.SUCCESS, url, vo);
    }
    
    /**
     * 뱃지 초기화
     * @param request
     * @param params
     * @return
     */
//    @RequestMapping(value = "/init_badge_no", method = RequestMethod.POST)
//    public Object initBadgeNo(HttpServletRequest request, @RequestBody MobileParams params) {
//    	
//    	String url = request.getRequestURL().toString();
//    	int result = 1; //error
//    	
//    	// TODO : check parameters
//    	String appid = params.getAppid();
//    	String cuid = params.getCuid();
//    	
//    	if(Utils.isEmpty(appid) || Utils.isEmpty(cuid)) {
//    		logger.info("ERR_PARAM_MISSING");
//    		result = Result.ERR_PARAM_MISSING;
//    		return Result.make(result, url, null);
//    	}
//    	
//    	MobileDTO dto = new MobileDTO(params);
//    	dto.setAppid(appid);
//    	dto.setCuid(cuid);
//    	
//    	logger.info("MobileDTO toString : {}", dto.toString());
//    	result = mobileService.initBadgeNo(dto);
//    	
//    	return Result.make(result, url, null);
//    }
    
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public Object auth(HttpServletRequest request, @RequestBody MobileParams params) {
    	
    	String url = request.getRequestURL().toString();
    	
    	String appid = params.getAppid();
    	String cuid = params.getCuid();
    	String device = params.getDevice();
    	
    	if(Utils.isEmpty(appid) || Utils.isEmpty(cuid) || Utils.isEmpty(device)) {
    		logger.info("ERR_PARAM_MISSING");
    		return Result.make(Result.ERR_PARAM_MISSING, url, null);
    	}
    	
    	MobileDTO dto = new MobileDTO(params);
    	int result = authService.issueAuthKey(dto);
    	
    	HashMap<String, String> authKey = null;
    	if(result == Result.SUCCESS) {
    		authKey = new HashMap<String, String>();
    		authKey.put("AUTHKEY", dto.getAuth());
    	}
    	
    	return Result.make(result, url, authKey);
    }    
}
