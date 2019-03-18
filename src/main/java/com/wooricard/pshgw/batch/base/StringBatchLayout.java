package com.wooricard.pshgw.batch.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.wooricard.pshgw.batch.repository.BatchRepository;

public abstract class StringBatchLayout extends BatchLayoutBase {

	protected List<HashMap<String, Object>> items;
	
	public StringBatchLayout(String transCode, BatchRepository repository, Logger logger) {
		super(transCode, repository, logger);
		
		items = new ArrayList<HashMap<String, Object>>();
	}
	
	@Override
	public void setFile(File file) {
		super.setFile(file);
		items.clear();
	}

	@Override
	public boolean save() throws IOException {
		items.clear();
		
		String line = null; 
		BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()), Charset.forName("EUC-KR"));
		line = reader.readLine();
		if(line == null || (header = parseHeader(line)) == null) {
			logger.debug("save : line = " + line);
			logger.error("save : line == null || (header = parseHeader()) == null");
			reader.close();
			return false;
		}
		
		long count = 0;
		while((line = reader.readLine()) != null) {
			if(! isFooter(line)) {
				HashMap<String, Object> item = parseItem(line);
				if(item != null) {
					items.add(item);
					count++;
					
					if(items.size() == INSERT_MAX_ITEMS) {
						long r = insertItems(repository, items);
						logger.debug("save : insertItems = " + r);
						items.clear();
					}
				}
				else {
					logger.debug("save : line = " + line);
					if(count > 0) {
						logger.info("save : parseItem() == null, count = " + count + " : bad record !!!");
					}
//					reader.close(); 
//					return false;
				}
				
				printProcessLog(count);
			}
			else {
				break;
			}
		}
		
		if(! items.isEmpty()) {
			count = insertItems(repository, items);
		}
		
		logger.info("save : count = " + count);
		
		if(! isTotalCountCorrect(line, count)) {
			logger.warn("save : Some records dropped. (total != loaded)");
		}
		
		insertHeader(repository, header);

		reader.close();

		return true;
	}
	
	protected abstract long insertItems(BatchRepository repository, List<HashMap<String, Object>> items);
	protected abstract Map<String, String> parseHeader(String str);
	protected abstract HashMap<String, Object> parseItem(String str);
	protected abstract boolean isFooter(String str);
	protected abstract boolean isTotalCountCorrect(String str, long count);		
}
