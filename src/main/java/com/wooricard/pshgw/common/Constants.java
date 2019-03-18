package com.wooricard.pshgw.common;

public class Constants {
	public static final String BATCH_DEFAULT_SENDER = "batch";
	public static final String BATCH_DEFAULT_SERVICE = "PUBLIC";
	
//	 public static final String PUSH_TYPE_D20 = "0F";
//	public static final String PUSH_TYPE_D30 = "0F";
//	public static final String PUSH_TYPE_D40 = "0F";
//	public static final String PUSH_TYPE_D50 = "11";
//	public static final String PUSH_TYPE_B20 = "0I";
	public static final String PUSH_TYPE_CMS = "11";
	
	public static final String TRANS_CODE_D20 	= "D20";
	public static final String TRANS_CODE_D30 	= "D30";
	public static final String TRANS_CODE_D40 	= "D40";
	public static final String TRANS_CODE_D50 	= "D50";
	public static final String TRANS_CODE_B20 	= "B20";
	public static final String TRANS_CODE_CMS 	= "CMS";
	
	public static final String PUSH_MSG_B20 = "우리카드 고객님,\n %d월 이용대금명세서입니다. 이용에 감사드립니다.";
	
	public static final String DNIS_DEFAULT 		= "0000";
	public static final String CHARS_NEWLINE		= "@#";
	
	public static final String CHARSET			= "EUC-KR";

	public static final String WPAY_TRANSCODE	= "WPAY";
	public static final String WPAY_APPID		= "com.wooricard.wpay";
	
	public static final String SMART_TRANSCODE	= "SMART";
	public static final String SMART_APPID		= "com.wooricard.smart";

	public static final int STR_CUT_NAME	 = 5;
	public static final int STR_CUT_MCHNAME	 = 10;
	public static final int STR_CUT_REJECT	 = 10;
	
	public static final String[] PARTITION_TABLES = {
		"T_PUSH_REALTIME_MSG",
		"T_PUSH_REALTIME_MSG_COMPLETED",
		"T_PUSH_BATCH_MSG",
		"T_PUSH_BATCH_MSG_COMPLETED",
		"T_PUSH_MSG_RECV_MASTER",
		"T_PUSH_SUCCESS"
	};

	public static final String[] PARTITION_PREFIX = {
		"P_REALTIME_MSG_",
		"P_REALTIME_MSG_COMPLETED_",
		"P_BATCH_MSG_",
		"P_BATCH_MSG_COMPLETED_",
		"P_MSG_RECV_MASTER_",
		"P_SUCCESS_"
	};
}
