package com.wooricard.pshgw.batch.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.wooricard.pshgw.batch.repository.BatchRepository;
import com.wooricard.pshgw.batch.repository.ResData;
import com.wooricard.pshgw.utils.Utils;

public abstract class ByteBatchLayout extends BatchLayoutBase {

	protected static byte TYPE_HEADER 	= '1';
	protected static byte TYPE_BODY 		= '2';
	protected static byte TYPE_FOOTER 	= '3';
	
	protected List<HashMap<String, Object>> items;
	
	private int headerItemSize;
	private int bodyItemSize;
	private int footerItemSize;
	
	protected ResData res;
	
	public ByteBatchLayout(String transCode, BatchRepository repository, int[] header, int[] body, int[] footer, Logger logger) {
		super(transCode, repository, logger);
		
		items = new ArrayList<HashMap<String, Object>>();

		headerItemSize = sum(header);
		bodyItemSize = sum(body);
		footerItemSize = sum(footer);
		
		res = repository.getResInfo(transCode);
	}
	
	@Override
	public void setFile(File file) {
		super.setFile(file);
		items.clear();
	}

	@Override
	public boolean save() throws IOException {
		items.clear();
		
		FileInputStream fis = new FileInputStream(file);
		FileChannel ch = fis.getChannel();
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(headerItemSize);
		int read = ch.read(buffer);
		if(read != headerItemSize) {
			logger.error("save : read != headerItemSize");
			ch.close();
			fis.close();
			return false;
		}
	
		buffer.flip();
		byte[] array = new byte[headerItemSize];
		buffer.get(array);
		header = parseHeader(array);
		if(header == null) {
			logger.error("save : header == null");
			ch.close();
			fis.close();
			return false;
		}
		
		int times = MAX_BUFFER_SIZE / bodyItemSize;
		array = new byte[bodyItemSize];
		buffer = ByteBuffer.allocateDirect(bodyItemSize * times);
		
		int size;
		long count = 0;
		
		while((read = ch.read(buffer)) > 0) {
			buffer.flip();
			
			while(buffer.hasRemaining()) {
				size = Math.min(buffer.remaining(), bodyItemSize);
				buffer.get(array, 0, size);
				
				if(! isFooter(array)) {
					HashMap<String, Object> item = parseItem(array);
					if(item != null) {
						items.add(item);
						count++;

						if(items.size() == INSERT_MAX_ITEMS) {
							insertItems(repository, items);
							items.clear();
						}
					}
					else {
						logger.error("save :  parseItem() == null, count = " + count + " : bad record !!!");
					}
					
					printProcessLog(count);
				}
				else {
					if(! items.isEmpty()) {
						insertItems(repository, items);
						items.clear();
					}
					
					if(bodyItemSize < footerItemSize) {
						int diff = footerItemSize - bodyItemSize;
						if(buffer.hasRemaining()) {
							size =  Math.min(buffer.remaining(), diff);
							byte[] subArray = new byte[size];
							buffer.get(subArray);
							array = Utils.mergeArray(array, subArray);
						}
						
						if(size < diff) {
							buffer.clear();
							read = ch.read(buffer);
							if(diff - size < read) {
								byte[] subArray = new byte[diff - size];
								buffer.get(subArray);
								array = Utils.mergeArray(array, subArray);
							}
						}
					}

					break;
				}
			}
			
			buffer.clear();
		}
		
		if(! items.isEmpty()) {
			try {
				insertItems(repository, items);
				items.clear();
			}
			catch(Exception e) {
				count -= items.size();
			}
		}
		
		logger.info("save : count = " + count);
		
		if(! isTotalCountCorrect(array, count)) {
			logger.warn("save : Some records dropped. (total != loaded)");
		}
		
		insertHeader(repository, header);
		
		ch.close();
		fis.close();

		return true;
	}
		
	private int sum(int[] array) {
		int total = 0;

		for(int i = 0; i < array.length; i++)
			total += array[i];
		
		return total;
	}
	
	protected abstract long insertItems(BatchRepository repository, List<HashMap<String, Object>> items);
	protected abstract Map<String, String> parseHeader(byte[] array);
	protected abstract HashMap<String, Object> parseItem(byte[] array);
	protected abstract boolean isFooter(byte[] array);
	protected abstract boolean isTotalCountCorrect(byte[] array, long count);	
}
