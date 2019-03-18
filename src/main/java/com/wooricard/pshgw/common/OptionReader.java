package com.wooricard.pshgw.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wooricard.pshgw.utils.Utils;

@Component
public class OptionReader {
	private static final Logger logger = LoggerFactory.getLogger(OptionReader.class);
	
	private static final String PREFIX_RUN_OPTION		= "switch.";
	private static final String POSTFIX_RUN_OPTION		= ".run";
 static final String PREFIX_RESERVE_TIME 	= "time.";
	
	@Value("${batch.options.path}")
	private String optionFile;
	
	@Value("${settings.properties.path}")
	private String settingsFile;

	private FileTime optionTime;
	private FileTime settingsTime;
	
	private Map<String, String> optionMap;
	private Map<String, String> settingsMap;
	
	
	@PostConstruct
	public void init() {
		optionMap = new HashMap<String, String>();
		settingsMap = new HashMap<String, String>();
		
		loadOptionFile();
		loadSettingsFile();
	}
	
	public String getRunOption(String type) {
		String key = PREFIX_RUN_OPTION + type + POSTFIX_RUN_OPTION;
		return findSettingsValue(key);
	}
	
	public String getReserveTime(String type) {
		String key = PREFIX_RESERVE_TIME + type.toLowerCase();
		return findOptionsValue(key);
	}
	
	private String findSettingsValue(String key) {
		String result = null;
		synchronized(settingsMap) {
			if(loadSettingsFile()) {
				result = settingsMap.get(key);
			}
		}
		return result;
	}
	
	private String findOptionsValue(String key) {
		String result = null;
		synchronized(optionMap) {
			if(loadOptionFile()) {
				result = optionMap.get(key);
			}
		}
		return result;
	}
	
	private boolean loadOptionFile() {		
		if(Utils.isEmpty(optionFile)) {
			logger.warn("loadOptionFile : pathFile is empty !!");
			return false;
		}
		
		File file = new File(optionFile);
		if(! file.exists() || ! file.isFile()) {
			logger.warn("loadOptionFile : ! file.exists() || ! file.isFile()");
			return false;
		}
		
		if(optionTime != null) {
			try {
				FileTime newTime = Files.getLastModifiedTime(file.toPath());
				if(optionTime.compareTo(newTime) < 0) {
					optionTime = newTime;
					logger.info("loadOptionFile : file modified !!");
				}
				else {
					return true;
				}
			} 
			catch (IOException e) {
				logger.warn(String.format("loadOptionFile : %s : %s", e.getClass().getName(), e.getMessage()));
				return false;
			}
		}
		else {
			try {
				optionTime = Files.getLastModifiedTime(file.toPath());
			} 
			catch (IOException e) {
			}
		}
		
		optionMap.clear();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null) {
				if(Utils.isEmpty(line) || line.startsWith("#"))
					continue;
			
				int idx = line.indexOf("=");
				if(idx < 1)
					continue;
				
				optionMap.put(line.substring(0, idx), idx == line.length() - 1 ? "" : line.substring(idx + 1));
			}
			
			reader.close();
			
			return true;
		} 
		catch (FileNotFoundException e) {
		} 
		catch (IOException e) {
		}
		finally {
			if(reader != null) {
				try {
					reader.close();
				} 
				catch (IOException e) {
				}
			}
		}
		
		return false;
	}
	
	private boolean loadSettingsFile() {		
		if(Utils.isEmpty(settingsFile)) {
			logger.warn("loadSettingsFile : settingsFile is empty !!");
			return false;
		}
		
		File file = new File(settingsFile);
		if(! file.exists() || ! file.isFile()) {
			logger.warn("loadSettingsFile : ! file.exists() || ! file.isFile()");
			return false;
		}
		
		if(settingsTime != null) {
			try {
				FileTime newTime = Files.getLastModifiedTime(file.toPath());
				if(settingsTime.compareTo(newTime) < 0) {
					settingsTime = newTime;
					logger.info("loadSettingsFile :  file modified !!");
				}
				else {
					return true;
				}
			} 
			catch (IOException e) {
				logger.warn(String.format("loadSettingsFile : %s : %s", e.getClass().getName(), e.getMessage()));
				return false;
			}
		}
		else {
			try {
				settingsTime = Files.getLastModifiedTime(file.toPath());
			} 
			catch (IOException e) {
			}
		}
		
		settingsMap.clear();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null) {
				if(Utils.isEmpty(line) || line.startsWith("#") || ! line.startsWith(PREFIX_RUN_OPTION)) {
					continue;
				}
			
				int idx = line.indexOf("=");
				if(idx < 1)
					continue;
				
				settingsMap.put(line.substring(0, idx), idx == line.length() - 1 ? "" : line.substring(idx + 1));
			}
			
			reader.close();
			
			return true;
		} 
		catch (FileNotFoundException e) {
		} 
		catch (IOException e) {
		}
		finally {
			if(reader != null) {
				try {
					reader.close();
				} 
				catch (IOException e) {
				}
			}
		}
		
		return false;
	}
}
