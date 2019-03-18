package com.wooricard.pshgw.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class Utils {
	public static final String FRMT_DATETIME		= "yyyyMMddHHmmss";
	public static final String FRMT_DATE			= "yyyyMMdd";
	
//	private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 
	
	public static boolean isEmpty(String str) {
		return (str == null || str.isEmpty());
	}
	
	public static boolean isEmpty(StringBuffer str) {
		return (str == null || Utils.isEmpty(str.toString()));
	}
	
	public static String dateToStr(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);	
	}
	
	public static boolean validateStrDate(String str) {
		if(Utils.isEmpty(str))
			return false;
	
		int len = str.length();
		if(len != 8 && len != 14)
			return false;
		
		String format = len == 8 ? Utils.FRMT_DATE : Utils.FRMT_DATETIME;
        try {
            DateFormat df = new SimpleDateFormat(format);
            df.setLenient(false);
            df.parse(str);
            return true;
        } 
        catch (ParseException e) {
            return false;
        }		
	}
	
	public static String resultToStr(int result) {
		return String.format("%04d", result);
	}
	
	public static Date strToDate(String str) {
		if(Utils.isEmpty(str))
			return null;
		
		SimpleDateFormat df = null;
		switch(str.length()) {
		case 8:
			df = new SimpleDateFormat(Utils.FRMT_DATE);
			break;
			
		case 14:
			df = new SimpleDateFormat(Utils.FRMT_DATETIME);
			break;
			
		default:
			return null;
		}
		
		Date result = null;
		try {
			df.setLenient(false);
			result = df.parse(str);
		} 
		catch (ParseException e) {
		}
		
		return result;
	}

	public static Date strToDate(String str, String format) {
		if(Utils.isEmpty(str))
			return null;
		
		SimpleDateFormat df = new SimpleDateFormat(format);
		
		Date result = null;
		try {
			df.setLenient(false);
			result = df.parse(str);
		} 
		catch (ParseException e) {
		}
		
		return result;
	}
	
	public static String safeStr(String str) {
		if(isEmpty(str))
			return "";
		
		return str;
	}	
	
//	public static String encryptPassword(String pwd) {
//		return passwordEncoder.encode(pwd);
//	}
//	
//	public static boolean isPasswordCorrect(String raw, String pwd) {
//		return passwordEncoder.matches(raw, pwd);
//	}

//	public static String makeUploadFileUrl(String root, boolean withDate) {
//		String subDir = withDate ? new SimpleDateFormat("yyyyMM").format(new Date()) : "";
//		
//		File dir = new File(root + File.separator + subDir);
//		if(! dir.exists())
//			dir.mkdirs();
//		
//		String newName = null;
//		while(true) {
//			newName = UUID.randomUUID().toString().replaceAll("-", "") + Constants.DEFAULT_EXT;
//			File temp = new File(dir, newName);
//			if(! temp.exists())
//				break;
//		}
//		
//		if(! Utils.isEmpty(subDir)) {
//			newName = subDir + File.separator + newName;
//		}
//		
//		return newName;
//	}	
	
	public static String parametersToString(Object obj, String... params) {
		final String SEPARATOR = ", ";
		
		Class<?> cls = obj.getClass();
		Method[] methods = cls.getMethods();
		
		String result = "";
		for(String p : params) {
			for(Method m : methods) {
				if(m.getName().equals(makeGetterName(p))) {
					try {
						Object val = m.invoke(obj);
						result += p + ": " + (val != null ? "\"" + val.toString() + "\"" : "null") + SEPARATOR;
					} 
					catch (IllegalAccessException e) {
					} 
					catch (IllegalArgumentException e) {
					} 
					catch (InvocationTargetException e) {
					}
					break;
				}
			}
		}
		
		if(result.endsWith(SEPARATOR)) {
			result = result.substring(0, result.length() - SEPARATOR.length());
		}
		
		return result;
	}

	public static String resultToString(Object result) {
		String str = "";
		if(result == null)
			return str;
		
		if(result instanceof Map<?, ?>) {
			Set<?> set = ((Map<?, ?>) result).entrySet();
			Iterator<?> itr = set.iterator();
			while(itr.hasNext()) {
				Entry<?, ?> entry = (Entry<?, ?>) itr.next();
				if(entry.getValue() instanceof String) {
					str += entry.getKey().toString() + " = " + entry.getValue().toString() + "\n";
				}
				else 
					str += "[" + entry.getKey().toString() + "]\n" + Utils.resultToString(entry.getValue());
			}
		}
		else if(result instanceof List<?>) {
			List<?> list = (List<?>) result;
			for(Object obj : list) {
				str += Utils.resultToString(obj) + "\n";
			}
		}
		
		return str;
	}
	
	public static byte[] mergeArray(byte[] array1, byte[] array2) {
		if(array1 == null || array1.length < 1 || array2 == null || array2.length < 1)
			return null;
		
		byte[] result = new byte[array1.length + array2.length];
		for(int i = 0; i < array1.length; i++) {
			result[i] = array1[i];
		}
		
		for(int i = 0; i < array2.length; i++) {
			result[array1.length + i] = array2[i];
		}
		
		return result;
	}
	
	private static String makeGetterName(String field) {
		return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}

//	public static String pwdToAsterisk(String pwd) {
//		String result = "null";
//		if(pwd != null) {
//			for(int i = 0; i < pwd.length(); i++)
//				result += "*";
//		}
//		 
//		return result;
//	}
}
