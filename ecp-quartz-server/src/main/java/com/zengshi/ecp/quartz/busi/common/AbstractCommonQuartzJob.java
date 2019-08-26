package com.zengshi.ecp.quartz.busi.common;

import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zengshi.paas.utils.LogUtil;
import com.alibaba.fastjson.JSON;


/**
 * Description: <br>
 * Date: 2014-9-14 <br>
 * 
 */
@DisallowConcurrentExecution
public abstract class AbstractCommonQuartzJob implements Job {

    private static final String MODULE = AbstractCommonQuartzJob.class.getName();

    /**
     * 获取执行日志；
     * 
     * @return
     */
    abstract protected String getModule();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = null;
        Map<String, String> extendParams = new java.util.HashMap<>();
        long start = System.currentTimeMillis();
        long end = 0;
        if (context == null) {

        } else {
            jobDataMap = context.getJobDetail().getJobDataMap();
            
            for (String key : jobDataMap.keySet()) {
                if (jobDataMap.get(key) != null) {
                    extendParams.put(key, jobDataMap.getString(key));
                }
            }
        }
        
        LogUtil.info(this.getModule(), "定时任务"+context.getJobDetail().getKey().getName()+"入参："+JSON.toJSONString(extendParams));

        try {
            ///调用接口服务；
            this.doJob(extendParams);
            
        } catch (Exception e) {
            LogUtil.error(this.getModule(), "定时任务失败!", e);
            if(e instanceof JobExecutionException){
                throw (JobExecutionException)e;
            } else {
                throw new JobExecutionException(e);
            }
            
        } finally {
            end = System.currentTimeMillis();
            LogUtil.info(this.getModule(), "定时任务耗时:" + (end - start) + "毫秒,定时任务结束!");
        }

    }

    /**
     * 执行具体的Job工作内容
     * 
     * @return
     */
    abstract protected void doJob(Map<String, String> extendParams) throws Exception;

}
