package com.wooricard.pshgw.batch.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wooricard.pshgw.batch.service.BatchService;


public class AllowedTimeController extends QuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private BatchService batchService;
    private static boolean running;
    private static int count;
    
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    	count++;
    	
    	if(count % 10 == 0) {
    		logger.info("executeInternal : TimeController JobBean started.");
    	}
    	
        if(running) {
            logger.info("executeInternal : TimeController is already running. finishing this process. ");
            return;
        }
        
        running = true;
        int result = 0;
        
        try {
        	result = batchService.applySendAllowedTime();
        }
        catch(Exception e) {
        	logger.error("executeInternal : ", e);
        }
        
    	if(count % 10 == 0 || result > 0) {
    		logger.info("executeInternal : TimeController finished. result = " + result);
    	}
    	
        running = false;
    }

    public void setBatchService(BatchService batchService) {
        this.batchService = batchService;
    }
}
