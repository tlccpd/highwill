package com.company.core.web.servlet;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.DirectFieldAccessor;


public abstract class ParameterInjectionServlet extends HttpServlet {
   
   
   public static final long serialVersionUID = 1;
   
   protected Logger logger = Logger.getLogger(this.getClass());
   protected ArrayList<String> appliedFields = new ArrayList<String>();
   protected ServletContext servletContext;

   
   @Override
   @SuppressWarnings("unchecked")
   public final void init() throws ServletException{
      
      DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(this);
      
      ServletContext context = getServletContext();
      Enumeration<String> enu = context.getInitParameterNames();
      while(enu.hasMoreElements()){
         String paramName = enu.nextElement();
         if(!fieldAccessor.isWritableProperty(paramName))
            continue;
         try{
            fieldAccessor.setPropertyValue(paramName, context.getInitParameter(paramName));
            appliedFields.add(paramName);
         }catch(Exception e){
            try{
               BeanUtils.setProperty(this, paramName, context.getInitParameter(paramName));
               appliedFields.add(paramName);
            }catch(Exception ie){
               logger.debug("Fail to set property("+paramName+")", ie);
            }
         }
      }

      enu = getInitParameterNames();
      while(enu.hasMoreElements()){
         String paramName = enu.nextElement();
         if(!fieldAccessor.isWritableProperty(paramName))
            continue;
         try{
            fieldAccessor.setPropertyValue(paramName, getInitParameter(paramName));
            appliedFields.add(paramName);
         }catch(Exception e){
            try{
               BeanUtils.setProperty(this, paramName, getInitParameter(paramName));
               appliedFields.add(paramName);
            }catch(Exception ie){
               logger.debug("Fail to set property("+paramName+")", ie);
            }
         }
      }

      postInit();
      printAppliedState();
   }

   protected void postInit()throws ServletException{
   }
   
   protected void printAppliedState(){
      logger.info("# Servlet - " + this.getClass().getSimpleName());
      if(appliedFields.size() == 0){
         appliedFields = null;
         return;
      }
      DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(this);
      for(String fieldName: appliedFields){
         try{
            Object fieldValue = null;
            fieldValue = fieldAccessor.getPropertyValue(fieldName);
            if(fieldValue instanceof Collection){
               boolean isFirst = true;
               for(Object obj: ((Collection<?>)fieldValue)){
                  if(isFirst){
                     logger.info("  > "+fieldName);
                     isFirst = false;
                  }
                  logger.info("\t\t = " + obj);
               }
            }else if(fieldValue instanceof Object[]){
               boolean isFirst = true;
               for(Object obj: (Object[])fieldValue){
                  if(isFirst){
                     logger.info("  > "+fieldName);
                     isFirst = false;
                  }
                  logger.info("\t= " + obj);
               }
            }else{
               logger.info("  > "+fieldName + " = " + fieldAccessor.getPropertyValue(fieldName));
            }
         }catch(Exception e){
            logger.error("Failed to retrieve servlet state", e);
         }
      }
      appliedFields.clear();
      appliedFields = null;
   }
}
