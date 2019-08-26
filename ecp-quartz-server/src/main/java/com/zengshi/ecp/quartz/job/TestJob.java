package com.zengshi.ecp.quartz.job;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;


public class TestJob implements Job {
	private static Log logger = LogFactory.getLog(TestJob.class);
	private static Lock lock = new ReentrantLock();

	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		if (lock.tryLock()) {
			try {
				JobKey jobKey = (JobKey) context.getJobDetail().getKey();
				logger.info("Executing job: " + jobKey + " executing at " + new Date() + ", fired by: " + context.getTrigger().getKey());
				if (context.getMergedJobDataMap().size() > 0) {
					Set<String> keys = context.getMergedJobDataMap().keySet();
					for (String key : keys) {
						String val = context.getMergedJobDataMap().getString(key);
						logger.info(" - jobDataMap entry: " + key + " = " + val);
					}
				}
				if (logger.isDebugEnabled())
					logger.debug("Account Info Job Scan Start...");
				//IAccountManageSV accountSV = (IAccountManageSV) ServiceUtil.getService(IAccountManageSV.class, "gx");

				//IBOAccountValue account = accountSV.getBean(10000);
				//System.out.println("---------------" + account.getLoginName());

				if (logger.isDebugEnabled())
					logger.debug("Account Info Job Scan Over...");
			} catch (Exception e) {
				logger.error("Account Info Job error!", e);
				throw new JobExecutionException(e);
			} finally {
				lock.unlock();
			}
		}
	}

	public static void main(String args[]) {
		try {
			new TestJob().execute(null);
		} catch (JobExecutionException e) {
			e.printStackTrace();
		}
	}
}
