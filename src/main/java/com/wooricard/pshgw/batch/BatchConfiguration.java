package com.wooricard.pshgw.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.wooricard.pshgw.batch.job.B20;
import com.wooricard.pshgw.batch.job.CMS;
import com.wooricard.pshgw.batch.job.D20;
import com.wooricard.pshgw.batch.job.D30;
import com.wooricard.pshgw.batch.job.D40;
import com.wooricard.pshgw.batch.job.D50;
import com.wooricard.pshgw.batch.job.DW;
import com.wooricard.pshgw.batch.job.DeleteOldData;
import com.wooricard.pshgw.batch.job.DropPartition;
import com.wooricard.pshgw.batch.job.ReplaceSMS;
import com.wooricard.pshgw.batch.job.AllowedTimeController;
import com.wooricard.pshgw.batch.service.BatchService;

@Configuration
public class BatchConfiguration {

	private static final String allowedTimeControlTime = "0/2 * * * * ?";    

///////////////////////////////////////////////////////////////////
// Common
	
	@Autowired
	private BatchService batchServiceImpl;
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setTriggers(
			D20JobCronTrigger().getObject(),
			D30JobCronTrigger().getObject(),
			D40JobCronTrigger().getObject(),
			D50JobCronTrigger().getObject(),
			B20JobCronTrigger().getObject(),
			CmsJobCronTrigger().getObject(),
			DeleteOldDataJobCronTrigger().getObject(),
			DropPartitionJobCronTrigger().getObject(),
			DWJobCronTrigger().getObject(),
			AllowedTimeControllerCronTrigger().getObject(),
			ReplaceSmsJobCronTrigger().getObject()
		);
		
		batchServiceImpl.saveBatchStatus();
		
		return scheduler;
	}	
	
////////////////////////////////////////////////////////////////////
// D20
	
    @Value("${batch.d20.time.cronexpr}")
    private String d20Start;    
	
	@Bean
	public CronTriggerFactoryBean D20JobCronTrigger() {
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(D20Job().getObject());
		stFactory.setStartDelay(5000);
		stFactory.setCronExpression(d20Start);
		
		return stFactory;
	}
	
	@Bean
	public JobDetailFactoryBean D20Job() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		
		factory.setJobClass(D20.class);
		factory.setApplicationContextJobDataKey("applicationContext");
		factory.setDurability(true);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchService", batchServiceImpl);
		factory.setJobDataAsMap(map);
		
		return factory;
	}
	
///////////////////////////////////////////////////////////////////
// D30
	
    @Value("${batch.d30.time.cronexpr}")
    private String d30Start;    
 	
	@Bean
	public CronTriggerFactoryBean D30JobCronTrigger() {
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(D30Job().getObject());
		stFactory.setStartDelay(5000);
		stFactory.setCronExpression(d30Start);
		
		return stFactory;
	}
	
	@Bean
	public JobDetailFactoryBean D30Job() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		
		factory.setJobClass(D30.class);
		factory.setApplicationContextJobDataKey("applicationContext");
		factory.setDurability(true);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchService", batchServiceImpl);
		factory.setJobDataAsMap(map);
		
		return factory;
	}

///////////////////////////////////////////////////////////////////
// D40
	
    @Value("${batch.d40.time.cronexpr}")
    private String d40Start;    
	
	@Bean
	public CronTriggerFactoryBean D40JobCronTrigger() {
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(D40Job().getObject());
		stFactory.setStartDelay(5000);
		stFactory.setCronExpression(d40Start);
		
		return stFactory;
	}
	
	@Bean
	public JobDetailFactoryBean D40Job() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		
		factory.setJobClass(D40.class);
		factory.setApplicationContextJobDataKey("applicationContext");
		factory.setDurability(true);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchService", batchServiceImpl);
		factory.setJobDataAsMap(map);
		
		return factory;
	}

///////////////////////////////////////////////////////////////////
// D50

	@Value("${batch.d50.time.cronexpr}")
	private String d50Start;    

	@Bean
	public CronTriggerFactoryBean D50JobCronTrigger() {
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(D50Job().getObject());
		stFactory.setStartDelay(5000);
		stFactory.setCronExpression(d50Start);

		return stFactory;
	}

	@Bean
	public JobDetailFactoryBean D50Job() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();

		factory.setJobClass(D50.class);
		factory.setApplicationContextJobDataKey("applicationContext");
		factory.setDurability(true);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchService", batchServiceImpl);
		factory.setJobDataAsMap(map);

		return factory;
	}

///////////////////////////////////////////////////////////////////
// B20
	
    @Value("${batch.b20.time.cronexpr}")
    private String b20Start;    
	
	@Bean
	public CronTriggerFactoryBean B20JobCronTrigger() {
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(B20Job().getObject());
		stFactory.setStartDelay(5000);
		stFactory.setCronExpression(b20Start);
		
		return stFactory;
	}
	
	@Bean
	public JobDetailFactoryBean B20Job() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		
		factory.setJobClass(B20.class);
		factory.setApplicationContextJobDataKey("applicationContext");
		factory.setDurability(true);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchService", batchServiceImpl);
		factory.setJobDataAsMap(map);
		
		return factory;
	}
	
///////////////////////////////////////////////////////////////////
// CMS

	@Value("${batch.cms.time.cronexpr}")
	private String cmsStart;    

	@Bean
	public CronTriggerFactoryBean CmsJobCronTrigger() {
		CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
		factory.setJobDetail(CmsJob().getObject());
		factory.setStartDelay(5000);
		factory.setCronExpression(cmsStart);

		return factory;
	}

	@Bean
	public JobDetailFactoryBean CmsJob() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();

		factory.setJobClass(CMS.class);
		factory.setApplicationContextJobDataKey("applicationContext");
		factory.setDurability(true);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchService", batchServiceImpl);
		factory.setJobDataAsMap(map);

		return factory;
	}	
	
///////////////////////////////////////////////////////////////////
// delete old data

	@Value("${delete.time.cronexpr}")
	private String deleteTime;    

	@Bean
	public CronTriggerFactoryBean DeleteOldDataJobCronTrigger() {
		CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
		factory.setJobDetail(DeleteOldDataJob().getObject());
		factory.setStartDelay(5000);
		factory.setCronExpression(deleteTime);

		return factory;
	}

	@Bean
	public JobDetailFactoryBean DeleteOldDataJob() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();

		factory.setJobClass(DeleteOldData.class);
		factory.setApplicationContextJobDataKey("applicationContext");
		factory.setDurability(true);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchService", batchServiceImpl);
		factory.setJobDataAsMap(map);

		return factory;
	}		

///////////////////////////////////////////////////////////////////
// drop partition

	@Value("${delete.partition.cronexpr}")
	private String dropTime;    

	@Bean
	public CronTriggerFactoryBean DropPartitionJobCronTrigger() {
		CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
		factory.setJobDetail(DropPartitionJob().getObject());
		factory.setStartDelay(5000);
		factory.setCronExpression(dropTime);

		return factory;
	}

	@Bean
	public JobDetailFactoryBean DropPartitionJob() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();

		factory.setJobClass(DropPartition.class);
		factory.setApplicationContextJobDataKey("applicationContext");
		factory.setDurability(true);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchService", batchServiceImpl);
		factory.setJobDataAsMap(map);

		return factory;
	}		

///////////////////////////////////////////////////////////////////
// DW

	@Value("${dw.time.cronexpr}")
	private String dwTime;    

	@Bean
	public CronTriggerFactoryBean DWJobCronTrigger() {
		CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
		factory.setJobDetail(DWJob().getObject());
		factory.setStartDelay(5000);
		factory.setCronExpression(dwTime);

		return factory;
	}

	@Bean
	public JobDetailFactoryBean DWJob() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();

		factory.setJobClass(DW.class);
		factory.setApplicationContextJobDataKey("applicationContext");
		factory.setDurability(true);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchService", batchServiceImpl);
		factory.setJobDataAsMap(map);

		return factory;
	}
	
///////////////////////////////////////////////////////////////////
// Send Time Controller

	@Bean
	public CronTriggerFactoryBean AllowedTimeControllerCronTrigger() {
		CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
		factory.setJobDetail(AllowedTimeControllerJob().getObject());
		factory.setStartDelay(5000);
		factory.setCronExpression(allowedTimeControlTime);

		return factory;
	}

	@Bean
	public JobDetailFactoryBean AllowedTimeControllerJob() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();

		factory.setJobClass(AllowedTimeController.class);
		factory.setApplicationContextJobDataKey("applicationContext");
		factory.setDurability(true);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("batchService", batchServiceImpl);
		factory.setJobDataAsMap(map);

		return factory;
	}

	
///////////////////////////////////////////////////////////////////
//replaceSMS

		@Value("${sms.time.cronexpr}")
		private String smsTime;
		
		@Bean
		public CronTriggerFactoryBean ReplaceSmsJobCronTrigger() {
			CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
			factory.setJobDetail(ReplaceSmsJob().getObject());
			factory.setStartDelay(5000);
			factory.setCronExpression(smsTime);

			return factory;
		}
		
		@Bean
		public JobDetailFactoryBean ReplaceSmsJob() {
			JobDetailFactoryBean factory = new JobDetailFactoryBean();

			factory.setJobClass(ReplaceSMS.class);
			factory.setApplicationContextJobDataKey("applicationContext");
			factory.setDurability(true);

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("batchService", batchServiceImpl);
			factory.setJobDataAsMap(map);

			return factory;
		}
}
