package com.store.comp.control.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.JobDetailBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.company.core.exception.NotExistException;
import com.company.core.exception.NotFoundException;
import com.company.core.type.Switch;
import com.company.core.type.YesNo;
import com.company.core.util.BeanFinder;
import com.company.core.util.ShellCommander;
import com.store.comp.control.dao.SchedulerDao;
import com.store.comp.control.dto.Schedule;
import com.store.comp.control.dto.Server;


@Service
public class SchedulerService {


   SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

   HashMap<String, CronTriggerBean> cronTriggerMap = new HashMap<String, CronTriggerBean>();
   HashMap<String, String> schedulerJobMap = null;

   boolean isInitialized = false;

   @Resource(name="hostNameShellCommander")
   ShellCommander commander;

   @Autowired
   protected SchedulerDao schedulerDao;

   Logger logger = Logger.getLogger(SchedulerService.class);

   String hostName;


   public void addSchedule(Schedule schedule){
      schedulerDao.insertSchedule(schedule);
   }

   public List<Schedule> getEntireSchedules(){
      return schedulerDao.selectEntireSchedules();
   }

   public Schedule getScheduleById(String schId) throws NotFoundException{
      Schedule schedule =  schedulerDao.selectScheduleById(schId);
      if(schedule == null)
         throw new NotFoundException(schId);
      return schedule;
   }

   public void changeSchedule(Schedule schedule) throws NotExistException{
      int cnt = schedulerDao.updateSchedule(schedule);
      if(cnt == 0)
         throw new NotExistException(schedule);
   }

   public void cancelSchedule(String schId) throws NotExistException{
      int cnt = schedulerDao.deleteSchedule(schId);
      if(cnt == 0)
         throw new NotExistException(schId);
   }

   public List<Server> getServerList(){
      return schedulerDao.selectServerList();
   }

   public Map<String, String> getSchedulerJobMap() {
      if(schedulerJobMap == null){
         String[] jobBeanNames = BeanFinder.getBeanNamesForType(QuartzJobBean.class);
         schedulerJobMap = new HashMap<String, String>();
         for(String name: jobBeanNames)
            schedulerJobMap.put(name, name);
      }
      return schedulerJobMap;
   }

   @SuppressWarnings("unchecked")
   public void reSchedule() throws Exception{

      logger.info("# Scheduler (re)start");

      schedulerFactory.stop();

      logger.info("  > Scheduler stopped.");

      if(hostName == null){
         commander.execute();
         hostName = commander.getResultString().trim();
      }

      Switch switchState = schedulerDao.selectSchedulingByServerName(hostName);
      logger.info("  > Host("+hostName+") scheduling state : "+switchState);

      if(switchState == Switch.OFF)
         return;

      cronTriggerMap.clear();
      List<Schedule> scheduleList = getEntireSchedules();
      for(Schedule sch: scheduleList){
         if(sch.getEnabled() == YesNo.NO)
            continue;
         String schId = Integer.toString(sch.getSchId());
         String beanId = sch.getBeanId();
         Object bean = BeanFinder.getBean(beanId);
         if(bean == null){
            logger.fatal("Bean("+beanId+") not found - Please move bean declaration to ContextLoaderListener's context");
            continue;
         }
         Class<QuartzJobBean> clazz = (Class<QuartzJobBean>)bean.getClass();
         CronTriggerBean trigger = createTrigger(schId, clazz, sch.getMinute(), sch.getHour(), sch.getDay(), sch.getMonth());
         cronTriggerMap.put(schId, trigger);
         logger.info("  > JOB : "+ beanId+" - "+sch.getMonth()+"."+sch.getDay()+"."+sch.getHour()+"."+sch.getMinute());
      }

      CronTriggerBean[] triggers = cronTriggerMap.values().toArray(new CronTriggerBean[cronTriggerMap.size()]);
      schedulerFactory.setOverwriteExistingJobs(true);
      schedulerFactory.setTriggers(triggers);
      schedulerFactory.setWaitForJobsToCompleteOnShutdown(false);
      schedulerFactory.afterPropertiesSet();

      logger.info("  > Scheduler started.");
      return;
   }

   private CronTriggerBean createTrigger(String id, Class<QuartzJobBean> clazz, String min, String hour, String day, String month) throws Exception{

      HashMap<String, Object> schedulerEnvMap = new HashMap<String, Object>();

      JobDetailBean jobDetail = new JobDetailBean();
      jobDetail.setName(id);
      jobDetail.setJobClass(clazz);
      jobDetail.setJobDataAsMap(schedulerEnvMap);
      jobDetail.afterPropertiesSet();

      CronTriggerBean trigger = new CronTriggerBean();
      trigger.setName("trigger-"+id);
      trigger.setJobDetail(jobDetail);
      trigger.setJobName(id);
      trigger.afterPropertiesSet();

      CronExpression expression = new CronExpression("0 "+min+" "+hour+" "+day+" "+month+" ? *");
      trigger.setCronExpression(expression);
      return trigger;
   }
}
