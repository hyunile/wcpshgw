package com.wooricard.pshgw.batch.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wooricard.pshgw.batch.service.BatchService;


public class CMS extends QuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private BatchService batchService;
    private static boolean running;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("executeInternal : CMS JobBean started.");
        
        if(running) {
            logger.info("executeInternal : CMS is already running. finishing this process. ");
            return;
        }
        
        running = true;
        int result = 0;
        
        try {
        	result = batchService.processCMS();
        }
        catch(Exception e) {
        	logger.error("executeInternal : ", e);
        }
        
        logger.info("executeInternal : CMS finished. processed items = " + result);
        running = false;
    }

    public void setBatchService(BatchService batchService) {
        this.batchService = batchService;
    }
}
