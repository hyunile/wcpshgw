package com.wooricard.pshgw.batch.layout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wooricard.pshgw.batch.repository.BatchRepository;
import com.wooricard.pshgw.common.Constants;
import com.wooricard.pshgw.utils.Utils;

public class DWLayout {
	private static final Logger logger = LoggerFactory.getLogger(DWLayout.class);

	private static byte TYPE_HEADER 	= '1';
	private static byte TYPE_BODY 	= '2';
	private static byte TYPE_FOOTER 	= '3';
	
	private static int FAILED		= -1;
	private static int STR_MAX	= 300;
	
	static String HEADER_DESCRIPTION = "PushResult";
	
	private static String[] BODY_ITEM_NAME = { 
		"CODE", 
		"SEQNO",
		"APPID",
		"CUID",
		"TYPE", 
		"TITLE", 
		"MESSAGE", 
		"EXT", 
		"RESULTCODE", 
		"RESULTMSG",
		"STATUS", 
		"LEGACYID",
		"REGDATE", 
		"READDATE" 
	};
	
	private static int[] HEADER_ITEM_SIZE = { 1, 8, 10 };
	private static int[] BODY_ITEM_SIZE = { 1, 15, 30, 50, 10, 255, 1000, 1000, 10, 100, 1, 15, 14, 14 };
	private static int[] FOOTER_ITEM_SIZE = { 1, 15 };

    private String targetDir;
    private String tempDir;
    private String filePrefix;
    
    private String date;
    private Date dateObj;
    
    private BatchRepository repository;
    
    private RandomAccessFile fos;
    private File file;
    private Charset charset;

    private static Calendar cal = Calendar.getInstance();
    
	public DWLayout(String targetDir, String tempDir, String filePrefix, BatchRepository repository) {
		this.targetDir = targetDir;
		this.tempDir = tempDir;
		this.filePrefix = filePrefix;
		this.repository = repository;
		
		charset = Charset.forName(Constants.CHARSET);
	}
	
	public boolean createFile(String date) {
		this.date = date;
		
		dateObj = Utils.strToDate(date);
		if(dateObj == null) {
			logger.error("createFile :  uncorrect date : date = " + date);
			return false;
		}
		
		file = new File(tempDir, filePrefix + "." + date);
		try {
			fos = new RandomAccessFile(file, "rw");
		} 
		catch (FileNotFoundException e) {
			logger.error(String.format("createFile : cannot create file. (%s)", file.getAbsolutePath()));
			return false;
		}
		
		return true;
	}
	
	public int save() {
		if(file == null) {
			logger.error(String.format("save : File not created."));
			return FAILED;
		}
		
//		int count = 0;
//		cal.setTime(dateObj);
//		cal.add(Calendar.DATE, 1);
//		String nextDate = Utils.dateToStr(cal.getTime(), Utils.FRMT_DATE);
//
//		try {
//			count = repository.getCompletedMessageCount(date, nextDate);
//		}
//		catch(Exception e) { 
//			logger.error("save : ", e); 
//			return FAILED;
//		}
		
		logger.info("save : date = " + date);
		
		FileChannel ch = null;
		ByteBuffer buffer = null;
		int result = 0;
		
		try {
			ch = fos.getChannel();
			buffer = ByteBuffer.allocateDirect(sum(BODY_ITEM_SIZE) + 1);
			
			if(! writeHeader(ch, buffer)) {
				logger.error(String.format("save : writeHeader() error."));
				return FAILED;
			}
				
			cal.setTime(dateObj);
			cal.add(Calendar.DATE, 1);
			String nextDate = Utils.dateToStr(cal.getTime(), Utils.FRMT_DATE);

			for(int i = 0; i < 24; i++) {
				String current = String.format("%s%02d", date, i);
				String next = String.format("%s%02d", date, i + 1);
				if(i == 23) {
					next = String.format("%s%02d", nextDate, 0);
				}
				
				List<Map<String, Object>> list = repository.getSingleCompletedMessagesByHour(current, next);
				logger.info(String.format("save (realtime): date = %s, time = %02d, count = %d", date, i, list.size()));
				result += writeBody(ch, buffer, list);		
				
				list = repository.getBatchCompletedMessagesByHour(current, next);
				logger.info(String.format("save (batch): date = %s, time = %02d, count = %d", date, i, list.size()));
				result += writeBody(ch, buffer, list);				
			}

			if(! writeTail(ch, buffer, result)) {
				logger.error(String.format("save : writeTail() error."));
				return FAILED;				
			}
			
			logger.info(String.format("save : total = %d", result));
		} 
		catch (Exception e) {
			logger.error("save : ", e);
			result = FAILED;
		}
		finally {
			if(ch != null) {
				try {
					ch.close();
				} 
				catch (IOException e) {
				}
			}
		} 
				
		return result;		
	}
	
	public boolean moveFileAndCleanUp(int result) {
		try {
			fos.close();
		} 
		catch (IOException e) {
		}
		
		Path newFile = null;
		
		if(result == FAILED) {
			try {
				Files.delete(file.toPath());
				logger.info("moveFileAndCleanUp : deleted : file = " + file.getAbsolutePath());
			}
			catch (IOException e) {
				logger.error(String.format("moveFileAndCleanUp : delete failed : file = %s, err = %s",
						file.getAbsolutePath(), e.getMessage()));
			}
		}
		else {
			try {
				Path target = Paths.get(targetDir + File.separator + file.getName());
				newFile = Files.move(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
//				String cmd = "chmod 660 " + newFile.toFile().getAbsolutePath();
//				logger.info("moveFileAndCleanUp : cmd = " + cmd);
//				shellCmd(cmd);
			} 
			catch (IOException e) {
				logger.error("moveFileAndCleanUp : ", e);
				return false;
			}
			
			logger.info("moveFileAndCleanUp : completed & moved : file = " + newFile.getFileName().toString());
		}
				
		return true;
	}

	private boolean writeHeader(FileChannel ch, ByteBuffer buffer) {
		buffer.clear();
		buffer.put(TYPE_HEADER);
		
		int len = Math.min(HEADER_ITEM_SIZE[1], date.length());
		buffer.put(date.getBytes(), 0, len);
		skipByBlank(buffer, HEADER_ITEM_SIZE[1], len);
		
		len = Math.min(HEADER_ITEM_SIZE[2], HEADER_DESCRIPTION.length());
		buffer.put(HEADER_DESCRIPTION.getBytes(), 0, len);
		skipByBlank(buffer, HEADER_ITEM_SIZE[2], len);
		
		buffer.put((byte) '\n');	
		buffer.flip();
		
		try {
			ch.write(buffer);
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private int writeBody(FileChannel ch, ByteBuffer buffer, List<Map<String, Object>> list) {
		if(list == null || list.isEmpty()) {
			return 0;
		}
		
		int len = 0;
		int result = 0;
		
		for(Map<String, Object> map : list) {
			buffer.clear();
			buffer.put(TYPE_BODY);
						
			for(int i = 1; i < BODY_ITEM_NAME.length; i++) {
				Object obj = map.get(BODY_ITEM_NAME[i]);
				if(obj != null) {
					String str = convertObjToStr(BODY_ITEM_NAME[i], obj);
					if(str != null) {
						str = str.replace("\n", " ");
						
						if(("MESSAGE".equals(BODY_ITEM_NAME[i]) || "EXT".equals(BODY_ITEM_NAME[i])) && str.length() > STR_MAX) {
							str = str.substring(0, STR_MAX);
						}
						
						byte[] temp = str.getBytes(charset);
						len = Math.min(BODY_ITEM_SIZE[i], temp.length);
						buffer.put(temp, 0, len);
						skipByBlank(buffer, BODY_ITEM_SIZE[i], len);
					}
				}
			}
			
			buffer.put((byte) '\n');
			buffer.flip();
			
			try {
				ch.write(buffer);
				result++;
			} 
			catch (IOException e) {
				logger.warn("writeBody : " + e.getClass().toString() + " : " + e.getMessage());
			}
		}
			
		return result;
	}

	private boolean writeTail(FileChannel ch, ByteBuffer buffer, int result) {
		buffer.clear();
		buffer.put(TYPE_FOOTER);
		
		int len = FOOTER_ITEM_SIZE[1];
		String count = String.format("%0" + len + "d", result);
		buffer.put(count.getBytes(), 0, len);
		
		buffer.put((byte) '\n');		
		buffer.flip();
		
		try {
			ch.write(buffer);
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	private String convertObjToStr(String name, Object obj) {
		String result = null;
		if("regDate".equalsIgnoreCase(name) || "readDate".equalsIgnoreCase(name)) {
			Date date = (Date) obj;
			result = Utils.dateToStr(date, Utils.FRMT_DATETIME);
		}
		else if("status".equalsIgnoreCase(name)) {
			BigDecimal status = (BigDecimal) obj;
			result = String.valueOf(status.intValue());
		}
		else {
			result = (String) obj;
		}
		
		return result;
	}
	
	private int sum(int[] size) {
		int total = 0;
		for(int i = 0; i < size.length; i++) {
			total += size[i];
		}
		
		return total;
	}
	
	private int skipByBlank(ByteBuffer buffer, int fieldSize, int writtenSize) {
		int gap = fieldSize - writtenSize;
		if(gap <= 0) {
			return 0;
		}
		
		for(int i = 0; i < gap; i++) {
			buffer.put((byte) ' ');
		}
		
		return gap;
	}
	
	private void shellCmd(String cmd) {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec(cmd);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while((line = reader.readLine()) != null) {
				logger.info("shellCmd : " + line);
			}
		} 
		catch (IOException e) {
			logger.error(String.format("shellCmd : cmd = %s : %s : %s", cmd, e.getClass().getName(), e.getMessage()));
		}
	}
}
