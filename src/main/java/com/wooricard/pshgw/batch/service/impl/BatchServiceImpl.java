package com.wooricard.pshgw.batch.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wooricard.pshgw.batch.base.BatchLayoutBase;
import com.wooricard.pshgw.batch.layout.B20Layout;
import com.wooricard.pshgw.batch.layout.CmsLayout;
import com.wooricard.pshgw.batch.layout.D20Layout;
import com.wooricard.pshgw.batch.layout.D30Layout;
import com.wooricard.pshgw.batch.layout.D40Layout;
import com.wooricard.pshgw.batch.layout.D50Layout;
import com.wooricard.pshgw.batch.layout.DWLayout;
import com.wooricard.pshgw.batch.repository.BatchRepository;
import com.wooricard.pshgw.batch.service.BatchService;
import com.wooricard.pshgw.common.Constants;
import com.wooricard.pshgw.common.OptionReader;
import com.wooricard.pshgw.common.Result;
import com.wooricard.pshgw.utils.Utils;

@Service
public class BatchServiceImpl implements BatchService {
	private static final Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);
	
	private static final String OPT_USE_FILE 	= "batchFile";
	private static final String OPT_USE_SMS 		= "sms";
	private static final String OPT_USE_DW 		= "dw";
	private static final String OPT_USE_DEL 		= "delOldData";

	@Autowired
	protected BatchRepository repository;
	
	@Autowired
	private OptionReader options;
	
//	@Value("${batch.job.use}") 
//	private String useBatch;
//	@Value("${dw.job.use}")
//	private String useDW;
//	@Value("${sms.job.use}")
//	private String useSms;
//	@Value("${delete.job.use}")
//	private String useDelete;
	
    @Value("${batch.d20.dir.read}")
    private String d20DirRead;
    @Value("${batch.d20.file.prefix}")
    private String d20FilePrefix;
    @Value("${batch.d20.appid}")
    private String d20AppId;
    @Value("${batch.d20.time.cronexpr}")
    private String d20Start;    
    
    @Value("${batch.d30.dir.read}")
    private String d30DirRead;
    @Value("${batch.d30.file.prefix}")
    private String d30FilePrefix;
    @Value("${batch.d30.appid}")
    private String d30AppId;
    @Value("${batch.d30.time.cronexpr}")
    private String d30Start;    

    @Value("${batch.d40.dir.read}")
    private String d40DirRead;
    @Value("${batch.d40.file.prefix}")
    private String d40FilePrefix;
    @Value("${batch.d40.appid}")
    private String d40AppId;
    @Value("${batch.d40.time.cronexpr}")
    private String d40Start;    

    @Value("${batch.d50.dir.read}")
    private String d50DirRead;
    @Value("${batch.d50.file.prefix}")
    private String d50FilePrefix;
    @Value("${batch.d50.appid}")
    private String d50AppId;
	@Value("${batch.d50.time.cronexpr}")
	private String d50Start;    

    @Value("${batch.b20.dir.read}")
    private String b20DirRead;
    @Value("${batch.b20.file.prefix}")
    private String b20FilePrefix;
    @Value("${batch.b20.appid}")
    private String b20AppId;
    @Value("${batch.b20.time.cronexpr}")
    private String b20Start;    

    @Value("${batch.cms.dir.read}")
    private String cmsDirRead;
    @Value("${batch.cms.file.prefix}")
    private String cmsFilePrefix;
    @Value("${batch.cms.appid}")
    private String cmsAppId;
	@Value("${batch.cms.time.cronexpr}")
	private String cmsStart;    

	@Value("${batch.backup.dir}")
	private String backupDir;    
	@Value("${file.remain.days}")
	private int remainDays;    
	
    @Value("${batch.sender.id}")
	private String senderId;
    @Value("${batch.service.code}")
	private String serviceCode;
	
	@Value("${delete.data.after.days}")
	int dataDays;    
	@Value("${delete.time.cronexpr}")
	private String deleteTime;    

	@Value("${delete.partition.months}")
	int partitionMonths;    
	@Value("${delete.partition.cronexpr}")
	private String dropTime;    

    @Value("${dw.target.dir}")
    private String dwTargetDir;
    @Value("${dw.temp.dir}")
    private String dwTempDir;
    @Value("${dw.file.prefix}")
    private String dwPrefix;
	@Value("${dw.time.cronexpr}")
	private String dwTime;    

	@Value("${sms.admin.domain}")
	private String PUSH_ADMIN_DOMAIN;	
    
	@Override
	public int processD20() {
		String useBatch = options.getRunOption(OPT_USE_FILE);
		if(! "Y".equalsIgnoreCase(useBatch)) {
			logger.info("processD20 : useBatch = " + useBatch);
			return 0;
		}
		
		D20Layout layout = new D20Layout(repository);
		layout.setSenderId(Constants.TRANS_CODE_D20);
		layout.setServiceCode(serviceCode);
		layout.setAppId(d20AppId);
		
		return process(d20DirRead, d20FilePrefix, layout);
	}
	
	@Override
	public int processD30() {
		String useBatch = options.getRunOption(OPT_USE_FILE);
		if(! "Y".equalsIgnoreCase(useBatch)) {
			logger.info("processD30 : useBatch = " + useBatch);
			return 0;
		}

		D30Layout layout = new D30Layout(repository);
		layout.setSenderId(Constants.TRANS_CODE_D30);
		layout.setServiceCode(serviceCode);
		layout.setAppId(d30AppId);

		return process(d30DirRead, d30FilePrefix, layout);
	}

	@Override
	public int processD40() {
		String useBatch = options.getRunOption(OPT_USE_FILE);
		if(! "Y".equalsIgnoreCase(useBatch)) {
			logger.info("processD40 : useBatch = " + useBatch);
			return 0;
		}

		D40Layout layout = new D40Layout(repository);
		layout.setSenderId(Constants.TRANS_CODE_D40);
		layout.setServiceCode(serviceCode);
		layout.setAppId(d40AppId);
		
		return process(d40DirRead, d40FilePrefix, layout);
	}

	@Override
	public int processD50() {
		String useBatch = options.getRunOption(OPT_USE_FILE);
		if(! "Y".equalsIgnoreCase(useBatch)) {
			logger.info("processD50 : useBatch = " + useBatch);
			return 0;
		}

		D50Layout layout = new D50Layout(repository);
		layout.setSenderId(Constants.TRANS_CODE_D50);
		layout.setServiceCode(serviceCode);
		layout.setAppId(d50AppId);
		
		return process(d50DirRead, d50FilePrefix, layout);
	}

	@Override
	public int processB20() {
		String useBatch = options.getRunOption(OPT_USE_FILE);
		if(! "Y".equalsIgnoreCase(useBatch)) {
			logger.info("processB20 : useBatch = " + useBatch);
			return 0;
		}

		B20Layout layout = new B20Layout(repository);
		layout.setSenderId(Constants.TRANS_CODE_B20);
		layout.setServiceCode(serviceCode);
		layout.setAppId(b20AppId);

		return process(b20DirRead, b20FilePrefix, layout);
	}
	
	@Override
	public int processCMS() {
		String useBatch = options.getRunOption(OPT_USE_FILE);
		if(! "Y".equalsIgnoreCase(useBatch)) {
			logger.info("processCMS : useBatch = " + useBatch);
			return 0;
		}

		CmsLayout layout = new CmsLayout(repository);
		layout.setSenderId(Constants.TRANS_CODE_CMS);
		layout.setServiceCode(serviceCode);
		layout.setAppId(cmsAppId);

		return process(cmsDirRead, cmsFilePrefix, layout);
	}
	
	private int process(String dir, final String prefix, BatchLayoutBase layout) {
		layout.setOptionReader(options);
		
		File path = new File(dir);
		File[] files = path.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.isFile() && pathname.getName().startsWith(prefix))
					return true;
				
				return false;
			}	
		});
		
		
		int result = 0;
		if(files != null && files.length > 0) {
			for(int i = 0; i < files.length; i++) {
				if(repository.selectBatchFileCount(files[i].getName()) > 0) {
					logger.info(String.format("process : file : %s, already processed.", files[i].getName()));
//					deleteFile(files[i]);
					moveFile(files[i], new File(backupDir));
					continue;
				}
				
				Date start = new Date();
				
				logger.info(String.format("process : file : %s, setFile().", files[i].getName()));
				layout.setFile(files[i]);
				try {
					logger.info("process : layout.save() start.");
					if(layout.save()) {
						result++;
						logger.info(String.format("process : file : %s, layout.save() = success.", files[i].getName()));
//						deleteFile(files[i]);
					}
					else {
						logger.error(String.format("process : file : %s, layout.save() = failed.", files[i].getName()));
						repository.saveFailedFileInfo(layout);
						
						result = layout.rollback(layout.getBatchId());
						logger.info(String.format("process : batchId = %s, rollback() : result = %d", layout.getBatchId(), result));
					}
					
					moveFile(files[i], new File(backupDir));
				} 
				catch (Exception e) {
					logger.error(String.format("process : file : %s, layout.save() = failed.", files[i].getName()), e);
					repository.saveFailedFileInfo(layout);
					
					result = layout.rollback(layout.getBatchId());
					logger.info(String.format("process : batchId = %s, rollback() : result = %d", layout.getBatchId(), result));

					moveFile(files[i], new File(backupDir));
				}
				
				Date finish = new Date();
				repository.saveBatchProcessLog(layout, dir, String.valueOf(result), start, finish);
			}
		}
		
		return result;	
	}
	
	@Override
	public int deleteOldData() {
		int total = 0;
		
		String useDelete = options.getRunOption(OPT_USE_DEL);
		if("Y".equalsIgnoreCase(useDelete)) {
			int result = repository.deleteOldData(dataDays);
			if(result > 0) {
				logger.info(String.format("deleteOldData : over %d days : result = %d", dataDays, result));
				total += result;
			}
			
			List<String> canceledIds = repository.getCanceledBatch();
			if(canceledIds != null && ! canceledIds.isEmpty()) {
				result = repository.deleteCanceledBatchMsgs(canceledIds);
				if(result > 0) {
					logger.info(String.format("deleteCanceledBatchMsgs : result = %d", result));
					total += result;
				}
			}
		}
		else {
			logger.info("deleteOldData : useDelete = " + useDelete);
		}

//		deleteLogFiles(logDays, logPath, logFileFormat);
		
		deleteOldFiles(backupDir, null, remainDays);
		deleteOldFiles(dwTargetDir, dwPrefix, remainDays);
		
		return total;
	}

	@Override
	public int dropPartition() {
		return repository.dropPartition(partitionMonths);
	}
	
//	private void deleteLogFiles(int days, String path, String fileFormat) {
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DATE, -days);
//		int start = fileFormat.indexOf("{");
//		int end = fileFormat.indexOf("}");
//		if(start == -1 || end == -1 || start >= end) {
//			logger.error("deleteLogFiles : file format not correct.");
//			return;
//		}
//		String format = fileFormat.substring(start + 1, end);
//		String date = Utils.dateToStr(cal.getTime(), format);		
//		final String ref = fileFormat.replace("{" + format + "}", date);
//		
//		File dir = new File(path);
//		File[] files = dir.listFiles(new FileFilter() {
//			@Override
//			public boolean accept(File file) {
//				if(! file.isDirectory() 
//					&& file.getName().length() == ref.length()
//					&& file.getName().compareTo(ref) <= 0
//				) {
//					return true;
//				}
//				
//				return false;
//			}
//		});
//		
//		if(files != null && files.length > 0) {
//			for(File f : files) {
//				deleteFile(f);
//			}
//		}
//	}
	
	private void deleteFile(File file) {
		try {
			boolean result = Files.deleteIfExists(file.toPath());
			if(result) {
				logger.info(String.format("deleteFile : file : %s : removed.", file.getName()));
			}
			else {
				logger.info(String.format("deleteFile : file : %s : not removed.", file.getName()));
			}
		}
		catch(Exception e) {
			logger.error(String.format("deleteFile : file : %s : not removed. (%s, %s)", file.getName(), e.getClass().getName(), e.getMessage()));
		}
	}

	private void moveFile(File file, File path) {
		logger.debug(String.format("moveFile : path = %s", path.getAbsolutePath()));
		if(! path.exists()) {
			try {
				boolean result = path.mkdirs();
				logger.debug("moveFile : mkdir = " + result);
			}
			catch(Exception e) {
				logger.warn(String.format("moveFile : path.mkdirs() : %s : %s", e.getClass().getName(), e.getMessage()));
			}
		}
		
		Path target = Paths.get(path.getAbsolutePath() + File.separator + file.getName());
		try {
			logger.debug("moveFile : target = " + target.toString());
			Files.move(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
		} 
		catch (Exception e) {
			logger.warn(String.format("moveFile : %s : %s", e.getClass().getName(), e.getMessage()));
		}
	}
	
	@Override
	public int processDW(String date) {
		String useDW = options.getRunOption(OPT_USE_DW);
		if(! "Y".equalsIgnoreCase(useDW)) {
			logger.info("processDW : useDW = " + useDW);
			return 0;
		}
		
		int result = 0;
		
		if(Utils.isEmpty(date)) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			date = Utils.dateToStr(cal.getTime(), Utils.FRMT_DATE);
		}
		
		DWLayout layout = new DWLayout(dwTargetDir, dwTempDir, dwPrefix, repository);
		if(layout.createFile(date)) {
			result = layout.save();
			layout.moveFileAndCleanUp(result);
		}
		else {
			result = -1;
		}
						
		return result;
	}
	
	@Override
	public int replaceSMS() {
		String useSms = options.getRunOption(OPT_USE_SMS);
		if(! "Y".equalsIgnoreCase(useSms)) {
			logger.info("replaceSMS : useSms = " + useSms);
			return 0;
		}
		
		int result = Result.SUCCESS;
		
		List<Map<String, String>> SmsFailList = repository.selectFailSmsList();
		//logger.info("SmsFailList.size() : {}", SmsFailList.size());
		//T_PUSH_BATCHJOB_LOG 리스트  (result가 0인 리스트)
		List<Map<String, String>> batchJoblogList = repository.selectBatchJobLogList();
		
		JsonArray jsonArray = new JsonArray();
		
		// 관리자화면에서 세팅한 SMS발송설정을 가져온다.
		Map<String, Object> selectSmsTimeErrList = repository.selectSmsTimeErrList();
		
		String allowYN = String.valueOf(selectSmsTimeErrList.get("ALLOW_YN")); // 관리자에서 설정한 사용여부
		String timeCnt = String.valueOf(selectSmsTimeErrList.get("TIME_CNT")); // 관리자에서 설정한 시간
		String errCnt = String.valueOf(selectSmsTimeErrList.get("ERR_CNT"));   // 관리자에서 설정한 에러건수
		
		//logger.info("replaceSMS errCnt  :  " + errCnt);  // 관리자에게 SMS발송을 위해 에러 체크 (T_PUSH_FAIL 테이블의 에러 카운트)
		//logger.info("replaceSMS timeCnt :  " + timeCnt);
		//logger.info("replaceSMS allowYN :  " + allowYN);
		int smsErrCnt = repository.selectErrCnt(timeCnt); //관리자에서 설정한 시간을 기준으로 발생한 에러건수를 가져온다.
		int adminSendSms = 0;
		if("Y".equalsIgnoreCase(allowYN)) { 			// 관리자 SMS 발송 사용상태 일 경우(Y:사용, N:미사용)
			if(smsErrCnt >= Integer.parseInt(errCnt)) { // 발생 에러건수가 관리자설정 에러건수보다 크면
				adminSendSms = 1;						// adminSendSms = 1 이면 Admin에게 발송준비 완료 
			}
		}
		if(SmsFailList.size() > 0 || adminSendSms == 1  || batchJoblogList.size() > 0) {
			//관리자 SMS발송
			if(adminSendSms == 1) {
				List<Map<String, String>> selectSmsPhoneList = repository.selectSmsPhoneList(); //관리자 권한 그룹 '0000000004' 인 대상
				Map<String, String> dupMap = new HashMap<String, String>();
				for (Map<String, String> map : selectSmsPhoneList) {
					if(map.get("USER_TP") != null && map.get("USE_YN").equals("Y")) {
						dupMap.put("CUID",  map.get("USER_ID").trim());
						dupMap.put("timeCnt",  timeCnt);
						int smsDupCheck = repository.smsDupCheck(dupMap); // 대상자 발송체크
						JsonObject object = new JsonObject();
					
						if(smsDupCheck == 0) { //T_PUSH_SMS에 발송이력이 없으면
							object.addProperty("USER_PHONE", map.get("USER_TP").trim());
							object.addProperty("CUID", map.get("USER_ID").trim());
							object.addProperty("USER_MSG", "PUSH시스템에 지속적인 오류가 발생하고 있습니다. 시스템점검 하시기 바랍니다.");
							object.addProperty("adminCheck", "Y");
							jsonArray.add(object);                                                                             
						}
					}
				}
			}
			
			//대체발송
			if(SmsFailList.size() > 0) {
				for (Map<String, String> map : SmsFailList) {
					JsonObject object = new JsonObject();
					object.addProperty("USER_PHONE",  map.get("TEL_NO").trim());
					object.addProperty("USER_MSG",  map.get("MESSAGE").trim());
					
					//로그에 적재할 정보s
					object.addProperty("PUSH_SEQNO",  String.valueOf(map.get("PUSH_SEQNO")));
					object.addProperty("RESULTCODE",  map.get("RESULTCODE"));
					object.addProperty("RESULTMSG",  map.get("RESULTMSG"));
					object.addProperty("SUB_TITLE",  map.get("SUB_TITLE"));
					object.addProperty("APPID",  map.get("APP_ID"));
					object.addProperty("CUID",  map.get("CUID"));
					object.addProperty("replaceSmsCheck",  "Y");
					//로그에 적재할 정보e					
					jsonArray.add(object);
				};
			}
	
			//batch sms 발송
			if(batchJoblogList.size() > 0) {
				List<Map<String, String>> selectSmsPhoneList = repository.selectSmsPhoneList(); //관리자 권한 그룹 '0000000004' 인 대상
				Map<String, String> dupMap = new HashMap<String, String>();
				
				String seqNo = String.valueOf(batchJoblogList.get(0).get("SEQNO"));
				//String fileName = batchJoblogList.get(0).get("FILE_NAME");
				//관리자들에게 sms보낼 정보 세팅
				for (Map<String, String> map : selectSmsPhoneList) {
					if(map.get("USER_TP") != null && map.get("USE_YN").equals("Y")) {
						dupMap.put("seqNo",  seqNo);
						int batchSmsDupCheck = repository.batchSmsDupCheck(dupMap); // 대상자 발송체크
						JsonObject object = new JsonObject();
						//logger.info("batchSmsDupCheck  : " + batchSmsDupCheck);
						if(batchSmsDupCheck == 0) { //T_PUSH_BATCHJOB_LOG 체크 							
							object.addProperty("USER_PHONE", map.get("USER_TP").trim());
							object.addProperty("USER_MSG", "배치파일이 정상적으로 업로드 되지 않았습니다. 확인하시기 바랍니다.");
							
							object.addProperty("CUID", map.get("USER_ID").trim());
							object.addProperty("PUSH_SEQNO", seqNo);
							object.addProperty("batchJobLogCheck", "Y");
							
							jsonArray.add(object);                                                                             
						}
					}
				}
			}
			
			//connect
			try {
				String smsUrl = PUSH_ADMIN_DOMAIN + "/wcpush/interface/sendSMSMap";
				
				URL url = new URL(smsUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Cache-Control", "no-cache");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Accept", "application/json");
				//data setting
				String param = jsonArray.toString();
				//request
				OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
				os.write(param);
				os.flush();
				int HttpResult = conn.getResponseCode();
				//response
				if(HttpResult == HttpURLConnection.HTTP_OK) {
					BufferedReader br  = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));	
					StringBuilder sb = new StringBuilder();
					String line;
					while((line = br.readLine()) != null) {
						sb.append(line);
					}
					br.close();
					
					if(sb.toString().equals("Y")) {
						if(SmsFailList.size() > 0) {
							for (Map<String, String> map : SmsFailList) {
								String pushSeqno = String.valueOf(map.get("PUSH_SEQNO"));
								//logger.info("pushSeqno : " + pushSeqno);
								result = repository.updateSmsSent(pushSeqno);
								if(result == 1) {
									logger.info(" ============= ReplaceSMS Send success. ( PUSH_SEQNO : {} ) ", pushSeqno);
								}else {
									logger.info(" ============= ReplaceSMS Send fail. ( PUSH_SEQNO : {} ) ", pushSeqno);
								}
							}
						}
						logger.info("Successful Send SMS.");
					}else {
						//result = Result.ERR_DATA_NOT_FOUND;
					}
				}else {
					result = Result.ERR_NETWORK;
				}
				
			} catch (Exception e) {
				result = Result.ERR_SYSTEM;
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public void saveBatchStatus() {
		String useBatch = options.getRunOption(OPT_USE_FILE);
		if("Y".equalsIgnoreCase(useBatch)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("code", Constants.TRANS_CODE_D20);
			map.put("dir", d20DirRead);
			map.put("prefix", d20FilePrefix);
			map.put("time", d20Start);
			
			repository.updateBatchStatus(map);
			
			map = new HashMap<String, String>();
			map.put("code", Constants.TRANS_CODE_D30);
			map.put("dir", d30DirRead);
			map.put("prefix", d30FilePrefix);
			map.put("time", d30Start);
			
			repository.updateBatchStatus(map);
			
			map = new HashMap<String, String>();
			map.put("code", Constants.TRANS_CODE_D40);
			map.put("dir", d40DirRead);
			map.put("prefix", d40FilePrefix);
			map.put("time", d40Start);
			
			repository.updateBatchStatus(map);
			
			map = new HashMap<String, String>();
			map.put("code", Constants.TRANS_CODE_D50);
			map.put("dir", d50DirRead);
			map.put("prefix", d50FilePrefix);
			map.put("time", d50Start);
			
			repository.updateBatchStatus(map);
			
			map = new HashMap<String, String>();
			map.put("code", Constants.TRANS_CODE_B20);
			map.put("dir", b20DirRead);
			map.put("prefix", b20FilePrefix);
			map.put("time", b20Start);
			
			repository.updateBatchStatus(map);
			
			map = new HashMap<String, String>();
			map.put("code", Constants.TRANS_CODE_CMS);
			map.put("dir", cmsDirRead);
			map.put("prefix", cmsFilePrefix);
			map.put("time", cmsStart);
			
			repository.updateBatchStatus(map);			
		}
		
		String useDW = options.getRunOption(OPT_USE_DW);
		if("Y".equalsIgnoreCase(useDW)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("code", "DW");
			map.put("dir", dwTargetDir);
			map.put("prefix", dwPrefix);
			map.put("time", dwTime);
			
			repository.updateBatchStatus(map);
		}
		
		String useDelete = options.getRunOption(OPT_USE_DEL);
		if("Y".equalsIgnoreCase(useDelete)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("code", "DelOldData");
			map.put("dir", "");
			map.put("prefix", "");
			map.put("time", deleteTime);
			
			repository.updateBatchStatus(map);
		}
	}

	@Override
	public int applySendAllowedTime() {
		return repository.applySendAllowedTime();
	}	
	
	private void deleteOldFiles(String dir, final String prefix, int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -days);
		final FileTime threshold = FileTime.fromMillis(cal.getTimeInMillis());

		File path = new File(dir);
		if(! path.exists()) {
			path.mkdirs();
		}

		File[] files = path.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if(! f.isFile()) {
					return false;
				}
				
				if(! Utils.isEmpty(prefix) && ! f.getName().startsWith(prefix)) {
					return false;
				}
				
				boolean result = false;
				FileTime time;
				try {
					time = Files.getLastModifiedTime(f.toPath());
					result = (time.compareTo(threshold) < 0);
				} 
				catch (IOException e) {
				}

				return result;
			}
		});
		
		if(files != null && files.length > 0) {
			for(File file : files) {
				deleteFile(file);
			}
		}
	}
}
