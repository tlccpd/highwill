package com.store.comp.control.schedule;


import org.quartz.JobExecutionContext;

import com.company.core.util.BeanFinder;


public class TestJobBean extends BaseJobBean {

   
   @Override
   protected void process(JobExecutionContext context) throws Exception{
      logger.info("=> TestJobBean is invoked!!! > "+BeanFinder.getBean("configuration"));
   }
}
