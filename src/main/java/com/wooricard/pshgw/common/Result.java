package com.wooricard.pshgw.common;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wooricard.pshgw.utils.Utils;

public class Result {
    private static final Logger logger = LoggerFactory.getLogger(Result.class);
    
	public static final int SUCCESS						= 0;
//	public static final int SUCCESS_TOKEN_REFRESHED	= 1000;
	
	public static final int ERR_PARAM_MISSING			= 1;
	public static final int ERR_PARAM_OUTOF_RANGE		= 2;
	public static final int ERR_DATA_FORMAT				= 3;
//	public static final int ERR_WRONG_CUSTOMER			= 4;	
//	public static final int ERR_INVALID_PARAM_VALUE	= 5;	
//	public static final int ERR_PAGE_NOT_EXISTS			= 6;	

	public static final int ERR_AUTHKEY_MISSING			= 11;
	public static final int ERR_AUTH_FAILED				= 12;
	public static final int ERR_AUTHKEY_EXPIRED			= 13;
	public static final int ERR_USER_NOT_FOUND			= 21;	// 14;
//	public static final int ERR_WRONG_PASSWORD			= 15;
	
	public static final int ERR_DATA_NOT_FOUND			= 22;
//	public static final int ERR_ALREADY_APPLIED			= 31;
//	public static final int ERR_ALREADY_STARTED			= 32;
//	public static final int ERR_NOT_STARTED				= 33;
//	public static final int ERR_NOT_IN_PROCESS			= 34;
//	public static final int ERR_MISSION_FINISHED		= 35;
//	
//	public static final int ERR_SUBMISSION_FULL			= 41;
//	public static final int ERR_MY_EXER_FULL			= 42;
//
//	public static final int ERR_DATA_DUPLICATED			= 51;
//	
//	public static final int ERR_PUSH_TOKEN_MISSING		=  61;
	
	public static final int ERR_SYSTEM					= 9001;
	public static final int ERR_DATABASE				= 9002;
	public static final int ERR_NETWORK					= 9003;
	public static final int ERR_UNKNOWN					= 9099;
	
	private static final String CODE		= "RESULT_CODE";
	private static final String MSG		= "RESULT_MSG";
	private static final String URL		= "REQUEST_URL";
	private static final String DATA		= "DATA";
	
	private static Map<Integer, String> resultMsgs;
	
	static {
		resultMsgs = new HashMap<Integer, String>();
		
		resultMsgs.put(SUCCESS, "Succeed.");
		resultMsgs.put(ERR_PARAM_MISSING, "Parameter missing.");
		resultMsgs.put(ERR_PARAM_OUTOF_RANGE, "Parameter value out of range.");
		resultMsgs.put(ERR_DATA_FORMAT, "Data format not correct.");
//		resultMsgs.put(ERR_WRONG_CUSTOMER, "Customer not correct.");
//		resultMsgs.put(ERR_INVALID_PARAM_VALUE, "Invalid parameter value.");
//		resultMsgs.put(ERR_PAGE_NOT_EXISTS, "Request page does not exists.");
		resultMsgs.put(ERR_AUTHKEY_MISSING, "Authentication key missing.");
		resultMsgs.put(ERR_AUTH_FAILED, "Authentication failed.");
		resultMsgs.put(ERR_AUTHKEY_EXPIRED, "Authentication key expired.");
		resultMsgs.put(ERR_USER_NOT_FOUND, "User not found.");
//		resultMsgs.put(ERR_WRONG_PASSWORD, "Wrong password.");
		resultMsgs.put(ERR_DATA_NOT_FOUND, "Data not found.");
//		resultMsgs.put(ERR_ALREADY_APPLIED, "Already applied.");
//		resultMsgs.put(ERR_ALREADY_STARTED, "Mission already started.");
//		resultMsgs.put(ERR_NOT_STARTED, "Mission not started.");
//		resultMsgs.put(ERR_NOT_IN_PROCESS, "Mission not in process.");
//		resultMsgs.put(ERR_MISSION_FINISHED, "Mission finished.");
//		resultMsgs.put(ERR_SUBMISSION_FULL, "Submission full.");
//		resultMsgs.put(ERR_MY_EXER_FULL, "My exercises full.");
//		resultMsgs.put(ERR_DATA_DUPLICATED, "Data duplicated.");
//		resultMsgs.put(ERR_PUSH_TOKEN_MISSING, "Push token missing.");
		resultMsgs.put(ERR_SYSTEM, "System error occured.");		
		resultMsgs.put(ERR_DATABASE, "Database error occured.");		
		resultMsgs.put(ERR_NETWORK, "Network error occured.");		
		resultMsgs.put(ERR_UNKNOWN, "Unknown error occured.");		
	}

	public static Map<String, Object> make(int resultCode, String url, Object data) {
		String msg = resultMsgs.get(resultCode);
		if(Utils.isEmpty(msg)) {
			resultCode = ERR_UNKNOWN;
			data = null;
			msg = resultMsgs.get(ERR_UNKNOWN);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(CODE, Utils.resultToStr(resultCode));
		result.put(MSG, msg);
		result.put(URL, url);
		if(data != null)
			result.put(DATA, data);
		
		logger.info("[ RESULT ] " + CODE + " = " + Utils.resultToStr(resultCode) + ", " + MSG + " = " + msg);
		return result;

	}

	public static String getMsg(int resultCode) {
		String msg = resultMsgs.get(resultCode);
		if(Utils.isEmpty(msg)) {
			msg = resultMsgs.get(ERR_UNKNOWN);
		}
		return msg;
	}
}
