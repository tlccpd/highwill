package com.store.comp.control.schedule;


import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


public abstract class BaseJobBean extends QuartzJobBean {
   
   
   Logger logger = Logger.getLogger(BaseJobBean.class);
   

   @Override
   protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
      long startTime = System.currentTimeMillis();
      try{
         logger.info("");
         logger.info("[START] "+this.getClass().getSimpleName());
         process(context);
      }catch(Exception e){
         logger.error("Scheduler job execution failed", e);
      }
      long endTime = System.currentTimeMillis();
      logger.info("[ END ] 소요 시간 "+((endTime-startTime)/1000)+" 초");
   }

   protected abstract void process(JobExecutionContext context) throws Exception;

}
